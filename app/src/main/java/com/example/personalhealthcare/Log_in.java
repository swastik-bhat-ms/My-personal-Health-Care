package com.example.personalhealthcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Log_in extends AppCompatActivity {
	FirebaseUser users_firebase;
	DatabaseReference reference;
	String userid = "";
//	names = "", user = "";
//	boolean check = false;
	FirebaseAuth log;
	private Button login;
	private EditText email;
	private TextView sigupt, forget_password_bt, login_textView, NotAccount_TextView, name;
	private EditText password;
	private ProgressBar progress;
	private ImageButton hide_show;
	private AlertDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		FirebaseApp.initializeApp(this);
		setContentView(R.layout.activity_mainaa);
		getSupportActionBar().hide();
		progress = findViewById(R.id.progressBar);
		email = findViewById(R.id.emailid);
		hide_show = findViewById(R.id.hide_show);
		password = findViewById(R.id.passwordb);
		login = findViewById(R.id.login);
		name = findViewById(R.id.textView);
		login_textView = findViewById(R.id.textView3);
		NotAccount_TextView = findViewById(R.id.not_account);
		sigupt = findViewById(R.id.sign_up);
		forget_password_bt = findViewById(R.id.forget_password);
		log = FirebaseAuth.getInstance();


		hide_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (password.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
					password.setTransformationMethod(new SingleLineTransformationMethod());

				} else {
					password.setTransformationMethod(new PasswordTransformationMethod());
				}
			}
		});

		forget_password_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login.setVisibility(View.GONE);
				email.setVisibility(View.GONE);
				name.setVisibility(View.GONE);
				password.setVisibility(View.GONE);
				sigupt.setVisibility(View.GONE);
				login_textView.setVisibility(View.GONE);
				NotAccount_TextView.setVisibility(View.GONE);
				hide_show.setVisibility(View.GONE);
				forget_password_bt.setVisibility(View.GONE);
				Fragment frag = new Forget_password();
				FragmentTransaction forget_fragment = getSupportFragmentManager().beginTransaction();
				forget_fragment.replace(R.id.containe, frag).addToBackStack(null).commit();

			}
		});


		sigupt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent second_page = new Intent(Log_in.this, signup.class);
				startActivity(second_page);
				finish();
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				progress.setVisibility(View.VISIBLE);
				loginUser();

			}
		});

	}


	private void loginUser() {

		if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
			if (TextUtils.isEmpty(email.getText().toString()) && TextUtils.isEmpty(password.getText().toString())) {
				email.setError("Email cannot be Empty");
				password.setError("Password cannot be empty");
				email.requestFocus();
				password.requestFocus();
				progress.setVisibility(View.GONE);
			} else if (TextUtils.isEmpty(password.getText().toString())) {
				password.setError("Password cannot be empty");
				password.requestFocus();
				progress.setVisibility(View.GONE);
			} else if (TextUtils.isEmpty(email.getText().toString())) {
				email.setError("Email cannot be Empty");
				email.requestFocus();
				progress.setVisibility(View.GONE);
			}
			progress.setVisibility(View.GONE);
		} else {
			log.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()) {

						FirebaseUser users_firebases = FirebaseAuth.getInstance().getCurrentUser();

						if (users_firebases != null) {
							reference = FirebaseDatabase.getInstance().getReference("User");
							userid = users_firebases.getUid();
							reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot snapshot) {
									User userprofile = snapshot.getValue(User.class);
									if (userprofile != null) {
//										Toast.makeText(Log_in.this, "in", Toast.LENGTH_SHORT).show();
										if (userprofile.user.equalsIgnoreCase("admin")) {
											Toast.makeText(Log_in.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
											progress.setVisibility(View.GONE);
											startActivity(new Intent(Log_in.this, adminpage.class));
											finish();

										} else {
											if (log.getCurrentUser().isEmailVerified()) {
												Toast.makeText(Log_in.this, "Fetching details. Please wait", Toast.LENGTH_SHORT).show();
												users_firebase = FirebaseAuth.getInstance().getCurrentUser();
												if (users_firebase != null) {
													reference = FirebaseDatabase.getInstance().getReference("User");
													userid = users_firebase.getUid();
													reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
														@Override
														public void onDataChange(@NonNull DataSnapshot snapshot) {

															User userprofile = snapshot.getValue(User.class);
															if (userprofile != null) {

																if (userprofile.user.equalsIgnoreCase("patient")) {
//                                                String namess = userprofile.Fullname + " " + userprofile.Lastname;
//                                                String userphone = userprofile.Phone;
//                                                names = namess;
//                                              user = userprofile.user;

																	SharedPreferences.Editor edit = Log_in.this.getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	edit.putString("User", userprofile.user);
																	edit.putString("Phoneno", userprofile.Phone);
																	edit.putString("email", userprofile.email);
																	edit.putString("address", userprofile.address);
																	edit.putString("district", userprofile.district);
																	edit.putString("pincode", userprofile.pincode);
																	edit.apply();
																} else if (userprofile.user.equalsIgnoreCase("doctor")) {
																	SharedPreferences.Editor edit = Log_in.this.getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	edit.putString("User", userprofile.user);
																	edit.putString("Phoneno", userprofile.Phone);
																	edit.putString("email", userprofile.email);
																	edit.putString("desig", userprofile.designation);
																	edit.putString("special", userprofile.specializtion);
																	edit.putString("address", userprofile.address);
																	edit.putString("district", userprofile.district);
																	edit.putString("pincode", userprofile.pincode);
																	edit.apply();

																}


																if (userprofile.user.equalsIgnoreCase("doctor") && userprofile.vaild) {
																	runthread();
																} else if (userprofile.user.equalsIgnoreCase("patient")) {
																	runthread();

																} else if (userprofile.user.equalsIgnoreCase("doctor") && userprofile.inprocess && !userprofile.vaild) {
																	progress.setVisibility(View.GONE);
																	AlertDialog.Builder builder = new AlertDialog.Builder(Log_in.this);
																	builder.setTitle("Application Rejected");
																	builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(DialogInterface dialog, int which) {
																			log.signOut();
																			dialog.dismiss();
																		}
																	}).setMessage("Your verification has been rejected.\nContact our admin for further process.\n Our HelpLine No: 6361530260 , 9873649306.\n Or Mail us on \n personalhealthcare2023@gmail.com ");
																	dialog = builder.create();
																	dialog.show();
																} else if (userprofile.user.equalsIgnoreCase("doctor") && !userprofile.vaild) {
																	progress.setVisibility(View.GONE);
																	AlertDialog.Builder builder = new AlertDialog.Builder(Log_in.this);
																	builder.setTitle("Under Process");
																	builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(DialogInterface dialog, int which) {
																			log.signOut();
																			dialog.dismiss();
																		}
																	}).setMessage("Your Account is in under checking process. Please wait until it is complete. ");
																	dialog = builder.create();
																	dialog.show();
																}
															}
														}

														@Override
														public void onCancelled(@NonNull DatabaseError error) {
															Toast.makeText(Log_in.this, "" + error, Toast.LENGTH_SHORT).show();
														}
													});
												}
											}
										}
									}


								}

								@Override
								public void onCancelled(@NonNull DatabaseError error) {
									Toast.makeText(Log_in.this, "" + error, Toast.LENGTH_SHORT).show();
								}
							});
						} else {
							Toast.makeText(Log_in.this, "Email not verified. First verify Email from mail sent in the inbox ", Toast.LENGTH_SHORT).show();
							FirebaseAuth.getInstance().signOut();
							progress.setVisibility(View.GONE);
						}
					} else {
						Toast.makeText(Log_in.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
						progress.setVisibility(View.GONE);
					}
				}
			});
		}
	}

	private void runthread() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Log_in.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);
				startActivity(new Intent(Log_in.this, MainActivity.class));
				finish();
			}
		}, 3000);
	}
}