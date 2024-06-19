package com.catis.controller;

import com.catis.model.entity.Formule;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.FormulePOJO;
import com.catis.repository.FormuleRepository;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/admin/formules")
public class FormuleController {
    @Autowired
    private FormuleRepository fr;

    @Autowired
    private OrganisationService os;

    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<Formule> machines = fr.findByActiveStatusTrue();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", machines);

    }

    @PostMapping
    public ResponseEntity<Object> addMachine(@RequestBody FormulePOJO pojo){
        Organisation o = pojo.getOrganisationId() == null ? null : os.findByOrganisationId(pojo.getOrganisationId().getId());
        Formule formule = new Formule();

        formule.setId(pojo.getId().toString());
        formule.setDescription(pojo.getDescription());
        formule.setOrganisation(o);

        formule = fr.save(formule);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", formule);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id){

        try{
            fr.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

    @Transactional
    @GetMapping("/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Formule> cats = fr.findByActiveStatusTrue();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Formule c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getDescription() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "OK", catsSelect);
    }
}
