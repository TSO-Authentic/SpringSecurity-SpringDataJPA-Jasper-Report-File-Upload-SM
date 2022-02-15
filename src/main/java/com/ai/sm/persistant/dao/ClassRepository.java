package com.ai.sm.persistant.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.sm.persistant.dto.ClassDTO;

@Repository
public interface ClassRepository extends JpaRepository<ClassDTO, String> {

	List <ClassDTO> findByClassIdOrClassName(String classId , String className);
	
}
