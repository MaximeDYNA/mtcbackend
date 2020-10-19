package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Contact;

public interface ContactRepository extends CrudRepository<Contact, String>{

}
