package com.example.personalhealthcare;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class patient_appointment extends Fragment {
	private final ArrayList<appointments> appointmentsArrayList = new ArrayList<>();
	String key;
	ProgressBar progressBar;
	boolean relative = false;
	private RecyclerView recycleview;
	private TextView no_appointment;
	private String uid;


	public patient_appointment() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_patient_appointment, container, false);
		recycleview = (RecyclerView) v.findViewById(R.id.pat_approved_recycle);

		no_appointment = (TextView) v.findViewById(R.id.textView001);

		recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar61);
		FirebaseUser users_firebase = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
		String userid = users_firebase.getUid();
		String m = "asd";
		Query query = ref.child(userid).child("fixappoinment");
		query.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				appointmentsArrayList.clear();
				for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
					String dd = dataSnapshot.getValue().toString();
					if (!dd.isEmpty()) {
						String[] spilt = dd.split("[++]");
						key = spilt[2];
//						Toast.makeText(getActivity(), "key" + key, Toast.LENGTH_SHORT).show();
					}
					FirebaseDatabase.getInstance().getReference("User").child(key)
							 .addListenerForSingleValueEvent(new ValueEventListener() {
								 @Override
								 public void onDataChange(@NonNull DataSnapshot snapsho) {
									 User userprofile = snapsho.getValue(User.class);
									 if (userprofile != null) {
										 uid = dataSnapshot.getKey();
										 if (key == null) {
											 no_appointment.setVisibility(View.VISIBLE);
										 } else {
											 String de = dataSnapshot.getValue().toString();
											 String[] details = de.split("[++]");
											 String v = dataSnapshot.getKey();
											 String day = "";
											 String mh = "";
											 String yr = "2023";
											 if (v.length() == 8) {
												 day = v.substring(0, 2);
												 mh = v.substring(2, 4);
											 } else if (v.length() == 7) {
												 day = v.substring(0, 2);
												 mh = v.substring(2, 3);
											 } else if (v.length() == 6) {
												 day = v.substring(0, 1);
												 mh = v.substring(1, 2);
											 }
											 String totaldate = day + "-" + mh + "-" + yr;
											 //String age= String.valueOf(details[3]);
											 appointmentsArrayList.add(new appointments(userprofile.Fullname, totaldate, details[0]));
//											 Toast.makeText(getActivity(), "" + key, Toast.LENGTH_SHORT).show();
										 }
									 }
								 }

								 @Override
								 public void onCancelled(@NonNull DatabaseError error) {

								 }
							 });
				}
				runthread();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.d("Error", error.getMessage());
			}
		});

		return v;
	}

	private void runthread() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listadapter2 itemadapter = new listadapter2(getContext(), appointmentsArrayList, uid, key);
				if (key == null) {
					no_appointment.setVisibility(View.VISIBLE);
				}
				progressBar.setVisibility(View.GONE);
				recycleview.setAdapter(itemadapter);
			}
		}, 3000);
	}
}