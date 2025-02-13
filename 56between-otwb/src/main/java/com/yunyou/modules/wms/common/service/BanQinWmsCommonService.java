package com.yunyou.modules.wms.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import com.yunyou.modules.wms.common.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公共类 
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmsCommonService {
    @Autowired
    private BanQinCdWhPackageService cdWhPackageService;

    /**
     * Description :除法，精确8位小数位
     *
     * @param double1 除数
     * @param double2 被除数
     */
    public Double doubleDivide(Double double1, Double double2) {
        return BigDecimal.valueOf(double1 == null ? 1 : double1).divide(BigDecimal.valueOf(double2 == null ? 1 : double2), 8, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * Description :根据包装代码查询包装明细
     *
     * @param packCode 包装代码
     * @param orgId    机构ID
     */
    public BanQinCdWhPackageEntity getCdWhPackageRelation(String packCode, String orgId) {
        BanQinCdWhPackageEntity packageEntity = cdWhPackageService.findByPackageCode(packCode, orgId);
        if (null == packageEntity) {
            throw new WarehouseException("查询不到包装代码" + packCode + "明细信息");
        }
        // 序号倒序
        List<BanQinCdWhPackageRelation> cdWhPackageRelations = packageEntity.getCdWhPackageRelations().stream().sorted(Comparator.comparing(BanQinCdWhPackageRelation::getCdprSequencesNo).reversed()).collect(Collectors.toList());
        packageEntity.setCdWhPackageRelations(cdWhPackageRelations);
        return packageEntity;
    }

    /**
     * 根据包装代码、获取包装明细单位换算信息
     *
     * @param packCode 包装代码
     * @param uom      包装单位
     * @param orgId    机构ID
     */
    public BanQinCdWhPackageRelationEntity getPackageRelationAndQtyUom(String packCode, String uom, String orgId) {
        return getPackageRelationAndQtyUom(packCode, uom, null, orgId);
    }

    /**
     * 根据包装代码、获取包装明细单位换算信息 及 换算单位数量
     *
     * @param packCode 包装代码
     * @param uom      包装单位
     * @param qtyEa    EA数量
     * @param orgId    机构ID
     */
    public BanQinCdWhPackageRelationEntity getPackageRelationAndQtyUom(String packCode, String uom, Double qtyEa, String orgId) {
        BanQinCdWhPackageRelationEntity entity = null;
        BanQinCdWhPackageEntity packageEntity = getCdWhPackageRelation(packCode, orgId);
        for (BanQinCdWhPackageRelation item : packageEntity.getCdWhPackageRelations()) {
            if (uom.equals(item.getCdprUnit())) {
                entity = new BanQinCdWhPackageRelationEntity();
                BeanUtils.copyProperties(item, entity);
                entity.setCdprQuantity(item.getCdprQuantity().intValue());
                // 单位换算，根据包装代码获取包装单位换算信息，根据EA数量及单位 换算出单位数量
                if (qtyEa != null) {
                    int unitQty = item.getCdprQuantity().intValue();
                    Double qtyUom = doubleDivide(qtyEa, (double) unitQty);
                    entity.setQtyUom(qtyUom);
                }
                break;
            }
        }
        if (entity == null) {
            throw new WarehouseException("查询不到包装单位信息");
        }
        return entity;
    }

    /**
     * Description :根据包装代码、获取包装明细单位换算信息 及 换算单位数量
     *
     * @param packageRelationList 包装代码明细信息
     * @param uom                 包装单位
     * @param qtyEa               EA数量
     */
    public BanQinCdWhPackageRelationEntity getPackageRelationAndQtyUom(List<BanQinCdWhPackageRelation> packageRelationList, String uom, Double qtyEa) {
        BanQinCdWhPackageRelationEntity entity = null;
        for (BanQinCdWhPackageRelation item : packageRelationList) {
            if (uom.equals(item.getCdprUnit())) {
                entity = new BanQinCdWhPackageRelationEntity();
                BeanUtils.copyProperties(item, entity);
                entity.setCdpaCode(item.getPmCode());
                entity.setCdprQuantity(item.getCdprQuantity().intValue());
                if (qtyEa != null) {
                    int unitQty = item.getCdprQuantity().intValue();
                    Double qtyUom = doubleDivide(qtyEa, (double) unitQty);
                    entity.setQtyUom(qtyUom);
                }
                break;
            }
        }
        if (entity == null) {
            throw new WarehouseException("查询不到包装单位信息");
        }
        return entity;
    }

    /**
     * Description : 出库效期校验
     *
     * @param isValidity  是否有效期控制
     * @param shelfLife   保质期
     * @param outLifeDays 出库效期
     * @param lotAtt01    生产日期
     * @param lotAtt02    失效日期
     * @return true: 不进行效期控制/在效期控制范围内 false:不在效期控制范围内，不允许出库
     */
    public boolean checkOutValidity(String isValidity, Double shelfLife, String liftType, Double outLifeDays, Date lotAtt01, Date lotAtt02) {
        // 当前日间
        Date currentTime = new Date();
        // 1、批次属性 生产日期、失效日期不为空
        if (lotAtt01 == null || lotAtt02 == null) {
            return true;
        }
        // 2、是否有效期控制必须为Y
        if (isValidity == null || isValidity.equals(WmsConstants.NO)) {
            return true;
        }
        // 3、效期类型=生产日期P/失效日期E
        if (liftType == null || ((!liftType.equalsIgnoreCase(WmsCodeMaster.LIFE_TYPE_P.getCode())) && !liftType.equalsIgnoreCase(WmsCodeMaster.LIFE_TYPE_E.getCode()))) {
            return true;
        }
        // 4、出库效期天数>=0
        if (outLifeDays == null) {
            return true;
        }
        // 5、 出库效期，生产日期P/失效日期E，不可预配、分配 当前日期-生产日期>出库效期(单位：天) ，则false
        // 失效日期-当前日期<出库效期(单位：天) ，则false
        if (liftType.equalsIgnoreCase(WmsCodeMaster.LIFE_TYPE_P.getCode())) {
            // 生产天数=当前日期-生产日期
            long inDays = (currentTime.getTime() - lotAtt01.getTime()) / (24 * 60 * 60 * 1000);
            return inDays <= outLifeDays;
        } else if (liftType.equalsIgnoreCase(WmsCodeMaster.LIFE_TYPE_E.getCode())) {
            // 失效天数=失效日期-当前日期
            long outDays = (lotAtt02.getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000);
            return outDays >= outLifeDays;
        }
        return true;
    }

    /**
     * Description :生产日期、失效日期的逻辑校验
     *
     * @param lotAtt01 生产日期
     * @param lotAtt02 失效日期
     * @param lotAtt03 入库日期
     */
    public void checkProductAndExpiryDate(Date lotAtt01, Date lotAtt02, Date lotAtt03) {
        // 生产日期 > 当前日期
        if (null != lotAtt01) {
            if (lotAtt01.after(new Date())) {
                throw new WarehouseException("生产日期不能大于当前日期");
            }
        }
        // 生产日期 > 失效日期
        if (null != lotAtt01 && null != lotAtt02) {
            if (lotAtt01.after(lotAtt02)) {
                throw new WarehouseException("生产日期不能大于失效日期");
            }
        }
        // 入库日期>失效日期
        if (null != lotAtt02 && null != lotAtt03) {
            if (lotAtt03.after(lotAtt02)) {
                throw new WarehouseException("入库日期不能大于失效日期");
            }
        }
    }

    /**
     * Description :计算两个时间相差的天数
     *
     * @param date1 开始时间
     * @param date2 结束时间
     */
    public Long dateDiff(Date date1, Date date2) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date1 = simpleDateFormat.parse(simpleDateFormat.format(date1));
            date2 = simpleDateFormat.parse(simpleDateFormat.format(date2));
            long diff = date2.getTime() - date1.getTime();
            return diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 根据日期和天数，计算距离该日期的前后几天的日期。
     *
     * @param fmDate 日期
     * @param diff   相差天数
     */
    public Date getNextDay(Date fmDate, int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(fmDate); // 设置当前日期
        c.add(Calendar.DATE, diff); // 日期加diff
        return c.getTime();
    }

    /**
     * Description :求两个数组的差集
     *
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 数组差集
     */
    public Object[] minus(Object[] arr1, Object[] arr2) {
        LinkedList<Object> list = new LinkedList<>();
        LinkedList<Object> history = new LinkedList<>();
        Object[] longerArr = arr1;
        Object[] shorterArr = arr2;
        // 找出较长的数组来减较短的数组
        if (arr1.length > arr2.length) {
            longerArr = arr2;
            shorterArr = arr1;
        }
        for (Object str : longerArr) {
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (Object str : shorterArr) {
            if (list.contains(str)) {
                history.add(str);
                list.remove(str);
            } else {
                if (!history.contains(str)) {
                    list.add(str);
                }
            }
        }
        return list.toArray();
    }

    /**
     * Description :库存周转规则配置拼SQL方法
     */
    public BanQinCdRuleRotationSqlEntity getRuleRotationSql(BanQinCdRuleRotationSqlParamEntity sqlParamEntity) {
        BanQinCdRuleRotationSqlEntity sqlEntity = new BanQinCdRuleRotationSqlEntity();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<BanQinCdRuleRotationDetail> ruleRotationDetailList = sqlParamEntity.getRuleRotationDetailList();
        // 排序
        String orderBySql = "";
        // where条件
        StringBuilder whereSql = new StringBuilder();
        // WM_INV_LOT_ATT 表
        String invLotAtt = "";
        // 如果没有库存周转规则明细，那么默认按批次号升序
        if (ruleRotationDetailList.size() == 0) {
            // 库存批次属性01-12 字段
            String lotAtt;
            String lotAttValue;
            // 如果出库单明细批次属性不为空，那么进行精确查询
            if (sqlParamEntity.getLotAtt01() != null) {
                lotAtt = WmsConstants.LOT_ATT01;
                lotAttValue = format.format(sqlParamEntity.getLotAtt01());
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt02() != null) {
                lotAtt = WmsConstants.LOT_ATT02;
                lotAttValue = format.format(sqlParamEntity.getLotAtt02());
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt03() != null) {
                lotAtt = WmsConstants.LOT_ATT03;
                lotAttValue = format.format(sqlParamEntity.getLotAtt03());
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt04() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt04())) {
                lotAtt = WmsConstants.LOT_ATT04;
                lotAttValue = sqlParamEntity.getLotAtt04();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt05() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt05())) {
                lotAtt = WmsConstants.LOT_ATT05;
                lotAttValue = sqlParamEntity.getLotAtt05();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt06() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt06())) {
                lotAtt = WmsConstants.LOT_ATT06;
                lotAttValue = sqlParamEntity.getLotAtt06();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt07() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt07())) {
                lotAtt = WmsConstants.LOT_ATT07;
                lotAttValue = sqlParamEntity.getLotAtt07();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt08() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt08())) {
                lotAtt = WmsConstants.LOT_ATT08;
                lotAttValue = sqlParamEntity.getLotAtt08();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt09() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt09())) {
                lotAtt = WmsConstants.LOT_ATT09;
                lotAttValue = sqlParamEntity.getLotAtt09();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt10() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt10())) {
                lotAtt = WmsConstants.LOT_ATT10;
                lotAttValue = sqlParamEntity.getLotAtt10();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt11() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt11())) {
                lotAtt = WmsConstants.LOT_ATT11;
                lotAttValue = sqlParamEntity.getLotAtt11();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            if (sqlParamEntity.getLotAtt12() != null && StringUtils.isNotEmpty(sqlParamEntity.getLotAtt12())) {
                lotAtt = WmsConstants.LOT_ATT12;
                lotAttValue = sqlParamEntity.getLotAtt12();
                whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(" = '").append(lotAttValue).append("'");
            }
            // 如果查询条件为空，那么默认按批次号排序
            if (StringUtils.isBlank(whereSql.toString())) {
                orderBySql += " LOT_NUM asc ";
            }
        } else {// 库存周转规则有明细
            for (BanQinCdRuleRotationDetail model : ruleRotationDetailList) {
                // 库存批次属性01-12 字段
                String lotAtt = null;
                // 出库单明细行 批次属性4-12值
                String lotAttValue = null;
                // 出库单明细行 批次属性1-3值，日期类型
                // 将库存周转规则明细批次属性代码 和 库存批次属性 相对应
                // 并且获取出库单明细行对应批次属性值
                if (WmsConstants.LOT_ATT01.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT01;
                    // 日期类型
                    if (sqlParamEntity.getLotAtt01() != null) {
                        lotAttValue = format.format(sqlParamEntity.getLotAtt01());
                    }
                } else if (WmsConstants.LOT_ATT02.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT02;
                    if (sqlParamEntity.getLotAtt02() != null) {
                        lotAttValue = format.format(sqlParamEntity.getLotAtt02());
                    }
                } else if (WmsConstants.LOT_ATT03.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT03;
                    if (sqlParamEntity.getLotAtt03() != null) {
                        lotAttValue = format.format(sqlParamEntity.getLotAtt03());
                    }
                } else if (WmsConstants.LOT_ATT04.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT04;
                    if (sqlParamEntity.getLotAtt04() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt04();
                    }
                } else if (WmsConstants.LOT_ATT05.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT05;
                    if (sqlParamEntity.getLotAtt05() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt05();
                    }
                } else if (WmsConstants.LOT_ATT06.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT06;
                    if (sqlParamEntity.getLotAtt06() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt06();
                    }
                } else if (WmsConstants.LOT_ATT07.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT07;
                    if (sqlParamEntity.getLotAtt07() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt07();
                    }
                } else if (WmsConstants.LOT_ATT08.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT08;
                    if (sqlParamEntity.getLotAtt08() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt08();
                    }
                } else if (WmsConstants.LOT_ATT09.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT09;
                    if (sqlParamEntity.getLotAtt09() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt09();
                    }
                } else if (WmsConstants.LOT_ATT10.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT10;
                    if (sqlParamEntity.getLotAtt10() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt10();
                    }
                } else if (WmsConstants.LOT_ATT11.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT11;
                    if (sqlParamEntity.getLotAtt11() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt11();
                    }
                } else if (WmsConstants.LOT_ATT12.equals(model.getLotAtt())) {
                    lotAtt = WmsConstants.LOT_ATT12;
                    if (sqlParamEntity.getLotAtt12() != null) {
                        lotAttValue = sqlParamEntity.getLotAtt12();
                    }
                }
                // 条件等号
                String sign = null;
                // 库存周转规则，精确查询
                if (model.getOrderBy().equals(WmsCodeMaster.ORDER_BY_EQUAL.getCode())) {
                    // 精确查询 =
                    sign = "=";
                    // 参数批次属性值不为空，则写查询条件
                    if (lotAttValue != null) {
                        whereSql.append(" and ").append(invLotAtt).append(lotAtt).append(sign).append("'").append(lotAttValue).append("'");
                    }
                } else {
                    // 库存周转规则，不精确查询，并且按升/降序排序
                    String descAsc = "";
                    // 库存周转规则，条件查询
                    if (model.getOrderBy().equals(WmsCodeMaster.ORDER_BY_ASC.getCode())) {
                        // 不精确查询,升序>=,ASC
                        sign = ">=";
                        descAsc = "ASC";
                    } else if (model.getOrderBy().equals(WmsCodeMaster.ORDER_BY_DESC.getCode())) {
                        // 不精确查询,降序<=,DESC
                        sign = "<=";
                        descAsc = "DESC";
                    }
                    // 参数批次属性值不为空，则写查询条件
                    if (lotAttValue != null) {
                        // 条件
                        whereSql.append(" and (").append(invLotAtt).append(lotAtt).append(sign).append("'").append(lotAttValue).append("'").append(" or ").append(invLotAtt).append(lotAtt).append(" is null)");
                    }
                    // 拼Order By SQL语句
                    if (StringUtils.isBlank(orderBySql)) {
                        orderBySql += invLotAtt + lotAtt + " " + descAsc;
                    } else {
                        orderBySql += ", " + invLotAtt + lotAtt + " " + descAsc;
                    }
                }
            }
        }
        sqlEntity.setOrderBySql(orderBySql);
        sqlEntity.setWhereSql(whereSql.toString());
        return sqlEntity;
    }

    /**
     * Description :行号数值型转定长的字符串
     */
    public String getLineNo(Integer lineCount, int length) {
        String lineNo = getString(length - 1) + "1";
        if (lineCount > 0) {
            lineNo = getString(length - lineCount.toString().length()) + lineCount.toString();
        }
        return lineNo;
    }

    public String getString(int length) {
        StringBuilder str00 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str00.append("0");
        }
        return str00.toString();
    }

    public static String getResultMessage(String message) {
        return StringUtils.isNotBlank(message) ? message.replace(WmsConstants.DATA_HAS_EXPIRED, WmsConstants.NULL_STRING) : "";
    }

}