package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import com.catis.objectTemporaire.DefectResponse;
import com.catis.model.entity.Inspection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.MesureVisuel;
import com.catis.repository.MesureVisuelRepository;

@Service
public class MesureVisuelService {

    @Autowired
    private MesureVisuelRepository mesurevisuel;
    @Autowired
    private InspectionService inspectionService;


    public MesureVisuel addDataInspection(MesureVisuel mesurevisuels) {
        MesureVisuel m = mesurevisuel.save(mesurevisuels);
        return m;

    }

    public MesureVisuel addSignatureToMesureVisuel(DefectResponse defectResponse) {

        //Optional<MesureVisuel> mesureVis = mesurevisuel.byIdInspection(defectResponse.getIdinspection()).stream().findFirst();
        Inspection i = inspectionService.findInspectionById(defectResponse.getIdinspection());
        //if(mesureVis.isPresent()){
            MesureVisuel mesure = new MesureVisuel();
            //mesure.setGieglanFile();
            mesure.setSignature1(defectResponse.getSignature1());
            mesure.setSignature2(defectResponse.getSignature2());
            mesure.setOrganisation(i.getOrganisation());
            mesure = mesurevisuel.save(mesure);
            return mesure;
        //}

        //return null;



    }


    public List<String> ImagePathList(Long visiteId) {

        MesureVisuel m = mesurevisuel.findByGieglanFile_Inspection_VisiteIdVisite(visiteId);
        List<String> paths = new ArrayList<>();
        paths.add(m.getImage1());
        paths.add(m.getImage2());

        return paths;
    }
}
