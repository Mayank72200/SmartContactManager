package com.contactmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contactmanager.entity.Contacts;

@Repository
public interface ContactRepo extends JpaRepository<Contacts, Integer> {

}
