package com.example.personalhealthcare;

import java.util.Dictionary;
import java.util.Hashtable;

public class reply {
	Dictionary<String, String> dict = new Hashtable<>();

	reply() {
		dict.put("head", "Some of the Best practices include Aspirin,\n Resting in a darkened room may also help\n\n" +
				 "Request an appointment if you are suffering from below symptoms\n" +
				 "Feeling worse than usual\n" +
				 "Get a sudden, severe headaches\n" +
				 "Become confused, slur your speech or faint\n" +
				 "Having same problems from days");
		dict.put("stomach", "I’m sorry to hear that you have stomach pain.\n" +
				 "Some home remedies that may help with mild stomach pain are \nDrinking plenty of fluids," +
				 " eating bland foods, using a hot pack \nAnd taking over-the-counter medications for heartburn or gas.\n " +
				 "However, if your pain is persistent, severe, or accompanied by\n other symptoms such as fever, vomiting," +
				 " diarrhea, blood in stool or vomit, swelling of the abdomen \nor loss of appetite,\n\nYou should see a " +
				 "doctor as soon as possible");
		dict.put("eyes", "I’m sorry to hear that you’re experiencing eye pain. \n" +
				 "There are several home remedies that you can try to relieve eye pain. Some of these remedies include:\n" +
				 "Warm compress: A warm compress can help relieve pain and discomfort, especially pain associated with eye infections, such as conjunctivitis, blepharitis, or a sty.\n" +
				 "A clean, warm, moist towel may be placed over the eyes1.\n" +
				 "Cotton balls dipped in rose water: To reduce inflammatory pain in eyes, use cotton balls dipped in rose water.\n" +
				 "You can place them over your eyes for a few minutes to experience soothing relief2.\n" +
				 "Cucumber slices: Cucumber slices have anti-inflammatory properties that can help reduce swelling and inflammation around the eyes.\n" +
				 "Place a slice of cucumber over each eye for 10-15 minutes3.\n" +
				 "If your symptoms persist or worsen,\n\n" +
				 "Please Request an appointment from an ophthalmologist or primary care physician.\n" +
				 "I hope this helps!");
		dict.put("eye", "I’m sorry to hear that you’re experiencing eye pain. \n" +
				 "There are several home remedies that you can try to relieve eye pain. Some of these remedies include:\n" +
				 "Warm compress: A warm compress can help relieve pain and discomfort, especially pain associated with eye infections, such as conjunctivitis, blepharitis, or a sty.\n" +
				 "A clean, warm, moist towel may be placed over the eyes1.\n" +
				 "Cotton balls dipped in rose water: To reduce inflammatory pain in eyes, use cotton balls dipped in rose water.\n" +
				 "You can place them over your eyes for a few minutes to experience soothing relief.\n" +
				 "Cucumber slices: Cucumber slices have anti-inflammatory properties that can help reduce swelling and inflammation around the eyes.\n" +
				 "Place a slice of cucumber over each eye for 10-15 minutes3.\n" +
				 "If your symptoms persist or worsen, \n" +
				 "Please Request an appointment from an ophthalmologist or primary care physician.\n" +
				 "I hope this helps!\n");
	}

	public String replymessage(String input, boolean ans) {
		input = input.toLowerCase();
		input = input.trim();
		if (ans) {
			return process(input);
		}

		switch (input) {
			case "set appointment":
				ans = false;
				return "Confirm! Do you want to set appointment(Yes/No)";
			case "chat":
				ans = true;

				return "Please tell us the problem you are suffering from\nExample: Head,Stomach,Eyes";
			default:
				return "Wrong input. Try again part one";
		}
	}

	public String process(String input) {
		if (input.equalsIgnoreCase("set Appointment")) {
			return "Confirm! Do you want to set appointment(Yes/No)";
		} else {
			input = input.toLowerCase();
			if (dict.get(input) != null) {
				return dict.get(input);
			} else {
				return "Please input the part of the\nbody in which the pain is.\nExample Head,Leg,stomach";
			}
		}
	}

	public void setappointment(String bodypart) {

		if (bodypart.equalsIgnoreCase("yes")) {

		}
	}

	public String setdoc() {
		return "asd";
	}
}
