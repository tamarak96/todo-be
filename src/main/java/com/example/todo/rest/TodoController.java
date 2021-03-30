package com.example.todo.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.todo.exception.ActionNotAllowedException;
import com.example.todo.exception.EntityNotPresentException;
import com.example.todo.dto.Todo;
import com.example.todo.json.TodoJson;
import com.example.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@Controller
@RequiredArgsConstructor
public class TodoController {

	@Autowired
	private TodoService service;

	@GetMapping("/")
	public ResponseEntity<Object> getAllTodos(Principal principal) throws EntityNotPresentException{

		List<Todo> result = service.listAll(principal.getName());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/todo/{id}")
	public ResponseEntity<Object> getTodoById(@PathVariable Integer id, Principal principal)
			throws EntityNotPresentException, ActionNotAllowedException {
		Todo result = service.get(id, principal.getName());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/todo")
	public ResponseEntity<Object> addTodo(@RequestBody TodoJson todo, Principal principal) throws EntityNotPresentException {
			Todo result = service.save(todo, principal.getName());
			return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/todo/{id}")
	public ResponseEntity<Object> updateTodo(@RequestBody TodoJson todo, @PathVariable Integer id, Principal principal) 
			throws IllegalArgumentException, EntityNotPresentException, ActionNotAllowedException {
			Todo updateTodo = service.update(todo, id, principal.getName());
			return new ResponseEntity<>(updateTodo, HttpStatus.OK);
	}

	@DeleteMapping("/todo/{id}")
	public ResponseEntity<Object> deleteTodo(@PathVariable Integer id, Principal principal)
			throws IllegalArgumentException, EntityNotPresentException, ActionNotAllowedException {
		service.delete(id, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
			
	}

	@PutMapping("/user/{userId}/todo/{todoId}")
	public ResponseEntity<Object> asigneTodoToUser(@PathVariable Integer userId, @PathVariable Integer todoId) 
			throws EntityNotPresentException{
			 service.asigneTodoToUser(userId, todoId);
			 return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/todos/user/{id}")
	public ResponseEntity<Object> getUserTodos(@PathVariable Integer id) throws EntityNotPresentException {
		List<Todo> result = service.getUserTodos(id);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

}
