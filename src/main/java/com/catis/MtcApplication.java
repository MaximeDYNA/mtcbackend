package com.catis;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableAsync
public class MtcApplication {
    @Autowired
    private Environment env;

    public static void main(String[] args) {

        SpringApplication.run(MtcApplication.class, args);


    }
    //@PostConstruct
    public void test(){
        RestTemplate r = new RestTemplate();

        String urlDuFou =
                env
                        .getProperty("control.service.ip");
        Object response = r.postForObject(urlDuFou + 1,null , Object.class);
        System.out.println("Post reussi voici la reponse "+ ToStringBuilder.reflectionToString(response));
    }


}
