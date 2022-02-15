package com.ai.sm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.sm.persistant.dao.StudentRepository;
import com.ai.sm.persistant.dto.StudentDTO;

@Service
public class StudentService {

	@Autowired
	private StudentRepository sRepo;

	public List<StudentDTO> findByStudentIdOrStudentNameOrClassName(String studentId, String studentName,
			String className) {
		return sRepo.findByStudentIdOrStudentNameOrClassName(studentId, studentName, className);
	}

	public List<StudentDTO> findAll() {
		return sRepo.findAll();
	}

	public void deleteById(String id) {
		sRepo.deleteById(id);
	}

	public void save(StudentDTO dto) {
		sRepo.save(dto);
	}
}
