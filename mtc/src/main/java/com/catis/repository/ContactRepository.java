package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long>{
	Contact findByPartenaire_PartenaireId(long id);
}
