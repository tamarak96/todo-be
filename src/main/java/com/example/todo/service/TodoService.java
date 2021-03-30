package com.example.todo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.exception.ActionNotAllowedException;
import com.example.todo.exception.EntityNotPresentException;
import com.example.todo.dao.TodoRepository;
import com.example.todo.dao.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.todo.dto.Todo;
import com.example.todo.dto.User;
import com.example.todo.json.TodoJson;

@Service
@Transactional
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public List<Todo> listAll(String username) throws EntityNotPresentException{
		User user  = userRepo.findByUserName(username).orElseThrow(()->new EntityNotPresentException("User is not present"));
	
		return Optional.ofNullable(user.getTodos()).orElse(Collections.emptyList());
	}
	
	public Todo save(TodoJson todoJson, String username) throws EntityNotPresentException {		
		User user = userRepo.findByUserName(username).orElseThrow(()->new EntityNotPresentException("User is not present"));
		
		Todo todo = new Todo();
		todo.setName(todoJson.getName());
		todo.setUser(user);
		return todoRepo.save(todo);
	}
	
	public Todo update(TodoJson todoJson, Integer id, String username) throws IllegalArgumentException, EntityNotPresentException, ActionNotAllowedException{
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		User authenticatedUser  = userRepo.findByUserName(username).orElseThrow(()->new EntityNotPresentException("User is not present"));
		Todo todo = todoRepo.findById(id).orElseThrow(()->new EntityNotPresentException("Todo is not present"));
		
		if(!authenticatedUser.equals(todo.getUser())) {
			throw new ActionNotAllowedException("Action Not Allowed");
		}
		
		todo.setName(todoJson.getName());
		return todoRepo.save(todo);
	}
	
	public Todo get(Integer id, String username) throws EntityNotPresentException, ActionNotAllowedException {
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		User authenticatedUser  = userRepo.findByUserName(username).orElseThrow(()->new EntityNotPresentException("User is not present"));
		Todo todo = todoRepo.findById(id).orElseThrow(()->new EntityNotPresentException("Todo is not present"));
		
		if(!authenticatedUser.equals(todo.getUser())) {
			throw new ActionNotAllowedException("Action Not Allowed");
		}
		return todo;
	}
	
	public void delete(Integer id, String username) throws IllegalArgumentException, EntityNotPresentException, ActionNotAllowedException {
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		
		User authenticatedUser  = userRepo.findByUserName(username).orElseThrow(()->new EntityNotPresentException("User is not present"));
		Todo todo = todoRepo.findById(id).orElseThrow(()-> new EntityNotPresentException("Todo is not present"));
		
		if(!authenticatedUser.equals(todo.getUser())) {
			throw new ActionNotAllowedException("Action Not Allowed");
		}
		
		todo.setDeleted(true);
		todoRepo.save(todo);
	}
	
	public void asigneTodoToUser(Integer userId, Integer todoId) throws EntityNotPresentException {
		User user = userRepo.findById(userId).orElseThrow(()-> new EntityNotPresentException("User is not present"));
		Todo todo = todoRepo.findById(todoId).orElseThrow(()-> new EntityNotPresentException("Todo is not present"));
		
		todo.setUser(user);
		todoRepo.save(todo);
		
	}
	
	public List<Todo> getUserTodos(Integer userId) throws EntityNotPresentException {
		User user = userRepo.findById(userId).orElseThrow(()-> new EntityNotPresentException("User is not present"));
		return Optional.ofNullable(user.getTodos()).orElse(Collections.emptyList());
		
	}
}
