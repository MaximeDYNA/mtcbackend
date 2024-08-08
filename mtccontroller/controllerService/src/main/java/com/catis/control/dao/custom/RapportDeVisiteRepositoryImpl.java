package com.catis.control.dao.custom;

import com.catis.control.entities.RapportDeVisite;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class RapportDeVisiteRepositoryImpl implements RapportDeVisiteRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Map<String, String> getResultVisiteId(UUID idVisite) {
        return em.createQuery("SELECT r.result, m.code FROM RapportDeVisite r " +
            "join fetch r.seuil s join fetch s.formule f join fetch " +
            "f.measure m join r.visite v where v.idVisite = ?1", Tuple.class)
            .setParameter(1, idVisite)
            .getResultStream()
            .collect(Collectors.toMap(
                tuple -> ((String)tuple.get("result")).toString(),
                tuple -> ((String)tuple.get("code")).toString()
            )
        );
    }
}
