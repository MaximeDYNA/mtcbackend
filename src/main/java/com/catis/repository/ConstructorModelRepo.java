package com.catis.repository;

import com.catis.model.ConstructorModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConstructorModelRepo extends CrudRepository<ConstructorModel, Long> {

    List<ConstructorModel> findByActiveStatusTrue();

}
