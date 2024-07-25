package com.example.personalhealthcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class account extends Fragment {


	private ImageView profiledp;
	private TextView user_name;
	private TextView show;
	private TextView user_phone_no;
	private TextView user_emailid;
	private FirebaseStorage storageReference;
	private DatabaseReference databaseReference;
	private CardView specializtion_card;
	private TextView addressview;
	private TextView user_special;

	public account() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_account, container, false);

		profiledp = (ImageView) v.findViewById(R.id.profiledp1);
		user_name = (TextView) v.findViewById(R.id.user_name1);
		show = (TextView) v.findViewById(R.id.show1);

		user_phone_no = (TextView) v.findViewById(R.id.user_phoneno1);
		user_emailid = (TextView) v.findViewById(R.id.user_emailid1);


		storageReference = FirebaseStorage.getInstance();
		databaseReference = FirebaseDatabase.getInstance().getReference();
		specializtion_card = v.findViewById(R.id.specializtion_card1);

		addressview = v.findViewById(R.id.addressview1);
//		progressBar9 = v.findViewById(R.id.progressBar9);
		user_special = v.findViewById(R.id.user_special1);
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


		return v;
	}
}