package com.example.todo.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.todo.dao.UserRepository;
import com.example.todo.dto.User;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public MyUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(userName);
		
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: "+ userName));
		
		MyUserDetails myUserDetails = new MyUserDetails(user.get());
		
		return myUserDetails;
	}

}
