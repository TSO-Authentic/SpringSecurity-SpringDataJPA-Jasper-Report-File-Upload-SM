package com.ai.sm.persistant.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.sm.persistant.dto.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, String> {

	List<UserDTO> findByIdOrName(String id, String name);
	
}
