package com.catis.config;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;

@Configuration
public class TurnOffEmbeddedTomcatReload {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {

            @Override
            public void customize(TomcatServletWebServerFactory container) {
                container.addContextCustomizers(new TomcatContextCustomizer() {
                    @Override
                    public void customize(Context cntxt) {
                        cntxt.setReloadable(false);
                    }
                });
            }
        };
    }
}