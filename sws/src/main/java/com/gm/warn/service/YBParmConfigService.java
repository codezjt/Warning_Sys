package com.gm.warn.service;

import com.gm.warn.dao.YBParmConfigDao;
import com.gm.warn.entity.YBParmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YBParmConfigService {
    @Autowired
    private YBParmConfigDao ybParmConfigDao;

    public void addYBparmConfig(YBParmConfig ybParmConfig)
    {
        ybParmConfigDao.save(ybParmConfig);
    }

    public List<YBParmConfig> getAllConfig() {
        return ybParmConfigDao.findAll();
    }
}
