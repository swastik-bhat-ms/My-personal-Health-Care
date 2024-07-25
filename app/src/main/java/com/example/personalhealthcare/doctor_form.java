package com.example.personalhealthcare;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class doctor_form extends Fragment implements AdapterView.OnItemSelectedListener {
	private static final int PICK_IMAGE = 1;
	Activity content = getActivity();
	Uri imageuri = null;
	boolean checkp = false;
	Spinner spinner, location;
	FirebaseAuth register_fire;
	int year, month, date;
	String desig = "", district = "";
	String gender = "Not Selected";
	String user = "Doctor";
	private RadioButton rd_male, rd_female, rd_others;
	private Button register;
	private StorageReference storageReference;
	private ImageButton calender_button;
	private TextView invalid_phone, loginp, settingtext;
	private EditText phonen, first, last, dob, address, pincode;
	private ProgressBar progressBar;
	private String email_id;
	private EditText specializtion;
	private Button upload;
	private ImageView profiledp;
	private EditText registration_no;
	private AlertDialog.Builder builder;

	public void onDestroy() {
		super.onDestroy();
		FirebaseAuth.getInstance().signOut();
	}

	public void onItemSelected(AdapterView<?> adapterView, View views, int i, long l) {
		desig = spinner.getSelectedItem().toString();
		district = location.getSelectedItem().toString();


	}

	public void onNothingSelected(AdapterView<?> adapterView) {
		spinner.requestFocus();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup sign_up_form,
	                         Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_doctor_form, sign_up_form, false);
		rd_female = v.findViewById(R.id.rd_female);
		profiledp = v.findViewById(R.id.imageView4);
		rd_male = v.findViewById(R.id.rd_male);
		rd_others = v.findViewById(R.id.rd_others);
		register = v.findViewById(R.id.register);
		calender_button = v.findViewById(R.id.calender_button);
		spinner = v.findViewById(R.id.spinner2);
		upload = v.findViewById(R.id.button);
		location = v.findViewById(R.id.location);
		registration_no = v.findViewById(R.id.editTextText4);
		specializtion = v.findViewById(R.id.specializtion);

		register_fire = FirebaseAuth.getInstance();


		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.designation, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.location, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		location.setAdapter(adapter2);
		location.setOnItemSelectedListener(this);


		//buttons
		register = v.findViewById(R.id.register);

		//textview

		loginp = v.findViewById(R.id.log_inp);
		settingtext = v.findViewById(R.id.textView4);
		invalid_phone = v.findViewById(R.id.invalid);

		progressBar = v.findViewById(R.id.progressBar2);
		//editView

		first = v.findViewById(R.id.first_Name);
		address = v.findViewById(R.id.clinic_address);
		pincode = v.findViewById(R.id.pincode);
		last = v.findViewById(R.id.last_name);
		dob = v.findViewById(R.id.dob);
		phonen = v.findViewById(R.id.phonet);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 18;


		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PICK_IMAGE);
			}
		});


		dob.setEnabled(false);// textfile disabled
		calender_button.setOnClickListener(new View.OnClickListener() {

			//@RequiresApi(api = Build.VERSION_CODES.N)
			@Override
			public void onClick(View view) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
				Date date3 = null;
				Date date2 = null;
				month = calendar.get(Calendar.MONTH);
				date = calendar.get(Calendar.DAY_OF_MONTH);
				String datestring = date + "-" + month + "-" + year + " 00:00:00";
				try {
					date2 = sdf.parse(datestring);
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
				datePickerDialog.getDatePicker().setMaxDate(maxmillis);
				datePickerDialog.show();
			}
		});

		loginp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity(), Log_in.class));
				getActivity().finish();

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
				} else if (rd_male.isChecked()) {
					gender = "Male";
				} else if (rd_others.isChecked()) {
					gender = "Others";
				} else {
					gender = "Not Selected";
				}
				if (first.getText().toString().isEmpty() || last.getText().toString().isEmpty() || dob.getText().toString().isEmpty() ||
						 gender.equals("Not Selected") || address.getText().toString().isEmpty() || !checkp || registration_no.getText().toString().isEmpty()
						 || pincode.getText().toString().isEmpty() || desig.equalsIgnoreCase("Select Designation") ||
						 district.equalsIgnoreCase("select district") || imageuri == null) {
					if (imageuri == null) {
						Toast.makeText(getActivity(), "Image must be Uploaded", Toast.LENGTH_SHORT).show();
					}
					if (address.getText().toString().isEmpty()) {
						address.setError("Address must be filled");
						address.requestFocus();
					}
					if (pincode.getText().toString().isEmpty()) {
						pincode.setError("Pincode must be filled");
						pincode.requestFocus();
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
					if (desig.isEmpty() || desig.equalsIgnoreCase("Select Designation")) {
						spinner.requestFocus();
						Toast.makeText(getActivity(), "Please select Designation", Toast.LENGTH_SHORT).show();
					}
					if (district.isEmpty() || district.equalsIgnoreCase("select district")) {
						location.requestFocus();
						Toast.makeText(getActivity(), "Please select District", Toast.LENGTH_SHORT).show();
					}
					if (registration_no.getText().toString().isEmpty()) {
						registration_no.requestFocus();
						registration_no.setError("Cannot be Empty");
					}
					Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
				} else {
					progressBar.setVisibility(View.VISIBLE);
					createUser();
				}

			}
		});
		SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		email_id = sh.getString("email", "");
		return v;
	}

	private void createUser() {
		String fname = first.getText().toString();
		String lname = last.getText().toString();
		String date = dob.getText().toString();
		String Phone = phonen.getText().toString();
		String designation = desig;
		String pincodeadd = pincode.getText().toString();
		final String gendlast = gender;
		String addresss = address.getText().toString();
		String specialization = specializtion.getText().toString();
		String districtloc = district;
		String registeration_no_string = registration_no.getText().toString();
		final boolean vaild = false;
		final String users = user;

		User user = new User(fname, lname, date, Phone, email_id, gendlast, users, designation, pincodeadd, addresss, districtloc, specialization, registeration_no_string, vaild);
		FirebaseDatabase.getInstance().getReference("User").child(register_fire.getCurrentUser().getUid()).setValue(user)
				 .addOnCompleteListener(new OnCompleteListener<Void>() {
					 @Override
					 public void onComplete(@NonNull Task<Void> task) {
						 if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "Account is been Created.\n Log in to access your account", Toast.LENGTH_SHORT).show();
							 progressBar.setVisibility(View.GONE);
							 builder = new AlertDialog.Builder(getActivity());
							 builder.setTitle("Successful");
							 builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
								 @Override
								 public void onClick(DialogInterface dialog, int which) {
									 FirebaseAuth.getInstance().signOut();
									 startActivity(new Intent(getActivity(), Log_in.class));
									 getActivity().finish();
								 }
							 }).setMessage("Your Account is been created. After the verification," +
									  " You can access your account").create().show();


						 } else {
//							 Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
							 progressBar.setVisibility(View.GONE);
						 }
					 }
				 });
	}

	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageuri = data.getData();

			//next line will immediately set the dp but the process will take 10-20 seconds

			profiledp.setImageURI(imageuri);
			upload();
		}
	}

	private void upload() {

		if (imageuri != null) {
			storageReference = FirebaseStorage.getInstance().getReference();
			StorageReference fileref = storageReference.child("registration_images").child(registration_no.getText().toString());
			fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    imageuplode upload = new imageuplode("image", fileref.getDownloadUrl().toString());
////                    String uploadid = databaseReference.push().getKey();
////                    FirebaseDatabase db = FirebaseDatabase.getInstance();
////
////                    if (user.equals("Patient")) {
////                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");
////                        FirebaseUser users_firebase = FirebaseAuth.getInstance().getCurrentUser();
////                        String userid = users_firebase.getUid();
////                        DatabaseReference r = reference.child(userid);
////                        r.child("image").setValue(uploadid);
////
////                    }
////                    else if(user.equals("Doctor")) {
////                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctor");
////                        FirebaseUser users_firebase = FirebaseAuth.getInstance().getCurrentUser();
////                        String userid = users_firebase.getUid();
////                        DatabaseReference r = reference.child(userid);
////                        r.child("image").setValue(uploadid);
//                    }
				}

			});
			//profiledp.setImageURI();
		} else {
			Toast.makeText(getActivity(), "NO Image Selected", Toast.LENGTH_SHORT).show();
		}

	}
}