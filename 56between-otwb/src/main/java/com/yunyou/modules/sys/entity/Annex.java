package com.yunyou.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class Annex extends DataEntity<Annex> {
    private static final long serialVersionUID = 4688355306666427143L;

    private Integer type;
    private String pkId;
    private String fileName;
    private Long fileSize;
    private String path;
    private User uploadBy;
    private Date uploadDate;

    public Annex() {
    }

    public Annex(String id) {
        super(id);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(User uploadBy) {
        this.uploadBy = uploadBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

}
