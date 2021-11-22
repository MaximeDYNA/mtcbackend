package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Classification;

import java.util.UUID;

public interface ClassificationRepository extends CrudRepository<Classification, UUID> {

}
