package com.yunyou.modules.sys.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.TreeService;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.mapper.OfficeMapper;
import com.yunyou.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机构Service
 *
 * @author yunyou
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeMapper, Office> {
    @Autowired
    private OfficeMapper officeMapper;

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    public List<Office> findList(Boolean isAll) {
        if (isAll != null && isAll) {
            return UserUtils.getOfficeAllList();
        } else {
            return UserUtils.getOfficeList();
        }
    }

    public List<Office> findAllList() {
        return mapper.findCompanyData(new Office(null));
    }

    @Override
    public List<Office> findList(Office office) {
        office.setParentIds(office.getParentIds() + "%");
        return officeMapper.findByParentIdsLike(office);
    }

    public Office getByCode(String code) {
        return officeMapper.getByCode(code);
    }

    @Override
    public List<Office> getChildren(String parentId) {
        return officeMapper.getChildren(parentId);
    }

    @Override
    @Transactional
    public void save(Office office) {
        super.save(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Override
    @Transactional
    public void delete(Office office) {
        super.delete(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    public Page<Office> findCompanyData(Page<Office> page, Office office) {
        dataRuleFilter(office);
        office.setPage(page);
        page.setList(this.findCompanyData(office));
        return page;
    }

    public List<Office> findCompanyData(Office office) {
        return officeMapper.findCompanyData(office);
    }

    public Office getByName(String name) {
        List<Office> officeList = officeMapper.getByName(name);
        if (CollectionUtil.isEmpty(officeList)) {
            return null;
        } else {
            return officeList.get(0);
        }
    }

    public void saveValidator(Office office) {
        if (StringUtils.isBlank(office.getType())) {
            throw new GlobalException("机构类型不能为空");
        }
        if (OfficeType.ORG_CENTER.getValue().equals(office.getType()) && office.getParent() != null && StringUtils.isNotBlank(office.getParent().getId())) {
            Office parent = this.get(office.getParent().getId());
            String[] parentIds = (parent.getParentIds() + "," + parent.getId()).split(",");
            for (String parentId : parentIds) {
                Office o = this.get(parentId);
                if (o != null && OfficeType.ORG_CENTER.getValue().equals(o.getType())) {
                    throw new GlobalException("上级机构中已存在组织中心");
                }
            }
        }
    }

    public Page<Office> findOutletMatchedOrg(Page<Office> page, Office office) {
        dataRuleFilter(office);
        office.setPage(page);
        page.setList(officeMapper.findOutletMatchedOrg(office));
        return page;
    }

}
