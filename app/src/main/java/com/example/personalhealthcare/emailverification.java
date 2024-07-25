package com.example.personalhealthcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class emailverification extends Fragment {
	FirebaseAuth register;
	EditText email, password;
	Button verfiy_btn;
	ProgressBar progressBar;
	Bundle bundle = new Bundle();
	boolean checkpass = false;
	ImageButton hide_pass;
	private Button continue_btn, resend_otp;
	private TextView desp;
	private TextView txt;
	private TextView invalid_pass;
	private TextView email_txt;

	@Override
	public void onDestroy() {
		super.onDestroy();
		register.signOut();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment


		View view = inflater.inflate(R.layout.fragment_emailverification, container, false);

		email = (EditText) view.findViewById(R.id.emailverfiy);
		email_txt = (TextView) view.findViewById(R.id.email_txt);
		verfiy_btn = (Button) view.findViewById(R.id.btn);
		resend_otp = (Button) view.findViewById(R.id.resend_otp);
		continue_btn = (Button) view.findViewById(R.id.cont);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar4);
		desp = (TextView) view.findViewById(R.id.dep);
		txt = (TextView) view.findViewById(R.id.email_txt);
		password = (EditText) view.findViewById(R.id.password_verify);
		hide_pass = view.findViewById(R.id.hide_show);
		invalid_pass = (TextView) view.findViewById(R.id.invaildpass);

		register = FirebaseAuth.getInstance();

		hide_pass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (password.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
					password.setTransformationMethod(new SingleLineTransformationMethod());

				} else {
					password.setTransformationMethod(new PasswordTransformationMethod());
				}
			}
		});


		verfiy_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
					if (!password.getText().toString().isEmpty() && password.getText().toString().length() >= 8) {
						progressBar.setVisibility(View.VISIBLE);
						verfiy_btn.setEnabled(false);
						SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
						edit.putString("email", email.getText().toString());
						edit.apply();
						bundle.putString("email", email.getText().toString());
						bundle.putString("password", password.getText().toString());
						verfiylink();
					} else {
						Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
						invalid_pass.setVisibility(View.VISIBLE);
						verfiy_btn.setEnabled(true);
						progressBar.setVisibility(View.GONE);
					}
				} else {
					Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
					email.setError("Wrong Email type");
					email.requestFocus();
					verfiy_btn.setEnabled(true);
					progressBar.setVisibility(View.GONE);
				}
			}
		});


		continue_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				register = null;
				register = FirebaseAuth.getInstance();


				register.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
							patient_form patient_form = new patient_form();
							getActivity().findViewById(R.id.sign_uppage).setVisibility(View.VISIBLE);
							getActivity().findViewById(R.id.spinner_user).setVisibility(View.VISIBLE);
							FragmentTransaction patient_fragment = getActivity().getSupportFragmentManager().beginTransaction();
							patient_fragment.replace(R.id.sign_up_form, patient_form).addToBackStack("null").commit();

						} else {
							Toast.makeText(getActivity(), "Email is not been Verified. \nPlease check your mail", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});


		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (password.getText().toString().length() < 8) {
					invalid_pass.setVisibility(View.VISIBLE);
				} else {
					invalid_pass.setVisibility(View.INVISIBLE);
				}
				if (password.getText().toString().isEmpty()) {

					invalid_pass.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		resend_otp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				register.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful())
							Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		return view;
	}

	public void verfiylink() {
		register = FirebaseAuth.getInstance();
		String emailid = email.getText().toString();
		String pass = password.getText().toString();

		register.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					register.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Toast.makeText(getActivity(), "Email sent to your mail.Please Check spam", Toast.LENGTH_LONG).show();
								Toast.makeText(getActivity(), "User will be Registered after verifying the email \nLog in after verifying the email To Continue", Toast.LENGTH_LONG).show();
								progressBar.setVisibility(View.GONE);
								hide_pass.setVisibility(View.INVISIBLE);
								password.setVisibility(View.GONE);
								email.setVisibility(View.GONE);
								verfiy_btn.setVisibility(View.GONE);
								txt.setVisibility(View.GONE);
								desp.setVisibility(View.VISIBLE);
								continue_btn.setVisibility(View.VISIBLE);
								resend_otp.setVisibility(View.VISIBLE);

							}
						}
					});
				} else if (!task.isSuccessful()) {
					register.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
								FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										register.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
											@Override
											public void onComplete(@NonNull Task<AuthResult> task) {
												if (task.isSuccessful()) {
													register.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
														@Override
														public void onComplete(@NonNull Task<Void> task) {
															if (task.isSuccessful()) {
																hide_pass.setVisibility(View.INVISIBLE);
																email.setVisibility(View.GONE);
																email_txt.setVisibility(View.GONE);
																verfiy_btn.setVisibility(View.GONE);
																Toast.makeText(getActivity(), "Email sent to your mail.Please Check spam", Toast.LENGTH_LONG).show();
																Toast.makeText(getActivity(), "User will be Registered after verifying the email \nLog in after verifying the email To Continue", Toast.LENGTH_LONG).show();


																password.setVisibility(View.GONE);
																desp.setVisibility(View.VISIBLE);
																continue_btn.setVisibility(View.VISIBLE);
																resend_otp.setVisibility(View.VISIBLE);
																progressBar.setVisibility(View.GONE);


															}
														}
													});
												} else {
													Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
												}
											}
										});
									}

								});
							} else
								Toast.makeText(getActivity(), "Email already exits", Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.GONE);
							verfiy_btn.setEnabled(true);


						}
					});
				}

			}
		});
	}
}