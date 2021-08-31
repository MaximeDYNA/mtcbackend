package com.catis.service;

import com.catis.model.entity.Inspection;
import com.catis.objectTemporaire.OpenAlprResponseDTO;
import com.catis.objectTemporaire.OpenAlprResponseList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class OpenAlprService {

    public double getPresenceConfidence(String openalpruri, String apiKey, Inspection inspection){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inspection.getDateFin());
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(openalpruri)
                .queryParam("api_key", apiKey)
                .queryParam("start", oneHourBack )
                .queryParam("end", inspection.getDateFin());

        System.err.println(builder.toUriString());

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

// Note: here we are making this converter to process any kind of response,
// not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
        OpenAlprResponseList response = restTemplate.getForObject(
                builder.toUriString(),
                OpenAlprResponseList.class);

        List<OpenAlprResponseDTO> cars = response.getOpenAlprResponseDTOList();
        return calculateMatchingPercentage(inspection.getVisite().getCarteGrise().getNumImmatriculation(), cars);
    }

    public double calculateMatchingPercentage(String ref, List<OpenAlprResponseDTO> cars){
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
