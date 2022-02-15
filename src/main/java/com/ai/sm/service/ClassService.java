package com.ai.sm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.sm.persistant.dao.ClassRepository;
import com.ai.sm.persistant.dto.ClassDTO;

@Service
public class ClassService {

	@Autowired
	private ClassRepository cRepo;

	public List<ClassDTO> findAll() {
		return cRepo.findAll();
	}
	
	public List<ClassDTO> findByClassIdOrClassName(String classId, String className) {
		return cRepo.findByClassIdOrClassName(classId, className);
	}

	public void save(ClassDTO dto) {
		cRepo.save(dto);
	}

}
