package com.contactmanager.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entity.User;
import com.contactmanager.helper.Message;
import com.contactmanager.entity.User;
import com.contactmanager.repo.ContactRepo;
import com.contactmanager.repo.UserRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home - Smart Contact Manager");

		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About - Smart Contact Manager");

		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult resultb, @RequestParam(value="agreement",defaultValue = "false")boolean agreement, Model model, HttpSession session) {
		
		try {
			
			
			if(!agreement) {
				
				System.out.println("Please agree terms and Conditions !!!");
				throw new Exception("Please agree terms and Conditions !!!");
			}
			
			if(resultb.hasErrors()) {
				
				System.out.println("Error : "+resultb.toString());
				model.addAttribute("user",user);
				return "signup";
				
				
			}
			
			user.setRole("Role_User");
			user.setIsenabled(true);
			user.setImageUrl("default.png");
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement "+ agreement);
			System.out.println("User " + user);
			model.addAttribute("user",user);
			
			User result= this.userRepo.save(user);
			
			model.addAttribute("user",new User());
			
			session.setAttribute("message", new Message("Registration Successfull !! ","alert-success"));
			
			return "signup";


		}
		catch (Exception e){
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went Wrong !! "+ e.getMessage(),"alert-danger" ));
			
			return "signup";

		}
			
	}
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title","Login Page");
		return "login";
		
	}
	
	
	
}


