package com.contactmanager.Cotroller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.entity.Contacts;
import com.contactmanager.entity.User;
import com.contactmanager.helper.Message;
import com.contactmanager.repo.ContactRepo;
import com.contactmanager.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {

		if (principal == null) {

			return "login";
		}

		else {

			return "normal/user_dashboard";
		}
	}

	@ModelAttribute
	public String addCommonData(Model model, Principal principal) {

		if (principal == null) {

			return "login";
		}

		else {
			String userName = principal.getName();
			System.out.println("Username " + userName);

			User user = userRepo.getUserByUsername(userName);

			System.out.println("User " + user);

			model.addAttribute("user", user);

			return "normal/user_dashboard";
		}

	}

	@GetMapping("/add-contact")
	public String addContactsForm(Model model, Principal principal) {

		if (principal == null) {

			return "login";
		} else {
			model.addAttribute("title", "Add Contacts");
			model.addAttribute("contact", new Contacts());

			return "normal/add_contact_form";
		}

	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contacts contact, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session,Model model) {
		
	
		try {
			
			if(principal==null) {
				return "login";
			}
			else {
			
			String name = principal.getName();
			
			User user= userRepo.getUserByUsername(name);
			contact.setUser(user);
			user.getContacts().add(contact);
			
			
			if(file.isEmpty()) {
				
				System.out.print("No Image");
				contact.setImageURl("contact.png");			
			}
			
			else {
				
				contact.setImageURl(file.getOriginalFilename());
				
				File saveFile =new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			
			
			userRepo.save(user);
			
			
			System.out.println("Data "+contact.getImageURl());
			
			
			return "normal/add_contact_form";
			}
		}
		
		catch(Exception e) {
			
			System.out.println("ERROR "+e.getMessage());
			session.setAttribute("message", new Message("Something went Wrong... Try Again !!!","danger"));

			
		}
		return "normal/add_contact_form";
		
		
		
		
	}
	
	@GetMapping("/showContacts/{page}")
	public String showContacts(Model model, Principal principal, @PathVariable("page") Integer page) {
		
		if(principal==null) {
			
			return "login";
			
		}
		else {
			
			String username =principal.getName();
			User user = userRepo.getUserByUsername(username);
			
			Pageable pr =  PageRequest.of(page, 5);
			
			Page<Contacts> contacts= contactRepo.findContactsByUser(user.getUid(),pr);
			
			model.addAttribute("contacts",contacts);
			model.addAttribute("currentPage",page);
			model.addAttribute("totalPages",contacts.getTotalPages());
			model.addAttribute("userId", user.getUid());
			
			
			return "normal/showContacts";
		}
		
	}
	
	@GetMapping("{cid}/contact")
	public String showContactDetail(@PathVariable ("cid") int cid, Model model,Principal principal) {
		
		if(principal==null) {
			
			
			return "login";
		}
		else {
		System.out.println("CID"+cid);
		
		Optional<Contacts> contactdet =contactRepo.findById(cid);
		Contacts contact = contactdet.get();
		System.out.println(contact.getImageURl());
		
		String username=principal.getName();
		
		User user= userRepo.getUserByUsername(username);
		
		if(user.getUid()==contact.getUser().getUid()) {
			
			model.addAttribute("contact",contact);
			model.addAttribute("title", contact.getName());
		}
		
		
		
		
		
		return "normal/contact_details";
		
		}
		
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, Model model,Principal principal) {
		
			User user= userRepo.getUserByUsername(principal.getName());
			Optional<Contacts> contactop = contactRepo.findById(cid);
			Contacts contact = contactop.get();
			
			user.getContacts().remove(contact);
			
			userRepo.save(user);
		
		
		return "redirect:/user/showContacts/0";
		
		
		
	}
	
	@PostMapping("/updateForm/{cid}")
	public String updateForm(@PathVariable("cid") int cid, Model model) {
		
		model.addAttribute("title","Update Contact");
		Contacts contact = contactRepo.findById(cid).get();
		model.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
	
	@PostMapping("/process-updateForm")
	public String updateFormProcess(@ModelAttribute Contacts contact, @RequestParam("profileImage") MultipartFile file, Principal principal, Model model, HttpSession session) {
		
		if(principal==null) {
			
			return "login";
		}
		
		else {
			
		
		try {
			
			Contacts Oldcontact= contactRepo.findById(contact.getCid()).get();
			User user= userRepo.getUserByUsername(principal.getName());

			
			if(!file.isEmpty()) {
				
				//deleting photo
				
				File deletePath =new ClassPathResource("static/img").getFile();
				File deleteFile= new File(deletePath, Oldcontact.getImageURl());
				deleteFile.delete();
				
				
				//Updating photo
				File saveFile =new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename()+user.getUid());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImageURl(file.getOriginalFilename());
				
			}
			
			else {
				
				contact.setImageURl(Oldcontact.getImageURl());
			}
			
			contact.setUser(user);
			contactRepo.save(contact);
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		return "redirect:/user/"+contact.getCid()+"/contact";
		
		}
	}
	
	@GetMapping("/profile")
	public String myprofile(Model model) {
		
		model.addAttribute("title","My Profile");
		
		return "normal/profile";
	}
	
	
	
	
	
	
	
	
	
	
	
}

