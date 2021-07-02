package com.catis.service;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.catis.model.CarteGrise;
import com.catis.model.GieglanFile;
import com.catis.model.GieglanFile.FileType;
import com.catis.model.GieglanFile.StatusType;
import com.catis.repository.GieglanFileRepository;
import com.catis.model.Inspection;
import com.catis.model.ValeurTest;

@Service("GieglanService")
@Transactional
public class GieglanFileService {

    private CarteGrise carteGrise;

    private Set<ValeurTest> codeGieglans = new HashSet<>();

    @Autowired
    private GieglanFileRepository gieglanFileRepository;

    public void createFileGieglanOfCgrise(CarteGrise carteGrise, Inspection inspection) {

        Set<GieglanFile> files = new HashSet<>();
        this.carteGrise = carteGrise;
        GieglanFile file = new GieglanFile();
        file.setActiveStatus(true);
        file.setName(inspection.getIdInspection() + ".CG");
        file.setType(FileType.CARD_REGISTRATION);
        file.setStatus(StatusType.INITIALIZED);
        file.setInspection(inspection);
        file.setOrganisation(inspection.getOrganisation());
        file.setFileCreatedAt(new Date());
        this.creategieglanforCardGrise(file);
        file.setValeurTests(this.codeGieglans);
        files.add(file);
        inspection.setGieglanFiles(files);
        this.gieglanFileRepository.save(file);
    }

    private void creategieglanforCardGrise(GieglanFile file) {

        HashMap<String, String> codeCgrises = new HashMap<>();
        codeCgrises.put("0200", this.carteGrise.getNumImmatriculation());
        codeCgrises.put("0202", this.carteGrise.getVehicule().getChassis());
        codeCgrises.put("0208", "");
        codeCgrises.put("0209", "");
        codeCgrises.put("0210", "");
        codeCgrises.put("0211", "");
        codeCgrises.put("0212", (this.carteGrise.getProduit().getCategorieVehicule().getType() == "VL") ? "VL" : "HV");
        codeCgrises.put("0213", this.carteGrise.getVehicule().getMarqueVehicule().getLibelle());
        codeCgrises.put("0214", "");//modele du vehicule obligatoire
        codeCgrises.put("0215", "");
        codeCgrises.put("0216", this.carteGrise.getCarteGriseId().toString());
        codeCgrises.forEach((key, value) -> {
            Integer crc = this.generateCrc(value);
            if (crc != null) {
                ValeurTest valeurTest = new ValeurTest();
                valeurTest.setActiveStatus(true);
                valeurTest.setCode(key);
                valeurTest.setOrganisation(file.getOrganisation());
                valeurTest.setGieglanFile(file);
                valeurTest.setValeur(value);
                valeurTest.setCrc(crc);
                valeurTest.setStatus(StatusType.VALIDATED);
                this.codeGieglans.add(valeurTest);
            }
        });
    }

    /**
     * Convert value to crc
     *
     * @param v
     * @return crc
     */
    private Integer generateCrc(String v) {
        if (v == null) return null;
        if (v.isEmpty()) return (int) '\0';
        int code = 0;
        for (int i = 0, j = 1; i < v.length(); i++, j++) {
            code += (int) v.charAt(i) * j;
            if (5 == j) j = 0;
        }
        return code;
    }
    public List<GieglanFile> findByExtensionAndVisite(String libelle, Long idVisite){
        List<GieglanFile> gieglanfiles = gieglanFileRepository
                .findByCategorieTestLibelleAndInspection_Visite_IdVisite(libelle, idVisite, PageRequest.of(0,1, Sort.by("id").ascending()));
        return gieglanfiles;
    }

    public List<GieglanFile> findActiveByInspection(Long inspectionId){
        List<GieglanFile> gieglanfiles = gieglanFileRepository
                .findByInspection_IdInspectionAndActiveStatusTrue(inspectionId);
        return gieglanfiles;
    }

}
