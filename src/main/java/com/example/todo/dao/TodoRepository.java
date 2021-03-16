package com.example.todo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.dto.Todo;

public interface TodoRepository extends JpaRepository<Todo,Integer>{

}
