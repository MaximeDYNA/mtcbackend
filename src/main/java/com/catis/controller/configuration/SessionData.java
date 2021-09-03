package com.catis.controller.configuration;

import com.catis.objectTemporaire.UserInfoIn;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;


public class SessionData {
    @Autowired
    HttpServletRequest request;



    public static Long getOrganisationId(HttpServletRequest request){
        return Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId());
    }
}
