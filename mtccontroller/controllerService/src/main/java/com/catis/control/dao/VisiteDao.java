/**
 * 
 */
package com.catis.control.dao;

import com.catis.control.entities.Control;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.catis.control.entities.Visite;

import java.util.List;
import java.util.UUID;

/**
 * @author AubryYvan
 */
@Repository
public interface VisiteDao extends JpaRepository<Visite, UUID> {

	@Query(value = "select v from Visite v join fetch v.inspection i "
		+ "left join fetch i.lexiques le left join le.classification cl "
		+ "join fetch i.gieglanFiles f left join f.valeurTests join fetch v.carteGrise "
		+ "cg join fetch cg.produit p join fetch p.categorieTestProduits cv join fetch "
		+ "cv.categorieTest ct where v.statut = 2 "
		+ "and v.encours = true and f.status = 'VALIDATED' and f.type = 'MEASURE' and f.fileCreatedAt "
		+ "in (select max(fi.fileCreatedAt) from GieglanFile fi where fi.status = 'VALIDATED' group by fi.name) "
		+ "and ct.activeStatus = true order by v.createdDate desc"
	)
	List<Visite> getVisiteWithReceivedTest(Pageable pageable);

	@Query(value = "select v from Visite v join fetch v.inspection i "
		+ "join fetch i.gieglanFiles f where v.control = ?1  and v <> ?2 "
		+ "and f.isAccept = false and f.type = 'MEASURE' and f.status = 'VALIDATED' "
		+ "order by v.createdDate desc"
	)
	List<Visite> getLastVisiteWithTestFailed(Control control, Visite visite, Pageable pageable);
}
