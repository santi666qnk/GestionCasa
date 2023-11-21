package com.smc.casa.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.smc.casa.dto.UserRegistrationDto;
import com.smc.casa.model.User;
import org.springframework.stereotype.Service;

public interface UserService extends UserDetailsService {
	User save(UserRegistrationDto registrationDto);
	List<User> getAll();
	
}