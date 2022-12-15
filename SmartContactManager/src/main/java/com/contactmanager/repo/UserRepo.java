package com.contactmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactmanager.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	
	@Query("select u from User u where u.email = :email")
	public User getUserByUsername(@Param("email") String email);

}
