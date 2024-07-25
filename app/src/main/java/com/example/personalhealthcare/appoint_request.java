package com.example.personalhealthcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class appoint_request extends Fragment {
	set_appointment set_appointment = new set_appointment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_appoint_request, container, false);
		Spinner doc = view.findViewById(R.id.spinnerr);
		ArrayAdapter<Docdatabase> arrayAdapter = null;
		Docdatahelper docdatahelper = Docdatahelper.getdoc(getActivity());
		ArrayList<Docdatabase> alist = (ArrayList<Docdatabase>) docdatahelper.DocdataDao().getThese();
		ArrayList aalist = new ArrayList<>();
		for (int i = 0; i < alist.size(); i++) {
			//aalist.add(alist.get(i).getFullname());
			// aalist.add(alist.get(i).getEmail());
			// aalist.add(alist.get(i).getDesignation());
			String li = alist.get(i).getFullname() + "  " + alist.get(i).getDesignation();
			aalist.add(li);
		}
		arrayAdapter = new ArrayAdapter<Docdatabase>(getActivity(), android.R.layout.simple_spinner_item, aalist);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		doc.setAdapter(arrayAdapter);
		doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String docadd = alist.get(position).getFullname() + "\nDesignation: " + alist.get(position).getDesignation() + " \nAddress: " + alist.get(position).getAddress();
				Bundle b = new Bundle();
				b.putString("docname", docadd);
//                set_appointment.setArguments(b);
//                Toast.makeText(getActivity(), alist.get(position).getDesignation(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		//alist.add();
		Button email = view.findViewById(R.id.request);
		email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle gotdate = getArguments();
				String date = gotdate.getString("date");
				Bundle b = new Bundle();
				b.putString("date", date);
				set_appointment.setArguments(b);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, set_appointment).addToBackStack("homeFragment").commit();


			}
		});

		return view;
	}
}