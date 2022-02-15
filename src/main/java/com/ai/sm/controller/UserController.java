package com.ai.sm.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.sm.model.SearchBean;
import com.ai.sm.model.UserBean;
import com.ai.sm.persistant.dto.UserDTO;
import com.ai.sm.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService uService;

	@ModelAttribute("currentUserName")
	public void getCurrentUsername(HttpServletRequest session, HttpSession httpSes) {

		String currentUserId = session.getRemoteUser();
		List<UserDTO> currentUser = new ArrayList<>();
		currentUser = uService.findByIdOrName(currentUserId, "");
		String currentUserName = currentUser.get(0).getName();
		String currentUserPhoto = currentUser.get(0).getImg();
		httpSes.setAttribute("userId", currentUserId);
		httpSes.setAttribute("userName", currentUserName);
		httpSes.setAttribute("userImg", currentUserPhoto);
	}

	@GetMapping(value = "/welcome")
	public String mainPage() {
		return "M00001";
	}

	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/setupUser")
	public ModelAndView setupUser(@ModelAttribute("Error") String Error, @ModelAttribute("Success") String Success,
			ModelMap model) {
		model.addAttribute("Error", Error);
		model.addAttribute("Success", Success);

		return new ModelAndView("USR001", "sBean", new SearchBean());
	}

	@GetMapping(value = "/searchUser")
	public String searchUser(@ModelAttribute("sBean") SearchBean searchBean, ModelMap model) {

		List<UserDTO> userList = new ArrayList<>();
		UserDTO uDTO = new UserDTO();

		uDTO.setId(searchBean.getUserId());
		uDTO.setName(searchBean.getUserName());

		if (!uDTO.getId().equals("") || !uDTO.getName().equals("")) {
			userList = uService.findByIdOrName(uDTO.getId(), uDTO.getName());
		} else {
			userList = uService.findAll();
		}

		if (userList.size() == 0) {
			model.addAttribute("Error", "No User Found !!!");
		} else {
			model.addAttribute("userList", userList);
			model.addAttribute("Success", "Search done Successfully");
		}
		return "USR001";
	}

	@GetMapping(value = "/setupAddUser")
	public ModelAndView addUser() {

		return new ModelAndView("USR002", "uBean", new UserBean());
	}

	@PostMapping(value = "/addUser")
	public String addUser(@ModelAttribute("uBean") @Validated UserBean userBean, BindingResult br, ModelMap model)
			throws IOException {
		if (br.hasErrors()) {
			return "USR002";
		}
		MultipartFile img = userBean.getImg();
		UserDTO uDTO = new UserDTO();

		if (userBean.getPassword().equals(userBean.getConfirm())) {

			uDTO.setId(userBean.getId());

			List<UserDTO> checkUserList = uService.findByIdOrName(uDTO.getId(), uDTO.getName());

			if (checkUserList.size() != 0) {
				model.addAttribute("Error", "User ID has been already used..... Choose another user ID");
			} else {
				uDTO.setName(userBean.getName());

				uDTO.setPassword(userBean.getPassword());

				uDTO.setRole("ROLE_USER");
				uDTO.setEnable(1);

				String fileName = img.getOriginalFilename();
				if (fileName.isEmpty()) {
					model.addAttribute("fileError", "Please Choose an Image");
					return "USR002";
				}
				String dir = "./images/" + uDTO.getId() + "/";
				Path path = Paths.get(dir);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}
				Path filePath = path.resolve(fileName);
				InputStream inputStream = img.getInputStream();
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

				uDTO.setImg("/images/" + uDTO.getId() + "/" + fileName);

				uService.save(uDTO);

				List<UserDTO> list = uService.findByIdOrName(uDTO.getId(), uDTO.getName());
				int i = list.size();

				if (i > 0) {
					model.addAttribute("Success", "User registered Successfully");
				} else {
					model.addAttribute("Error", "User register fail !!!");
				}
			}
		} else {
			model.addAttribute("Error", "Passwords didn't match !!!");
		}
		return "USR002";
	}

	@GetMapping(value = "setupUpdateUser")
	public ModelAndView setupUpdateUser(@RequestParam String id) {
		UserDTO uDTO = new UserDTO();
		uDTO.setId(id);

		List<UserDTO> list = uService.findByIdOrName(uDTO.getId(), uDTO.getName());
		UserBean userBean = new UserBean();
		for (UserDTO upDTO : list) {
			userBean.setId(upDTO.getId());
			userBean.setName(upDTO.getName());
			userBean.setPassword(upDTO.getPassword());
			userBean.setConfirm(upDTO.getPassword());
		}
		return new ModelAndView("USR002-01", "uBean", userBean);
	}

	@PostMapping(value = "updateUser")
	public String updateUser(@ModelAttribute("uBean") @Validated UserBean userBean, BindingResult br, ModelMap model)
			throws IOException {
		if (br.hasErrors()) {
			return "USR002-01";
		}

		MultipartFile img = userBean.getImg();
		UserDTO uDTO = new UserDTO();
		if (userBean.getPassword().equals(userBean.getConfirm())) {
			uDTO.setId(userBean.getId());
			uDTO.setName(userBean.getName());
			uDTO.setPassword(userBean.getPassword());

			String fileName = img.getOriginalFilename();

			if (!fileName.isEmpty()) {
				String dir = "./images/" + uDTO.getId() + "/";
				Path delPath = Paths.get("." + uDTO.getImg());
				Path newPath = Paths.get(dir);
				Path filePath = newPath.resolve(fileName);

				Files.deleteIfExists(delPath);
				InputStream inputStream = img.getInputStream();
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				uDTO.setImg("/images/" + uDTO.getId() + "/" + fileName);
			} else {
				uDTO.setImg(uService.findByIdOrName(uDTO.getId(), "").get(0).getImg());
			}
			uDTO.setRole("ROLE_USER");
			uDTO.setEnable(1);
			
			uService.save(uDTO);
			List<UserDTO> list = uService.findByIdOrName(uDTO.getId(), uDTO.getName());
			int i = list.size();

			if (i > 0) {
				model.addAttribute("Success", "User Updated Successfully");
			}
		} else {
			model.addAttribute("Error", "Password didn't match !!!");
		}
		return "USR002-01";

	}

	@GetMapping(value = "/deleteUser")
	public String deleteUser(@RequestParam String id, RedirectAttributes redir, ModelMap model,
			HttpServletRequest session) {
		UserDTO uDTO = new UserDTO();
		uDTO.setId(id);

		if (uDTO.getId().equals(session.getRemoteUser())) {
			redir.addAttribute("Error", "Cann't delete this current login user !!!");
		} else {

			uService.deleteById(id);
			List<UserDTO> list = uService.findByIdOrName(id, "");
			int i = list.size();
			if (i == 0) {
				redir.addAttribute("Success", "Deleted " + id + " Successfully");
			}

		}
		return "redirect:/user/setupUser";
	}
	
	@GetMapping("/userReport")
	public ResponseEntity<byte[]> studentReport() throws Exception, JRException {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(uService.findAll());
		JasperReport compileReport = JasperCompileManager
				.compileReport(new FileInputStream("src/main/resources/reports/user/UReport.jrxml"));

		Map<String, Object> map = new HashMap<>();
		JasperPrint report = JasperFillManager.fillReport(compileReport, map, dataSource);
		byte[] dataPDF = JasperExportManager.exportReportToPdf(report);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=UsersReport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(dataPDF);
	}
}
