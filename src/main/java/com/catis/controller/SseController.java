package com.catis.controller;


import com.catis.controller.configuration.SessionData;
import com.catis.objectTemporaire.EventDto;
import com.catis.repository.NotificationService;
import com.catis.service.EmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class SseController {

    //private static Logger log = LoggerFactory.getLogger(SseController.class);
    @Autowired
    HttpServletRequest request;

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static List<SseEmitter> emitters= new CopyOnWriteArrayList<>();


    @Autowired
    private final EmitterService emitterService;
    @Autowired
    private NotificationService notificationService;

    /*@GetMapping(value="/public/subscribe",consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribeToEvents() {
        String keycloakId = SessionData.getKeycloakId(request);
        //log.info("Subscribing member ", keycloakId);
        SseEmitter emitter = emitterService.createEmitter(keycloakId);

        try{
            emitter.send(SseEmitter.event().name("INIT").data(new EventDto("Ex", new HashMap<String, Object>(){{
                put("abc", "abc");
            }})));
            notificationService.sendNotification(keycloakId, new EventDto("Ex notification", new HashMap<String, Object>(){{
                put("abcd", "abcd");
            }}));
        }catch(IOException e){
            //log.error(e.getMessage());
        }
        return emitter;
    }*/

  /*  @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publishEvent( @RequestBody EventDto event) {
        log.debug("Publishing event {} for member with id {}", event, memberId);
        notificationService.sendNotification(memberId, event);
    }*/

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
