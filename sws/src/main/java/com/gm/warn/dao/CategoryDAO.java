package com.gm.warn.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gm.warn.entity.Category;


public interface CategoryDAO extends JpaRepository<Category, Integer> {

}
