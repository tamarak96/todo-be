package com.example.todo.exception;

public class ActionNotAllowedException extends Exception{
	public ActionNotAllowedException(String message) {
		super(message);
	}
}
