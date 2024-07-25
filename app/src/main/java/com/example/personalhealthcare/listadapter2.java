package com.example.personalhealthcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class listadapter2 extends RecyclerView.Adapter<listadapter2.ViewHolder> {
	String key;
	Context context;
	ArrayList<appointments> appointments = new ArrayList<>();
	String uid;

	public listadapter2(Context context, ArrayList<appointments> appointments, String uid, String key) {
		this.context = context;
		this.appointments = appointments;
		this.uid = uid;
		this.key = key;

	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v;
		v = LayoutInflater.from(context).inflate(R.layout.patient_view, parent, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
		holder.txtname.setText(appointments.get(position).name);
//        String date = appointments.get(position).date;
//        String prob = appointments.get(position).problem + "++" + FirebaseAuth.getInstance().getCurrentUser().getUid();
		holder.txtdate.setText(appointments.get(position).date);
		holder.txtprob.setText(appointments.get(position).problem);
		Toast.makeText(context, "" + appointments.get(position).uid, Toast.LENGTH_SHORT).show();
//        uid = appointments.get(position).uid;

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
										  .child("fixappoinment").child(appointments.get(position).date.replace("-", "")).removeValue();

								 FirebaseDatabase.getInstance().getReference("User").child(key)
										  .child("fixedappoitnments").child(FirebaseAuth.getInstance().getUid()).removeValue();

								 findphno(key, "The appointment has been canceled");
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
				Log.d("database error", error.getMessage());
			}
		});
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView txtname, txtprob, txtdate, txtnoappointments;
		Button decline;

		public ViewHolder(View itemview) {
			super(itemview);
			txtname = itemview.findViewById(R.id.problem_view1);
			txtdate = itemview.findViewById(R.id.date_view2);
			txtprob = itemview.findViewById(R.id.textView270);
			decline = itemview.findViewById(R.id.cancel);

		}
	}
}
