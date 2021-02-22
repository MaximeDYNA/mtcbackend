package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.catis.model.DefectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.MesureVisuel;
import com.catis.repository.MesureVisuelRepository;

@Service
public class MesureVisuelService {

    @Autowired
    private MesureVisuelRepository mesurevisuel;


    public MesureVisuel addDataInspection(MesureVisuel mesurevisuels) {
        MesureVisuel m = mesurevisuel.save(mesurevisuels);
        return m;

    }

    public MesureVisuel addSignatureToMesureVisuel(DefectResponse defectResponse) {

        Optional<MesureVisuel> mesureVis = mesurevisuel.byIdInspection(defectResponse.getIdinspection()).stream().findFirst();
        if(mesureVis.isPresent()){
            MesureVisuel mesure = mesureVis.get();
            mesure.setSignature1(defectResponse.getSignature1());
            mesure.setSignature2(defectResponse.getSignature2());
            mesure = mesurevisuel.save(mesure);
            return mesure;
        }

        return null;



    }


    public List<String> ImagePathList(Long visiteId) {

        MesureVisuel m = mesurevisuel.findByInspection_VisiteIdVisite(visiteId);
        List<String> paths = new ArrayList<>();
        paths.add(m.getImage1());
        paths.add(m.getImage2());

        return paths;
    }
}
