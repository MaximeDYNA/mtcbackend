package com.catis.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.baeldung.spring.data.es.repository")
@ComponentScan(basePackages = { "com.baeldung.spring.data.es.service" })
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;


    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(host)
            .build();

        return RestClients.create(clientConfiguration)
            .rest();
    }
}

