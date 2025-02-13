package com.yunyou.modules.interfaces.edi.utils;

public enum EdiMethod {

    ORDER {
        public String getURL() {
            return "/saveOrderInfo";
        }
    },
    INV {
        public String getURL() {
            return "/saveInvInfo";
        }
    },
    SAVE {
        public String getURL() {
            return "/saveInfo";
        }
    };

    public String getURL() { return ""; }
}