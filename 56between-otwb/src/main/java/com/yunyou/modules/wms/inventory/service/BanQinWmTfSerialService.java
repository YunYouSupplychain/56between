package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfSerial;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTfSerialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 序列号转移Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTfSerialService extends CrudService<BanQinWmTfSerialMapper, BanQinWmTfSerial> {

	public BanQinWmTfSerial get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmTfSerial> findList(BanQinWmTfSerial banQinWmTfSerial) {
		return super.findList(banQinWmTfSerial);
	}
	
	public Page<BanQinWmTfSerial> findPage(Page<BanQinWmTfSerial> page, BanQinWmTfSerial banQinWmTfSerial) {
		return super.findPage(page, banQinWmTfSerial);
	}
	
	@Transactional
	public void save(BanQinWmTfSerial banQinWmTfSerial) {
		super.save(banQinWmTfSerial);
	}
	
	@Transactional
	public void delete(BanQinWmTfSerial banQinWmTfSerial) {
		super.delete(banQinWmTfSerial);
	}
	
	public BanQinWmTfSerial findFirst(BanQinWmTfSerial banQinWmTfSerial) {
        List<BanQinWmTfSerial> list = findList(banQinWmTfSerial);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 获取序列号转移明细
     * @param tfNo
     * @param lineNo
     * @return
     */
    public List<BanQinWmTfSerial> getByTfNoAndLineNo(String tfNo, String lineNo, String orgId) {
        BanQinWmTfSerial condition = new BanQinWmTfSerial();
        condition.setTfNo(tfNo);
        condition.setLineNo(lineNo);
        condition.setOrgId(orgId);
        return findList(condition);
    }

    /**
     * 根据转移单号、行号、类型、序列号 获取序列号转移明细
     * @param tfNo
     * @param lineNo
     * @param tfMode
     * @param serialNo
     * @return
     */
    public BanQinWmTfSerial getWmTfSerialModel(String tfNo, String lineNo, String tfMode, String serialNo, String orgId) {
        BanQinWmTfSerial example = new BanQinWmTfSerial();
        example.setTfNo(tfNo);
        example.setLineNo(lineNo);
        example.setTfMode(tfMode);
        example.setSerialNo(serialNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }
	
}