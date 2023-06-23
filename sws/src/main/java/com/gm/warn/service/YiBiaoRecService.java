package com.gm.warn.service;

import com.gm.warn.dao.YiBiaoRecDao;
import com.gm.warn.entity.YiBiaoRec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YiBiaoRecService {

    @Autowired
    private YiBiaoRecDao yiBiaoRecDao;


    public List<YiBiaoRec> getTzrqData(String date) {
        return yiBiaoRecDao.getTzrqData(date);
    }
}
