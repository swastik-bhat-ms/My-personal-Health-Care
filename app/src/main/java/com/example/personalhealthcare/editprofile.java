package com.example.personalhealthcare;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class editprofile extends Fragment implements AdapterView.OnItemSelectedListener {
	private static final int PICK_IMAGE = 1;
	EditText editname, editphonefild;
	DatabaseReference reference;
	FirebaseUser users_firebase;
	String user = "";
	Uri imageuri = null;
	ImageButton nameeditbtn;
	ImageButton editphone, editspecial;
	EditText editnamelastname;
	String userid;
	CardView specializtion_card;
	EditText editspecailtext;
	TextView user_special;
	Spinner spinner1;
	private ImageView profiledp;
	private TextView user_name, user_phone_no, user_emailid, show, addressview;
	private FloatingActionButton uploadimage;
	private FirebaseStorage storageReference;
	private DatabaseReference databaseReference;
	private ImageButton editsaddress;
	private EditText edit_pincode, edit_adress;
	private String district;
	private ProgressBar progressBar9;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("MissingInflatedId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
		profiledp = (ImageView) view.findViewById(R.id.profiledp);
		user_name = (TextView) view.findViewById(R.id.user_name);
		show = (TextView) view.findViewById(R.id.show);
		nameeditbtn = view.findViewById(R.id.nameeditbtn);
		editphone = view.findViewById(R.id.editphone);
		user_phone_no = (TextView) view.findViewById(R.id.user_phoneno);
		user_emailid = (TextView) view.findViewById(R.id.user_emailid);
		editsaddress = (ImageButton) view.findViewById(R.id.editsaddress);
		uploadimage = (FloatingActionButton) view.findViewById(R.id.uploadimage);
		storageReference = FirebaseStorage.getInstance();
		databaseReference = FirebaseDatabase.getInstance().getReference();
		specializtion_card = view.findViewById(R.id.specializtion_card);
		editspecial = view.findViewById(R.id.editspeical);
		addressview = view.findViewById(R.id.addressview);
		progressBar9 = view.findViewById(R.id.progressBar9);
		user_special = view.findViewById(R.id.user_special);
		//StorageReference st = storageReference.child(FirebaseAuth.getInstance().getUid());
		StorageReference st = storageReference.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());

		st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {

				Picasso.get().load(uri).into(profiledp);


			}
		});

		SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		user_name.setText(sh.getString("Names", ""));
		user_phone_no.setText(sh.getString("Phoneno", ""));
		user_emailid.setText(sh.getString("email", ""));
		String user = sh.getString("User", "");
		user_special.setText(sh.getString("special", ""));
		addressview.setText(sh.getString("address", "") + "\n" + sh.getString("district", "") + "\n" + sh.getString("pincode", ""));
		if (user.equals("Patient")) {
			show.setText("This is your username. This name will be visible to Doctors");
			specializtion_card.setVisibility(View.GONE);

		} else if (user.equals("Doctor")) {
			show.setText("This is your username. This name will be visible to Patient");
		}
		View veiw1 = inflater.inflate(R.layout.addressedit, null);
		edit_adress = veiw1.findViewById(R.id.edit_adress);
		edit_pincode = veiw1.findViewById(R.id.edit_pin);
		spinner1 = veiw1.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter11 = ArrayAdapter.createFromResource(getActivity(), R.array.location, android.R.layout.simple_spinner_item);
		adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter11);
		spinner1.setOnItemSelectedListener(this);

		editsaddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog dialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater1 = getActivity().getLayoutInflater();

				builder.setView(veiw1)
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.dismiss();

							 }
						 }).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 if (!edit_adress.getText().toString().isEmpty() && !edit_pincode.getText().toString().isEmpty()) {
									 Map<String, Object> address = new HashMap<>();
									 address.put("address", edit_adress.getText().toString());
									 address.put("pincode", edit_pincode.getText().toString());
									 if (!spinner1.getSelectedItem().toString().equalsIgnoreCase("select district")) {
										 address.put("district", spinner1.getSelectedItem().toString());

									 }
									 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(address).addOnCompleteListener(new OnCompleteListener<Void>() {
										 @Override
										 public void onComplete(@NonNull Task<Void> task) {
											 if (task.isSuccessful()) {
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
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.putString("address", userprofile.address);
																	 edit.putString("district", userprofile.district);
																	 edit.putString("pincode", userprofile.pincode);
																	 edit.apply();
																 } else if (userprofile.user.equalsIgnoreCase("doctor")) {
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.putString("desig", userprofile.designation);
																	 edit.putString("address", userprofile.address);
																	 edit.putString("district", userprofile.district);
																	 edit.putString("pincode", userprofile.pincode);
																	 edit.apply();
																 }
															 }
															 addressview.setText(edit_adress.getText().toString() + "\n" + sh.getString("district", "") + "\n" + edit_pincode.getText().toString());
															 Toast.makeText(getActivity(), "Successfully changed", Toast.LENGTH_SHORT).show();
														 }

														 @Override
														 public void onCancelled(@NonNull DatabaseError error) {
															 Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
														 }
													 });
												 }
											 }
										 }
									 });
								 }
							 }
						 });


				dialog = builder.create();
				Window window = dialog.getWindow();
				WindowManager.LayoutParams layoutParam = window.getAttributes();
				layoutParam.gravity = Gravity.BOTTOM;
				window.setAttributes(layoutParam);
				dialog.show();

			}
		});
		uploadimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PICK_IMAGE);


			}
		});
		editspecial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog dialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater1 = getActivity().getLayoutInflater();
				View veiw1 = inflater1.inflate(R.layout.newspecialzaition, null);
				editspecailtext = veiw1.findViewById(R.id.editTextText3);
				builder.setView(veiw1)
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.dismiss();
							 }
						 }).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 if (!editspecailtext.getText().toString().isEmpty()) {
									 Map<String, Object> name = new HashMap<>();
									 name.put("specializtion", editspecailtext.getText().toString());
									 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(name).addOnCompleteListener(new OnCompleteListener<Void>() {
										 @Override
										 public void onComplete(@NonNull Task<Void> task) {
											 if (task.isSuccessful()) {
												 user_special.setText(editspecailtext.getText().toString());
												 Toast.makeText(getActivity(), "Successfully changed", Toast.LENGTH_SHORT).show();
											 }
										 }
									 });
								 }
							 }
						 });
				dialog = builder.create();
				Window window = dialog.getWindow();
				WindowManager.LayoutParams layoutParam = window.getAttributes();
				layoutParam.gravity = Gravity.BOTTOM;
				window.setAttributes(layoutParam);
				dialog.show();
			}
		});

		nameeditbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog dialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater1 = getActivity().getLayoutInflater();
				View veiw1 = inflater1.inflate(R.layout.newname, null);
				editname = veiw1.findViewById(R.id.editTextText);
				editnamelastname = veiw1.findViewById(R.id.editTextText2);
				builder.setView(veiw1)
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.dismiss();
							 }
						 }).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 String name1 = editname.getText().toString();
								 String name2 = editnamelastname.getText().toString();
								 if (!editname.getText().toString().isEmpty() && !editnamelastname.getText().toString().isEmpty()) {
									 Map<String, Object> name = new HashMap<>();
									 name.put("Fullname", editname.getText().toString());
									 name.put("Lastname", editnamelastname.getText().toString());
									 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(name).addOnCompleteListener(new OnCompleteListener<Void>() {
										 @Override
										 public void onComplete(@NonNull Task<Void> task) {
											 if (task.isSuccessful()) {
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
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.apply();
																 } else if (userprofile.user.equalsIgnoreCase("doctor")) {
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.putString("desig", userprofile.designation);
																	 edit.apply();
																 }
															 }
															 user_name.setText(name1 + " " + name2);
															 Toast.makeText(getActivity(), "Successfully changed", Toast.LENGTH_SHORT).show();
														 }

														 @Override
														 public void onCancelled(@NonNull DatabaseError error) {
															 Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
														 }
													 });
												 }
											 }
										 }
									 });
								 } else {
									 Toast.makeText(getActivity(), "Invalid Input Try Again", Toast.LENGTH_SHORT).show();
								 }
							 }
						 });
				dialog = builder.create();
				Window window = dialog.getWindow();
				WindowManager.LayoutParams layoutParam = window.getAttributes();
				layoutParam.gravity = Gravity.BOTTOM;
				window.setAttributes(layoutParam);
				dialog.show();

			}
		});


		editphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog dialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater1 = getActivity().getLayoutInflater();
				View veiw12 = inflater1.inflate(R.layout.editphone, null);
				builder.setView(veiw12)
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.dismiss();
							 }
						 }).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 editphonefild = veiw12.findViewById(R.id.editTextText1212);
								 if (!editphonefild.getText().toString().isEmpty() && editphonefild.getText().toString().length() == 10) {
									 Map<String, Object> name = new HashMap<>();
									 name.put("Phone", editphonefild.getText().toString());
									 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(name).addOnCompleteListener(new OnCompleteListener<Void>() {
										 @Override
										 public void onComplete(@NonNull Task<Void> task) {
											 if (task.isSuccessful()) {

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
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.apply();
																 } else if (userprofile.user.equalsIgnoreCase("doctor")) {
																	 SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
																	 edit.putString("Names", userprofile.Fullname + " " + userprofile.Lastname);
																	 edit.putString("User", userprofile.user);
																	 edit.putString("Phoneno", userprofile.Phone);
																	 edit.putString("email", userprofile.email);
																	 edit.putString("desig", userprofile.designation);
																	 edit.apply();
																 }
															 }
															 user_phone_no.setText(editphonefild.getText().toString());
															 Toast.makeText(getActivity(), "Successfully changed", Toast.LENGTH_SHORT).show();
														 }

														 @Override
														 public void onCancelled(@NonNull DatabaseError error) {
															 Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
														 }
													 });
												 }
											 }
										 }
									 });
								 } else {
									 Toast.makeText(getActivity(), "Invalid Input Try Again", Toast.LENGTH_SHORT).show();
								 }
							 }
						 });
				dialog = builder.create();
				Window window = dialog.getWindow();
				WindowManager.LayoutParams layoutParam = window.getAttributes();
				layoutParam.gravity = Gravity.BOTTOM;
				window.setAttributes(layoutParam);
				dialog.show();

			}
		});


		return view;
	}

//    private String getExtension(Uri uri) {
//        ContentResolver cr = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(uri));
//
//    }

	private void upload() {
		if (imageuri != null) {
			storageReference = null;
			storageReference = FirebaseStorage.getInstance();
			StorageReference fileref = storageReference.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());
			fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
					//    progressBar9.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
					profiledp.setImageURI(imageuri);
				}

			});
			//profiledp.setImageURI();
		} else {
			Toast.makeText(getActivity(), "NO Image Selected", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageuri = data.getData();
			//  progressBar9.setVisibility(View.VISIBLE);
			//next line will immediately set the dp but the process will take 10-20 seconds
			String uri = imageuri.toString();
			upload();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		district = spinner1.getSelectedItem().toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}