package com.example.personalhealthcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class availdoctor extends Fragment {
	;

	RecyclerView recyclerView;
	List<doctorlist> doclist;
	private SearchView searchView;

	public availdoctor() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_availdoctor, container, false);
		searchView = view.findViewById(R.id.searchview);
		searchView.clearFocus();

		recyclerView = view.findViewById(R.id.availabledoctor);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		//initdata();
		recyclerView.setAdapter(new doclistadapter(initdata()));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				filterList(query.trim());
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				filterList(newText.trim());
				return true;
			}
		});

		return view;
	}

	private void filterList(String Text) {
		List<doctorlist> filteredList = new ArrayList<>();
		for (doctorlist doc : doclist) {
			if (doc.getDocdis().toLowerCase().contains(Text.toLowerCase())) {
				filteredList.add(doc);
			}
		}
		if (filteredList.isEmpty()) {
			Toast.makeText(getActivity(), "no data found", Toast.LENGTH_SHORT).show();
		} else {
//            new doclistadapter(doclist).setfilteredlist(filteredList);
			recyclerView.setAdapter(new doclistadapter(filteredList));
		}
	}

	private List<doctorlist> initdata() {
		Docdatahelper docdatahelper = Docdatahelper.getdoc(getActivity());
		ArrayList<Docdatabase> dlist = (ArrayList<Docdatabase>) docdatahelper.DocdataDao().getThese();
		doclist = new ArrayList<>();
		for (int i = 0; i < dlist.size(); i++) {
			doclist.add(new doctorlist(dlist.get(i).getFullname(), dlist.get(i).getDesignation(), dlist.get(i).getDistrict(), dlist.get(i).getSpecializtion()));
		}
		return doclist;
	}
}