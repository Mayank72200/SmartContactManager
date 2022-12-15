package com.contactmanager.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.entity.User;
import com.contactmanager.repo.UserRepo;


public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("There is no User with !! ");
		}
		
		CustomUserDetails customUserDetails = new  CustomUserDetails(user);
		
		return customUserDetails;
	}

}
