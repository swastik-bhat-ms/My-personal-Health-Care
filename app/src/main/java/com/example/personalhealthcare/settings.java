package com.example.personalhealthcare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class settings extends Fragment {
	String name;
	TextView nameid;
	String user = "null";
	ImageView dp;
	private Button logout, delete_account;
	private Button aboutus;
	private ProgressBar progressBar;
	private Button help, edit_profile, account;
	private editprofile editprofil;
	private FirebaseAuth login;
	private String uid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("MissingInflatedId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		logout = (Button) view.findViewById(R.id.logout);
		aboutus = (Button) view.findViewById(R.id.aboutus);
		dp = (ImageView) view.findViewById(R.id.dp);
		nameid = (TextView) view.findViewById(R.id.nameid);
		account = view.findViewById(R.id.button4);
		help = (Button) view.findViewById(R.id.button5);
		edit_profile = (Button) view.findViewById(R.id.edit_profile);
		delete_account = (Button) view.findViewById(R.id.delete_account);
		progressBar = (ProgressBar) view.findViewById(R.id.delete_progress);
		login = FirebaseAuth.getInstance();
		uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment help = new help();
				FragmentTransaction helpacc = getActivity().getSupportFragmentManager().beginTransaction();
				helpacc.replace(R.id.container, help).addToBackStack("help").commit();
			}
		});

		account.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment account = new account();
				FragmentTransaction edit_fragment = getActivity().getSupportFragmentManager().beginTransaction();
				edit_fragment.replace(R.id.container, account).addToBackStack("account").commit();
			}
		});


		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Do you wish to logout from this account ? ").setPositiveButton("Logout", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						login.signOut();
						SharedPreferences.Editor ed = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
						ed.clear();
						ed.apply();
						startActivity(new Intent(getActivity(), Log_in.class));
						getActivity().finish();
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				AlertDialog alertDialog = builder.create();
				alertDialog.show();

			}
		});
		edit_profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				progressBar.setVisibility(View.VISIBLE);
				Fragment editprofile = new editprofile();
				FragmentTransaction edit_fragment = getActivity().getSupportFragmentManager().beginTransaction();
				edit_fragment.replace(R.id.container, editprofile).addToBackStack("settings").commit();
			}
		});
		StorageReference storageReference = FirebaseStorage.getInstance().getReference();

		StorageReference st = storageReference.child("profile").child(FirebaseAuth.getInstance().getUid());
		st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
				Picasso.get().load(uri).into(dp);
			}
		});
		SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		nameid.setText(sh.getString("Names", ""));
		delete_account.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//                progressBar.setVisibility(View.VISIBLE);
				Fragment deleteaccount = new deleteaccount();
				FragmentTransaction delete_fragment = getActivity().getSupportFragmentManager().beginTransaction();
				delete_fragment.replace(R.id.container, deleteaccount).addToBackStack("settings").commit();
//                deleteaccout();
			}
		});
		aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fragment aboutpage = new about();
				FragmentTransaction about_Fragment = getActivity().getSupportFragmentManager().beginTransaction();
				about_Fragment.replace(R.id.container, aboutpage).addToBackStack("settings").commit();
			}
		});

		return view;
	}

	private void deleteaccout() {

		FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
					FirebaseDatabase.getInstance().getReference("User").child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getActivity(), "Error. Try Again Later" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
					Toast.makeText(getActivity(), "" + uid, Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), Log_in.class));
					progressBar.setVisibility(View.GONE);
					SharedPreferences.Editor ed = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
					ed.clear();
					ed.apply();
					getActivity().finish();
				} else {
					Toast.makeText(getActivity(), "Error. Try Again Later" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
				}
			}
		});
	}
}