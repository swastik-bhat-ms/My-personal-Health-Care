package com.example.personalhealthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class deleteaccount extends Fragment {


	ImageButton hide_show;
	private TextView pass_lbl;
	private EditText deletw_password;
	private Button delete_btn;
	private ProgressBar progresbar;

	public deleteaccount() {
		// Required empty public constructor
	}


	// TODO: Rename and change types and number of parameters


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_deleteaccount, container, false);
		pass_lbl = v.findViewById(R.id.pass_lbl);
		deletw_password = v.findViewById(R.id.delete_password);
		delete_btn = v.findViewById(R.id.delete_btn);
		progresbar = v.findViewById(R.id.progressBar7);
		hide_show = v.findViewById(R.id.hide_show);


		hide_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (deletw_password.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
					deletw_password.setTransformationMethod(new SingleLineTransformationMethod());
				} else {
					deletw_password.setTransformationMethod(new PasswordTransformationMethod());
				}
			}
		});


		delete_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progresbar.setVisibility(View.VISIBLE);
				SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
				String email = sh.getString("email", "");
				AuthCredential credential = EmailAuthProvider.getCredential(email, deletw_password.getText().toString());
				FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
							FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									if (task.isSuccessful()) {
										Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
										startActivity(new Intent(getActivity(), Log_in.class));
										progresbar.setVisibility(View.GONE);
										SharedPreferences.Editor ed = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
										ed.clear();
										ed.apply();
										getActivity().finish();
									}
								}
							});
						}
					}
				});
			}
		});
		return v;
	}
}