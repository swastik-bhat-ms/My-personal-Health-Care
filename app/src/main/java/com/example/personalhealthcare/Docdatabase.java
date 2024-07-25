package com.example.personalhealthcare;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Docdatabase")
public class Docdatabase {
	@PrimaryKey(autoGenerate = true)
	private int id;
	@ColumnInfo(name = "Fullname")
	private String Fullname;
	@ColumnInfo(name = "registeration_no_string")
	private String registeration_no_string;
	@ColumnInfo(name = "Lastname")
	private String Lastname;
	@ColumnInfo(name = "Phone")
	private String Phone;
	@ColumnInfo(name = "address")
	private String address;
	@ColumnInfo(name = "age")
	private String age;
	@ColumnInfo(name = "designation")
	private String designation;
	@ColumnInfo(name = "district")
	private String district;
	@ColumnInfo(name = "email")
	private String email;
	@ColumnInfo(name = "gender")
	private String gender;
	@ColumnInfo(name = "pincode")
	private String pincode;
	@ColumnInfo(name = "specializtion")
	private String specializtion;
	@ColumnInfo(name = "user")
	private String user;
	@ColumnInfo(name = "asnwer")
	private String asnwer;
	@ColumnInfo(name = "inprocess")
	private boolean inprocess;


	public Docdatabase() {
	}

	@Ignore
	public Docdatabase(int id, String fullname, String lastname, String phone, String address, String age, String designation, String district, String email, String gender, String pincode, String specializtion, String user, String asnwer, String registeration_no_string) {
		this.id = id;
		Fullname = fullname;
		this.registeration_no_string = registeration_no_string;
		Lastname = lastname;
		Phone = phone;
		this.address = address;
		this.age = age;
		this.designation = designation;
		this.district = district;
		this.email = email;
		this.gender = gender;
		this.pincode = pincode;
		this.specializtion = specializtion;
		this.user = user;
		this.asnwer = asnwer;
	}

	@Ignore
	public Docdatabase(int id, String fullname, String lastname, String Phone, String address, String age, String designation, String district, String email, String gender, String pincode, String specializtion, String user) {
		this.id = id;
		Fullname = fullname;
		Lastname = lastname;
		this.Phone = Phone;
		this.address = address;
		this.age = age;
		this.designation = designation;
		this.district = district;
		this.email = email;
		this.gender = gender;
		this.pincode = pincode;
		this.specializtion = specializtion;
		this.user = user;
	}

	@Ignore
	public Docdatabase(String fullname, String registeration_no_string, String lastname, String phone, String address, String age, String designation, String district, String email, String gender, String pincode, String specializtion, String user, String asnwer, boolean inprocess) {
		Fullname = fullname;
		this.registeration_no_string = registeration_no_string;
		Lastname = lastname;
		Phone = phone;
		this.address = address;
		this.age = age;
		this.designation = designation;
		this.district = district;
		this.email = email;
		this.gender = gender;
		this.pincode = pincode;
		this.specializtion = specializtion;
		this.user = user;
		this.asnwer = asnwer;
		this.inprocess = inprocess;
	}

	@Ignore
	public Docdatabase(String fullname, String lastname, String Phone, String address, String age, String designation, String district, String email, String gender, String pincode, String specializtion, String user) {
		Fullname = fullname;
		Lastname = lastname;
		this.Phone = Phone;
		this.address = address;
		this.age = age;
		this.designation = designation;
		this.district = district;
		this.email = email;
		this.gender = gender;
		this.pincode = pincode;
		this.specializtion = specializtion;
		this.user = user;
	}

	@Ignore
	public Docdatabase(String asnwer) {
		this.asnwer = asnwer;
	}

	public String getAsnwer() {
		return asnwer;
	}

	public void setAsnwer(String asnwer) {
		this.asnwer = asnwer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullname() {
		return Fullname;
	}

	public void setFullname(String fullname) {
		Fullname = fullname;
	}

	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		Lastname = lastname;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		this.Phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSpecializtion() {
		return specializtion;
	}

	public void setSpecializtion(String specializtion) {
		this.specializtion = specializtion;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRegisteration_no_string() {
		return registeration_no_string;
	}

	public void setRegisteration_no_string(String registeration_no_string) {
		this.registeration_no_string = registeration_no_string;
	}

	public boolean getInprocess() {
		return inprocess;
	}

	public void setInprocess(boolean inprocess) {
		this.inprocess = inprocess;
	}
}