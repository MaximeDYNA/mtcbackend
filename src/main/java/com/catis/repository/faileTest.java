package com.catis.repository;

import com.catis.model.Control;
import com.catis.model.Visite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface faileTest extends JpaRepository<Visite, Long> {
    @Query(value = "select v from Visite v join fetch v.inspection i "
            + "join fetch i.gieglanFiles f where v.control = ?1  and v <> ?2 "
            + "and f.isAccept = 0 and f.type = 'MEASURE' and f.status = 'VALIDATED' "
            + "order by v.idVisite desc"
    )
    List<Visite> getBeforeLastVisite(Control control, Visite visite, Pageable pageable);

}