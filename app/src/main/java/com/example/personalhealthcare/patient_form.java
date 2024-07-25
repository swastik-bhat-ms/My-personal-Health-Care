package com.example.personalhealthcare;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class patient_form extends Fragment implements AdapterView.OnItemSelectedListener {

	public String emaill = "null", Pass = "";
	boolean checkpass = false, checkp = false;
	FirebaseAuth register_fire;
	String gender = "Not Selected", user = "Patient";
	int year, date, month;
	Spinner locat;
	String district = "";
	TextView in;
	private RadioGroup rd_group;
	private RadioButton rd_male, rd_female, rd_others;
	private TextView invalid_phone, loginp, settingtext;
	private EditText phonen, email, password, confirm, first, last, dob, address;
	private ProgressBar progressBar;
	private ImageButton calender_button;
	private Button register;
	private boolean checkpassc = false;

	patient_form() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		FirebaseAuth.getInstance().signOut();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	//@SuppressLint("MissingInflatedId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_form2, container, false);
		calender_button = v.findViewById(R.id.calender_button);
		rd_female = v.findViewById(R.id.rd_female);
		rd_male = v.findViewById(R.id.rd_male);
		rd_others = v.findViewById(R.id.rd_others);
		register = v.findViewById(R.id.register);

		register_fire = FirebaseAuth.getInstance();

		//buttons
		register = v.findViewById(R.id.register);

		//textview
//        invalid_confirm = v.findViewById(R.id.confirmerror);
//        invalid_pass = v.findViewById(R.id.passerror);
		loginp = v.findViewById(R.id.log_inp);
		settingtext = v.findViewById(R.id.textView4);
		invalid_phone = v.findViewById(R.id.invalid);
		in = v.findViewById(R.id.disinvaild);

		progressBar = v.findViewById(R.id.progressBar2);
		//editVie

//        password = v.findViewById(R.id.password);
//        confirm = v.findViewById(R.id.confirm_password);
		first = v.findViewById(R.id.first_Name);
		address = v.findViewById(R.id.Address);
		last = v.findViewById(R.id.last_name);
		dob = v.findViewById(R.id.dob);
		locat = v.findViewById(R.id.locatio);
		dob.setEnabled(false);// textfile disabled
		phonen = v.findViewById(R.id.phonet);

		//Toast.makeText(getActivity(), "in view first"+emaill, Toast.LENGTH_SHORT).show();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.location, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locat.setAdapter(adapter);
		locat.setOnItemSelectedListener(this);


		loginp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity(), Log_in.class));
				getActivity().finish();

			}
		});

		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 18;
		calender_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
				Date date3 = null;
				Date date2 = null;
				month = calendar.get(Calendar.MONTH);
				date = calendar.get(Calendar.DAY_OF_MONTH);
				int min_year = calendar.get(Calendar.YEAR) - 80;
				String datestring = date + "-" + month + "-" + year + " 00:00:00";
				String minimumdate = date + "-" + month + "-" + min_year + " 00:00:00";
				try {
					date2 = sdf.parse(datestring);
					date3 = sdf.parse(minimumdate);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
						dob.setText(String.valueOf(i2) + "/" + String.valueOf(i1 + 1) + "/" + String.valueOf(i));
					}
				}, year, month, date);
				long maxmillis = date2.getTime();
				long minmillis = date3.getTime();
				datePickerDialog.getDatePicker().setMaxDate(maxmillis);
				datePickerDialog.getDatePicker().setMinDate(minmillis);
				datePickerDialog.show();
			}
		});


		phonen.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				if ((phonen.getText().toString().trim()).length() != 10) {
					invalid_phone.setVisibility(View.VISIBLE);
					checkp = false;
				} else {
					invalid_phone.setVisibility(View.INVISIBLE);
					checkp = false;
				}
				if (((phonen.getText().toString().trim()).length() == 10)) {
					invalid_phone.setVisibility(View.INVISIBLE);
					checkp = true;
				}
				if (phonen.getText().toString().isEmpty()) {
					invalid_phone.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// check all this before register email,password,confirm,first,last,dob,phonen
				if (rd_female.isChecked()) {
					gender = "Female";
					settingtext.setText(gender);
				} else if (rd_male.isChecked()) {
					gender = "Male";
				} else if (rd_others.isChecked()) {
					gender = "Others";
				} else {
					gender = "Not Selected";
				}
				if (first.getText().toString().isEmpty() || last.getText().toString().isEmpty() ||
						 dob.getText().toString().isEmpty() || !checkp ||
						 gender.equals("Not Selected") || address.getText().toString().isEmpty()
						 || district.equalsIgnoreCase("select district") || district.isEmpty()) {
					if (address.getText().toString().isEmpty()) {
						address.setError("Address must be filled");
						address.requestFocus();
					}
					if (first.getText().toString().isEmpty()) {
						first.setError("Cannot be Empty");
						first.requestFocus();
					}
					if (last.getText().toString().isEmpty()) {
						last.setError("Cannot be Empty");
						last.requestFocus();
					}
					if (dob.getText().toString().isEmpty()) {
						dob.setError("Cannot be Empty");
						dob.requestFocus();
					}
					if (phonen.getText().toString().isEmpty() || phonen.getText().toString().length() != 10) {
						phonen.setError("Invalid Phone Number");
						phonen.requestFocus();
					}

					if (gender.equals("Not Selected")) {
						settingtext.setError("Not Selected");
						settingtext.requestFocus();
					}
					if (district.equalsIgnoreCase("select district") || district.isEmpty()) {
						locat.requestFocus();
						in.setVisibility(View.VISIBLE);

					}
					Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
				} else {
					progressBar.setVisibility(View.VISIBLE);
					createUser();
				}

			}
		});

		SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		emaill = sh.getString("email", "");
		return v;
	}

	public void createUser() {
//        Bundle bundle = this.getArguments();
//        if (bundle!=null) {
//            emaill = bundle.getString("email");
//            Toast.makeText(getActivity(), ""+emaill, Toast.LENGTH_SHORT).show();
//            Pass = bundle.getString("password");
//            Toast.makeText(getActivity(), "inside", Toast.LENGTH_SHORT).show();
//        }
		String fname = first.getText().toString();
		String lname = last.getText().toString();
		String date = dob.getText().toString();
		String Phone = phonen.getText().toString();
		String dis = district;
		FirebaseAuth log = null;
		final String gendlast = gender;
		final String users = user;
		User user = new User(fname, lname, date, Phone, emaill, gendlast, users, dis);
		FirebaseDatabase.getInstance().getReference("User").child(register_fire.getCurrentUser().getUid()).setValue(user)
				 .addOnCompleteListener(new OnCompleteListener<Void>() {
					 @Override
					 public void onComplete(@NonNull Task<Void> task) {
						 if (task.isSuccessful()) {
							 Toast.makeText(getActivity(), "Account is been Created.\n Log in to access your account", Toast.LENGTH_SHORT).show();
							 progressBar.setVisibility(View.GONE);
							 FirebaseAuth.getInstance().signOut();
							 startActivity(new Intent(getActivity(), Log_in.class));
							 getActivity().finish();

						 } else {
							 Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
							 progressBar.setVisibility(View.GONE);
						 }
					 }
				 });
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		district = locat.getSelectedItem().toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}