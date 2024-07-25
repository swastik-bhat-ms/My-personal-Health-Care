package com.example.personalhealthcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Map;


public class appointmentFragment extends Fragment {

	//SharedPreferences sharedPreferences= getActivity().getSharedPreferences("events",Context.MODE_PRIVATE);
	appoint_request appoint_request = new appoint_request();
	set_appointment set_appointment = new set_appointment();
	Map<String, ?> allentries;
	DatabaseReference databaseReference;
	FirebaseAuth mAuth;
	FirebaseUser currentUser;
	private CalendarView calendarView;
	private EditText text;
	private ImageButton imageButton;
	private Button deleteeventbutton;
	private TextView textView;

	// module for shared preference upload
	private void dataupload() {

		mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser != null) {
			// User is signed in
			Log.d("user", String.valueOf(currentUser));
		} else {
			Log.d("null user", String.valueOf(currentUser));
		}
		databaseReference = FirebaseDatabase.getInstance().getReference("User");
		allentries = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE).getAll();
		databaseReference.child(currentUser.getUid()).child("total event").setValue(allentries).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void unused) {
//				Toast.makeText(getActivity(), "succesfully uploaded", Toast.LENGTH_SHORT).show();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d("firabase upload", " upload   " + e);
//				Toast.makeText(getActivity(), "failed to upload to cloud", Toast.LENGTH_SHORT).show();
			}
		});

	}


	// module for fetching events
	private String alreadysavedevent(String saveddate) {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE);
		String event = sharedPreferences.getString(saveddate, "");
		return event;
	}

	//module for  save the events
	private void saveevent(String dateofevent, String event) {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE).edit();
		editor.putString(dateofevent, event);
//        editor.clear();
		editor.apply();
		dataupload();
	}

	@SuppressLint("MissingInflatedId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_appointment, container, false);
		// Inflate the layout for this fragment
		calendarView = view.findViewById(R.id.calendarView);
		text = view.findViewById(R.id.textView4);
		textView = view.findViewById(R.id.displaydateevent);
		imageButton = view.findViewById(R.id.imageButton);
		deleteeventbutton = view.findViewById(R.id.deleteevent);
		deleteeventbutton.setVisibility(View.INVISIBLE);
		calendarView.setMinDate(calendarView.getDate());
		text.setHint("enter or edit event");
		try {
			calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
				@Override
				public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(year, month, day);
					String date = String.valueOf(day);
					String mth = String.valueOf(month + 1);
					String yr = String.valueOf(year);
					String saveddate = date + mth + yr;
					imageButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							String event = text.getText().toString();
							if (!event.isEmpty()) {
								String date = String.valueOf(calendar.get(Calendar.DATE));
								String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
								String year = String.valueOf(calendar.get(Calendar.YEAR));
								String dateevent = (date + month + year);
								saveevent(dateevent, event);
								Toast.makeText(getActivity(), "event saved", Toast.LENGTH_SHORT).show();
							} else {
//								Toast.makeText(getActivity(), "you havent typed any event", Toast.LENGTH_SHORT).show();
							}
						}
					});
					String savedevent = alreadysavedevent(saveddate);
					if (savedevent.isEmpty()) {
						deleteeventbutton.setVisibility(View.INVISIBLE);
					} else {
						deleteeventbutton.setVisibility(View.VISIBLE);
					}
					textView.setText(savedevent);
					text.setText("");
					deleteeventbutton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							SharedPreferences.Editor editor = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE).edit();
							editor.remove(saveddate);
							editor.apply();
							Toast.makeText(getActivity(), "event deleted", Toast.LENGTH_SHORT).show();
							dataupload();
						}
					});
					Bundle tranferdate = new Bundle();
					String tranferabledate = date + "/" + mth + "/" + yr;
					tranferdate.putString("date", tranferabledate);
					appoint_request.setArguments(tranferdate);
				}
			});


		} catch (Exception e) {
			System.out.println(e);
		}

		return view;
	}

}