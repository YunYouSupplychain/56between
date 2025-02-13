package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述： 加工管理- 加工确认 (批量)
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitBatchConfirmService {
    @Autowired
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinKitConfirmService banQinKitConfirmService;

    public ResultMessage kitBatchConfirm(List<BanQinWmKitResultDetailEntity> entitys, List<BanQinWmTaskKitEntity> taskEntitys, String subSelectCode) {
        ResultMessage msg = new ResultMessage();
        try {
            // 循环父件加工结果
            for (BanQinWmKitResultDetailEntity entity : entitys) {
                try {
                    // 加工子件明细,
                    List<BanQinWmKitSubDetail> subModels = banQinWmKitSubDetailService.getByKitNoAndParentLineNo(entity.getKitNo(), entity.getParentLineNo(), entity.getOrgId());
                    if (subModels.size() == 0) {
                        msg.setSuccess(false);
                        msg.addMessage("[" + entity.getKitNo() + "][" + entity.getKitLineNo() + "]未生成子件，不能操作");
                        continue;
                    }
                    List<BanQinWmTaskKitEntity> taskKitEntitys = Lists.newArrayList();
                    Double mixNum = Double.MAX_VALUE;// 获取可加工数
                    boolean mixNumFlag = false;// 重新统计操作标记
                    if (entity.getQtyCurrentKitEa() == null || entity.getQtyCurrentKitEa() == 0D) {
                        mixNumFlag = true;
                        // 校验是否足够加工
                        for (BanQinWmKitSubDetail subModel : subModels) {
                            if (subModel.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                                // 如果是辅料，不参与计算
                                continue;
                            }
                            // 校验是否足够加工,拣货数>= 需加工数
                            if (WmsCodeMaster.SUB_KIT_RT.getCode().equals(entity.getSubSelectCode())) {
                                // 子件指定规则：根据库存周转规则
                                mixNumFlag = true;
                                // 如果可加工数为空，或者为0，那么重新计算可加数
                                Double temp = subModel.getQtyPkEa() / subModel.getQtyBomEa();
                                // 向下取整
                                temp = Math.floor(temp);
                                if (temp <= mixNum) {
                                    mixNum = temp;
                                }
                                // 获取加工任务,按库存周转规则，并且未完全加工的记录
                                taskKitEntitys.addAll(banQinKitConfirmService.getTaskKitEntitysByRotation(subModel));// 加工任务
                            }
                        }
                        if (mixNumFlag) {
                            if (mixNum == 0D) {
                                msg.setSuccess(false);
                                msg.addMessage(entity.getKitLineNo() + "可加工子件不足,不能操作");
                                continue;
                            }
                            if (mixNum.compareTo(entity.getQtyPlanEa()) < 0) {
                                entity.setQtyCurrentKitEa(mixNum);
                            } else {
                                entity.setQtyCurrentKitEa(entity.getQtyPlanEa());
                            }
                            entity.setQtyCurrentKitUom(entity.getQtyCurrentKitEa() / entity.getUomQty());
                        }
                    } else {
                        // 不需要重新统计
                        // 校验是否足够加工
                        for (BanQinWmKitSubDetail subModel : subModels) {
                            if (subModel.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                                // 如果是辅料，不参与计算
                                continue;
                            }
                            Double subKitEa = 0D;// 需加工数
                            // 校验是否足够加工,拣货数>= 需加工数
                            if (entity.getSubSelectCode().equals(WmsCodeMaster.SUB_KIT_MAN.getCode())) {
                                subKitEa = subModel.getQtyBomEa() * entity.getQtyCurrentKitEa();// 需加工数
                                // 子件指定规则:人工指定
                                Double allTaskKitEa = 0D;
                                for (BanQinWmTaskKitEntity taskEntity : taskEntitys) {
                                    if (taskEntity.getSubLineNo().equals(subModel.getSubLineNo())) {
                                        allTaskKitEa += taskEntity.getQtyEa();
                                    }
                                }
                                if (allTaskKitEa.compareTo(subKitEa) < 0) {
                                    msg.setSuccess(false);
                                    msg.addMessage(entity.getKitLineNo() + "可加工子件不足,不能操作");
                                    break;
                                }
                                taskKitEntitys.addAll(taskEntitys);// 加工任务
                            } else if (entity.getSubSelectCode().equals(WmsCodeMaster.SUB_KIT_RT.getCode())) {
                                // 子件指定规则：根据库存周转规则
                                // 如果可加工数大于0，那么校验
                                subKitEa = subModel.getQtyBomEa() * entity.getQtyCurrentKitEa();// 需加工数
                                // 校验是否足够加工,拣货数>= 需加工数
                                if (subModel.getQtyPkEa().compareTo(subKitEa) < 0) {
                                    msg.setSuccess(false);
                                    msg.addMessage(entity.getKitLineNo() + "可加工子件不足,不能操作");
                                    break;
                                }
                                // 获取加工任务,按库存周转规则，并且未完全加工的记录
                                taskKitEntitys.addAll(banQinKitConfirmService.getTaskKitEntitysByRotation(subModel));// 加工任务
                            }
                        }
                    }
                    if (taskKitEntitys.size() > 0) {
                        banQinKitConfirmService.kitConfirm(entity, subModels, taskKitEntitys);
                    }
                } catch (WarehouseException e) {
                    msg.setSuccess(false);
                    msg.addMessage(e.getMessage());
                }
            }
        } catch (Throwable e) {
            // 框架控制，如数据过期，抛出提示
            throw new RuntimeException(e.getMessage());
        } finally {
            // 操作成功提示信息
            if (StringUtils.isBlank(msg.getMessage())) {
                msg.addMessage("操作成功");
                msg.setSuccess(true);
            }
        }
        return msg;
    }

}
