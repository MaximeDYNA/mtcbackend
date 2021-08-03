package com.catis.repository;
import com.catis.model.entity.IntervenantFraude;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IntervenantFraudeRepository extends CrudRepository<IntervenantFraude, Long> {

    List<IntervenantFraude> findByActiveStatusTrue();
}
