package com.yunyou.modules.sys.entity;

import com.yunyou.core.persistence.DataEntity;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 数据字典Entity
 * @author lgf
 * @version 2017-01-16
 */
public class DictType extends DataEntity<DictType> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String description;		// 描述
	private List<DictValue> dictValueList = Lists.newArrayList();		// 子表列表
	
	public DictType() {
		super();
	}

	public DictType(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<DictValue> getDictValueList() {
		return dictValueList;
	}

	public void setDictValueList(List<DictValue> dictValueList) {
		this.dictValueList = dictValueList;
	}
}