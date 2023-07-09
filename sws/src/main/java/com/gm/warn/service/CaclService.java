package com.gm.warn.service;

import com.gm.warn.dao.CaclDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaclService {

    @Autowired
    CaclDao caclDao;

    public void updateCaclData(String name){
        caclDao.updateCacl(name);
    }
}
