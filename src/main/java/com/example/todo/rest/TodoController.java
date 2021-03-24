package com.example.todo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.todo.exception.EntityNotPresentException;
import com.example.todo.configuration.MyUserDetails;
import com.example.todo.dto.Todo;
import com.example.todo.json.TodoJson;
import com.example.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoController {

	@Autowired
	private TodoService service;

	@GetMapping("/")
	public ResponseEntity<Object> getAllTodos() throws EntityNotPresentException{

		MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Todo> result = service.listAll(details.getUsername());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/todo/{id}")
	public ResponseEntity<Object> getTodoById(@PathVariable Integer id) throws EntityNotPresentException {
		Todo result = service.get(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/todo")
	public ResponseEntity<Object> addTodo(@RequestBody TodoJson todo) {
			Todo result = service.save(todo);
			return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/todo/{id}")
	public ResponseEntity<Object> updateTodo(@RequestBody TodoJson todo, @PathVariable Integer id) throws IllegalArgumentException, EntityNotPresentException {
			Todo updateTodo = service.update(todo, id);
			return new ResponseEntity<>(updateTodo, HttpStatus.OK);
	}

	@DeleteMapping("/todo/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) throws IllegalArgumentException, EntityNotPresentException {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
			
	}

	@PutMapping("/user/{userId}/todo/{todoId}")
	public ResponseEntity<Object> asigneTodoToUser(@PathVariable Integer userId, @PathVariable Integer todoId) throws EntityNotPresentException{
			 service.asigneTodoToUser(userId, todoId);
			 return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/todos/user/{id}")
	public ResponseEntity<Object> getUserTodos(@PathVariable Integer id) throws EntityNotPresentException {
		List<Todo> result = service.getUserTodos(id);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

}
