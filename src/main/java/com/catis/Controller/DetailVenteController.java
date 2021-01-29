package com.catis.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.model.DetailVente;
import com.catis.service.DetailVenteService;
import com.catis.service.OperationCaisseService;
import com.catis.service.VenteService;

@RestController
public class DetailVenteController {

    @Autowired
    private DetailVenteService detailVenteService;
    @Autowired
    private VenteService venteService;
    @Autowired
    private OperationCaisseService ocs;
    private static Logger LOGGER = LoggerFactory.getLogger(DetailVenteController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/detailsventes")
    public void addVente(List<DetailVente> detailVentes) {
        detailVenteService.addVentes(detailVentes);
    }


}
