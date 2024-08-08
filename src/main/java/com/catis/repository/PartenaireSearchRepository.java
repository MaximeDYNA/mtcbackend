package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.catis.objectTemporaire.PartenaireSearch;



public interface PartenaireSearchRepository extends ElasticsearchRepository<PartenaireSearch, String> {

    List<PartenaireSearch> findByNomStartsWithIgnoreCase(String nom);
    
    List<PartenaireSearch> findByNomIgnoreCase(String nom);
    
    List<PartenaireSearch> findByCniIgnoreCase(String cni);
    
    List<PartenaireSearch> findByPassportIgnoreCase(String passport);
    
    List<PartenaireSearch> findByEmail(String email);
    
    List<PartenaireSearch> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
    
    PartenaireSearch findByClientId(UUID id);
    PartenaireSearch findByContactId(UUID id);
    PartenaireSearch  findByPartenaire_PartenaireId(UUID id);

    @Query("{\"bool\": {\"should\": [{\"regexp\": {\"nom.keyword\": {\"value\": \"?0.*\", \"flags\": \"ALL\", \"case_insensitive\": true, \"max_determinized_states\": 10000}}}, {\"regexp\": {\"prenom.keyword\": {\"value\": \"?1.*\", \"flags\": \"ALL\", \"case_insensitive\": true, \"max_determinized_states\": 10000}}}, {\"regexp\": {\"passport.keyword\": {\"value\": \"?2.*\", \"flags\": \"ALL\", \"case_insensitive\": true, \"max_determinized_states\": 10000}}}, {\"regexp\": {\"telephone.keyword\": {\"value\": \"?3.*\", \"flags\": \"ALL\", \"case_insensitive\": true, \"max_determinized_states\": 10000}}}]}}")
    List<PartenaireSearch> findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(String nom, String prenom, String passport, String telephone);


}