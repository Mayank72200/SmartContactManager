package com.contactmanager.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactmanager.entity.Contacts;
import com.contactmanager.entity.User;

@Repository
public interface ContactRepo extends JpaRepository<Contacts, Integer> {

	@Query("from Contacts as c where c.user.uid =:userId")
	public Page<Contacts> findContactsByUser(@Param("userId") int userId , Pageable pageable);
	
	
	public List<Contacts> findByNameContainingAndUser(String name, User user);
	
}
