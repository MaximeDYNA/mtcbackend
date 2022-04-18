package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Contact;

public interface ContactRepository extends CrudRepository<Contact, UUID> {
    Contact findByPartenaire_PartenaireId(UUID id);
    List<Contact> findByActiveStatusTrue();
    List<Contact> findByActiveStatusTrue(Pageable pageable);


}
