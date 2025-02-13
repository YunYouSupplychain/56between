package com.yunyou.modules.oms.common;

public enum BusinessOrderType {

    DC_ASN {
        public String getBusinessType() { return "14"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "ASN"; }
    },
    DC_SO {
        public String getBusinessType() { return "15"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "SO"; }
    },
    SUP_DC_ASN {
        public String getBusinessType() { return "16"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "ASN"; }
    },
    SUP_DC_SO {
        public String getBusinessType() { return "17"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "SO"; }
    },
    SUP_WMS_ASN {
        public String getBusinessType() { return "18"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "ASN"; }
    },
    SUP_WMS_SO {
        public String getBusinessType() { return "19"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "SO"; }
    },
    CONTRACT_LOGISTICS_ASN {
        public String getBusinessType() { return "20"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "ASN"; }
    },
    CONTRACT_LOGISTICS_SO {
        public String getBusinessType() { return "21"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "SO"; }
    },
    B2C_E_COMMERCE_ASN {
        public String getBusinessType() { return "22"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "ASN"; }
    },
    B2C_E_COMMERCE_SO {
        public String getBusinessType() { return "23"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "SO"; }
    },
    FAB_DC_ASN {
        public String getBusinessType() { return "24"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "ASN"; }
    },
    FAB_DC_SO {
        public String getBusinessType() { return "25"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "SO"; }
    },
    FAB_WMS_ASN {
        public String getBusinessType() { return "26"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "ASN"; }
    },
    FAB_WMS_SO {
        public String getBusinessType() { return "27"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "SO"; }
    },
    B2C_GP_ASN {
        public String getBusinessType() { return "28"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "ASN"; }
    },
    B2C_GP_SO {
        public String getBusinessType() { return "29"; }
        public String getPushSystem() { return "SMS"; }
        public String getOrderType() { return "SO"; }
    },
    LTL_LINE {
        public String getBusinessType() { return "30"; }
        public String getPushSystem() { return "TMS"; }
        public String getOrderType() { return ""; }
    },
    CUS_MRB {
        public String getBusinessType() { return "31"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "ASN"; }
    },
    SUP_MRB {
        public String getBusinessType() { return "32"; }
        public String getPushSystem() { return "WMS"; }
        public String getOrderType() { return "SO"; }
    },
    ;

    public String getBusinessType() {
        return "";
    }
    public String getPushSystem() {
        return "";
    }
    public String getOrderType() {
        return "";
    }
}
