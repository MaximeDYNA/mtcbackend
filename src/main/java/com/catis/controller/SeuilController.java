package com.catis.controller;

import com.catis.model.entity.Formule;
import com.catis.model.entity.Lexique;
import com.catis.model.entity.Organisation;
import com.catis.model.entity.Seuil;
import com.catis.objectTemporaire.SeuilPOJO;
import com.catis.repository.FormuleRepository;
import com.catis.repository.SeuilRepository;
import com.catis.service.LexiqueService;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/seuils")
public class SeuilController {
    @Autowired
    private OrganisationService os;
    @Autowired
    private SeuilRepository sr;
    @Autowired
    private FormuleRepository fr;
    @Autowired
    private LexiqueService ls;


    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<Seuil> seuilList = sr.findByActiveStatusTrue();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", seuilList);

    }

    @PostMapping
    public ResponseEntity<Object> addMachine(@RequestBody SeuilPOJO pojo){
        Organisation o = pojo.getOrganisationId() == null ? null : os.findByOrganisationId(pojo.getOrganisationId().getId());
        Formule f = pojo.getFormule() == null ? null : fr.findById(pojo.getFormule().getId()).get();
        Lexique l = pojo.getLexique() == null ? null : ls.findById(pojo.getLexique().getId());
        Seuil seuil = new Seuil();

        seuil.setId(pojo.getId());
        seuil.setCodeMessage(pojo.getCodeMessage());
        seuil.setDecision(pojo.isDecision());
        seuil.setFormule(f);
        seuil.setLexique(l);
        seuil.setOperande(pojo.getOperande());
        seuil.setOrganisation(o);
        seuil.setValue(pojo.getValue());

        seuil = sr.save(seuil);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", seuil);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){

        try{
            sr.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

}
