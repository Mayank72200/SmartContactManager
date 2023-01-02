package com.contactmanager.Cotroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.entity.Contacts;
import com.contactmanager.entity.User;
import com.contactmanager.repo.ContactRepo;
import com.contactmanager.repo.UserRepo;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private ContactRepo contactrepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal  principal){
		
		System.out.println(query);
		
		User user= userrepo.getUserByUsername(principal.getName());
		
		List<Contacts> contacts= contactrepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
		
	} 
	
	
	
}
