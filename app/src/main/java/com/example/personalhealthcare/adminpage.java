package com.example.personalhealthcare;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminpage extends AppCompatActivity {
	DatabaseReference reference;
	ValueEventListener querylist;
	private ArrayList<String> lists = new ArrayList<>();
	private ArrayList<appointments> appointmentsArrayList = new ArrayList<>();
	doctorlistadapter itemadapter = new doctorlistadapter(adminpage.this, appointmentsArrayList);
	private TextView textView35;
	private RecyclerView recycleview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adminpage);

		textView35 = findViewById(R.id.textView35);
		reference = FirebaseDatabase.getInstance().getReference();
		recycleview = findViewById(R.id.list);
		recycleview.setLayoutManager(new LinearLayoutManager(adminpage.this));
		recycleview.setAdapter(new doctorlistadapter(adminpage.this, appointmentsArrayList));
		Query query = reference.child("User").orderByChild("vaild").equalTo(false);
		ArrayList<Docdatabase> arrayList = new ArrayList<>();

		querylist = (new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Docdatabase data;
				itemadapter.notifyDataSetChanged();
				Docdatahelper sort = Docdatahelper.getdoc(adminpage.this);
				sort.clearAllTables();
				List<Docdatabase> da = new ArrayList<>();
				for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
					if (dataSnapshot.exists()) {
						data = dataSnapshot.getValue(Docdatabase.class);
						if (data != null) {
							arrayList.add(data);
							lists.add(dataSnapshot.getKey());
						}
					} else {
//							Toast.makeText(adminpage.this, "Null", Toast.LENGTH_SHORT).show();
					}

				}//for
				sort.DocdataDao().insertall(arrayList);
				da = sort.DocdataDao().getThese();
				final Uri[] imageuri = new Uri[da.size()];
				//Toast.makeText(adminpage.this, "1", Toast.LENGTH_SHORT).show();
				for (int i = 0; i < da.size(); i++) {
					if (da.get(i).getInprocess() == false)
						appointmentsArrayList.add(new appointments(da.get(i).getFullname(), da.get(i).getRegisteration_no_string(), lists.get(i), false));

				}
				query.removeEventListener(querylist);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
		query.addValueEventListener(querylist);

//        datadowload();
		setdoctorlist();


	}

	private void setdoctorlist() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {


				if (appointmentsArrayList.size() == 0) {
					textView35.setVisibility(View.VISIBLE);
				}
				//progressBar.setVisibility(View.GONE);
				recycleview.setAdapter(itemadapter);
				itemadapter.notifyDataSetChanged();

			}
		}, 3000);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("log out ", "Admin");
		FirebaseAuth.getInstance().signOut();
	}
}