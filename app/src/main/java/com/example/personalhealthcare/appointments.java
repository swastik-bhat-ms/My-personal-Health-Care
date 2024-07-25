package com.example.personalhealthcare;

public class appointments {

	String iamgeuri;
	String registration_no;
	String uid;
	String name, age, date, problem, array, relation;
	private boolean relative;

	public appointments() {
	}

	public appointments(String name, String age, String date, String problem, String uid) {
		this.name = name;
		this.age = age;
		this.date = date;
		this.problem = problem;
		this.uid = uid;

	}

	public appointments(String name, String relation, String age, String date, String problem, boolean relative, String uid) {
		this.name = name;
		this.age = age;
		this.date = date;
		this.relation = relation;
		this.problem = problem;
		this.relative = relative;
		this.uid = uid;
	}

	public appointments(String array) {
		this.array = array;
	}

	public appointments(String fullname, String detail, String s) {
		this.name = fullname;
		this.date = detail;
		this.problem = s;
	}


	public appointments(String fullname, String registration_no, String imageuri, boolean t) {
		this.name = fullname;
		this.registration_no = registration_no;
		this.iamgeuri = imageuri;
	}


	public String getArray() {
		return array;
	}

	public void setArray(String array) {
		this.array = array;
	}
}
