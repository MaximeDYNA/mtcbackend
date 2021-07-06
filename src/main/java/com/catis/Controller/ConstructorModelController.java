package com.catis.Controller;

import com.catis.model.entity.Constructor;
import com.catis.model.entity.ConstructorModel;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.ContructorModelPOJO;
import com.catis.repository.ConstructorModelRepo;
import com.catis.repository.ConstructorRepository;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/constructorModels")
public class ConstructorModelController {

    @Autowired
    private ConstructorModelRepo constructorModelRepo;
    @Autowired
    private ConstructorRepository cr;
    @Autowired
    private OrganisationService os;


    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<ConstructorModel> constructorList = new ArrayList<>();

        constructorModelRepo.findByActiveStatusTrue().forEach(constructorList::add);


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", constructorList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){

        try{
            constructorModelRepo.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody ContructorModelPOJO constructorPOJO){

        try{
            ConstructorModel c = new ConstructorModel();
            Organisation o = constructorPOJO.getOrganisationId() == null ? null : os.findByOrganisationId(constructorPOJO.getOrganisationId().getId());
            Constructor cons = constructorPOJO.getConstructor() == null ? null : cr.findById(constructorPOJO.getConstructor().getId()).get();
            c.setId(constructorPOJO.getId());
            c.setName(constructorPOJO.getName());
            c.setOrganisation(o);
            c.setConstructor(cons);
            c = constructorModelRepo.save(c);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

    @GetMapping("/select")
    public ResponseEntity<Object> forSelect(){

        List<ConstructorModel> cats = constructorModelRepo.findByActiveStatusTrue();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(ConstructorModel c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getName() +" | "
                    + (c.getOrganisation() == null? "Toutes" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}
