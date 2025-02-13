package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmTransportOrderTrackAction extends BaseAction {
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;

    public TmTransportOrderTrack get(String id) {
        return tmTransportOrderTrackService.get(id);
    }

    public Page<TmTransportOrderTrack> findPage(Page page, TmTransportOrderTrack qEntity) {
        return tmTransportOrderTrackService.findPage(page, qEntity);
    }

    public List<TmTransportOrderTrack> findList(TmTransportOrderTrack qEntity) {
        return tmTransportOrderTrackService.findList(qEntity);
    }

    public ResultMessage save(TmTransportOrderTrack entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmTransportOrderTrackService.save(entity);
            msg.setData(tmTransportOrderTrackService.get(entity.getId()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage removeAll(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) {
            return msg;
        }
        try {
            for (String id : ids) {
                tmTransportOrderTrackService.delete(new TmTransportOrderTrack(id));
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
