package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Contact;
import com.catis.repository.ContactRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;
	
	public void addContact(Contact contact) {
		contactRepository.save(contact);
	}
	public List<Contact> getContacts(){
		List<Contact> contacts = new ArrayList<>();
		contactRepository.findAll().forEach(contacts::add);
		return contacts;
	}
	public Contact getContactByPartenaireId(long id){
		return contactRepository.findByPartenaire_PartenaireId(id);
	}
	public Contact findById(Long id) {
		return contactRepository.findById(id).get();
	}
	
}
