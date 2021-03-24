package com.example.todo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.todo.exception.EntityNotPresentException;
import com.example.todo.configuration.MyUserDetails;
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
		Optional<User> user  = userRepo.findByUserName(username);
		if(!user.isPresent()) {
			throw new EntityNotPresentException("User is not present");
		}
		return Optional.ofNullable(user.get().getTodos()).orElse(Collections.emptyList());
	}
	
	public Todo save(TodoJson todoJson) {
		MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Todo todo = new Todo();
		todo.setName(todoJson.getName());
		
		Optional<User> user = userRepo.findByUserName(details.getUsername());
		if(user.isPresent()) {
			todo.setUser(user.get());
		}
		return todoRepo.save(todo);
	}
	
	public Todo update(TodoJson todoJson, Integer id) throws IllegalArgumentException, EntityNotPresentException{
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		
		Todo todo = todoRepo.findById(id).orElseThrow(()->new EntityNotPresentException("Todo is not present"));
		
		todo.setName(todoJson.getName());
		return todoRepo.save(todo);
	}
	
	public Todo get(Integer id) throws EntityNotPresentException {
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		return todoRepo.findById(id).orElseThrow(()->new EntityNotPresentException("Todo is not present"));
	}
	
	public void delete(Integer id) throws IllegalArgumentException, EntityNotPresentException {
		if(id==null) {
			throw new IllegalArgumentException("Id must not be null");
		}
		
		Todo todo = todoRepo.findById(id).orElseThrow(()-> new EntityNotPresentException("Todo is not present"));
		todo.setDeleted(true);
		
		todoRepo.save(todo);
	}
	
	public void asigneTodoToUser(Integer userId, Integer todoId) throws EntityNotPresentException {
		Optional<User> user = userRepo.findById(userId);
		Optional<Todo> todo = todoRepo.findById(todoId);
		
		if(!user.isPresent() || !todo.isPresent()) {
			throw new EntityNotPresentException("User/Todo not present");
		}
		
		todo.get().setUser(user.get());
		todoRepo.save(todo.get());
		
	}
	
	public List<Todo> getUserTodos(Integer userId) throws EntityNotPresentException {
		Optional<User> user = userRepo.findById(userId);
		if(!user.isPresent()) {
			throw new EntityNotPresentException("User is not present");
		}
		return Optional.ofNullable(user.get().getTodos()).orElse(Collections.emptyList());
		
	}
}
