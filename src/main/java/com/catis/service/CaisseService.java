package com.catis.service;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Caisse;
import com.catis.objectTemporaire.CaisseDTO;
import com.catis.repository.CaisseRepository;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@Transactional // Applied to the whole class
public class CaisseService {

    @Autowired
    private CaisseRepository caisseRepository;

    public Caisse addCaisse(Caisse caisse) {
        return caisseRepository.save(caisse);
    }

    public void updateCaisse(Caisse caisse) {
        caisseRepository.save(caisse);
    }

    // public List<Caisse> findAllCaisse(Pageable pageable) {
    //     List<Caisse> caisses = new ArrayList<>();
    //     caisseRepository.findByActiveStatusTrue(pageable).forEach(caisses::add);
    //     return caisses;
    // }

    // flemming implimented
    public Page<CaisseDTO> findAllCaisse(String search,Pageable pageable) {
        // Fetch the caisses as a Page object instead of a List
        Page<Caisse> caissesPage = caisseRepository.findByActiveStatusTrueAndLibelleContainingIgnoreCase(search, pageable);
        // Page<Caisse> caissesPage = caisseRepository.findByActiveStatusTrue(pageable);

        // Convert each Caisse to CaisseDTO and lazily fetch the organization name
        List<CaisseDTO> caisseDTOList = caissesPage.getContent().stream()
                .map(caisse -> {
                    CaisseDTO dto = new CaisseDTO();
                    dto.setCaisse_id(caisse.getCaisseId());
                    dto.setLibelle(caisse.getLibelle());
                    dto.setDescription(caisse.getDescription());
                    dto.setCreated_date(caisse.getCreatedDate());

                    // Lazy fetch the organisation name
                    dto.setOrganisation(caisse.getOrganisation() != null ? caisse.getOrganisation().getNom() : null);

                    return dto;
                })
                .collect(Collectors.toList());

        // Return the DTO list as a paginated response
        return new PageImpl<>(caisseDTOList, pageable, caissesPage.getTotalElements());
    }



    // public List<Caisse> findAllCaisse() {
    //     List<Caisse> caisses = new ArrayList<>();
    //     caisseRepository.findByActiveStatusTrue().forEach(caisses::add);
    //     return caisses;
    // }

    // flemming implimented
    // Method for fetching caisses based on search criteria
    public Page<Map<String, String>> findCaisseForSelect(String search, Pageable pageable) {
        // Fetch caisses that match the search query, applying pagination
        Page<Caisse> caissesPage = caisseRepository.findByActiveStatusTrueAndLibelleContainingIgnoreCaseOrOrganisationNomContainingIgnoreCase(
                search, search, pageable);

        // Map each Caisse to a simplified select option format
        List<Map<String, String>> caissesSelect = caissesPage.getContent().stream().map(c -> {
            Map<String, String> caisseMap = new HashMap<>();
            caisseMap.put("id", String.valueOf(c.getCaisseId()));
            caisseMap.put("name", c.getLibelle() + " | " + c.getOrganisation().getNom());
            return caisseMap;
        }).collect(Collectors.toList());

        // Return the mapped caisses in a paginated format
        return new PageImpl<>(caissesSelect, pageable, caissesPage.getTotalElements());
    }


    public Caisse findCaisseById(UUID idCaisse) {
        return caisseRepository.findById(idCaisse).get();
    }

    public void deleteCaisseById(UUID idCaisse) {
        caisseRepository.deleteById(idCaisse);
    }

}
