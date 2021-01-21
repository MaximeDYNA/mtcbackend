package com.catis.repository;

import java.util.List;

import com.catis.model.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catis.model.RapportDeVisite;
import com.catis.model.Visite;

public interface RapportDeVisiteRepo extends JpaRepository<RapportDeVisite, Long>{

	@Query(value = "select r from RapportDeVisite r inner join r.seuil s "
		+ "inner join s.formule f inner join f.mesures m where r.visite = ?1")
	List<RapportDeVisite> getRapportDeVisite(Visite visite);

	@Query(value = "select r from RapportDeVisite r inner join r.seuil s "
		+ "inner join s.formule f inner join f.mesures m inner join r.visite v "
		+ "inner join r.gieglanFile g where v.control = ?1 and v <> ?2 and g.isAccept = true")
	List<RapportDeVisite> getRapportOfLastVisite(Control control, Visite visite);
}
