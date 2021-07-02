package com.catis;

import com.catis.model.Visite;
import com.catis.service.AuditService;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


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
