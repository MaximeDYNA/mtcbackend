package com.catis.Controller;

import javax.servlet.http.HttpServletRequest;

import com.catis.model.entity.Organisation;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.Utilisateur;
import com.catis.objectTemporaire.ProprietaireDTO;
import com.catis.objectTemporaire.ProprietairePOJO;
import com.catis.service.OrganisationService;
import com.catis.service.UtilisateurService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Controleur;
import com.catis.objectTemporaire.UserDTO;
import com.catis.service.ControleurService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ControleurController {

    @Autowired
    public ControleurService controleurService;
    @Autowired
    private HttpServletRequest request;
    private static String serverUrl = "http://192.168.8.113:8180/auth";
    private static String realm = "mtckeycloak";
    @Autowired
    private OrganisationService os;
    @Autowired
    private UtilisateurService us;

    private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @GetMapping(value = "/api/v1/controleur/{keycloakId}")
    public ResponseEntity<Object> getInfosControleur(@PathVariable String keycloakId) {

        Controleur controleur = controleurService.findControleurBykeycloakId(keycloakId);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", controleur);

    }



    @GetMapping(value = "/api/v1/controleurinfo/{id}")
    public ResponseEntity<Object> getInfosControleur(@PathVariable Long id) {

        Controleur controleur = controleurService.findControleurById(id);
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .authorization(context.getTokenString())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                .build();
        UserResource userResource = keycloak.realm(realm).users().get(controleur.getUtilisateur().getKeycloakId());


        UserDTO user = new UserDTO();
        user.setNom(userResource.toRepresentation().getLastName());
        user.setPrenom(userResource.toRepresentation().getFirstName());
        user.setLogin(userResource.toRepresentation().getUsername());
        user.setEmail(userResource.toRepresentation().getEmail());

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", user);

    }

    @GetMapping(value = "/api/v1/admin/controleurs")
    public ResponseEntity<Object> getAll() {
        try{
            List<Controleur> controleurs = controleurService.findAllControleur();
            List<ProprietaireDTO> ps = new ArrayList<>();
            ProprietaireDTO pro;
            for(Controleur p : controleurs){
                pro = new ProprietaireDTO();
                pro.setIdControleur(p.getIdControleur());
                pro.setNom(p.getPartenaire().getNom());
                pro.setPrenom(p.getPartenaire().getPrenom());
                pro.setDateNaiss(p.getPartenaire().getDateNaiss());
                pro.setEmail(p.getPartenaire().getEmail());
                pro.setLieuDeNaiss(p.getPartenaire().getLieuDeNaiss());
                pro.setOrganisation(p.getOrganisation());
                pro.setPassport(p.getPartenaire().getPassport());
                pro.setPermiDeConduire(p.getPartenaire().getPermiDeConduire());
                pro.setTelephone(p.getPartenaire().getTelephone());
                pro.setCreatedDate(p.getPartenaire().getCreatedDate());
                pro.setCni(p.getPartenaire().getCni());
                pro.setAgremment(p.getAgremment());
                pro.setPartenaireId(p.getPartenaire().getPartenaireId());
                ps.add(pro);
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", ps);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", null);
        }

    }
    @PostMapping("/api/v1/admin/controleurs")
    public ResponseEntity<Object> addproprioAdmin(@RequestBody ProprietairePOJO proprietairePOJO) {


            LOGGER.trace("List des propriétaires des vehicules...");
            Controleur controleur = new Controleur();
            Date date = proprietairePOJO.getDateNaiss() == null ? null:
                    proprietairePOJO.getDateNaiss();
            Organisation organisation = proprietairePOJO.getOrganisationId()==null
                    ? null : os.findByOrganisationId(proprietairePOJO.getOrganisationId().getId());
            Utilisateur user = us.findUtilisateurById(proprietairePOJO.getUser().getId());

            Partenaire partenaire = new Partenaire(proprietairePOJO);
            partenaire.setOrganisation(organisation);
            partenaire.setDateNaiss(date);
            controleur.setPartenaire(partenaire);
            controleur.setOrganisation(organisation);
            controleur.setUtilisateur(user);
            controleur.setIdControleur(proprietairePOJO.getIdControleur());

            controleur = controleurService.addControleur(controleur);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    ,controleur);
          /*  try { } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }*/
    }

    @DeleteMapping("/api/v1/admin/controleurs/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try {
            controleurService.delete(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "KO", null);
        }
    }
}
