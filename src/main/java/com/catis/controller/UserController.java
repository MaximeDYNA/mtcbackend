package com.catis.controller;

import com.catis.controller.message.Message;
import com.catis.model.entity.Caisse;
import com.catis.model.entity.Organisation;
import com.catis.model.entity.Utilisateur;
import com.catis.objectTemporaire.ChildKanbanDTO;
import com.catis.objectTemporaire.UserData;
import com.catis.objectTemporaire.UserKeycloak;
import com.catis.service.KeycloakService;
import com.catis.service.OrganisationService;
import com.catis.service.UtilisateurService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private OrganisationService os;
    @Autowired
    private UtilisateurService us;
    @Autowired
    private PagedResourcesAssembler<Utilisateur> pagedResourcesAssembler;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsersOfOrganisation(@PathVariable Long id){

        List<UserKeycloak> userDTOList = keycloakService.getUserList();
        List<ChildKanbanDTO> children = os.findChildren(id);
        children.add(new ChildKanbanDTO(id,"",0));
        userDTOList = userDTOList.stream()
                .filter(userDTO -> children.stream()
                        .anyMatch(childKanbanDTO -> childKanbanDTO.getId() == userDTO.getOrganisationId()))
                .collect(Collectors.toList());

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Users", userDTOList);
    }

    @GetMapping("/select")
    public ResponseEntity<Object> getUsersOfMtcforSelect(){

        List<UserKeycloak> userDTOList = keycloakService.getUserList();
        List<Map<String, String>> users = new ArrayList<>();

        Map<String, String> user ;


        for(UserKeycloak u: userDTOList){
            user = new HashMap<>();
            user.put("id", u.getId());
            user.put("name", u.getFirstName() +" "+ u.getLastName() +" "+ u.getId());
            users.add(user);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Users", users);
    }
    @GetMapping("/system/select")
    public ResponseEntity<Object> getUsersOfBdforSelect(){

        List<Utilisateur> users = us.findAllUtilisateur();
        List<Map<String, String>> usersList = new ArrayList<>();
        Map<String, String> user ;

        for(Utilisateur u: users){
            user = new HashMap<>();
            user.put("id", String.valueOf(u.getUtilisateurId()));
            user.put("name", u.getKeycloakId() );
            usersList.add(user);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Users", usersList);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<Object> getUsersOfMtc(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        try {

            List<Utilisateur> users = us.findAllUtilisateur(PageRequest.of(page, size, Sort.by("createdDate").descending()));

            Page<Utilisateur> pages = new PageImpl<>(users, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);

            PagedModel<EntityModel<Utilisateur>> result = pagedResourcesAssembler
                    .toModel(pages);

            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, Message.ListOK + " Users", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, Message.ListKO + " Users", null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> enregistrer(@RequestBody UserData userData) {
        try {
            Utilisateur u = new Utilisateur();
            Organisation o = userData.getOrganisationId() == null ? null : os.findByOrganisationId(userData.getOrganisationId());
            u.setOrganisation(o);
            u.setKeycloakId(userData.getKeycloakId());
            u.setUtilisateurId(userData.getUtilisateurId());
            u = us.addUtilisateur(u);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", u);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, Message.ListKO + " Users", null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            us.deleteUtilisateurById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, Message.ListKO + " Users", null);
        }
    }
}
