package com.catis.controller.configuration;

import com.catis.objectTemporaire.UserInfoIn;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


public class SessionData {
    @Autowired
    HttpServletRequest request;



    public static UUID getOrganisationId(HttpServletRequest request){
        return UUID.fromString(String.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId()));
    }
}
