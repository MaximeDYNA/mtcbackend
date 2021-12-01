package com.catis.service;

import com.catis.repository.EmitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class EmitterService {

    private final long eventsTimeout;
    private final EmitterRepository repository;

    public EmitterService(@Value("180000") long eventsTimeout,
                          EmitterRepository repository) {
        this.eventsTimeout = Long.MAX_VALUE;
        this.repository = repository;
    }

    public SseEmitter createEmitter(String memberId) {
        log.info("Create SseEmitter for {}", memberId);
        SseEmitter emitter = new SseEmitter(eventsTimeout);
        emitter.onCompletion(() -> repository.remove(memberId));
        emitter.onTimeout(() -> repository.remove(memberId));
        emitter.onError(e -> {
            log.error("Create SseEmitter exception", e);
            repository.remove(memberId);
        });
        repository.addOrReplaceEmitter(memberId, emitter);
        return emitter;
    }

}