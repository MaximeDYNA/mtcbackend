package com.catis.service;

import java.util.ArrayList;
import java.util.List;

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

    public Contact getContactByPartenaireId(long id) {
        return contactRepository.findByPartenaire_PartenaireId(id);
    }

    public Contact findById(Long id) {
        return contactRepository.findById(id).get();
    }

    public void deleteById(Long id){
        contactRepository.deleteById(id);
    }


}
