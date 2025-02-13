package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotAttService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库公共方法类
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundCommonService {
    // 公共
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderManager;
    // 出库单明细行
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailManager;
    @Autowired
    protected BanQinWmWvHeaderService wmWvHeaderManager;
    // 波次单明细
    @Autowired
    protected BanQinWmWvDetailService wmWvDetailManager;
    // 商品
    @Autowired
    protected BanQinCdWhSkuService cdWhSkuManager;
    // 批次库位库存
    @Autowired
    protected BanQinWmInvLotLocService wmInvLotLocManager;
    // 批次号
    @Autowired
    protected BanQinWmInvLotAttService wmInvLotAttManager;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryServiceManager;
    // 销售订单
    @Autowired
    protected BanQinWmSaleHeaderService wmSaleHeaderManage;
    @Autowired
    protected BanQinWmSaleDetailService wmSaleDetailManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;

    /**
     * 按单位换算 单位EA数量
     *
     * @param qtyEa      可操作数量EA
     * @param qtyInv     库存可操作数量EA
     * @param qtyPackUom 单位换算数量
     */
    public Double qtyEaOpByUom(Double qtyEa, Double qtyInv, Integer qtyPackUom) {
        double qtyEaOp = 0D;
        // 数量不足操作
        if (qtyEa == 0 || qtyInv == 0 || qtyPackUom == 0) {
            return qtyEaOp;
        }
        // 如果可操作数量>=库存数量，那么按库存数量换算 如果可操作数量<库存数量，那么按操作数量换算
        if (qtyEa >= qtyInv) {
            // (求商)*单位换算数量
            qtyEaOp = Math.floor(qtyInv / qtyPackUom) * qtyPackUom;
        } else {
            qtyEaOp = Math.floor(qtyEa / qtyPackUom) * qtyPackUom;
        }
        return qtyEaOp;
    }

    /**
     * 根据库存周转规则 获取批次库存 可用于，预配、一步分配的库存周转规则优先
     *
     * @param ownerCode      货主
     * @param skuCode        商品
     * @param sqlParamEntity 库存周转规则拼SQL 入参对象
     */
    public List<BanQinWmInvLot> getInvLotByRuleRotation(String ownerCode, String skuCode, String orgId, BanQinCdRuleRotationSqlParamEntity sqlParamEntity) throws WarehouseException {
        // 查询商品
        BanQinCdWhSku skuModel = cdWhSkuManager.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
        if (skuModel == null) {
            // 查询不到货主{0}商品{1}
            throw new WarehouseException("查询不到货主" + ownerCode + "商品" + skuCode);
        }
        // 获取批次库存查询结果
        List<Map<String, Object>> objList = jdbcTemplate.queryForList(this.getInvLotSql(ownerCode, skuCode, orgId, sqlParamEntity));
        // 类型转换，并且获取可预配库存结果集
        List<BanQinWmInvLot> invLotList = Lists.newArrayList();
        if (objList.size() > 0) {
            for (Map<String, Object> map : objList) {
                // Map转换成WmInvLotModel对象
                BanQinWmInvLot model = CollectionUtil.mapToJavaBean(map, BanQinWmInvLot.class);
                // 临时文件，用于存放批次属性,校验效期控制
                BanQinWmInvLotAtt lotAttModel = CollectionUtil.mapToJavaBean(map, BanQinWmInvLotAtt.class);
                // 过滤不符合效期的库存记录
                if (wmCommon.checkOutValidity(skuModel.getIsValidity(), skuModel.getShelfLife(), skuModel.getLifeType(), skuModel.getOutLifeDays(),
                        lotAttModel.getLotAtt01(), lotAttModel.getLotAtt02())) {
                    invLotList.add(model);
                }
            }
        }
        return invLotList;
    }

    /**
     * 批次库存查询-库存周转规则 拼SQL 查询 预配、两步分配(库存周转规则优先)
     */
    public String getInvLotSql(String ownerCode, String skuCode, String orgId, BanQinCdRuleRotationSqlParamEntity sqlParamEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT wil.lot_num AS lotNum, ");
        sql.append("       wil.owner_code AS ownerCode, ");
        sql.append("   	   wil.sku_code AS skuCode, ");
        sql.append("       wil.qty AS qty, ");
        sql.append("       wil.qty_prealloc AS qtyPrealloc, ");
        sql.append("       wil.qty_alloc AS qtyAlloc, ");
        sql.append("       wil.qty_pk AS qtyPk, ");
        sql.append("       wil.org_id AS orgId, ");
        sql.append("       wila.lot_att01 AS lotAtt01, ");
        sql.append("       wila.lot_att02 AS lotAtt02 ");
        sql.append("  FROM wm_inv_lot wil ");
        sql.append("  LEFT JOIN wm_inv_lot_att wila ");
        sql.append("   ON wila.lot_num = wil.lot_num ");
        sql.append("  AND wila.org_id = wil.org_id ");
        sql.append("WHERE 1 = 1 ");
        // 可预配数量 = 总数量-预配数量-分配数量-已拣货数量-冻结数量>0
        sql.append("  AND (wil.qty - wil.qty_prealloc - " + "wil.qty_alloc - wil.qty_pk - wil.qty_hold) > 0 ");
        sql.append("  AND wila.owner_code ='").append(ownerCode).append("'");
        sql.append("  AND wila.sku_code ='").append(skuCode).append("'");
        sql.append("  AND wila.org_id ='").append(orgId).append("'");
        // 库存周转规则拼SQL获取
        BanQinCdRuleRotationSqlEntity rotationSqlEntity = this.wmCommon.getRuleRotationSql(sqlParamEntity);
        if (StringUtils.isNotEmpty(rotationSqlEntity.getWhereSql())) {
            sql.append(rotationSqlEntity.getWhereSql());
        }
        // 排序
        if (StringUtils.isNotEmpty(rotationSqlEntity.getOrderBySql())) {
            sql.append(" order by wila.").append(rotationSqlEntity.getOrderBySql());
        }
        return sql.toString();
    }

    /**
     * 一步分配库存查询获取 周转方式：包装优先
     */
    public List<BanQinAllocInvLotLocEntity> getInvLotLocByPackage(BanQinAllocInvLotLocSqlParamlEntity invParamEntity, String orgId) throws WarehouseException {
        /*Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("orgId", orgId);
        conditionMap.put("ownerCode", invParamEntity.getOwnerCode());// 货主
        conditionMap.put("skuCode", invParamEntity.getSkuCode());// 商品
        conditionMap.put("lotNum", invParamEntity.getLotNum());// 批次号
        conditionMap.put("areaCode", invParamEntity.getAreaCode());// 区域(商品明细)
        conditionMap.put("zoneCode", invParamEntity.getZoneCode());// 拣货区(商品明细)
        conditionMap.put("locCode", invParamEntity.getLocCode());// 拣货位(商品明细)
        conditionMap.put("traceId", invParamEntity.getTraceId());// 跟踪号(商品明细)
        conditionMap.put("skuLocCode", invParamEntity.getSkuLocCode());// 商品固定拣货位
        conditionMap.put("locUseType", invParamEntity.getLocUseType());// 库位使用类型
        // 12个批次属性
        BanQinCdRuleRotationSqlParamEntity sqlParamEntity = invParamEntity.getRuleRotationSqlParamEntity();
        conditionMap.put("lotAtt01", sqlParamEntity.getLotAtt01());
        conditionMap.put("lotAtt02", sqlParamEntity.getLotAtt02());
        conditionMap.put("lotAtt03", sqlParamEntity.getLotAtt03());
        conditionMap.put("lotAtt04", sqlParamEntity.getLotAtt04());
        conditionMap.put("lotAtt05", sqlParamEntity.getLotAtt05());
        conditionMap.put("lotAtt06", sqlParamEntity.getLotAtt06());
        conditionMap.put("lotAtt07", sqlParamEntity.getLotAtt07());
        conditionMap.put("lotAtt08", sqlParamEntity.getLotAtt08());
        conditionMap.put("lotAtt09", sqlParamEntity.getLotAtt09());
        conditionMap.put("lotAtt10", sqlParamEntity.getLotAtt10());
        conditionMap.put("lotAtt11", sqlParamEntity.getLotAtt11());
        conditionMap.put("lotAtt12", sqlParamEntity.getLotAtt12());*/

        StringBuilder sql = new StringBuilder();
        // 1、批次库位库存查询
        sql.append(getInvLotLocSql(invParamEntity.getOwnerCode(), invParamEntity.getSkuCode(), orgId));

        // 2、分配规则查询条件
        sql.append(getInvLotLocWhereSql(invParamEntity));

        // 3、周转规则条件、排序
        // 库存周转规则
        BanQinCdRuleRotationSqlEntity sqlRotationEntity = wmCommon.getRuleRotationSql(invParamEntity.getRuleRotationSqlParamEntity());
        // 库存周转规则查询条件
        if (StringUtils.isNotEmpty(sqlRotationEntity.getWhereSql())) {
            sql.append(sqlRotationEntity.getWhereSql());
        }
        sql.append(" order by ");
        // 库存周转规则排序
        if (StringUtils.isNotEmpty(sqlRotationEntity.getOrderBySql())) {
            sql.append(sqlRotationEntity.getOrderBySql());
            // 4、清仓优先排序、拣货顺序排序
            sql.append(",").append(getInvLotLocOrderBySql(invParamEntity));
        } else {
            sql.append(getInvLotLocOrderBySql(invParamEntity));
        }

        List<Map<String, Object>> objList = jdbcTemplate.queryForList(sql.toString());
        // 类型转换，拼SQL 执行后的Map 转换成具体的对象
        return getInvLotLocEntityList(objList, invParamEntity.getQtyPackUom(), invParamEntity.getCdWhSkuModel());
    }

    /**
     * 根据 批次号及分配规则条件 获取批次库位库存 两步分配(库存周转优先)、一步分配(库存周转规则优先)
     */
    public List<BanQinAllocInvLotLocEntity> getInvLotLocByRotation(BanQinAllocInvLotLocSqlParamlEntity invParamEntity, String orgId) throws WarehouseException {
        // 1、批次库位库存查询
        // 5、执行拼SQL 并且 类型转换
        // 获取批次库存查询结果
        String sql = getInvLotLocSql(invParamEntity.getOwnerCode(), invParamEntity.getSkuCode(), orgId) +
                // 2、批次号查询条件
                getInvLotLocWhereSqlByLotNum(invParamEntity.getLotNum()) +
                // 3、分配规则查询条件
                getInvLotLocWhereSql(invParamEntity) +
                // 排序
                " order by " +
                // 4、清仓优先排序、拣货顺序排序
                getInvLotLocOrderBySql(invParamEntity);
        List<Map<String, Object>> objList = jdbcTemplate.queryForList(sql);
        // 类型转换，拼SQL 执行后的Map 转换成具体的对象
        return getInvLotLocEntityList(objList, invParamEntity.getQtyPackUom(), invParamEntity.getCdWhSkuModel());
    }

    /**
     * 获取批次库位库存，类型转换 拼SQL 执行后的Map 转换成具体的对象
     */
    private List<BanQinAllocInvLotLocEntity> getInvLotLocEntityList(List<Map<String, Object>> objList, Integer qtyPackUom, BanQinCdWhSku cdWhSkuModel) throws WarehouseException {
        // 类型转换，并且获取可分配库存结果集
        List<BanQinAllocInvLotLocEntity> invLotLocList = Lists.newArrayList();
        if (objList.size() <= 0) {
            return invLotLocList;
        }
        for (Map<String, Object> map : objList) {
            // 1、Map转换成WmInvLotModel对象
            BanQinAllocInvLotLocEntity invLotLocEntity = CollectionUtil.mapToJavaBean(map, BanQinAllocInvLotLocEntity.class);
            // 2、单位数量是否满足
            BanQinWmInvLotLoc invLotLocModel = new BanQinWmInvLotLoc();
            BeanUtils.copyProperties(invLotLocEntity, invLotLocModel);
            // 库存可分配数量
            Double qtyAvailAlloc = wmInvLotLocManager.getQtyAllocAvailable(invLotLocModel);
            if (qtyAvailAlloc < qtyPackUom.doubleValue()) {
                continue;// 不足一单位库存数量，不能分配更新
            }
            // 3、过滤不符合效期的库存记录
            if (wmCommon.checkOutValidity(cdWhSkuModel.getIsValidity(), cdWhSkuModel.getShelfLife(), cdWhSkuModel.getLifeType(), cdWhSkuModel.getOutLifeDays(), invLotLocEntity.getLotAtt01(), invLotLocEntity.getLotAtt02())) {
                invLotLocList.add(invLotLocEntity);
            }
        }
        return invLotLocList;
    }

    /**
     * 根据货主、商品获取批次库位库存 - 拼SQL
     */
    public String getInvLotLocSql(String ownerCode, String skuCode, String orgId) {
        return "SELECT will.lot_num AS lotNum," +
                "       will.loc_code AS locCode," +
                "       will.trace_id AS traceId," +
                "       will.owner_code AS ownerCode," +
                "       will.sku_code AS skuCode," +
                "       will.qty AS qty," +
                "       will.qty_hold AS qtyHold," +
                "       will.qty_alloc AS qtyAlloc," +
                "       will.qty_pk AS qtyPk," +
                "       will.qty_pa_out AS qtyPaOut," +
                "       will.qty_pa_in AS qtyPaIn," +
                "       will.qty_rp_out AS qtyRpOut," +
                "       will.qty_rp_in AS qtyRpIn," +
                "       will.qty_mv_out AS qtyMvOut," +
                "       will.qty_mv_in AS qtyMvIn," +
                "       will.org_id AS orgId," +
                "       wila.lot_att01 AS lotAtt01," +
                "       wila.lot_att02 AS lotAtt02," +
                "       wila.lot_att03 AS lotAtt03," +
                "       wila.lot_att04 AS lotAtt04," +
                "       wila.lot_att05 AS lotAtt05," +
                "       wila.lot_att06 AS lotAtt06," +
                "       wila.lot_att07 AS lotAtt07," +
                "       wila.lot_att08 AS lotAtt08," +
                "       wila.lot_att09 AS lotAtt09," +
                "       wila.lot_att10 AS lotAtt10," +
                "       wila.lot_att11 AS lotAtt11," +
                "       wila.lot_att12 AS lotAtt12 " +
                "  FROM wm_inv_lot_loc will" +
                "  LEFT JOIN wm_inv_lot_att wila" +
                "    ON wila.lot_num = will.lot_num " +
                "   AND wila.org_id = will.org_id " +
                "  LEFT JOIN cd_wh_loc cwl " +
                "    ON cwl.loc_code = will.loc_code " +
                "   AND cwl.org_id = will.org_id " +
                "   AND cwl.is_enable = 'Y' " +
                "  LEFT JOIN cd_wh_zone cwz " +
                "    ON cwz.zone_code = cwl.zone_code " +
                "   AND cwz.org_id = cwl.org_id " +
                " WHERE 1 = 1 " +
                "   AND will.org_id ='" + orgId + "'" +
                "   AND will.owner_code ='" + ownerCode + "'" +
                "   AND will.sku_code ='" + skuCode + "'";
    }

    /**
     * 拣货区、拣货位 、固定拣货位、库位使用类型 查询条件 拣货区、拣货位的值从出库单明细拣货区、拣货位获取
     */
    private String getInvLotLocWhereSql(BanQinAllocInvLotLocSqlParamlEntity invParamEntity) {
        StringBuilder sql = new StringBuilder();
        // 优先级：拣货区/拣货位 > 固定拣货位 > 库位使用
        // 区域
        if (StringUtils.isNotEmpty(invParamEntity.getAreaCode())) {
            sql.append(" AND cwz.area_code =").append("'").append(invParamEntity.getAreaCode()).append("'");// 出库单行区域
        }
        // 拣货区
        if (StringUtils.isNotEmpty(invParamEntity.getZoneCode())) {
            sql.append(" AND cwl.zone_code =").append("'").append(invParamEntity.getZoneCode()).append("'"); // 出库单行库区
        }
        // 拣货位
        if (StringUtils.isNotEmpty(invParamEntity.getLocCode())) {
            sql.append(" AND cwl.loc_code =").append("'").append(invParamEntity.getLocCode()).append("'"); // 出库单行库位
        }
        // 跟踪号
        if (StringUtils.isNotEmpty(invParamEntity.getTraceId())) {
            sql.append(" AND will.trace_id =").append("'").append(invParamEntity.getTraceId()).append("'"); // 出库单行库位
        }
        // 固定拣货位
        if (StringUtils.isNotEmpty(invParamEntity.getSkuLocCode())) { // 并且分配规则的商品固定拣货位不为空
            sql.append(" AND will.loc_code =").append("'").append(invParamEntity.getSkuLocCode()).append("'"); // 固定拣货库位
        }
        // 使用类型
        if (StringUtils.isEmpty(invParamEntity.getSkuLocCode()) && StringUtils.isNotEmpty(invParamEntity.getLocUseType())) {
            // 分配规则的商品固定拣货位为空
            // 并且分配规则的库位使用类型不为空
            sql.append(" AND cwl.loc_use_type =").append("'").append(invParamEntity.getLocUseType()).append("'");// 使用类型
        }
        return sql.toString();
    }

    /**
     * 条件 - 批次号
     */
    private String getInvLotLocWhereSqlByLotNum(String lotNum) {
        return " AND will.lot_num = '" + lotNum + "'";
    }

    /**
     * 清仓优先排序，以及拣货顺序排序
     */
    private String getInvLotLocOrderBySql(BanQinAllocInvLotLocSqlParamlEntity invParamEntity) {
        StringBuilder sql = new StringBuilder();
        // 是否清仓优先
        if (invParamEntity.getIsClearFirst().equals(WmsConstants.YES)) {
            sql.append(" (will.qty - will.qty_hold - will.qty_alloc - will.qty_pk - will.qty_pa_out - will.qty_rp_out - will.qty_mv_out) ");
            sql.append(" asc ");// 是清仓优先，升序
        }
        // 如果不清仓优先，那么只按拣货库位排序
        // 按拣货库位排序
        if (sql.length() > 0) {
            sql.append(",");
        }
        sql.append(" cwl.pk_seq asc");
        return sql.toString();
    }

    /**
     * 超量分配库存数量统计并且获取 ---两步分配、手工分配
     *
     * @param qtyAllocUom 剩余分配单位数量
     * @param qtyPackUom  单位换算数量
     * @param skuLocCode  固定拣货位
     * @param ownerCode   货主
     * @param skuCode     商品
     * @param lotNum      批次号
     */
    public BanQinAllocInvLotLocEntity getOverInvLot(Double qtyAllocUom, Double qtyPackUom, String skuLocCode, String ownerCode, String skuCode, String lotNum, String orgId) {
        BanQinAllocInvLotLocEntity entity = null;
        // 库存数量统计
        BanQinWmInvLotLoc wmInvLotLoc = new BanQinWmInvLotLoc();
        wmInvLotLoc.setOwnerCode(ownerCode);
        wmInvLotLoc.setSkuCode(skuCode);
        wmInvLotLoc.setLotNum(lotNum);
        wmInvLotLoc.setOrgId(orgId);
        List<BanQinWmInvLotLoc> lotLocList = wmInvLotLocManager.findList(wmInvLotLoc);
        double qtyOver = lotLocList.stream().filter(s -> !skuLocCode.equals(s.getLocCode())).map(s -> s.getQty() - s.getQtyHold() - s.getQtyAlloc() - s.getQtyPk() - s.getQtyPaOut() - s.getQtyRpOut() - -s.getQtyMvOut()).mapToDouble(Double::doubleValue).sum();

        if (qtyOver <= 0D) {
            return entity;
        }
        // 超量分配单位数量
        Double qtyOverUom = 0D;
        if (qtyOver > 0) {
            // 超量分配单位数量
            qtyOverUom = wmCommon.doubleDivide(qtyOver, qtyPackUom);
        }
        // 计算超量分配库存操作数量
        // 库存分配操作数量
        Double qtyEaOp = qtyOverUom >= qtyAllocUom ? qtyAllocUom * qtyPackUom : qtyOverUom * qtyPackUom;
        entity = new BanQinAllocInvLotLocEntity();
        entity.setOwnerCode(ownerCode);
        entity.setSkuCode(skuCode);
        entity.setLocCode(skuLocCode);
        entity.setLotNum(lotNum);
        entity.setTraceId(WmsConstants.TRACE_ID);// 跟踪号,忽略*
        entity.setQty(qtyOver);
        entity.setQtyHold(0D);
        entity.setQtyAlloc(0D);
        entity.setQtyPk(0D);
        entity.setQtyMvIn(0D);
        entity.setQtyMvOut(0D);
        entity.setQtyPaIn(0D);
        entity.setQtyPaOut(0D);
        entity.setQtyRpIn(0D);
        entity.setQtyRpOut(0D);
        entity.setQtyEaOp(qtyEaOp);
        entity.setOrgId(orgId);

        return entity;
    }

    /**
     * 超量分配库存数量统计并且获取 ---一步分配，根据库存周转规则排序统计
     *
     * @param qtyPackUom                 单位数量
     * @param skuLocCode                 固定拣货位
     * @param ownerCode                  货主
     * @param skuCode                    商品
     * @param ruleRotationSqlParamEntity 库存周转拼SQL条件
     * @param skuModel                   商品信息
     */
    public List<BanQinAllocInvLotLocEntity> getOverInvLotLoc(Double qtyPackUom, String skuLocCode, String ownerCode, String skuCode, String orgId, BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity, BanQinCdWhSku skuModel) {
        List<BanQinAllocInvLotLocEntity> overInvLotLocList = Lists.newArrayList();
        // 执行SQL语句
        List<Map<String, Object>> objList = jdbcTemplate.queryForList(getOverInvLotLocSql(ownerCode, skuCode, orgId, ruleRotationSqlParamEntity));
        if (objList.size() <= 0) {
            return overInvLotLocList;
        }
        for (Map<String, Object> map : objList) {
            // 1、Map转换成WmInvLotModel对象
            BanQinAllocInvLotLocEntity invLotLocEntity = CollectionUtil.mapToJavaBean(map, BanQinAllocInvLotLocEntity.class);
            invLotLocEntity.setOwnerCode(ownerCode);// 货主
            invLotLocEntity.setSkuCode(skuCode);// 商品
            invLotLocEntity.setLocCode(skuLocCode);// 库位
            invLotLocEntity.setTraceId(WmsConstants.TRACE_ID);// 跟踪号,忽略*
            // 2、单位数量是否满足
            BanQinWmInvLotLoc invLotLocModel = new BanQinWmInvLotLoc();
            BeanUtils.copyProperties(invLotLocEntity, invLotLocModel);
            // 库存可分配数量
            Double qtyAvailAlloc = wmInvLotLocManager.getQtyAllocAvailable(invLotLocModel);
            if (qtyAvailAlloc < qtyPackUom) {
                continue;// 不足一单位库存数量，不能分配更新
            }
            // 获取批次号信息
            BanQinWmInvLotAtt invLotAttModel = wmInvLotAttManager.getByLotNum(invLotLocEntity.getLotNum(), orgId);
            if (invLotAttModel == null) {
                continue;
            }
            // 3、过滤不符合效期的库存记录
            if (wmCommon.checkOutValidity(skuModel.getIsValidity(), skuModel.getShelfLife(), skuModel.getLifeType(), skuModel.getOutLifeDays(), invLotAttModel.getLotAtt01(), invLotAttModel
                    .getLotAtt02())) {
                overInvLotLocList.add(invLotLocEntity);
            }
        }
        return overInvLotLocList;
    }

    /**
     * 一步分配 超量分配，根据库存周转规则并且按批次统计
     *
     * @param ownerCode 货主
     * @param skuCode   商品
     */
    private String getOverInvLotLocSql(String ownerCode, String skuCode, String orgId, BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT will.qty AS qty,");
        sql.append("       will.qty_hold AS qtyHold,");
        sql.append("       will.qty_alloc AS qtyAlloc,");
        sql.append("       will.qty_pk AS qtyPk,");
        sql.append("       will.qty_pa_out AS qtyPaOut,");
        sql.append("       will.qty_rp_out AS qtyRpOut,");
        sql.append("       will.qty_mv_out AS qtyMvOut,");
        sql.append("       will.lot_num AS lotNum,");
        sql.append("       wila.lot_att01 AS lotAtt01,");
        sql.append("       wila.lot_att02 AS lotAtt02,");
        sql.append("       wila.lot_att03 AS lotAtt03,");
        sql.append("       wila.lot_att04 AS lotAtt04,");
        sql.append("       wila.lot_att05 AS lotAtt05,");
        sql.append("       wila.lot_att06 AS lotAtt06,");
        sql.append("       wila.lot_att07 AS lotAtt07,");
        sql.append("       wila.lot_att08 AS lotAtt08,");
        sql.append("       wila.lot_att09 AS lotAtt09,");
        sql.append("       wila.lot_att10 AS lotAtt10,");
        sql.append("       wila.lot_att11 AS lotAtt11,");
        sql.append("       wila.lot_att12 AS lotAtt12 ");
        sql.append(" FROM(");
        sql.append("SELECT SUM(IFNULL(w.QTY, 0)) qty,");
        sql.append("       SUM(IFNULL(w.QTY_HOLD, 0)) qty_hold,");
        sql.append("       SUM(IFNULL(w.QTY_ALLOC,0)) qty_alloc,");
        sql.append("       SUM(IFNULL(w.QTY_PK, 0)) qty_pk,");
        sql.append("       SUM(IFNULL(w.QTY_PA_OUT, 0)) qty_pa_out,");
        sql.append("       SUM(IFNULL(w.QTY_RP_OUT, 0)) qty_rp_out,");
        sql.append("       SUM(IFNULL(w.QTY_MV_OUT, 0)) qty_mv_out,");
        sql.append("       w.lot_num,");
        sql.append("       w.org_id ");
        sql.append("  FROM wm_inv_lot_loc w");
        sql.append(" WHERE 1 = 1 ");
        sql.append("   AND w.org_id ='").append(orgId).append("'");
        sql.append("   AND w.owner_code ='").append(ownerCode).append("'");
        sql.append("   AND w.sku_code ='").append(skuCode).append("'");
        // 子查询group by 统计
        sql.append(" GROUP BY w.lot_num, w.org_id");
        sql.append(" ) will ");// 主表查询，包含子查询，别名
        sql.append(" LEFT JOIN wm_inv_lot_att wila");
        sql.append("  ON wila.lot_num = will.lot_num ");
        sql.append(" AND wila.org_id = will.org_id ");
        // 周转规则条件、排序
        // 库存周转规则
        BanQinCdRuleRotationSqlEntity sqlRotationEntity = wmCommon.getRuleRotationSql(ruleRotationSqlParamEntity);
        // 库存周转规则查询条件
        if (StringUtils.isNotEmpty(sqlRotationEntity.getWhereSql())) {
            sql.append(sqlRotationEntity.getWhereSql());
        }
        sql.append("  order by ");
        // 库存周转规则排序
        if (StringUtils.isNotEmpty(sqlRotationEntity.getOrderBySql())) {
            sql.append(sqlRotationEntity.getOrderBySql());
        }
        return sql.toString();
    }

    /**
     * 批量更新出库单状态 按波次单号、按出库单号 1、可写于事务内，每次事务提交时都进行一次单据的更新
     * (如果无性能问题，可保证单据的状态与其他数据同步) 2、可写于事务外，与最外层的for循环同级，循环更新所有明细后，另启一次事务单独进行单据的更新
     * (考虑到性能问题，但存在单据状态更新失败的风险，即状态与其他数据不同步)
     */
    @Transactional
    public void updateSoHeaderStatus(String processByCode, List<String> noList, String orgId) {
        // 条件集合长度
        // 按波次单更新状态
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            // 更新出库单
            // wmSoHeaderManager.updateSoHeaderStatusByWave(noList, user.getId(), orgId);
            for (String waveNo : noList) {
                List<BanQinWmSoEntity> soList = wmSoHeaderManager.findEntityByWaveNo(waveNo, orgId);
                if (CollectionUtil.isEmpty(soList))
                    throw new WarehouseException("数据已过期");
                for (BanQinWmSoEntity soEntity : soList) {
                    List<BanQinWmSoDetail> soDetailList = wmSoDetailManager.findBySoNo(soEntity.getSoNo(), soEntity.getOrgId());
                    List<BanQinWmSoDetail> noCancelList = soDetailList.stream().filter(s -> !"90".equals(s.getStatus())).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(noCancelList)) {
                        soEntity.setStatus("00");
                        wmSoHeaderManager.saveSoHeader(soEntity);
                        continue;
                    }
                    String max = noCancelList.stream().map(BanQinWmSoDetail::getStatus).max(Comparator.comparing(String::toString)).get();
                    String min = noCancelList.stream().map(BanQinWmSoDetail::getStatus).min(Comparator.comparing(String::toString)).get();
                    String status = "";
                    if (max.equals(min)) {
                        status = max;
                    } else {
                        if (Integer.parseInt(max) <= 20) status = "10";
                        else if (Integer.parseInt(max) <= 40) status = "30";
                        else if (Integer.parseInt(max) <= 60) status = "50";
                        else if (Integer.parseInt(max) <= 80) status = "70";
                    }
                    soEntity.setStatus(status);
                    wmSoHeaderManager.saveSoHeader(soEntity);
                }
            }
        }
        // 按出库单更新状态
        else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            // 更新出库单
            // wmSoHeaderManager.updateSoHeaderStatusBySo(noList, user.getId(), orgId);
            for (String soNo : noList) {
                BanQinWmSoHeader soHeader = wmSoHeaderManager.findBySoNo(soNo, orgId);
                if (null == soHeader)
                    throw new WarehouseException("数据已过期");
                List<BanQinWmSoDetail> soDetailList = wmSoDetailManager.findBySoNo(soHeader.getSoNo(), soHeader.getOrgId());
                List<BanQinWmSoDetail> noCancelList = soDetailList.stream().filter(s -> !"90".equals(s.getStatus())).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(noCancelList)) {
                    soHeader.setStatus("00");
                    wmSoHeaderManager.save(soHeader);
                    continue;
                }
                String max = noCancelList.stream().map(BanQinWmSoDetail::getStatus).max(Comparator.comparing(String::toString)).get();
                String min = noCancelList.stream().map(BanQinWmSoDetail::getStatus).min(Comparator.comparing(String::toString)).get();
                String status = "";
                if (max.equals(min)) {
                    status = max;
                } else {
                    if (Integer.parseInt(max) <= 20) status = "10";
                    else if (Integer.parseInt(max) <= 40) status = "30";
                    else if (Integer.parseInt(max) <= 60) status = "50";
                    else if (Integer.parseInt(max) <= 80) status = "70";
                }
                soHeader.setStatus(status);
                wmSoHeaderManager.save(soHeader);
            }
        }
    }

    /**
     * Description : 波次单明细状态更新 按波次单号、按出库单号 1、可写于事务内，每次事务提交时都进行一次单据的更新
     * (如果无性能问题，可保证单据的状态与其他数据同步) 2、可写于事务外，与最外层的for循环同级，循环更新所有明细后，另启一次事务单独进行单据的更新
     * (考虑到性能问题，但存在单据状态更新失败的风险，即状态与其他数据不同步)
     */
    @Transactional
    public void updateWvDetailStatus(String processByCode, List<String> noList, String orgId) {
        User user = UserUtils.getUser();
        // 按波次单更新状态
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            // 更新波次单
            wmWvDetailManager.updateWvDetailStatusByWave(noList, user.getId(), orgId);
        }
        // 按出库单更新状态
        else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            // 更新波次单
            wmWvDetailManager.updateWvDetailStatusBySo(noList, user.getId(), orgId);
        }
    }

    /**
     * 波次单明细状态更新，单行更新， 波次单状态=出库单状态
     */
    @Transactional
    public void updateWvDetailStatus(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader soHeaderModel = wmSoHeaderManager.findBySoNo(soNo, orgId);
        // 更新波次单明行状态
        wmWvDetailManager.updateStatus(soNo, soHeaderModel.getStatus(), orgId);
    }

    /**
     * 波次单状态更新 按波次单号、按出库单号 1、可写于事务内，每次事务提交时都进行一次单据的更新
     * (如果无性能问题，可保证单据的状态与其他数据同步) 2、可写于事务外，与最外层的for循环同级，循环更新所有明细后，另启一次事务单独进行单据的更新
     * (考虑到性能问题，但存在单据状态更新失败的风险，即状态与其他数据不同步)
     */
    @Transactional
    public void updateWvHeaderStatus(String processByCode, List<String> noList, String orgId) {
        // 按波次单更新状态
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            // 更新波次单
            // wmWvHeaderManager.updateWvHeaderStatusByWave(noList, user.getId(), orgId);
            for (String waveNo : noList) {
                BanQinWmWvHeader wvHeader = wmWvHeaderManager.findByWaveNo(waveNo, orgId);
                if (null == wvHeader)
                    throw new WarehouseException("数据已过期");
                List<BanQinWmWvDetail> wvDetailList = wmWvDetailManager.findByWaveNo(waveNo, orgId);
                List<BanQinWmWvDetail> noCancelList = wvDetailList.stream().filter(s -> !"90".equals(s.getStatus())).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(noCancelList)) {
                    wvHeader.setStatus("00");
                    wmWvHeaderManager.save(wvHeader);
                    continue;
                }
                String max = noCancelList.stream().map(BanQinWmWvDetail::getStatus).max(Comparator.comparing(String::toString)).get();
                String min = noCancelList.stream().map(BanQinWmWvDetail::getStatus).min(Comparator.comparing(String::toString)).get();
                String status = "";
                if (max.equals(min)) {
                    status = max;
                } else {
                    if (Integer.parseInt(max) <= 20) status = "10";
                    else if (Integer.parseInt(max) <= 40) status = "30";
                    else if (Integer.parseInt(max) <= 60) status = "50";
                    else if (Integer.parseInt(max) <= 80) status = "70";
                }
                wvHeader.setStatus(status);
                wmWvHeaderManager.save(wvHeader);
            }
        }
        // 按出库单更新状态
        else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            // 更新波次单
            // wmWvHeaderManager.updateWvHeaderStatusBySo(noList, user.getId(), orgId);
            for (String soNo : noList) {
                List<BanQinWmWvHeader> wmWvHeaders = wmWvHeaderManager.findBySoNo(soNo, orgId);
                for (BanQinWmWvHeader wvHeader : wmWvHeaders) {
                    List<BanQinWmWvDetail> wvDetailList = wmWvDetailManager.findByWaveNo(wvHeader.getWaveNo(), orgId);
                    List<BanQinWmWvDetail> noCancelList = wvDetailList.stream().filter(s -> !"90".equals(s.getStatus())).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(noCancelList)) {
                        wvHeader.setStatus("00");
                        wmWvHeaderManager.save(wvHeader);
                        continue;
                    }
                    String max = noCancelList.stream().map(BanQinWmWvDetail::getStatus).max(Comparator.comparing(String::toString)).get();
                    String min = noCancelList.stream().map(BanQinWmWvDetail::getStatus).min(Comparator.comparing(String::toString)).get();
                    String status = "";
                    if (max.equals(min)) {
                        status = max;
                    } else {
                        if (Integer.parseInt(max) <= 20) status = "10";
                        else if (Integer.parseInt(max) <= 40) status = "30";
                        else if (Integer.parseInt(max) <= 60) status = "50";
                        else if (Integer.parseInt(max) <= 80) status = "70";
                    }
                    wvHeader.setStatus(status);
                    wmWvHeaderManager.save(wvHeader);
                }
            }
        }
    }

    /**
     * 更新出库单行、波次单行、出库单、波次单
     */
    @Transactional
    public void updateOrder(ActionCode action, Double qtyEaOp, String soNo, String linNo, String waveNo, String orgId) throws WarehouseException {
        // 1、出库单明细行更新
        BanQinWmSoDetail wmSoDetailModel = wmSoDetailManager.updateByActionCode(action, qtyEaOp, soNo, linNo, orgId);
        // 构造入参
        List<String> noList = new ArrayList<>();
        noList.add(wmSoDetailModel.getSoNo());// 按出库单更新状态
        // 2、更新出库单状态
        updateSoHeaderStatus(ProcessByCode.BY_SO.getCode(), noList, orgId);
        // 3、更新波次单状态,波次单明细行状态 = 出库单状态
        updateWvDetailStatus(soNo, orgId);
        // 4、更新波次单状态
        updateWvHeaderStatus(ProcessByCode.BY_SO.getCode(), noList, orgId);
        // 更新销售订单的状态
        if (StringUtils.isNotEmpty(wmSoDetailModel.getSaleNo()) && StringUtils.isNotEmpty(wmSoDetailModel.getSaleLineNo())) {
            wmSaleDetailManager.updateByActionCode(action, qtyEaOp, wmSoDetailModel.getSaleNo(), wmSoDetailModel.getSaleLineNo(), orgId);
            updateSaleStatus(Lists.newArrayList(wmSoDetailModel.getSaleNo()), orgId);
        }
    }

    /**
     * 未生成装车单的发运订单装车交接 更新装车状态
     */
    @Transactional
    public void updateLdStatusByNoLd(String soNo, String status, String orgId) throws WarehouseException {
        this.wmSoHeaderManager.updateLdStatusBySoNo(soNo, status, orgId);
        this.wmSoDetailManager.updateLdStatusBySoNo(soNo, status, orgId);
    }

    /**
     * SALE单不为取消或关闭状态时，统计明细状态，更新单头状态
     */
    @Transactional
    public void updateSaleStatus(List<String> saleNos, String orgId) {
        wmSaleHeaderManage.updateWmSaleHeaderStatus(saleNos, UserUtils.getUser().getId(), orgId);
    }

    /**
     * 回填SALE单的已生成SO数
     */
    @Transactional
    public void backfillQtySo(BanQinWmSoDetail wmSoDetailModel) {
        String saleNo = wmSoDetailModel.getSaleNo();
        String saleLineNo = wmSoDetailModel.getSaleLineNo();
        Double qtySoEa = wmSoDetailModel.getQtySoEa();// 定货数
        Double qytShipEa = wmSoDetailModel.getQtyShipEa();// 已发运数
        String orgId = wmSoDetailModel.getOrgId();
        if (StringUtils.isNotEmpty(saleNo) && StringUtils.isNotEmpty(saleLineNo)
                && (WmsCodeMaster.SO_NEW.getCode().equals(wmSoDetailModel.getStatus())// 删除，取消订单时
                || WmsCodeMaster.SO_PART_SHIPPING.getCode().equals(wmSoDetailModel.getStatus()))// 关闭订单时，回填未发运的数。
        ) {
            BanQinWmSaleDetail wmSaleDetailModel = wmSaleDetailManager.findBySaleNoAndLineNo(saleNo, saleLineNo, orgId);
            if (null != wmSaleDetailModel) {
                Double qtySoSale = wmSaleDetailModel.getQtySoEa();// 已生成SO数
                wmSaleDetailModel.setQtySoEa(qtySoSale - (qtySoEa - qytShipEa));
                wmSaleDetailManager.save(wmSaleDetailModel);
            }
        }
    }

    /**
     * 更新打包状态
     */
    @Transactional
    public void updatePackStatus(BanQinWmSoEntity wmSoEntity) {
        List<BanQinWmSoDetail> detailList = wmSoDetailManager.findBySoNo(wmSoEntity.getSoNo(), wmSoEntity.getOrgId());
        List<BanQinWmSoAlloc> allocList = wmSoAllocService.getBySoNo(wmSoEntity.getSoNo(), wmSoEntity.getOrgId());
        long hasPack = allocList.stream().filter(s -> null != s.getPackTime()).count();
        long hasPick = detailList.stream().filter(d -> WmsCodeMaster.SO_FULL_PICKING.getCode().equals(d.getStatus())).count();
        String packStatus = WmsCodeMaster.PACK_NEW.getCode();
        if (CollectionUtil.isNotEmpty(allocList)) {
            if (hasPick == detailList.size() && hasPack == allocList.size()) {
                packStatus = WmsCodeMaster.PACK_FULL.getCode();
            } else if (hasPack == 0) {
                packStatus = WmsCodeMaster.PACK_NEW.getCode();
            } else {
                packStatus = WmsCodeMaster.PACK_PART.getCode();
            }
        }
        wmSoEntity.setPackStatus(packStatus);
        wmSoHeaderManager.saveSoHeader(wmSoEntity);
    }

}