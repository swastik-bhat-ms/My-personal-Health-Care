package com.example.personalhealthcare;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notfication extends BroadcastReceiver {
	private static final int NOTIFICATION_ID2 = 2;
	public String event;
	public String event2;
	ValueEventListener listener;
	ValueEventListener listener2;
	String title = "My Application";
	Context cont;
	String phno;
	String finalall;
	DatabaseReference reference;
	String id;
	String action;
	notificationCreater notificationCreater = new notificationCreater();

	@Override
	public void onReceive(Context context, Intent intent) {
		cont = context;
		action = intent.getStringExtra("action");
//        id = intent.getStringExtra("docuid");
//        finalall=intent.getStringExtra("date");
//        phno=intent.getStringExtra("phno");
		SharedPreferences sh = context.getSharedPreferences("secondnot", Context.MODE_PRIVATE);
		id = sh.getString("docuid", "");
		finalall = sh.getString("date", "");
		phno = sh.getString("phno", "");
		FirebaseAuth muth = FirebaseAuth.getInstance();
		FirebaseUser user = muth.getCurrentUser();
		assert user != null;
		String cuid = user.getUid();
		Log.d("Received Intent", "Action: " + action);
		Log.d("Received Intent", "ID: " + id);
		Log.d("Received Intent", "Date: " + finalall);
		Log.d("Received Intent", "Phone Number: " + phno);
		if (id != null) {
			reference = FirebaseDatabase.getInstance().getReference("User").child(id).child("appoinment");
		} else {
			Log.d("null", "id of doc");
		}
		if (action.equals("1st")) {
			SharedPreferences sharedPreferences = context.getSharedPreferences("eventss", Context.MODE_PRIVATE);
			event = sharedPreferences.getString("event", "");
			// Create and display the notification
			notificationCreater.createNotification(context, title, event);
		} else if (action.equals("2nd")) {
			SharedPreferences sharedPreferences = context.getSharedPreferences("eventss", Context.MODE_PRIVATE);
			event2 = sharedPreferences.getString("event2", "");
			// Create and display the notification
			notificationCreater.createNotification(context, title, event2);
		} else if (action.equals("appoint")) {
			listener = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					if (snapshot.exists()) {
						notificationCreater.newnotification(context.getApplicationContext(), "Personal Health Care", "Doctore still not confirmed your request, do you want to postpone this appointment to the next day ?");
					} else {
						Log.d("snapshot ", "not exist");
					}
					reference.removeEventListener(listener);
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					Log.d("database error in broadcast", String.valueOf(error));
				}
			};
			reference.child(cuid).addListenerForSingleValueEvent(listener);
		}
//            Toast.makeText(context, "nulll action", Toast.LENGTH_SHORT).show();
		if (action.equals("ok")) {
			listener2 = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					String[] date = finalall.split("\\+\\+");
					String[] cdate = date[2].split("-");
					int nextdate = Integer.parseInt(cdate[0]) + 1;
					String nextrealdate = nextdate + "-" + cdate[1] + "-" + cdate[2];
					String all;
					if (date.length == 3) {
						all = date[0] + "++" + date[1] + "++" + nextrealdate + "++";
					} else {
						all = date[0] + "++" + date[1] + "++" + nextrealdate + "++" + date[3] + "++" + date[4] + "++" + date[5] + "++";
					}
					Log.d("Received Intent", "Action: " + action);
					Log.d("Received Intent", "ID: " + id);
					Log.d("Received Intent", "Date: " + finalall);
					Log.d("Received Intent", "Phone Number: " + phno);
					reference.child(cuid).setValue(all).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							try {
								if (task.isSuccessful()) {
									smssender(phno, "The Patient have extended the request of appointment");
								} else {
//									Toast.makeText(context, "task is unsuccesful", Toast.LENGTH_SHORT).show();
								}

							} catch (Exception exe) {
//								Toast.makeText(context, "error while completting", Toast.LENGTH_SHORT).show();
							}
						}
					});
					reference.removeEventListener(listener2);
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			};
			reference.addListenerForSingleValueEvent(listener2);
			NotificationManagerCompat notman = NotificationManagerCompat.from(context);
			notman.cancel(NOTIFICATION_ID2);

		} else if (action.equals("cancel")) {
			reference.child(cuid).removeValue();
			Log.d("Received Intent", "Action: " + action);
			Log.d("Received Intent", "ID: " + id);
			Log.d("Received Intent", "Date: " + finalall);
			Log.d("Received Intent", "Phone Number: " + phno);
			NotificationManagerCompat notman = NotificationManagerCompat.from(context);
			notman.cancel(NOTIFICATION_ID2);
		}

	}

	public void smssender(String pno, String txtxmsg) {
		if (ContextCompat.checkSelfPermission(cont, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) cont, new String[]{Manifest.permission.SEND_SMS}, 1);
		}
		SmsManager.getDefault().sendTextMessage(pno, null, txtxmsg, null, null);
//        Toast.makeText(getActivity(), "SMS Sent", Toast.LENGTH_SHORT).show();
	}

}
