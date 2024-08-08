package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.catis.objectTemporaire.VisiteSearchService;

public interface VisiteSearchRespository extends ElasticsearchRepository<VisiteSearchService, UUID> {


    @Query("{\"bool\":{\"should\":[{\"match\":{\"carteGrise.numImmatriculation.keyword\":\"?0\"}},{\"match\":{\"carteGrise.proprietaireVehicule.partenaire.nom\":\"?1\"}},{\"match\":{\"carteGrise.proprietaireVehicule.partenaire.prenom\":\"?2\"}},{\"match\":{\"carteGrise.vehicule.chassis\":\"?3\"}}]}}")
    List<VisiteSearchService> searchByNumImmatriculationOrPartenaireNomOrPartenairePrenom(String numImmatriculation, String partenaireNom, String partenairePrenom, String chassis);
    

    @Query("{\"bool\":{\"should\":[{\"regexp\":{\"carteGrise.numImmatriculation.keyword\":{\"value\":\"?0\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000}}},{\"regexp\":{\"carteGrise.proprietaireVehicule.partenaire.nom\":{\"value\":\"?0\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000}}},{\"regexp\":{\"carteGrise.proprietaireVehicule.partenaire.prenom\":{\"value\":\"?0\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000}}},{\"regexp\":{\"carteGrise.vehicule.chassis\":{\"value\":\"?0\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000}}}]}}}")
    List<VisiteSearchService> findVisiteByRegexpFields(String searchTerm);

}
