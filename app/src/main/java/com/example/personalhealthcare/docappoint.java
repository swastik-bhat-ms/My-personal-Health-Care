package com.example.personalhealthcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class docappoint extends Fragment {
	CalendarView calendarView;
	TextView textView;

	public String fetchevent(String currentdate) {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("docevent", Context.MODE_PRIVATE);
		String docevent = sharedPreferences.getString(currentdate, "");
		return docevent;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_docappoint, container, false);
		calendarView = view.findViewById(R.id.doccalender);
		textView = view.findViewById(R.id.totaldocevents);
		calendarView.setMinDate(calendarView.getDate());
		calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
				String date = String.valueOf(dayOfMonth);
				String mth = String.valueOf(month + 1);
				String yr = String.valueOf(year);
				String crntdate = date + mth + yr;
//                Toast.makeText(getActivity(), crntdate, Toast.LENGTH_SHORT).show();
				String appointments = fetchevent(crntdate);
				textView.setText(appointments);
			}
		});
		return view;
	}
}