package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieTest;

import java.util.List;

public interface CategorieTestRepo extends CrudRepository<CategorieTest, Long> {

    List<CategorieTest> findByActiveStatusTrue();
}
