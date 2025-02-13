package com.yunyou.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SynchronizedNoService {
    @Autowired
    private SysRuleGenNoService noService;

    public synchronized String getDocumentNo(String type) {
        return noService.getDocumentNo(type);
    }

    public synchronized List<String> getDocumentNo(String type, int size) {
        return noService.getDocumentNo(type, size);
    }
}
