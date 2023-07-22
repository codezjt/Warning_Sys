package com.gm.warn.service;

import com.gm.warn.dao.CaclDao;
import log.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaclService {

    @Autowired
    CaclDao caclDao;

    @Action(description = "更新统计信息")
    public void updateCaclData(String name){
        caclDao.updateCacl(name);
    }
}
