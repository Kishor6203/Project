package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String username);

}
