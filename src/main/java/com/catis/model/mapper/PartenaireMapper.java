// package com.catis.model.mapper;

// import java.util.UUID;

// import org.modelmapper.ModelMapper;
// import org.springframework.stereotype.Component;

// import com.catis.model.entity.Partenaire;
// import com.catis.objectTemporaire.PartenaireSearch;

// @Component
// public class PartenaireMapper {
//     private final ModelMapper modelMapper;

//     public PartenaireMapper() {
//         this.modelMapper = new ModelMapper();
//     }

//     public PartenaireSearch toPartenaireSearch(Partenaire partenaire) {
//         return modelMapper.map(partenaire, PartenaireSearch.class);
//     }

//     public Partenaire fromPartenaireSearch(PartenaireSearch partenaireSearch) {
//         Partenaire partenaire = new Partenaire();
//         UUID uuid = UUID.fromString(partenaireSearch.getId());
//         partenaire.setPartenaireId(uuid);
//         partenaire.setNom(partenaireSearch.getNom());
//         partenaire.setPrenom(partenaireSearch.getPrenom());
//         partenaire.setPassport(partenaireSearch.getPassport());
//         partenaire.setLieuDeNaiss(partenaireSearch.getLieuDeNaiss());
//         partenaire.setPermiDeConduire(partenaireSearch.getPermiDeConduire());
//         partenaire.setEmail(partenaireSearch.getEmail());
//         partenaire.setTelephone(partenaireSearch.getTelephone());
//         partenaire.setCni(partenaireSearch.getCni());  
//         partenaire.setClientId(partenaireSearch.getClientId());
//         partenaire.setContactId(partenaireSearch.getContactId());


//         return partenaire;
//     }
// }
