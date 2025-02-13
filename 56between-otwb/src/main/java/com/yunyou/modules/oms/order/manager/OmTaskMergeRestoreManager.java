package com.yunyou.modules.oms.order.manager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.order.service.OmTaskDetailService;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作业任务合并、还原Service
 *
 * @author liujianhua
 */
@Service
public class OmTaskMergeRestoreManager {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmTaskAllocManager omTaskAllocManager;
    @Autowired
    private PushTaskService pushTaskService;

    private String nullToCharacter(String str) {
        return StringUtils.isBlank(str) ? "" : str;
    }

    /**
     * 功能描述：任务合并，任务头信息会进行合并产生一个新任务头，明细没有合并规则，只将合并的任务所有明细合集作为新任务的明细
     * <p>
     * 将相同机构、下发仓库、任务类型、业务订单类型、货主、客户、收货人、收货地址的作业任务合并
     *
     * @param list 待合并作业任务
     * @return 返回合并结果List<JSONObject>；含三部分内容：① 合并后的新作业任务 ② 关联的原始任务(需要将新任务的ID绑定到这些任务的关联ID上) ③ 待删除的旧任务(本身是合并任务)
     */
    private List<JSONObject> merge(List<OmTaskHeader> list) {
        List<JSONObject> rsList = Lists.newArrayList();

        // 合并分组维度：机构、下发仓库、任务类型、业务订单类型、货主、客户、收货人、收货地址
        Map<String, List<OmTaskHeader>> map = list.stream().collect(Collectors.groupingBy(
                omTaskHeader -> omTaskHeader.getOrgId()
                        + nullToCharacter(omTaskHeader.getWarehouse())
                        + omTaskHeader.getTaskType()
                        + omTaskHeader.getBusinessOrderType()
                        + omTaskHeader.getOwner()
                        + nullToCharacter(omTaskHeader.getCustomer())
                        + nullToCharacter(omTaskHeader.getConsignee())
                        + nullToCharacter(omTaskHeader.getConsigneeArea())
                        + nullToCharacter(omTaskHeader.getConsigneeAddressArea())
                        + nullToCharacter(omTaskHeader.getConsigneeAddress())));
        for (List<OmTaskHeader> values : map.values()) {
            // 两条及以上才存在合并
            if (values.size() <= 1) {
                continue;
            }
            OmTaskHeader oldTask = values.get(0);
            String chainNos = values.stream().map(OmTaskHeader::getChainNo).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
            String businessNos = values.stream().map(OmTaskHeader::getBusinessNo).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
            String customerNos = values.stream().map(OmTaskHeader::getCustomerNo).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
            String prefixOrderNos = values.stream().map(OmTaskHeader::getDef1).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
            String lotNums = values.stream().map(OmTaskHeader::getLotNum).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));

            OmTaskHeader newTask = new OmTaskHeader();
            BeanUtils.copyProperties(oldTask, newTask);
            newTask.setId("");
            newTask.setCreateDate(new Date());
            newTask.setUpdateDate(new Date());
            newTask.setChainNo(chainNos);
            newTask.setBusinessNo(businessNos);
            newTask.setCustomerNo(customerNos);
            newTask.setDef1(prefixOrderNos);
            newTask.setLotNum(lotNums);
            newTask.setTaskNo(noService.getDocumentNo(GenNoType.OM_TASK_NO.name()));
            newTask.setStatus(OmsConstants.OMS_TASK_STATUS_20);

            List<OmTaskDetail> omTaskDetails = Lists.newArrayList();
            List<String> associatedTaskIds = Lists.newArrayList();
            List<String> delTaskIds = Lists.newArrayList();
            for (OmTaskHeader omTaskHeader : values) {
                List<OmTaskDetail> omTaskDetailList = omTaskHeader.getOmTaskDetailList();
                if (CollectionUtil.isEmpty(omTaskDetailList)) {
                    omTaskDetailList = omTaskDetailService.findList(new OmTaskDetail(null, omTaskHeader.getId()));
                }
                for (OmTaskDetail omTaskDetail : omTaskDetailList) {
                    OmTaskDetail newTaskDetail = new OmTaskDetail();
                    BeanUtils.copyProperties(omTaskDetail, newTaskDetail);
                    newTaskDetail.setId("");
                    newTaskDetail.setHeaderId(null);
                    newTaskDetail.setCreateDate(new Date());
                    newTaskDetail.setUpdateDate(new Date());
                    omTaskDetails.add(newTaskDetail);
                }
                // 根据当前任务是否已经是合并任务（判断依据：是否存在关联任务）
                List<OmTaskHeader> tasks = omTaskHeaderService.getByAssociatedTaskId(omTaskHeader.getId());
                if (CollectionUtil.isEmpty(tasks)) {
                    associatedTaskIds.add(omTaskHeader.getId());
                } else {
                    associatedTaskIds.addAll(tasks.stream().map(OmTaskHeader::getId).collect(Collectors.toList()));
                    delTaskIds.add(omTaskHeader.getId());
                }
            }
            newTask.setOmTaskDetailList(omTaskDetails);

            JSONObject rsMap = new JSONObject();
            rsMap.put("MERGETASK", newTask);
            rsMap.put("ASSOCIATED", associatedTaskIds);
            rsMap.put("REMOVE", delTaskIds);
            rsList.add(rsMap);
        }
        return rsList;
    }

    /**
     * 描述：任务合并
     */
    @Transactional
    public void merge(List<OmTaskHeader> taskList, String orgId) {
        if (CollectionUtil.isEmpty(taskList) || taskList.size() <= 1) {
            return;
        }
        // 符合条件的任务
        List<OmTaskHeader> list = Lists.newArrayList();

        for (OmTaskHeader omTaskHeader : taskList) {
            if (!OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())
                    || !OmsConstants.OMS_TASK_STATUS_20.equals(omTaskHeader.getStatus())) {
                // 非出库任务，忽略
                // 非确认状态，忽略
                continue;
            }
            if (!OmsConstants.TASK_SOURCE_CO.equals(omTaskHeader.getTaskSource())
                    || omChainHeaderService.isIntercepted(omTaskHeader.getChainNo(), omTaskHeader.getOrgId())) {
                // 非供应链订单，忽略
                // 供应链订单已拦截，忽略
                continue;
            }
            list.add(omTaskHeader);
        }
        this.mergeAfter(this.merge(list));

        // 系统控制参数：是否合并同批次运输任务
        final String IS_MERGE_BATCH_TRANS_TASK = SysControlParamsUtils.getValue(SysParamConstants.IS_MERGE_BATCH_TRANS_TASK, orgId);
        if (OmsConstants.OMS_Y.equals(IS_MERGE_BATCH_TRANS_TASK)) {
            // 符合的同批次运输任务
            List<OmTaskHeader> batchList = Lists.newArrayList();

            list.stream().filter(o -> StringUtils.isNotBlank(o.getLotNum())).forEach(omTaskHeader -> {
                List<OmTaskHeader> batchTransTask = omTaskHeaderService.getBatchTask(OmsConstants.OMS_TASK_TYPE_03, Lists.newArrayList(omTaskHeader.getLotNum()), omTaskHeader.getOrgId());
                if (CollectionUtil.isEmpty(batchTransTask)) {
                    return;
                }
                for (OmTaskHeader o : batchTransTask) {
                    batchList.add(omTaskHeaderService.get(o.getId()));
                }
            });
            this.mergeAfter(this.merge(batchList));
        }
    }

    /**
     * 描述：任务合并后
     */
    @Transactional
    public void mergeAfter(List<JSONObject> mergeList) {
        if (CollectionUtil.isEmpty(mergeList)) {
            return;
        }
        for (JSONObject json : mergeList) {
            // 合并任务
            OmTaskHeader mergeTask = json.getObject("MERGETASK", new TypeReference<OmTaskHeader>() {
            });
            // 合并任务关联的原始任务
            List<String> associatedTaskIds = json.getObject("ASSOCIATED", new TypeReference<List<String>>() {
            });
            // 待删除任务
            List<String> delTaskIds = json.getObject("REMOVE", new TypeReference<List<String>>() {
            });

            omTaskHeaderService.save(mergeTask);
            // 将原始任务绑定的关联任务ID改为合并任务的ID
            for (String associatedTaskId : associatedTaskIds) {
                omTaskHeaderService.updateAssociatedTask(associatedTaskId, mergeTask.getId(), OmTaskHeader.DEL_FLAG_DELETE);
            }
            for (String id : delTaskIds) {
                omTaskHeaderService.delete(new OmTaskHeader(id));
            }
        }
    }

    /**
     * 描述：任务还原
     */
    @Transactional
    public void restoreTask(String taskId) {
        OmTaskHeader header = omTaskHeaderService.get(taskId);
        if (header == null) {
            return;
        }

        List<OmTaskHeader> list = Lists.newArrayList(header);
        if (OmsConstants.OMS_TASK_TYPE_01.equals(header.getTaskType())) {
            // 找出同批次运输任务，一起取消
            List<OmTaskHeader> batchTask = omTaskHeaderService.getBatchTask(OmsConstants.OMS_TASK_TYPE_03, Lists.newArrayList(header.getLotNum()), header.getOrgId());
            if (CollectionUtil.isNotEmpty(batchTask)) {
                for (OmTaskHeader o : batchTask) {
                    list.add(omTaskHeaderService.get(o.getId()));
                }
            }
        } else if (OmsConstants.OMS_TASK_TYPE_03.equals(header.getTaskType())) {
            // 找出同批次出库任务，一起取消
            List<OmTaskHeader> batchTask = omTaskHeaderService.getBatchTask(OmsConstants.OMS_TASK_TYPE_01, Lists.newArrayList(header.getLotNum()), header.getOrgId());
            if (CollectionUtil.isNotEmpty(batchTask)) {
                for (OmTaskHeader o : batchTask) {
                    list.add(omTaskHeaderService.get(o.getId()));
                }
            }
        }
        for (OmTaskHeader omTaskHeader : list) {
            // 获取关联任务
            List<OmTaskHeader> omTaskHeaders = omTaskHeaderService.getByAssociatedTaskId(omTaskHeader.getId());
            if (CollectionUtil.isEmpty(omTaskHeaders)) {
                throw new OmsException("[" + omTaskHeader.getTaskNo() + "]非合并任务，无法还原");
            }
            // 取消下发
            pushTaskService.cancelPush(omTaskHeader.getId());
            // 取消分配
            if (OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
                omTaskAllocManager.unAllocate(omTaskHeader.getId());
            }
            // 解除绑定，还原
            omTaskHeaders.stream().map(OmTaskHeader::getId).forEach(id -> omTaskHeaderService.updateAssociatedTask(id, null, OmTaskHeader.DEL_FLAG_NORMAL));
            // 执行删除任务及明细
            omTaskHeaderService.delete(omTaskHeader);
        }
    }

}