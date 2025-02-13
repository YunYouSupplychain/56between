package com.yunyou.core.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 数据Entity类
 */
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    protected User createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    protected Date createDate;
    /**
     * 更新者
     */
    protected User updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    protected Date updateDate;
    /**
     * 删除标记（0：正常；1：删除；2：审核）
     */
    protected String delFlag;
    /**
     * 版本号
     */
    protected int recVer;
    /**
     * 备注
     */
    protected String remarks;
    /**
     * 模糊查询字段
     */
    protected String codeAndName;

    public DataEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public DataEntity(String id) {
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        if (!this.isNewRecord) {
            if (this.getIdType().equals(IDTYPE_UUID)) {
                setId(IdGen.uuid());
            }
        }
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.createBy = user;
            this.updateBy = user;
        } else {
            this.createBy = UserUtils.getSystemUser();
            this.updateBy = UserUtils.getSystemUser();
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
        } else {
            this.updateBy = UserUtils.getSystemUser();
        }
        this.updateDate = new Date();
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public int getRecVer() {
        return recVer;
    }

    public void setRecVer(int recVer) {
        this.recVer = recVer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }
}