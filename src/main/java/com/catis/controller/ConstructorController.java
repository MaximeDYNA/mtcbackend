package com.catis.controller;

import com.catis.model.entity.Constructor;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.ConstructorPOJO;
import com.catis.repository.ConstructorRepository;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin/constructors")
public class ConstructorController {

    @Autowired
    private ConstructorRepository constructorRepository;
    @Autowired
    private OrganisationService os;


    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<Constructor> constructorList = new ArrayList<>();

        constructorRepository.findByActiveStatusTrue().forEach(constructorList::add);


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", constructorList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){

        try{
            constructorRepository.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody ConstructorPOJO constructorPOJO){

        try{

            Constructor c = new Constructor();
            Organisation o = constructorPOJO.getOrganisation() == null ? null : os.findByOrganisationId(constructorPOJO.getOrganisation().getId());
            c.setId(constructorPOJO.getId());
            c.setName(constructorPOJO.getName());
            c.setOrganisation(o);
            c = constructorRepository.save(c);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

    @GetMapping("/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Constructor> cats = constructorRepository.findByActiveStatusTrue();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Constructor c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getName() +" | "
                    + (c.getOrganisation() == null? "Toutes" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "OK", catsSelect);
    }

}
