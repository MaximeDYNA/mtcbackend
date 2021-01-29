package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.GieglanFile;

public interface GieglanFileRepository extends CrudRepository<GieglanFile, Long> {

    List<GieglanFile> findByInspection_IdInspection(Long idInpection);
}
