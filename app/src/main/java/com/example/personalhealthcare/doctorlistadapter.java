package com.example.personalhealthcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class doctorlistadapter extends RecyclerView.Adapter<doctorlistadapter.ViewHolder> {

	Uri[] imageuri;
	Context context;
	ArrayList<appointments> appointments = new ArrayList<>();

	public doctorlistadapter(Context context, ArrayList<appointments> appointments) {
		this.context = context;
		this.appointments = appointments;

	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v;
		v = LayoutInflater.from(context).inflate(R.layout.dctor_list, parent, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}


	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
		holder.doc_name.setText(appointments.get(position).name);
		StorageReference storageReference = FirebaseStorage.getInstance().getReference();

		StorageReference st = storageReference.child("registration_images").child(appointments.get(position).registration_no);

		st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
				Picasso.get().load(uri).into(holder.imageView);
			}
		});

		holder.register.setText(appointments.get(position).registration_no);
		holder.accpt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseDatabase.getInstance().getReference("User").child(appointments.get(position).iamgeuri).child("vaild").setValue(true);
				findphno(appointments.get(position).iamgeuri, "You have been verified by Admin. Now you can login");
				Toast.makeText(context, "Doctor has been verified ", Toast.LENGTH_SHORT).show();
				appointments.remove(position);
				notifyItemRemoved(position);
				notifyDataSetChanged();
				notifyItemChanged(position);


			}
		});
		holder.decline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseDatabase.getInstance().getReference("User").child(appointments.get(position).iamgeuri).child("vaild").setValue(false);
				FirebaseDatabase.getInstance().getReference("User").child(appointments.get(position).iamgeuri).child("inprocess").setValue(true);
				Toast.makeText(context, "Doctor has been rejected ", Toast.LENGTH_SHORT).show();
				findphno(appointments.get(position).iamgeuri, "Your verification has been rejected.Contact our admin for further process. Our HelpLine No: Phone No \n Gmail: ");
				appointments.remove(position);
				notifyItemRemoved(position);
				notifyDataSetChanged();
				notifyItemChanged(position);
			}
		});
//		if (position > 0) {
//
//		}
	}

	@Override
	public int getItemCount() {
		return appointments.size();
	}

	public void smssender(String pno, String txtxmsg) {
		if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.SEND_SMS}, 1);
		}
		SmsManager.getDefault().sendTextMessage(pno, null, txtxmsg, null, null);
//		Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();
	}

	public void findphno(String uid, String txtmsg) {
		FirebaseDatabase.getInstance().getReference("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					String phn = snapshot.child("Phone").getValue(String.class);
					if (phn.isEmpty()) {
//						Toast.makeText(context, "empty phn", Toast.LENGTH_SHORT).show();
					} else {
//						Toast.makeText(context, phn, Toast.LENGTH_SHORT).show();
						smssender(phn, txtmsg);
					}
				} else {
//					Toast.makeText(context, "no data snapshot", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.d("Database error", error.getMessage());
			}
		});
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView doc_name;
		Button accpt, decline;
		ImageView imageView;
		TextView register;


		public ViewHolder(View doctorlistadapter) {
			super(doctorlistadapter);
			doc_name = doctorlistadapter.findViewById(R.id.doctor_name);
			accpt = doctorlistadapter.findViewById(R.id.button7);
			decline = doctorlistadapter.findViewById(R.id.button8);
			imageView = doctorlistadapter.findViewById(R.id.imageView5);
			register = doctorlistadapter.findViewById(R.id.register_no_txt);

		}
	}
}
