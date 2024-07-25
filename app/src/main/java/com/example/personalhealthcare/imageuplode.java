package com.example.personalhealthcare;

public class imageuplode {
	private String name;
	private String imageuri;

	public imageuplode() {
	}

	public imageuplode(String name, String imageuri) {
		if (name.trim().equals("")) {
			name = "No name";
		}
		this.name = name;
		this.imageuri = imageuri;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageuri() {
		return imageuri;
	}

	public void setImageuri(String imageuri) {
		this.imageuri = imageuri;
	}
}
