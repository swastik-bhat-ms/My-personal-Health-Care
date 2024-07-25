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


public class view_appointmentfrag extends Fragment {


	String key;
	private RecyclerView recycleview;
	private TextView no_appointment;
	private ProgressBar progressBar;
	private FirebaseUser users_firebase;
	private ArrayList<appointments> appointmentsArrayList = new ArrayList<>();

	public view_appointmentfrag() {
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
		View v;
		v = inflater.inflate(R.layout.fragment_view_appointmentfrag, container, false);
		recycleview = (RecyclerView) v.findViewById(R.id.approved_recycle);

		no_appointment = (TextView) v.findViewById(R.id.textView00);

		recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar6);
		users_firebase = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
		String userid = users_firebase.getUid();
		Query query = ref.child(userid).child("fixedappoitnments");
		query.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				appointmentsArrayList.clear();
				for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

					FirebaseDatabase.getInstance().getReference("User").child(dataSnapshot.getKey())
							 .addListenerForSingleValueEvent(new ValueEventListener() {
								 @Override
								 public void onDataChange(@NonNull DataSnapshot snapsho) {
									 User userprofile = snapsho.getValue(User.class);
									 if (userprofile != null) {
										 key = dataSnapshot.getKey();
										 if (key == null) {
											 no_appointment.setVisibility(View.VISIBLE);
										 }
										 String de = dataSnapshot.getValue().toString();
										 String[] details = de.split("[++]");
										 //String age= String.valueOf(details[3]);
										 appointmentsArrayList.add(new appointments(userprofile.Fullname,
												  details[4], details[6]));

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
				listadapter itemadapter = new listadapter(getContext(), appointmentsArrayList, key);
				if (key == null) {
					no_appointment.setVisibility(View.VISIBLE);
				}
				progressBar.setVisibility(View.GONE);
				recycleview.setAdapter(itemadapter);
			}
		}, 3000);
	}
}