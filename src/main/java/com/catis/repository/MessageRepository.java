package com.catis.repository;

import com.catis.model.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findByCode(String code);
}
