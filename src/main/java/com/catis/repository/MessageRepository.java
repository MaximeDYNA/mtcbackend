package com.catis.repository;

import com.catis.model.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
    Message findByCode(String code);
}
