package com.yunyou.modules.wms.common.lock;

import com.yunyou.core.transaction.lock.AbstractLockInfo;

public class LockLotInfo extends AbstractLockInfo {

    /**
     * 批次号
     */
    protected String lotNum;
    /**
     * 机构ID
     */
    protected String orgId;

    public LockLotInfo(String lotNum, String orgId) {
        super();
        this.lotNum = lotNum;
        this.orgId = orgId;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "LockSkuInfo[lotNum=" + lotNum + ",orgId=" + orgId + "]";
    }

    @Override
    public int compareTo(AbstractLockInfo o) {
        if (o == null) {
            return -1;
        }
        return this.toString().compareTo(o.toString());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lotNum == null) ? 0 : lotNum.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LockLotInfo other = (LockLotInfo) obj;
        if (lotNum == null) {
            return other.lotNum == null;
        } else {
            return lotNum.equals(other.lotNum);
        }
    }
}
