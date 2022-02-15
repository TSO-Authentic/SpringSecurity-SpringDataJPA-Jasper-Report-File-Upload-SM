package com.ai.sm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ai.sm.persistant.dao.UserRepository;
import com.ai.sm.persistant.dto.UserDTO;

@Service
public class UserService {

	
	@Autowired
	private UserRepository uRepo;

	@Autowired
	private BCryptPasswordEncoder encode;

	public void deleteById(String id) {
		uRepo.deleteById(id);
	}

	public List<UserDTO> findAll() {
		return uRepo.findAll();
	}

	public List<UserDTO> findByIdOrName(String id, String name) {
		return uRepo.findByIdOrName(id, name);
	}

	public void save (UserDTO uDTO) {
		String encodePw = encode.encode(uDTO.getPassword());
		uDTO.setPassword(encodePw);
		uRepo.save(uDTO);
	}
	
}
