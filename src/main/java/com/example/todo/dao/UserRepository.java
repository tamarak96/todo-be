package com.example.todo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.dto.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	Optional<User> findByUserName(String userName);
}
