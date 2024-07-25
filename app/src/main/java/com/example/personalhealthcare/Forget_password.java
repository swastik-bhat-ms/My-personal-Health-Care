package com.example.personalhealthcare;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forget_password extends Fragment {

	Activity context;
	EditText Email_add;
	FirebaseAuth firebaseAuth;
	ProgressBar progressBar;
	Button email_send;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity();
		View v = inflater.inflate(R.layout.fragment_forget_password, container, false);
		Email_add = (EditText) v.findViewById(R.id.email_address);
		email_send = (Button) v.findViewById(R.id.Email_send);
		progressBar = (ProgressBar) v.findViewById(R.id.barprogress);
		firebaseAuth = FirebaseAuth.getInstance();
		email_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				progressBar.setVisibility(View.VISIBLE);
				if (!Email_add.getText().toString().isEmpty()) {
					firebaseAuth.sendPasswordResetEmail(Email_add.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Toast.makeText(context, "Email has been sent", Toast.LENGTH_SHORT).show();
								progressBar.setVisibility(View.GONE);
							} else {
								Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
								progressBar.setVisibility(View.GONE);
							}
						}
					});
				} else {
					Email_add.setError("Invaild Email");
					Email_add.requestFocus();
				}

			}
		});

		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		context.findViewById(R.id.textView).setVisibility(View.VISIBLE);
		context.findViewById(R.id.not_account).setVisibility(View.VISIBLE);
		context.findViewById(R.id.textView3).setVisibility(View.VISIBLE);
		context.findViewById(R.id.emailid).setVisibility(View.VISIBLE);
		context.findViewById(R.id.passwordb).setVisibility(View.VISIBLE);
		context.findViewById(R.id.forget_password).setVisibility(View.VISIBLE);
		context.findViewById(R.id.login).setVisibility(View.VISIBLE);
		context.findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
	}
}
