package com.ai.sm.model;

import javax.validation.constraints.NotEmpty;

public class ClassBean {

	@NotEmpty(message = "Class Id must not be empty")
	private String id;
	@NotEmpty(message = "Class Name must not be empty")
	private String name;

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

}
