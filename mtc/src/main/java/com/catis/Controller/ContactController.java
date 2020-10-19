package com.catis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.model.Contact;
import com.catis.service.ContactService;

@RestController
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@RequestMapping(method= RequestMethod.POST, value="/api/v1/contacts")
	private void addContact(Contact contact) {
		
	}
	
}
