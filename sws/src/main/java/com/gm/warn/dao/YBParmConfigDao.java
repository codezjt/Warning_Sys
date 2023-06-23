package com.gm.warn.dao;

import com.gm.warn.entity.YBParmConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YBParmConfigDao extends JpaRepository<YBParmConfig, Integer>, JpaSpecificationExecutor<YBParmConfig> {
}
