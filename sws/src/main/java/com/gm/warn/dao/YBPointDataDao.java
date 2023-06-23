package com.gm.warn.dao;

import com.gm.warn.entity.YBPointData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YBPointDataDao extends JpaRepository<YBPointData, Integer>, JpaSpecificationExecutor<YBPointData> {

}
