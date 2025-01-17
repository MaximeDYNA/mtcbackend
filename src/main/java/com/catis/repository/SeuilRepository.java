package com.catis.repository;

import com.catis.model.entity.Seuil;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SeuilRepository extends CrudRepository<Seuil, String> {

    List<Seuil> findByActiveStatusTrue();
}
