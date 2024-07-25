package com.example.personalhealthcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class itemadapter extends RecyclerView.Adapter<itemadapter.ViewHolder> {
	Context context;
	ArrayList<appointments> appointments = new ArrayList<>();
	String uid;


	public itemadapter(Context context, ArrayList<appointments> appointments, String uid) {
		this.context = context;
		this.appointments = appointments;
		this.uid = uid;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v;
		v = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}


	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
		holder.txtname.setText(appointments.get(position).name);
		holder.txtage.setText(appointments.get(position).age);
		String senddate = appointments.get(position).date;
		String datee = appointments.get(position).date.replace("-", "");
		String prob = appointments.get(position).problem + "++" + FirebaseAuth.getInstance().getCurrentUser().getUid();
		holder.txtdate.setText(appointments.get(position).date);
		holder.txtprob.setText(appointments.get(position).problem);
		String all = appointments.get(position).name + "++" + appointments.get(position).age +
				 "++" + appointments.get(position).date + "++" + appointments.get(position).problem + "++";
		SharedPreferences sh = context.getSharedPreferences("Number", Context.MODE_PRIVATE);
		holder.decline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder;
				builder = new AlertDialog.Builder(context);
				builder.setTitle("Delete!").setMessage("Confirm! Decline the Appointment")
						 .setCancelable(true)
						 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance()
													.getCurrentUser().getUid())
										  .child("appoinment").child(appointments.get(position).uid).removeValue();
								 findphno(appointments.get(position).uid, "The appointment has been declained on " + senddate);
//
							 }
						 }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.cancel();
							 }
						 }).create().show();
			}
		});
		holder.accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder;
				builder = new AlertDialog.Builder(context);
				builder.setTitle("Accept!").setMessage("Confirm! Accept the Appointment")
						 .setCancelable(true)
						 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 //adding to the patient
								 FirebaseDatabase.getInstance().getReference("User").child(appointments.get(position).uid)
										  .child("fixappoinment").child(datee).setValue(prob);

								 //removing node from doc
								 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance()
													.getCurrentUser().getUid())
										  .child("appoinment").child(appointments.get(position).uid).removeValue();

								 //adding to another node

								 FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance()
													.getCurrentUser().getUid())
										  .child("fixedappoitnments").child(appointments.get(position).uid).setValue(all);
								 findphno(appointments.get(position).uid, "appointment has been accepted on " + senddate);
								 AlertDialog.Builder builder;
								 builder = new AlertDialog.Builder(context);
								 builder.setTitle("Appointment Fixed").setMessage("Appointment has been fixed").create().show();
							 }
						 }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								 dialog.cancel();
							 }
						 }).create().show();
			}
		});

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
				Log.d("Databse error", String.valueOf(error));
			}
		});
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView txtname, txtage, txtprob, txtdate, txtnoappointments;
		Button accept, decline;

		public ViewHolder(View itemview) {
			super(itemview);
			txtname = itemview.findViewById(R.id.problem_view);
			txtage = itemview.findViewById(R.id.textView29);
			txtdate = itemview.findViewById(R.id.date_view);
			txtprob = itemview.findViewById(R.id.textView27);
			accept = itemview.findViewById(R.id.button2);
			decline = itemview.findViewById(R.id.button3);

		}
	}

}
