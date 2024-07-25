package com.example.personalhealthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	//    private RadioButton rd_male,rd_female,rd_others;
//    boolean checkpass = false, checkp = false;
//    boolean d_checkpass = false, d_checkp = false;
//    private TextView invalid_phone, invalid_pass, invalid_confirm, loginp, settingtext;
//    private EditText phonen, email, password, confirm, first, last, dob, address;
//    private ProgressBar progressBar;
//
//
//
//
//    private Button d_register;
//    private RadioGroup d_rd_group;
//    private RadioButton d_rd_male,d_rd_female,d_rd_others;
//    private TextView d_invalid_phone, d_invalid_pass, d_invalid_confirm, d_loginp, d_settingtext;
//    private EditText d_phonen, d_email, d_password, d_confirm, d_first, d_last, d_dob, d_address;
//    private ProgressBar d_progressBar;
//
	Spinner spinner;
//      Spinner d_spinner;
//    String gender="Not Selected";
//
//    // private DatePicker;
//    FirebaseAuth register_fire;

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		String text = adapterView.getItemAtPosition(i).toString();
		if (text.equals("Patient")) {
			Fragment fraggg = new patient_form();
			getSupportFragmentManager().beginTransaction().replace(R.id.sign_up_form, fraggg).addToBackStack(null).commit();

		}
		if (text.equals("Doctor")) {

			Fragment fragg = new doctor_form();
			getSupportFragmentManager().beginTransaction().replace(R.id.sign_up_form, fragg).addToBackStack(null).commit();
		}


	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		FirebaseAuth.getInstance().signOut();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		getSupportActionBar().hide();


		emailverification emailver = new emailverification();
		getSupportFragmentManager().beginTransaction().replace(R.id.sign_up_form, emailver).addToBackStack(null).commit();
		// rd_group= findViewById(R.id.radioGroup);


		// Patients R.id

		// Radio Buttons
//        rd_female=findViewById(R.id.rd_female);
//        rd_male= findViewById(R.id.rd_male);
//        rd_others = findViewById(R.id.rd_others);
//        //Radio Buttons for Doctor Page
//        rd_female=findViewById(R.id.rd_female);
//        rd_male= findViewById(R.id.rd_male);
//        rd_others = findViewById(R.id.rd_others);
//
//
//        //buttons
//       // register = findViewById(R.id.register);
//
//        //textview
//        invalid_confirm = findViewById(R.id.confirmerror);
//        invalid_pass = findViewById(R.id.passerror);
//        loginp = findViewById(R.id.log_inp);
//        settingtext = findViewById(R.id.textView4);
//        invalid_phone = findViewById(R.id.invalid);
//
//        progressBar = findViewById(R.id.progressBar2);
//        //editVie
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        confirm = findViewById(R.id.confirm_password);
//        first = findViewById(R.id.first_Name);
//        address= findViewById(R.id.Address);
//        last = findViewById(R.id.last_name);
//        dob = findViewById(R.id.dob);
//        phonen = findViewById(R.id.phonet);
//
//
//
//
		spinner = findViewById(R.id.spinner_user);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(signup.this, Log_in.class));
		finish();

	}


}