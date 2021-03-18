package com.example.todo.rest;

import java.util.List;
import java.util.Optional;

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

import com.example.todo.configuration.MyUserDetails;
import com.example.todo.dto.Todo;
import com.example.todo.json.TodoJSON;
import com.example.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class TodoController {

	@Autowired
	private TodoService service;
	
	@GetMapping("/todos")
	public ResponseEntity<?> getAllTodos(){
		
		MyUserDetails details = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Optional<List<Todo>> result = service.listAll(details.getUsername());
		return result.isPresent() ? 
				new ResponseEntity<>(result,HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/todos/{id}")
	public ResponseEntity<?> getTodoById(@PathVariable Integer id){
		Optional<Todo> result = service.get(id);
		return result.isPresent() ? 
				new ResponseEntity<>(result,HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
		
	@PostMapping("/todo")
	public ResponseEntity<?> addTodo(@RequestBody TodoJSON todo) {
		try {
			Todo saveTodo = service.save(todo);
			return new ResponseEntity<Todo>(saveTodo, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/todos/{id}")
	public ResponseEntity<?> updateTodo(@RequestBody TodoJSON todo, @PathVariable Integer id) throws Exception{
		try {
			Todo updateTodo = service.update(todo, id);
			return new ResponseEntity<Todo>(updateTodo, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/todos/{id}")
	public ResponseEntity<?>  delete(@PathVariable Integer id) {
		try {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/user/{user_id}/todo/{todo_id}")
	public ResponseEntity<?> asigneTodoToUser(@PathVariable Integer user_id, @PathVariable Integer todo_id) throws Exception{
		try {
			service.asigneTodoToUser(user_id, todo_id);
			return new ResponseEntity<Todo>(HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/todos/user/{id}")
	public ResponseEntity<?> getUserTodos(@PathVariable Integer id) throws Exception{
		Optional<List<Todo>> result = service.getUserTodos(id);
		return result.isPresent() ? 
				new ResponseEntity<>(result,HttpStatus.OK) :
				new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
}
