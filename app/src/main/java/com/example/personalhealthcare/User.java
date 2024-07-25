package com.example.personalhealthcare;

import android.net.Uri;

// using to pass the values to the FireDataBase to save
public class User {
	public boolean vaild;
	public boolean inprocess;
	public String specializtion;
	public String Fullname, age, email, Lastname, Phone, gender, user, designation, pincode, address, district, appiontment, registeration_no_string;
	public Uri image;
	public User(boolean vaild, boolean inprocess, String specializtion, String fullname, String age, String email, String lastname, String phone, String gender, String user, String designation, String pincode, String address, String district, String appiontment, String registeration_no_string, Uri image) {
		this.vaild = vaild;
		this.inprocess = inprocess;
		this.specializtion = specializtion;
		Fullname = fullname;
		this.age = age;
		this.email = email;
		Lastname = lastname;
		Phone = phone;
		this.gender = gender;
		this.user = user;
		this.designation = designation;
		this.pincode = pincode;
		this.address = address;
		this.district = district;
		this.appiontment = appiontment;
		this.registeration_no_string = registeration_no_string;
		this.image = image;
	}

	public User(String appointment) {

		this.appiontment = appointment;
	}

	public User() {

	}


	public User(String Fullname, String Lastname, String age, String Phone, String email, String gender, String user, String district) {
		this.Fullname = Fullname;
		this.age = age;
		this.email = email;
		this.Lastname = Lastname;
		this.user = user;
		this.Phone = Phone;
		this.gender = gender;
		this.district = district;
	}

	public User(String Fullname, String Lastname, String age, String Phone, String email, String gender, String user, String designation, String pincode, String address, String district, String specializtion, String registeration_no_string, boolean valid) {
		this.Fullname = Fullname;
		this.age = age;
		this.email = email;
		this.Lastname = Lastname;
		this.user = user;
		this.Phone = Phone;
		this.gender = gender;
		this.designation = designation;
		this.pincode = pincode;
		this.address = address;
		this.district = district;
		this.specializtion = specializtion;
		this.vaild = valid;
		this.registeration_no_string = registeration_no_string;
	}


}
