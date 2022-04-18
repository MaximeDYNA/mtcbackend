package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Contact;
import com.catis.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getContacts(Pageable pageable) {
        List<Contact> contacts = new ArrayList<>();
        contactRepository.findByActiveStatusTrue(pageable).forEach(contacts::add);
        return contacts;
    }
    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contactRepository.findByActiveStatusTrue().forEach(contacts::add);
        return contacts;
    }

    public Contact getContactByPartenaireId(UUID id) {
        return contactRepository.findByPartenaire_PartenaireId(id);
    }

    public Contact findById(UUID id) {
        return contactRepository.findById(id).get();
    }

    public void deleteById(UUID id){
        contactRepository.deleteById(id);
    }


}
