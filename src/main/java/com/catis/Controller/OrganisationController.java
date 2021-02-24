package com.catis.Controller;

import com.catis.Controller.message.Message;
import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserInfoIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.Organisation;
import com.catis.service.OrganisationService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;
    @Autowired
    HttpServletRequest request;

    private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/organisations")
    public void creerOrganisation(@RequestBody Organisation organisation) {
        organisationService.addOrgansiation(organisation);
    }

    @GetMapping("/api/v1/organisation")
    public ResponseEntity<Object> connectedUserOrganisation() {

                UserDTO u =UserInfoIn.getUserInfo(request);
                Organisation organisation = organisationService.findByOrganisationId(Long.valueOf(u.getOrganisanionId()));

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", organisation);
        /*try {} catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Organisation", null);
        }*/
    }
}
