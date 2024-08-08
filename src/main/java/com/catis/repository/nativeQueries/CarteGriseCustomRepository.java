package com.catis.repository.nativeQueries;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.catis.model.entity.CarteGrise;

public interface CarteGriseCustomRepository {
    Page<CarteGrise> findByActiveStatusTrueAndSearch(String search, Pageable pageable);
}
