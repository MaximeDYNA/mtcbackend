package com.catis.controller;

import com.catis.model.control.GieglanFile;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.*;
import com.catis.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
@CrossOrigin

public class SseController {

    private static Logger log = LoggerFactory.getLogger(VisiteController.class);


    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();

    @GetMapping(value="/public/subscribe",consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(){
        System.out.println("---Subscribe---");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        try{
            emitter.send(SseEmitter.event().name("INIT"));
        }catch(IOException e){
            log.error(e.getMessage());
        }
        emitters.add(emitter);
        emitter.onCompletion(()->emitters.remove(emitter));

        return emitter;
    }



}
