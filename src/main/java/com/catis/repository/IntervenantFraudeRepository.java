package com.catis.repository;
import com.catis.model.entity.IntervenantFraude;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface IntervenantFraudeRepository extends CrudRepository<IntervenantFraude, UUID> {

    List<IntervenantFraude> findByActiveStatusTrue();
}
