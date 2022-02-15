package com.ai.sm.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ai.sm.model.ClassBean;
import com.ai.sm.persistant.dto.ClassDTO;
import com.ai.sm.service.ClassService;

@Controller
@RequestMapping("/user")
public class ClassController {

	@Autowired
	private ClassService cService;

	
	@GetMapping(value = "/setupClass")
	public ModelAndView setupClass() {
		return new ModelAndView("BUD003", "cBean", new ClassBean());
	}

	@PostMapping(value = "/addClass")
	public String addClass(@ModelAttribute("cBean") @Validated ClassBean classBean, BindingResult br, ModelMap model) {
		if (br.hasErrors()) {
			return "BUD003";
		}

		ClassDTO cDTO = new ClassDTO();
		cDTO.setClassId(classBean.getId());
		cDTO.setClassName(classBean.getName());

		List<ClassDTO> checkClassList = cService.findByClassIdOrClassName(cDTO.getClassId(), cDTO.getClassName());

		if (checkClassList.size() != 0) {
			model.addAttribute("Error", "Class ID has been already used.... Choose another class ID");
		} else {

			cService.save(cDTO);
			List<ClassDTO> list = cService.findByClassIdOrClassName(cDTO.getClassId(), cDTO.getClassName());
			int i = list.size();

			if (i > 0) {
				model.addAttribute("Success", "Class registered Successfully");
			} else {
				model.addAttribute("Error", "Class registered fail !!!");
			}
		}
		return "BUD003";
	}
}
