package com.catis.service;

import com.catis.model.entity.Inspection;
import com.catis.objectTemporaire.OpenAlprResponseDTO;
import com.catis.objectTemporaire.OpenAlprResponseList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OpenAlprService {

    @Autowired
    private WebClient.Builder webClient;


    public double getPresenceConfidence(String openalpruri, String apiKey, Inspection inspection){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inspection.getDateFin());
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(openalpruri)
                .queryParam("api_key", apiKey)
                .queryParam("start", sdf.format(oneHourBack))
                .queryParam("end", sdf.format(inspection.getDateFin()));

        System.err.println(builder.toUriString());

        Mono<OpenAlprResponseDTO[]> response = webClient.build().get()
                .uri(builder.toUriString())
                //.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.HOST, "cloud.openalpr.com")
                .accept(MediaType.ALL)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleErrors)
                .bodyToMono(OpenAlprResponseDTO[].class);
        System.out.println("--------------->"+ToStringBuilder.reflectionToString(response.block()));
        OpenAlprResponseDTO[] openAlprResponseDTOS = response.block();

        return calculateMatchingPercentage(inspection.getVisite().getCarteGrise().getNumImmatriculation(), openAlprResponseDTOS);
    }
    private Mono<Throwable> handleErrors(ClientResponse response ){
        System.err.println("HTTP ERROR "+ response.statusCode());
        return response.bodyToMono(String.class).flatMap(body -> {
            System.err.println("HTTP ERROR "+ response.statusCode());
            return Mono.error(new Exception());
        });
    }

    public double calculateMatchingPercentage(String ref, OpenAlprResponseDTO[] cars){
        double max =0;
        String lastSixDigits;
        if(ref.length() > 10){
            lastSixDigits = ref.substring(ref.length() - 6);
        }
        else
        {
            lastSixDigits = ref;
        }
        for(OpenAlprResponseDTO openAlprResponseDTO : cars){
            int distance = levenshteinDistance(lastSixDigits, openAlprResponseDTO.getFields().getBest_plate());
            int bigger = Math.max(lastSixDigits.length(), openAlprResponseDTO.getFields().getBest_plate().length());
            if(max < (bigger - distance) / bigger){
                max = (bigger - distance) / bigger;
            }
        }
        return max;
    }
    public int levenshteinDistance (String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
