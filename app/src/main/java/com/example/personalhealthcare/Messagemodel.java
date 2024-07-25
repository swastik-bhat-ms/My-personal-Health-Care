package com.example.personalhealthcare;

public class Messagemodel {
	private String message;
	private int sender;

	public Messagemodel(String message, int sender) {
		this.message = message;
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}


}




