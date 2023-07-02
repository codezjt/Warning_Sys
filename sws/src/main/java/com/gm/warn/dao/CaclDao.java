package com.gm.warn.dao;


import com.gm.warn.entity.CaclData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaclDao extends JpaRepository<CaclData, Integer> {

    @Query(value = "update cacl set cacl = ?2 where name = ?1", nativeQuery = true)
    void updateCacl(String name, int data);

    @Query(value = "insert into cacl(name, cacl) values(?1, ?2)", nativeQuery = true)
    void addCacl(String name, int data);



}
