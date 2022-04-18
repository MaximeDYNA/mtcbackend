package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieTest;

import java.util.List;
import java.util.UUID;

public interface CategorieTestRepo extends CrudRepository<CategorieTest, UUID> {

    List<CategorieTest> findByActiveStatusTrue();
}
