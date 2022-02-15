package com.ai.sm.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.sm.model.SearchBean;
import com.ai.sm.model.StudentBean;
import com.ai.sm.persistant.dto.ClassDTO;
import com.ai.sm.persistant.dto.StudentDTO;
import com.ai.sm.service.ClassService;
import com.ai.sm.service.StudentService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/user")

public class StudentController {

	@Autowired
	private StudentService sService;

	@Autowired
	private ClassService cService;


	@GetMapping(value = "/setupStudentSearch")
	public ModelAndView setupStudentSearch(@ModelAttribute("Success") String Success, ModelMap model) {
		model.addAttribute("Success", Success);

		return new ModelAndView("BUD001", "sStuBean", new SearchBean());
	}

	@GetMapping(value = "/searchStudent")
	public String searchStudent(@ModelAttribute("sStuBean") SearchBean searchBean, ModelMap model) {

		List<StudentDTO> studentList = new ArrayList<>();
		StudentDTO sDTO = new StudentDTO();

		sDTO.setStudentId(searchBean.getStuId());
		sDTO.setStudentName(searchBean.getStuName());
		sDTO.setClassName(searchBean.getClassName());

		if (!sDTO.getStudentId().equals("") || !sDTO.getStudentName().equals("") || !sDTO.getClassName().equals("")) {
			studentList = sService.findByStudentIdOrStudentNameOrClassName(sDTO.getStudentId(), sDTO.getStudentName(),
					sDTO.getClassName());
		} else {
			studentList = sService.findAll();
		}

		if (studentList.size() == 0) {
			model.addAttribute("Error", "No Student Found !!!");
		} else {
			model.addAttribute("studentList", studentList);
			model.addAttribute("Success", "Search done Successfully");
		}
		return "BUD001";
	}

	@GetMapping(value = "/setupStudentAdd")
	public ModelAndView setupAddStudent() {

		return new ModelAndView("BUD002", "stuBean", new StudentBean());
	}

	@PostMapping(value = "/addStudent")
	public String addStudent(@ModelAttribute("stuBean") @Validated StudentBean studentBean, BindingResult br,
			ModelMap model) {

		boolean condition1 = br.hasErrors();
		boolean condition3 = (studentBean.getYear().equals("Year") || studentBean.getMonth().equals("Month")
				|| studentBean.getDay().equals("Day"));
		boolean condition2 = (studentBean.getClassName().equals("ClassName"));
		if (condition1) {
			return "BUD002";
		} else if (condition2 ) {
			model.addAttribute("Error","Choose one class !");
			
			return "BUD002";

		}else if(condition3) {
			model.addAttribute("Error", "RegisterDate can't be blank!");
			return "BUD002";
		}
		else {

			StudentDTO sDTO = new StudentDTO();
			sDTO.setStudentId(studentBean.getId());

			List<StudentDTO> checkStudentList = sService.findByStudentIdOrStudentNameOrClassName(sDTO.getStudentId(),
					sDTO.getStudentName(), sDTO.getClassName());

			if (checkStudentList.size() != 0) {
				model.addAttribute("Error", "Student ID has been already exist ... Choose another Student ID");
			} else {
				sDTO.setStudentName(studentBean.getName());
				sDTO.setClassName(studentBean.getClassName());
				sDTO.setRegisterDate(studentBean.getYear() + "-" + studentBean.getMonth() + "-" + studentBean.getDay());
				sDTO.setStatus(studentBean.getStatus());
				sService.save(sDTO);

				List<StudentDTO> list = sService.findByStudentIdOrStudentNameOrClassName(sDTO.getStudentId(),
						sDTO.getStudentName(), sDTO.getClassName());
				int i = list.size();

				if (i > 0) {
					model.addAttribute("Remain", studentBean);
					model.addAttribute("Success", "Student registered Successfully");
				}
			}
		}
		return "BUD002";
	}

	@GetMapping(value = "/setupUpdateStudent")
	public ModelAndView setupUpdateStudent(@RequestParam("studentId") String studentId, ModelMap model) {
		StudentDTO sDTO = new StudentDTO();
		sDTO.setStudentId(studentId);

		List<StudentDTO> list = sService.findByStudentIdOrStudentNameOrClassName(sDTO.getStudentId(),
				sDTO.getStudentName(), sDTO.getClassName());
		StudentBean studentBean = new StudentBean();
		for (StudentDTO res : list) {
			studentBean.setId(res.getStudentId());
			studentBean.setName(res.getStudentName());
			studentBean.setClassName(res.getClassName());
			studentBean.setStatus(res.getStatus());
			String[] date = res.getRegisterDate().toString().split("-");
			studentBean.setYear(date[0]);
			studentBean.setMonth(date[1]);
			studentBean.setDay(date[2]);

		}
		return new ModelAndView("BUD002-01", "stuBean", studentBean);
	}

	@PostMapping(value = "/studentUpdate")
	public String updateStudent(@ModelAttribute("stuBean") @Validated StudentBean studentBean, BindingResult br,
			ModelMap model) {

		boolean condition1 = br.hasErrors();
		boolean condition2 = (studentBean.getYear().equals("Year") || studentBean.getMonth().equals("Month")
				|| studentBean.getDay().equals("Day"));
		if (condition1) {
			return "BUD002-01";
		} else if (condition2) {

			model.addAttribute("ErrorDate", "RegisterDate can't be blank!");
			return "BUD002-01";

		} else {

			StudentDTO sDTO = new StudentDTO();
			sDTO.setStudentId(studentBean.getId());
			sDTO.setStudentName(studentBean.getName());
			sDTO.setClassName(studentBean.getClassName());
			sDTO.setRegisterDate(studentBean.getYear() + "-" + studentBean.getMonth() + "-" + studentBean.getDay());
			sDTO.setStatus(studentBean.getStatus());

			sService.save(sDTO);
			List<StudentDTO> list = sService.findByStudentIdOrStudentNameOrClassName(sDTO.getStudentId(),
					sDTO.getStudentName(), sDTO.getClassName());
			int i = list.size();

			if (i > 0) {
				model.addAttribute("Success", "Student successfully updated");
			}
		}
		return "BUD002-01";
	}

	@GetMapping(value = "/deleteStudent")
	public String deleteStudent(@RequestParam String id, ModelMap model, RedirectAttributes redir) {
		StudentDTO sDTO = new StudentDTO();
		sDTO.setStudentId(id);

		sService.deleteById(id);
		List<StudentDTO> list = sService.findByStudentIdOrStudentNameOrClassName(id,
				sDTO.getStudentName(), sDTO.getClassName());
		int i = list.size();

		if (i ==  0) {
			redir.addAttribute("Success", "Deleted " + id + " Successfully");
		}

		return "redirect:/user/setupStudentSearch";
	}

	@ModelAttribute("cList")
	public List<String> classList() {

		List<ClassDTO> classList = cService.findAll();
		List<String> clList = new ArrayList<>();

		for (int i = 0; i < classList.size(); i++) {
			clList.add(classList.get(i).getClassName());
		}

		return clList;
	}

	@GetMapping("/studentReport")
	public ResponseEntity<byte[]> studentReport() throws Exception, JRException {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sService.findAll());
		JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/reports/student/StudentReport.jrxml"));
		
		Map<String, Object> map = new HashMap<>();
		JasperPrint report = JasperFillManager.fillReport(compileReport, map, dataSource);
		byte[] data = JasperExportManager.exportReportToPdf(report);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=StudentsReport.pdf");
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}
}
