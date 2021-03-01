package com.catis.Controller;

import com.catis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @Autowired
    private VisiteService vs;
    @Autowired
    private ProduitService ps;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;

    @GetMapping("/api/v1/visite/maj/{id}")
    public void majvisiteEvent(@PathVariable Long id){
        try {
            System.out.println("testttttttttttttttttttttttttttttttttttttttttttttttttttt\n");
            VisiteController.dispatchEdit(vs.findById(id),
                    vs, gieglanFileService, catSer, ps);
            System.out.println("le Job a effectué un chanqement sur la visite n°"+id+" :)");

        }
        catch (Exception e) {
            System.out.println("Erreur survenur lors de la notification du Job");
        }
    }
}