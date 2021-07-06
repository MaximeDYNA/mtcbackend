package com.catis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.entity.Partenaire;
import com.catis.service.PartenaireService;

@RestController
public class PartenaireController {

    @Autowired
    private PartenaireService partenaireService;

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/partenaires")
    public void creerPartenaire(@RequestBody Partenaire partenaire) {
        partenaireService.addPartenaire(partenaire);
    }
}
