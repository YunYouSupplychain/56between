package com.yunyou.modules.wms.inbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerial;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerialEntity;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmAsnSerialMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 入库序列Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAsnSerialService extends CrudService<BanQinWmAsnSerialMapper, BanQinWmAsnSerial> {
    @Autowired
    @Lazy
    private BanQinWmInvSerialService banQinWmInvSerialService;
    @Autowired
    @Lazy
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;

    public BanQinWmAsnSerial get(String id) {
        return super.get(id);
    }

    public List<BanQinWmAsnSerial> findList(BanQinWmAsnSerial banQinWmAsnSerial) {
        return super.findList(banQinWmAsnSerial);
    }

    public Page<BanQinWmAsnSerialEntity> findPage(Page page, BanQinWmAsnSerialEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmAsnSerial banQinWmAsnSerial) {
        super.save(banQinWmAsnSerial);
    }

    @Transactional
    public void delete(BanQinWmAsnSerial banQinWmAsnSerial) {
        super.delete(banQinWmAsnSerial);
    }

    /**
     * 描述： 保存序列号
     *
     * @param wmAsnSerialEntities
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage saveAllSerial(List<BanQinWmAsnSerialEntity> wmAsnSerialEntities) {
        ResultMessage msg = new ResultMessage();
        // 过滤掉状态为DELETE的model
//        wmAsnSerialEntities = ArrayUtils.filterDelete(wmAsnSerialEntities);
        Map<String, List<BanQinWmAsnSerialEntity>> map = wmAsnSerialEntities.stream().collect(Collectors.groupingBy(o -> o.getAsnNo() + "@@" + o.getOrgId()));
        for (Map.Entry<String, List<BanQinWmAsnSerialEntity>> entry : map.entrySet()) {
            String[] asnNoAndOrgId = entry.getKey().split("@@");
            String asnNo = asnNoAndOrgId[0];
            for (BanQinWmAsnSerialEntity wmAsnSerialEntity : wmAsnSerialEntities) {
                // 校验序列号在入库单中唯一
                BanQinWmAsnSerial wmAsnSerial = getAsnSerial(asnNo, wmAsnSerialEntity.getOwnerCode(), wmAsnSerialEntity.getSkuCode(), wmAsnSerialEntity.getSerialNo(), wmAsnSerialEntity.getOrgId());
                if (wmAsnSerial != null) {
                    msg.addMessage(wmAsnSerialEntity.getSerialNo() + "商品" + wmAsnSerialEntity.getSkuCode() + "序列号已存在");// 序列号已存在
                    msg.setSuccess(false);
                    return msg;
                }
                this.save(wmAsnSerialEntity);
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 保存序列号
     */
    @Transactional
    public ResultMessage saveEntity(BanQinWmAsnSerialEntity wmAsnSerialEntity) {
        ResultMessage msg = new ResultMessage();
        // 校验序列号在入库单中唯一
        BanQinWmAsnSerial wmAsnSerial = getAsnSerial(wmAsnSerialEntity.getAsnNo(), wmAsnSerialEntity.getOwnerCode(), wmAsnSerialEntity.getSkuCode(), wmAsnSerialEntity.getSerialNo(), wmAsnSerialEntity.getOrgId());
        if (wmAsnSerial != null) {
            msg.addMessage(wmAsnSerialEntity.getSerialNo() + "商品" + wmAsnSerialEntity.getSkuCode() + "序列号已存在");
            msg.setSuccess(false);
            return msg;
        }
        this.save(wmAsnSerialEntity);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 描述： 收货确认时，保存序列号
     *
     * @param asnNo
     * @param ownerCode
     * @param skuCode
     * @param rcvLotnum
     * @param wmAsnSerialEntity
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void saveRcvSerial(String asnNo, String ownerCode, String skuCode, String rcvLotnum, BanQinWmAsnSerialEntity wmAsnSerialEntity) throws WarehouseException {
        BanQinWmAsnSerial wmAsnSerialModel = new BanQinWmAsnSerial();
        // 是否要求按照计划入库的序列号做扫描收货
        String ASN_IS_PLAN_SERIAL = WmsConstants.NO;
        String serialNo = wmAsnSerialEntity.getSerialNo();
        BanQinWmAsnSerial check = this.getAsnSerial(asnNo, ownerCode, skuCode, serialNo, wmAsnSerialEntity.getOrgId());
        // 是否校验导入
        if (WmsConstants.YES.equals(ASN_IS_PLAN_SERIAL)) {
            if (null == check || null == check.getId()) {
                throw new WarehouseException("序列号" + serialNo + "不在计划中");
            }
        }
        // 收货明细行号不为空，表示扫描，进行保存
        if (null != wmAsnSerialEntity.getRcvLineNo()) {
            if (null != check && null != check.getId()) {
                if (!WmsCodeMaster.SERIAL_PLAN_NOT_SCAN.getCode().equals(check.getStatus())) {
                    throw new WarehouseException(serialNo + "序列号已存在");// 序列号已存在
                } else {
                    check.setStatus(WmsCodeMaster.SERIAL_PLAN_SCAN.getCode());
                    check.setScanOp(wmAsnSerialEntity.getScanOp());
                    check.setScanTime(wmAsnSerialEntity.getScanTime());
                    check.setRcvLineNo(wmAsnSerialEntity.getRcvLineNo());
                    BeanUtils.copyProperties(check, wmAsnSerialModel);
                }
            } else {
                BeanUtils.copyProperties(wmAsnSerialEntity, wmAsnSerialModel);
                wmAsnSerialModel.setStatus(WmsCodeMaster.SERIAL_NOT_PLAN_SCAN.getCode());
            }
            wmAsnSerialModel.setLotNum(rcvLotnum);
            wmAsnSerialModel.setId(IdGen.uuid());
            wmAsnSerialModel.setIsNewRecord(true);
            wmAsnSerialModel.setDataSource("扫描");
            this.save(wmAsnSerialModel);
        }
    }

    /**
     * 删除序列号
     */
    @Transactional
    public ResultMessage removeSerial(String[] serialIds) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorStr = new StringBuilder();
        List<BanQinWmAsnSerial> list = Lists.newArrayList();
        for (String serialId : serialIds) {
            BanQinWmAsnSerial banQinWmAsnSerial = this.get(serialId);
            // 校验是否已存在
            BanQinWmInvSerial check = banQinWmInvSerialService.getByOwnerCodeAndSkuCodeAndSerialNo(banQinWmAsnSerial.getOwnerCode(), banQinWmAsnSerial.getSkuCode(), banQinWmAsnSerial.getSerialNo(), banQinWmAsnSerial.getOrgId());
            if (null != check && check.getId() != null) {
                errorStr.append(banQinWmAsnSerial.getSerialNo()).append("\n");
            } else {
                list.add(banQinWmAsnSerial);
            }
        }
        if (StringUtils.isNotEmpty(errorStr.toString())) {
            msg.setSuccess(false);
            msg.setMessage(errorStr + "序列号已收货，不能操作");
        } else {
            this.deleteAll(list);;
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 查询符合条件的序列号
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnSerial> getByAsnNo(String asnNo, String orgId) {
        BanQinWmAsnSerial model = new BanQinWmAsnSerial();
        model.setAsnNo(asnNo);
        model.setOrgId(orgId);
        return mapper.findList(model);
    }

    /**
     * 描述： 查询符合条件的序列号
     *
     * @param asnNo
     * @param ownerCode
     * @param skuCode
     * @param serialNo
     * @author Jianhua on 2019/1/28
     */
    public BanQinWmAsnSerial getAsnSerial(String asnNo, String ownerCode, String skuCode, String serialNo, String orgId) {
        BanQinWmAsnSerial model = new BanQinWmAsnSerial();
        model.setAsnNo(asnNo);
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setSerialNo(serialNo);
        model.setOrgId(orgId);
        List<BanQinWmAsnSerial> list = mapper.findList(model);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Description :按单号和收货行号删除(删除无计划扫描的)
     *
     * @param asnNo
     * @param lineNo
     * @return
     * @Author: Ramona.Wang
     * @Create Date: 2014-10-27
     */
    @Transactional
    public void removeByAsnNoAndRcvLineNo(String asnNo, String lineNo, String orgId) {
        mapper.removeByAsnNoAndRcvLineNo(asnNo, lineNo, orgId);
    }

    /**
     * 描述： 取消收货时，更新计划已扫描的序列号
     *
     * @param asnNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void updateSerialForCancelRcv(String asnNo, String lineNo, String orgId) {
        mapper.updateSerialForCancelRcv(asnNo, lineNo, orgId);
    }

    /**
     * 描述： 按单号删除
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void removeByAsnNo(String asnNo, String orgId) {
        mapper.removeByAsnNo(asnNo, orgId);
    }

    /**
     * 序列号导入
     * @param list
     * @param orgId
     * @param ownerCode
     * @return
     */
    @Transactional
    public ResultMessage importSerial(List<BanQinWmAsnSerial> list, String orgId, String ownerCode, String asnNo) {
        ResultMessage msg = new ResultMessage();
        StringBuffer errorMsg = new StringBuffer();
        List<String> skuCodeList = Lists.newArrayList();
        List<String> skuAndSerial = Lists.newArrayList();
        List<BanQinWmAsnSerial> serialList = Lists.newArrayList();
        for (int i = 0, size = list.size(); i < size; i++) {
            BanQinWmAsnSerial row = list.get(i);
            if (null == row || (StringUtils.isEmpty(row.getSkuCode()) && (StringUtils.isEmpty(row.getSerialNo())))) {
                break;
            }
            StringBuffer rowMsg = new StringBuffer();
            if (StringUtils.isEmpty(row.getSkuCode())) {
                rowMsg.append("商品不能为空;");
            }
            if (StringUtils.isEmpty(row.getSerialNo())) {
                rowMsg.append("序列号不能为空;");
            }
            if (StringUtils.isNotEmpty(row.getSkuCode()) && !skuCodeList.contains(row.getSkuCode())) {
                skuCodeList.add(row.getSkuCode());
            }
            String key = row.getSerialNo()+ "@@" + row.getSkuCode();
            if (StringUtils.isNotEmpty(row.getSkuCode()) && StringUtils.isNotEmpty(row.getSerialNo()) && !skuAndSerial.contains(key)) {
                BanQinWmAsnHeader asnHeader = banQinWmAsnHeaderService.getByAsnNo(asnNo, orgId);
                BanQinWmAsnSerial wmAsnSerialModel = new BanQinWmAsnSerial();
                wmAsnSerialModel.setAsnNo(asnNo);
                wmAsnSerialModel.setOwnerCode(ownerCode);
                wmAsnSerialModel.setStatus(WmsCodeMaster.SERIAL_PLAN_NOT_SCAN.getCode());
                wmAsnSerialModel.setDataSource("EXCEL");
                wmAsnSerialModel.setSkuCode(row.getSkuCode());
                wmAsnSerialModel.setSerialNo(row.getSerialNo());
                wmAsnSerialModel.setOrgId(orgId);
                wmAsnSerialModel.setHeadId(asnHeader.getId());
                serialList.add(wmAsnSerialModel);
                skuAndSerial.add(key);
            }
            if (StringUtils.isNotEmpty(rowMsg.toString())) {
                errorMsg.append("行号" + (i + 1) + ":" + rowMsg.toString() + "<br>");
            }
        }

        // 判断商品是否存在
        if (CollectionUtil.isNotEmpty(skuCodeList)) {
            StringBuilder skuCodeString = new StringBuilder();
            for (String skuCode : skuCodeList) {
                skuCodeString.append("'").append(skuCode).append("',");
            }
            skuCodeString.deleteCharAt(skuCodeString.length() - 1);
            String sql = "SELECT DISTINCT\n" +
                    "\twadr.sku_code \n" +
                    "FROM\n" +
                    "\twm_asn_detail_receive wadr\n" +
                    "\tLEFT JOIN cd_wh_sku cws ON cws.sku_code = wadr.sku_code \n" +
                    "\tAND cws.owner_code = wadr.owner_code \n" +
                    "\tAND cws.org_id = wadr.org_id \n" +
                    "WHERE\n" +
                    "\t1 = 1 \n" +
                    "\tAND wadr.STATUS = '00' \n" +
                    "\tAND cws.is_serial = 'Y' \n" +
                    "\tAND wadr.sku_code IN (" + skuCodeString.toString() + ")\n" +
                    "\tAND wadr.asn_no = '" + asnNo + "'\n" +
                    "\tAND wadr.org_id = '" + orgId + "'\n";

            List<Object> items = mapper.execSelectSql(sql);
            for (Object item : items) {
                skuCodeList.remove(item.toString());
            }
            // 不存在的SKU
            if (skuCodeList.size() > 0) {
                errorMsg.append("商品");
                for (int i = 0; i < skuCodeList.size(); i++) {
                    errorMsg.append(skuCodeList.get(i));
                    if (i < skuCodeList.size() - 1) {
                        errorMsg.append(",");
                    }
                    if ((i + 1) % 5 == 0) {
                        errorMsg.append("\n");
                    }
                }
                errorMsg.append("不在此订单中，或不需要序列号管理<br>");
            }
        }
        ResultMessage checkMsg = this.checkAsnSerial(asnNo, orgId, skuAndSerial);
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            errorMsg.append(checkMsg.getMessage() + "序列号已存在");
        }
        // 校验失败
        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            msg.setMessage(errorMsg.toString());
            msg.setSuccess(false);
            return msg;
        } else {
            for (BanQinWmAsnSerial asnSerial : serialList) {
                this.save(asnSerial);
            }
            msg.setSuccess(true);
            msg.setMessage("导入成功");
        }

        return msg;
    }

    /**
     * 校验序列号在入库单中唯一
     * @param asnNo
     * @param serialNos
     * @return
     */
    public ResultMessage checkAsnSerial(String asnNo, String orgId, List<String> serialNos) {
        ResultMessage msg = new ResultMessage();
        StringBuilder serialNoAndSku = new StringBuilder("");
        for (String serialNo : serialNos) {
            String array[] = serialNo.split("@@", -1);
            serialNoAndSku.append("('").append(array[0]).append("','").append(array[1]).append("'),");
        }
        if (serialNoAndSku.length() > 0) {
            serialNoAndSku.deleteCharAt(serialNoAndSku.length() - 1);
        }
        List<BanQinWmAsnSerial> list = mapper.checkAsnSerialQuery(asnNo, orgId, serialNoAndSku.toString());
        if (list.size() > 0) {
            String str = "";
            for (BanQinWmAsnSerial item : list) {
                str = str + item.getSerialNo() + "商品" + item.getSkuCode() + "\n";
                serialNos.remove(item.getSerialNo() + "@@" + item.getSkuCode());
            }
            msg.addMessage(str);
        }
        msg.setData(serialNos);
        return msg;
    }
}