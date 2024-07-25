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
import java.util.Calendar;


public class appointmentrequest extends Fragment {
	//	String key;
	String key;
	ProgressBar progressBar;
	private RecyclerView recycleview;
	private FirebaseUser users_firebase;
	private ArrayList<appointments> appointmentsArrayList = new ArrayList<>();
	private TextView no_appointment;


	public appointmentrequest() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_appointmentrequest, container, false);
		recycleview = (RecyclerView) v.findViewById(R.id.req_set);
		no_appointment = (TextView) v.findViewById(R.id.noreqst);
		recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar5);
		users_firebase = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
		String userid = users_firebase.getUid();
		Query query = ref.child(userid).child("appoinment");
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
//										 Toast.makeText(getActivity(), "" + details.length, Toast.LENGTH_SHORT).show();
										 if (details.length > 6) {
											 boolean relative = true;
											 appointmentsArrayList.add(new appointments(details[6], details[10], details[8], details[4], details[0] + " " + details[2], relative, key));
										 } else {
											 Calendar calendar = Calendar.getInstance();
											 String add = userprofile.age.concat("/");
//											 Toast.makeText(getActivity(), "" + add, Toast.LENGTH_SHORT).show();
											 String[] agea = add.split("/");
											 String finalage = agea[2];
											 int calage = Integer.parseInt(finalage);
//                                   Toast.makeText(getActivity(), "Age"+userprofile.age, Toast.LENGTH_SHORT).show();
											 String age = String.valueOf(calendar.get(Calendar.YEAR) - calage);
											 appointmentsArrayList.add(new appointments(userprofile.Fullname,
													  age, (details[4]), details[0] + "  " + details[2], key));
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
				itemadapter itemadapter = new itemadapter(getContext(), appointmentsArrayList, key);
				if (key == null) {
					no_appointment.setVisibility(View.VISIBLE);
				}
				progressBar.setVisibility(View.GONE);
				recycleview.setAdapter(itemadapter);
			}
		}, 3000);
	}
}