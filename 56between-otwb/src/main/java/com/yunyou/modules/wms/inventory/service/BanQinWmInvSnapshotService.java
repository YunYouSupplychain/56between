package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvSnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 库存每日快照Service
 *
 * @author ZYF
 * @version 2021/04/25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvSnapshotService extends BaseService {
    @Autowired
    private BanQinWmInvSnapshotMapper mapper;

    @Transactional
    public void generateInvLotSnapshot(String date) {
        mapper.clearInvLotSnapshot(date);
        mapper.initInvLotSnapshot(date);
    }

    @Transactional
    public void generateInvLotLocSnapshot(String date) {
        mapper.clearInvLotLocSnapshot(date, null);
        mapper.initInvLotLocSnapshot(date);
    }

    @Transactional
    public void generateInvLotLocSnapshotByActTran(Date date, String orgId) {
        String invDate = DateUtils.formatDate(date, DateFormatUtil.PATTERN_ISO_ON_DATE);
        String day = DateUtils.formatDate(DateUtil.endOfDate(date), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND);

        mapper.clearInvLotLocSnapshot(invDate, orgId);
        mapper.generateInvLotLocSnapshotByActTran(invDate, day, orgId);
    }
}