package com.ai.sm.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class UserBean {
	@NotEmpty(message = "User Id can't be blank")
	private String id;
	@NotEmpty(message = "User name can't be blank")
	private String name;
	@NotEmpty(message = "Password can't be blank")
	private String password;
	@NotEmpty(message = "Confirm password can't be blank")
	private String confirm;
	
	private MultipartFile img;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public MultipartFile getImg() {
		return img;
	}
	public void setImg(MultipartFile img) {
		this.img = img;
	}
	
	
}
