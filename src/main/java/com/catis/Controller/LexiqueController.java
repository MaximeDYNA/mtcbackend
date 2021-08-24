package com.catis.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.entity.Client;
import com.catis.model.entity.Lexique;
import com.catis.model.entity.VersionLexique;
import com.catis.objectTemporaire.LexiqueChildDTO;
import com.catis.objectTemporaire.LexiqueDTO;
import com.catis.objectTemporaire.LexiquePOJO;
import com.catis.objectTemporaire.LexiqueReceived;
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
            lexiq = lexiqueService.editLexiqueByCode(lexiq);
            lexiq.setLibelle(l.getLibelle());

            lexiq.setParent(lexiqueService.findByCode(l.getParent() == null ? l.getParent() : l.getParent().replace("\"", "")));
            if (l.getHaschild().equals("TRUE") || l.getHaschild().equals("true")) {
                lexiq.setHaschild(true);
            } else
                lexiq.setHaschild(false);
            lexiq.setVersionLexique(vl);
            lexiq.setVisuel(Boolean.valueOf(l.getVisual()));
            lexiq.setCategorieVehicule(categorieVehiculeService.findById(Long.valueOf(l.getCategoryId())));
            lexiqueService.add(lexiq);
        }
        vl = versionLexiqueService.findById(vl.getId());


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", vl);
    }


    @GetMapping(value = "/api/v1/admin/lexiques/{id}")
    public ResponseEntity<Object> getLexiquesForUpdate(@PathVariable Long id) {

        LexiqueReceived lr = new LexiqueReceived();
        List<LexiquePOJO> list = new ArrayList<>();

        for (Lexique l : lexiqueService.findByVersionLexique(id)) {
            LexiquePOJO pojo = new LexiquePOJO();
            pojo.setId(l.getId());
            pojo.setCode(l.getCode());
            pojo.setLibelle(l.getLibelle());
            pojo.setParent(l.getParent() == null ? null : l.getParent().getCode());
            pojo.setVisual(l.getVisuel().toString());
            pojo.setHaschild(l.getHaschild().toString());
            pojo.setCategoryId(l.getCategorieVehicule().getId().intValue());
            List<Long> ids = new ArrayList<>();
            for (Client i : l.getClients()) {
                ids.add(i.getClientId());
            }

            pojo.setVersion(l.getVersionLexique().getId());
            list.add(pojo);

        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", list);
    }

    @GetMapping(value = "/api/v1/all/lexiques/read/{id}")
    public ResponseEntity<Object> readLexiques(@PathVariable Long id) {

        LexiqueDTO lexiqueDTO;
        LexiqueChildDTO lexiqueChildDTO;
        List<LexiqueDTO> parents = new ArrayList<>();
        List<LexiqueChildDTO> children;

        for (Lexique l : lexiqueService.findByVersionLexique(id)) {
            //le code recupère uniquement les parents et leurs enfants
            if (l.getParent() == null) {
                lexiqueDTO = new LexiqueDTO();
                lexiqueDTO.setId(l.getId());
                lexiqueDTO.setName(l.getCode() + " :" + l.getLibelle());
                children = new ArrayList<>();
                for (Lexique child : l.getChilds()) {
                    lexiqueChildDTO = new LexiqueChildDTO();
                    lexiqueChildDTO.setId(child.getId());
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
