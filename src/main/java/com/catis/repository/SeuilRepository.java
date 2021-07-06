package com.catis.repository;

import com.catis.model.entity.Seuil;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeuilRepository extends CrudRepository<Seuil, Long> {

    List<Seuil> findByActiveStatusTrue();
}
