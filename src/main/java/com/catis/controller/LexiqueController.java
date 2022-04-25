package com.catis.controller;

import java.util.*;

import com.catis.objectTemporaire.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.Client;
import com.catis.model.entity.Lexique;
import com.catis.model.entity.VersionLexique;
import com.catis.service.CategorieVehiculeService;
import com.catis.service.ClassificationService;
import com.catis.service.ClientService;
import com.catis.service.LexiqueService;
import com.catis.service.VersionLexiqueService;

@RestController
@CrossOrigin
public class LexiqueController {

    private VersionLexiqueService versionLexiqueService;
    private LexiqueService lexiqueService;
    private ClientService clientService;
    private CategorieVehiculeService categorieVehiculeService;
    private ClassificationService cS;

    @Autowired
    public LexiqueController(VersionLexiqueService versionLexiqueService, LexiqueService lexiqueService,
                             ClientService clientService, CategorieVehiculeService categorieVehiculeService, ClassificationService cS) {
        super();
        this.versionLexiqueService = versionLexiqueService;
        this.lexiqueService = lexiqueService;
        this.clientService = clientService;
        this.categorieVehiculeService = categorieVehiculeService;
        this.cS = cS;
    }


    @PostMapping(value = "/api/v1/admin/lexique")
    @Transactional
    public ResponseEntity<Object> ajouterLexique(@RequestBody LexiqueReceived lexique) {
        /**Version lexique***/

        VersionLexique vl = new VersionLexique();
        vl.setId(lexique.getId() == null ? null : lexique.getId());

        vl.setLibelle(lexique.getNom());
        vl.setVersion(lexique.getVersion());
        vl = versionLexiqueService.add(vl);

        /********------******/

        for (LexiquePOJO l : lexique.getRows()) {
            Lexique lexiq = new Lexique();
            //lexiq.setId(l.getId() == null ? null : l.getId());

            lexiq.setClassification(cS.findById(l.getClassificationId()) == null ? null :
                    (cS.findById(l.getClassificationId()).isPresent() ? cS.findById(l.getClassificationId()).get() : null ));
            lexiq.setCode(l.getCode().replace("\"", ""));
            System.out.println("le code est le suivant :" + lexiq.getCode());
            lexiq = lexiqueService.editLexiqueByCode(lexiq);
            lexiq.setLibelle(l.getLibelle());

            lexiq.setParent(lexiqueService.findByCode(l.getParent() == null ? l.getParent() : l.getParent().replace("\"", "")));
            if (l.getHaschild().equals("TRUE") || l.getHaschild().equals("true")) {
                lexiq.setHaschild(true);
            } else
                lexiq.setHaschild(false);
            lexiq.setVersionLexique(vl);
            lexiq.setVisuel(Boolean.valueOf(l.getVisual()));
            lexiq.setCategorieVehicule(categorieVehiculeService.findById(l.getCategoryId()));
            lexiqueService.add(lexiq);
        }
        vl = versionLexiqueService.findById(vl.getId());


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", vl);
    }


    @GetMapping(value = "/api/v1/admin/lexiques/{id}")
    public ResponseEntity<Object> getLexiquesForUpdate(@PathVariable UUID id) {

        LexiqueReceived lr = new LexiqueReceived();
        List<LexiquePOJO> list = new ArrayList<>();

        for (Lexique l : lexiqueService.findByVersionLexique(id)) {
            LexiquePOJO pojo = new LexiquePOJO();
            pojo.setId(UUID.fromString(l.getId()));
            pojo.setCode(l.getCode());
            pojo.setLibelle(l.getLibelle());
            pojo.setParent(l.getParent() == null ? null : l.getParent().getCode());
            pojo.setVisual(l.getVisuel().toString());
            pojo.setHaschild(l.getHaschild().toString());
            pojo.setCategoryId(l.getCategorieVehicule().getId());
            List<UUID> ids = new ArrayList<>();
            for (Client i : l.getClients()) {
                ids.add(i.getClientId());
            }

            pojo.setVersion(l.getVersionLexique().getId());
            list.add(pojo);

        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", list);
    }

    @PostMapping(value = "/api/v1/all/lexiques/read")
    public ResponseEntity<Object> readLexiques(@RequestBody LexiqueAndCategorieDTO lexiqueAndCategorieDTO) {

        System.out.println("***********The server has received request from android tablet**********");
        System.out.println("this is payload *"+ ToStringBuilder.reflectionToString(lexiqueAndCategorieDTO));
        System.out.println("************************************************************************");
        LexiqueChildDTO lexiqueChildDTO;
        List<LexiqueDTO> parents = new ArrayList<>();

        for (Lexique l : lexiqueService.findByVersionLexiqueAndCategorie(lexiqueAndCategorieDTO.getVersion(), lexiqueAndCategorieDTO.type)) {
            //le code recupère uniquement les parents et leurs enfants
            if (l.getParent() == null) {
                LexiqueDTO lexiqueDTO = new LexiqueDTO();
                lexiqueDTO.setId(UUID.fromString(l.getId()));
                lexiqueDTO.setName(l.getCode() + " :" + l.getLibelle());
                List<LexiqueChildDTO>  children = new ArrayList<>();
                for (Lexique child : l.getChilds()) {
                    lexiqueChildDTO = new LexiqueChildDTO();
                    lexiqueChildDTO.setId(UUID.fromString(child.getId()));
                    lexiqueChildDTO.setName(child.getCode() + " :" + child.getLibelle());
                    children.add(lexiqueChildDTO);
                }
                lexiqueDTO.setChildren(children);
                parents.add(lexiqueDTO);
            }
        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", parents);
    }
    @GetMapping("/api/v1/admin/lexique/select")
    public ResponseEntity<Object> getLexiquesOfMtcforSelect(){

        List<Lexique> cats = lexiqueService.findAllActive();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Lexique c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getId()));
            cat.put("name", c.getCode() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "OK", catsSelect);
    }


}
