package com.gm.warn.service;

import com.gm.warn.dao.YBPointDataDao;
import com.gm.warn.entity.YBPointData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YBPointDataService {

    @Autowired
    private YBPointDataDao ybPointDataDao;

    public void addYBData(YBPointData ybPointData)
    {
        ybPointDataDao.save(ybPointData);
    }

    public List<YBPointData> getAllYBdata()
    {
        return ybPointDataDao.findAll();
    }
}
