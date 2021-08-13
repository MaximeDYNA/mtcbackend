package com.catis.Controller.configuration;

import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserInfoIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


public class SessionData {
    @Autowired
    HttpServletRequest request;



    public static Long getOrganisationId(HttpServletRequest request){
        return Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId());
    }
}
