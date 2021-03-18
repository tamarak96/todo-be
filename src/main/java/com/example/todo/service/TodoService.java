package com.example.todo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.dao.TodoRepository;
import com.example.todo.dao.UserRepository;

import java.util.List;
import java.util.Optional;

import com.example.todo.dto.Todo;
import com.example.todo.dto.User;
import com.example.todo.json.TodoJSON;

@Service
@Transactional
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public Optional<List<Todo>> listAll(String username){
		Optional<User> user  = userRepo.findByUserName(username);
		return Optional.ofNullable(user.get().getTodos());
	}
	
	public Todo save(TodoJSON todoJSON) {
		Todo todo = new Todo();
		todo.setName(todoJSON.getName());
		return todoRepo.save(todo);
	}
	
	public Todo update(TodoJSON todoJSON, Integer id) throws Exception{
		if(id==null) {
			throw new Exception("Id must not be null");
		}
		
		Todo todo = todoRepo.findById(id).orElseThrow();
		
		todo.setName(todoJSON.getName());
		return todoRepo.save(todo);
	}
	
	public Optional<Todo> get(Integer id) {
		return Optional.ofNullable(todoRepo.findById(id).get());
	}
	
	public void delete(Integer id) throws Exception {
		if(id==null) {
			throw new Exception("Id must not be null");
		}
		
		Todo todo = todoRepo.findById(id).orElseThrow();
		todo.setDeleted(true);
		
		todoRepo.save(todo);
	}
	
	public void asigneTodoToUser(Integer user_id, Integer todo_id) throws Exception {
		Optional<User> user = userRepo.findById(user_id);
		Optional<Todo> todo = todoRepo.findById(todo_id);
		
		if(!user.isPresent() || !todo.isPresent()) {
			throw new Exception("");
		}
		
		todo.get().setUser(user.get());
		todoRepo.save(todo.get());
		
	}
	
	public Optional<List<Todo>> getUserTodos(Integer user_id) throws Exception {
		Optional<User> user = userRepo.findById(user_id);
		if(!user.isPresent()) {
			throw new Exception("User is not present");
		}
		return Optional.ofNullable(user.get().getTodos());
		
	}
}
