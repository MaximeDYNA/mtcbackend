package com.catis.Controller;

import com.catis.model.Taxe;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/taxes")
public class TaxeController {
    @Autowired
    private TaxeService taxeService;
    @Autowired
    private TaxeProduitService taxeProduitService;

    @GetMapping
    public ResponseEntity<Object> getUsersOfOrganisation(){
        List<Taxe> taxes = taxeService.getAllActiveTax();
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", taxes );

    }


}
