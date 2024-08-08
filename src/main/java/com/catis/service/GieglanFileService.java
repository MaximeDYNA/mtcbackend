package com.catis.service;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.persistence.Query;

import com.catis.model.entity.Visite;

import org.checkerframework.checker.units.qual.g;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.catis.model.entity.CarteGrise;
import com.catis.model.control.GieglanFile;
import com.catis.model.control.GieglanFile.FileType;
import com.catis.model.control.GieglanFile.StatusType;
import com.catis.repository.GieglanFileRepository;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.ValeurTest;

@Service("GieglanService")
@Transactional
public class GieglanFileService {

    private CarteGrise carteGrise;

    @Autowired
    private GieglanFileRepository gieglanFileRepository;
   
    @PersistenceContext
    private EntityManager entityManager;





    @Transactional
    public void updateGieglanFileStatus(UUID controlId, UUID visiteId) {
        String updateQuery = String.format("UPDATE gieglan_file g " +
                             "INNER JOIN t_inspection i ON g.inspection_id = i.id  " +
                             "INNER JOIN t_visite v ON i.visite_id = v.id " +
                             "SET g.is_accept = 0  " +
                             "WHERE v.control_id = '%s' " +
                             "AND v.id <> '%s' " +
                             "AND g.type = 'MEASURE' " +
                             "AND g.status = 'VALIDATED' " +
                             "AND g.name LIKE '%%json'", controlId.toString(), visiteId.toString());

        Query query = entityManager.createNativeQuery(updateQuery);
        query.executeUpdate();
    }

    public void createFileGieglanOfCgrise(CarteGrise carteGrise, Inspection inspection) {

        Set<GieglanFile> files = new HashSet<>();
        this.carteGrise = carteGrise;
        GieglanFile file = new GieglanFile();
        file.setActiveStatus(true);
        file.setName(inspection.getFileId() + ".CG");
        file.setType(FileType.CARD_REGISTRATION);
        file.setStatus(StatusType.INITIALIZED);
        file.setInspection(inspection);
        file.setOrganisation(inspection.getOrganisation());
        file.setFileCreatedAt(new Date());
        file.setValeurTests(creategieglanforCardGrise(file,inspection));
        files.add(file);
        inspection.setGieglanFiles(files);
        this.gieglanFileRepository.save(file);
    }

    private Set<ValeurTest> creategieglanforCardGrise(GieglanFile file, Inspection inspection) {

        HashMap<String, String> codeCgrises = new HashMap<>();
        Set<ValeurTest> codeGieglans = new HashSet<>();
        codeCgrises.put("0200", this.carteGrise.getNumImmatriculation());
        codeCgrises.put("0202", this.carteGrise.getVehicule().getChassis());
        codeCgrises.put("0208", "");
        codeCgrises.put("0209", "");
        codeCgrises.put("0210", "");
        codeCgrises.put("0211", "");
        codeCgrises.put("0212", "VL".equals(this.carteGrise.getProduit().getCategorieVehicule().getType()) ? "LV" : "HV");
        codeCgrises.put("0213", this.carteGrise.getVehicule().getMarqueVehicule().getLibelle());
        codeCgrises.put("0214", "");//modele du vehicule obligatoire
        codeCgrises.put("0215", "");
        codeCgrises.put("0216", inspection.getIdInspection().toString());
        codeCgrises.put("7000", "1");
        codeCgrises.put("7010", "1-1");
        codeCgrises.put("7011", "01");
        codeCgrises.put("7012", "0");
        codeCgrises.put("7014", "00");
        codeCgrises.put("7020", "1;0;1;1;1");
        codeCgrises.put("7841", "");
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
                codeGieglans.add(valeurTest);
            }
        });

        return codeGieglans;
    }

    // old method allows dupliacted icons
    // @Transactional
    // public List<GieglanFile> getGieglanFileFailed(Visite v) {
    //     return gieglanFileRepository.getGieglanFileFailed(v.getControl(), v);
    // }

    // flemming implimented
    @Transactional
    public List<GieglanFile> getGieglanFileFailed(Visite v) {
        // Fetch the list from the repository
        List<GieglanFile> gieglanFiles = gieglanFileRepository.getGieglanFileFailed(v.getControl(), v);
        
        // Convert the list to a set to remove duplicates
        Set<GieglanFile> uniqueGieglanFiles = new HashSet<>(gieglanFiles);
        
        // Convert the set back to a list
        List<GieglanFile> uniqueGieglanFileList = new ArrayList<>(uniqueGieglanFiles);
        
        // Return the list without duplicates
        return uniqueGieglanFileList;
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
    public List<GieglanFile> findByExtensionAndVisite(String libelle, UUID idVisite){
        List<GieglanFile> gieglanfiles = gieglanFileRepository
                .findByCategorieTestLibelleAndInspection_Visite_IdVisite(libelle, idVisite, PageRequest.of(0,1, Sort.by("id").ascending()));
        return gieglanfiles;
    }

    public List<GieglanFile> getGieglan(Visite visite){
        List<GieglanFile> gieglanfiles = gieglanFileRepository
                .getMyGieglanFile(visite, PageRequest.of(0,7, Sort.by("id").ascending()));
        return gieglanfiles;
    }

    public List<GieglanFile> findActiveByInspection(UUID inspectionId){
        List<GieglanFile> gieglanfiles = gieglanFileRepository
                .findByInspection_IdInspectionAndActiveStatusTrue(inspectionId);
        return gieglanfiles;
    }

}
