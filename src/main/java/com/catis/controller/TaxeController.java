package com.catis.controller;

import com.catis.model.entity.Organisation;
import com.catis.model.entity.SessionCaisse;
import com.catis.model.entity.Taxe;
import com.catis.model.entity.TaxeProduit;
import com.catis.objectTemporaire.ObjectForSelect;
import com.catis.objectTemporaire.TaxePOJO;
import com.catis.service.OrganisationService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

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
    @Autowired
    private PagedResourcesAssembler<Taxe> pagedResourcesAssembler;

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<Object> getTax(@RequestParam("page") int page,
                                                         @RequestParam("size") int size){
        List<Taxe> taxes = taxeService.getAllActiveTax(PageRequest.of(page, size, Sort.by("createdDate").descending()));
        Page<Taxe> pages = new PageImpl<>(taxes, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
        PagedModel<EntityModel<Taxe>> result = pagedResourcesAssembler
                .toModel(pages);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", result );

    }
    @Transactional
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
                //ajoute la ligne TaxeProduit seulement si elle n'existe pas déjà
                    if(!taxeProduitService.findBytaxeIdAndProduitId(taxe.getTaxeId(), i.getId()).isPresent())
                        taxeProduits.add(tp);
            }
        }
        taxe.setTaxeProduit(taxeProduits);

        Taxe taxeReturn = taxeService.save(taxe);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", taxeReturn );

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id){
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
