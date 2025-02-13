package com.yunyou.modules.sys.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.TreeService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.mapper.AreaMapper;
import com.yunyou.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 * @author yunyou
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaMapper, Area> {
	@Autowired
	private AreaMapper areaMapper;

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Override
	@Transactional
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	@Override
	@Transactional
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	@Transactional
	public Area getAreaByUser(String userId) {
		Area returnArea = null;
		List<Area> areaList = areaMapper.getAreaByUserId(userId);
		if(areaList != null && !areaList.isEmpty()){
			returnArea = areaList.get(0);
		}
		return returnArea;
	}

	@Transactional
	public Area getAreaByOffice(String officeId) {
		Area returnArea = null;
		List<Area> areaList = areaMapper.getAreaByOfficeId(officeId);
		if(areaList != null && !areaList.isEmpty()){
			returnArea = areaList.get(0);
		}
		return returnArea;
	}

	public List<Area> findCountToArea() {
		return mapper.findCountToArea();
	}

	public Area getByCode(String code){
		return mapper.getByCode(code);
	}

	public String getFullName(String id) {
		StringBuilder s = new StringBuilder();
		if (StringUtils.isBlank(id)) return s.toString();

		Area area = get(id);
		while (area != null && StringUtils.isNotBlank(area.getParentId())){
			s.insert(0, area.getName());
			area = this.get(area.getParentId());
		}
		return s.toString();
	}

	public List<Area> findAreaByName(String name) {
		return areaMapper.findAreaByName(name);
	}
}