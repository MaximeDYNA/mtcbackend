package com.catis.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;


import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.CaisseDTO;
import com.catis.objectTemporaire.CaissePOJO;
import com.catis.objectTemporaire.ObjectForSelect;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Caisse;
import com.catis.service.CaisseService;


@RestController
@CrossOrigin
public class CaisseController {
    @Autowired
    private CaisseService caisseService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private PagedResourcesAssembler<CaisseDTO> pagedResourcesAssembler;
//     @Autowired
//     private PagedResourcesAssembler<Caisse> pagedResourcesAssembler;


    private static Logger logger = LoggerFactory.getLogger(CaisseController.class);

//     @GetMapping(value ="/api/v1/admin/caisses", params = {"page", "size"})
//     public ResponseEntity<Object> afficherLesCaisses(@RequestParam("page") int page,
//                                                      @RequestParam("size") int size) {

//         List<Caisse> caisses = caisseService.findAllCaisse(PageRequest.of(page, size, Sort.by("createdDate").descending()));

//         Page<Caisse> pages = new PageImpl<>(caisses, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
//         PagedModel<EntityModel<Caisse>> result = pagedResourcesAssembler
//                 .toModel(pages);

//         return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
//                 result);

//     }


        @GetMapping(value ="/api/v1/admin/caisses", params = { "search", "page", "size"})
        public ResponseEntity<Object> afficherLesCaisses(@RequestParam("search") String search, @RequestParam("page") int page,
        @RequestParam("size") int size) {
                logger.info("searching with " + search);
                if(search == null || search.isEmpty()){
                        search = "";
                }
                // Call the service method to get the paginated CaisseDTOs
                logger.info("Call the service method to get the paginated CaisseDTOs");
                Page<CaisseDTO> caissesDTOPage = caisseService.findAllCaisse(search, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));

                // Use the paged resources assembler to format the result
                PagedModel<EntityModel<CaisseDTO>> result = pagedResourcesAssembler.toModel(caissesDTOPage);

                return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", result);
        }


//     @Transactional
//     @GetMapping("/api/v1/admin/caisses/select")
//     public ResponseEntity<Object> getCaissesOfMtcforSelect(){

//         List<Caisse> caisses = caisseService.findAllCaisse();
//         List<Map<String, String>> caissesSelect = new ArrayList<>();

//         Map<String, String> caisse;


//         for(Caisse c: caisses){
//             caisse = new HashMap<>();
//             caisse.put("id", String.valueOf(c.getCaisseId()));
//             caisse.put("name", c.getLibelle() +" | "+ c.getOrganisation().getNom());
//             caissesSelect.add(caisse);
//         }

//         return ApiResponseHandler.generateResponse(HttpStatus.OK,
//                 true, Message.ListOK + " Caisses", caissesSelect);
//     }


        // Endpoint to get caisses with search functionality
    @GetMapping("/api/v1/admin/caisses/select")
    public ResponseEntity<Object> getCaissesForSelect(@RequestParam(value = "search", required = false, defaultValue = "") String search) {
        // Fixed page size of 10
        PageRequest pageable = PageRequest.of(0, 10);  // Always return the first 10 elements

        // Fetch paginated caisses based on search criteria
        Page<Map<String, String>> caissesSelect = caisseService.findCaisseForSelect(search, pageable);

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Caisses fetched successfully", caissesSelect);
    }

    @Transactional
    @PostMapping("/api/v1/admin/caisses")
    public ResponseEntity<Object> save(@RequestBody CaissePOJO caissePOJO) {

        Caisse caisse = new Caisse();
        Organisation organisation = caissePOJO.getOrganisation() == null?
                null : organisationService.findOrganisationById(caissePOJO.getOrganisation().getId());

        // id is auto generated, flemming removed this line        
        // caisse.setCaisseId(caissePOJO.getCaisse_id());
        caisse.setLibelle(caissePOJO.getLibelle() == null ? null : caissePOJO.getLibelle() );
        caisse.setDescription(caissePOJO.getDescription() == null ? null : caissePOJO.getDescription());
        caisse.setOrganisation(organisation);
        caisse = caisseService.addCaisse(caisse);

        CaissePOJO dto = new CaissePOJO();
        dto.setCaisse_id(caisse.getCaisseId());
        dto.setLibelle(caisse.getLibelle());
        dto.setDescription(caisse.getDescription());
        ObjectForSelect organ = new ObjectForSelect();

        // Set the id and name using the setter methods
        organ.setId(caisse.getOrganisation() != null ? caisse.getOrganisation().getOrganisationId() : null);
        organ.setName(caisse.getOrganisation() != null ? caisse.getOrganisation().getNom() : null);
        dto.setOrganisation(organ);
                    
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                dto);

    }

    @DeleteMapping("/api/v1/admin/caisses/{id}")
    public ResponseEntity<Object> delete (@PathVariable UUID id) {

        caisseService.deleteCaisseById(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                null);
    }


}
