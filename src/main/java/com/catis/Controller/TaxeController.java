package com.catis.Controller;

import com.catis.model.Organisation;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.objectTemporaire.ObjectForSelect;
import com.catis.objectTemporaire.TaxePOJO;
import com.catis.service.OrganisationService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/taxes")
public class TaxeController {
    @Autowired
    private TaxeService taxeService;
    @Autowired
    private TaxeProduitService taxeProduitService;
    @Autowired
    private ProduitService ps;
    @Autowired
    private OrganisationService os;

    @GetMapping
    public ResponseEntity<Object> getUsersOfOrganisation(){
        List<Taxe> taxes = taxeService.getAllActiveTax();
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", taxes );

    }
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody TaxePOJO taxePOJO){
        Taxe taxe = new Taxe();
        Organisation o = taxePOJO.getOrganisationId() == null ? null : os.findByOrganisationId(taxePOJO.getOrganisationId());
        taxe.setIncluse(taxePOJO.isIncluse());
        taxe.setOrganisation(o);
        taxe.setDescription(taxePOJO.getDescription());
        taxe.setValeur(taxePOJO.getValeur());
        taxe.setNom(taxePOJO.getNom());
        taxe.setTaxeId(taxePOJO.getTaxeId());
        Set<TaxeProduit> taxeProduits = new HashSet<>();
        TaxeProduit tp;
        if(taxePOJO.getProduits() != null){
            for(ObjectForSelect i : taxePOJO.getProduits()){
                tp = new TaxeProduit(taxe, ps.findById(i.getId()));
                tp.setOrganisation(o);
                taxeProduits.add(tp);
            }
        }
        taxe.setTaxeProduit(taxeProduits);

        Taxe taxeReturn = taxeService.save(taxe);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", taxeReturn );

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            taxeService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "KO", null);
        }
    }

}
