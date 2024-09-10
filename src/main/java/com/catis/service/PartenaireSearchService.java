// package com.catis.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.catis.objectTemporaire.PartenaireSearch;
// import com.catis.repository.PartenaireSearchRepository;

// import java.util.ArrayList;
// import java.util.List;


// @Service
// public class PartenaireSearchService  {
//     @Autowired
//     private PartenaireSearchRepository repository;

//     public List<PartenaireSearch> searchPartenaires(String query) {
//         return repository.findByNomIgnoreCaseAndPrenomIgnoreCase(query, query);
//     }

//     public PartenaireSearch saveOrUpdatePartenaire(PartenaireSearch partenaireDocument) {
//         return repository.save(partenaireDocument);
//     }

//     public List<PartenaireSearch> findAllPartenaire() {
//         List<PartenaireSearch> partenaires = new ArrayList<>();
//         repository.findAll().forEach(partenaires::add);
//         return partenaires;
//     }
//     public List<PartenaireSearch> findPartenaireByNom(String nom) {
//         return repository
//                 .findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(nom, nom, nom, nom);
//     }



//     public void deletePartenaire(String id) {
//         repository.deleteById(id);
//     }

//     public boolean isNomAlreadyExist(String nom) {
//         return !repository.findByNomIgnoreCase(nom).isEmpty();
//     }

//     public boolean isCniAlreadyExist(String cni) {
//         return !repository.findByCniIgnoreCase(cni).isEmpty();
//     }

//     public boolean isPassportAlreadyExist(String passport) {
//         return !repository.findByPassportIgnoreCase(passport).isEmpty();
//     }

//     public boolean isEmailAlreadyExist(String email) {
//         return !repository.findByEmail(email).isEmpty();
//     }

//     public boolean isNomEtPrenomAlreadyExist(String nom, String prenom) {
//         return !repository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom).isEmpty();
//     }

// }
