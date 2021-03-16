package com.example.todo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.dao.TodoRepository;
import java.util.List;

import com.example.todo.dto.Todo;
import com.example.todo.json.TodoJSON;

@Service
@Transactional
public class TodoService {
	
	@Autowired
	private TodoRepository repo;
	
	public List<Todo> listAll(){
		return repo.findAll();
	}
	
	public Todo save(TodoJSON todoJSON) {
		Todo todo = new Todo();
		todo.setName(todoJSON.getName());
		return repo.save(todo);
	}
	
	public Todo update(TodoJSON todoJSON, Integer id) throws Exception{
		if(id==null) {
			throw new Exception("Id must not be null");
		}
		
		Todo todo = repo.findById(id).orElseThrow();
		
		todo.setName(todoJSON.getName());
		return repo.save(todo);
	}
	
	public Todo get(Integer id) {
		return repo.findById(id).orElse(null);
	}
	
	public void delete(Integer id) throws Exception {
		if(id==null) {
			throw new Exception("Id must not be null");
		}
		
		Todo todo = repo.findById(id).orElseThrow();
		todo.setDeleted(true);
		
		repo.save(todo);
	}
}
