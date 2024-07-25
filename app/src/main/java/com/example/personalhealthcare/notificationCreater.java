package com.example.personalhealthcare;

import static android.os.Build.VERSION_CODES;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class notificationCreater {
	private static final int NOTIFICATION_ID1 = 1;
	private static final int NOTIFICATION_ID2 = 2;
	private static final String CHANNEL_ID1 = "Event Remainder";
	private static final String CHANNEL_ID2 = "Event Resetter";


	long[] vib = {0, 200, 500, 200, 3000};

	public void createNotification(Context context, String title, String content) {
		createNotificationChannel1(context);
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("action", "1st");
		// Create the PendingIntent
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		// Create notification
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID1);
		builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setContentIntent(pendingIntent);
		builder.setVibrate(vib);
		builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
		builder.setAutoCancel(true);

		// Display notification
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
				ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 3000);
			}
		}
		notificationManager.notify(NOTIFICATION_ID1, builder.build());
	}

	public void newnotification(Context context, String title, String content) {
		createNotificationChannel2(context);
		//notification

		//intents for buttons
		Intent okbutton = new Intent(context, notfication.class);
		okbutton.putExtra("action", "ok");
		PendingIntent pendingok = PendingIntent.getBroadcast(context, 0, okbutton, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		NotificationCompat.Action action1 = new NotificationCompat.Action.Builder(R.drawable.ic_twotone_app_settings_alt_24, "ok", pendingok).build();
		Intent cancelbutton = new Intent(context, notfication.class);
		cancelbutton.putExtra("action", "cancel");
		PendingIntent pendingcancel = PendingIntent.getBroadcast(context, 1, cancelbutton, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		NotificationCompat.Action action2 = new NotificationCompat.Action.Builder(R.drawable.guardar, "cancel", pendingcancel).build();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID2)
				 .setSmallIcon(R.drawable.ic_baseline_notifications_24)
				 .setContentTitle(title)
				 .setContentText(content)
				 .setVibrate(vib)
				 .setPriority(NotificationCompat.PRIORITY_DEFAULT)
				 .setAutoCancel(false)
				 .setOngoing(true)
				 .addAction(action1)
				 .addAction(action2);

		// notifying the manager
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
				ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 3000);
			}
		}
		notificationManager.notify(NOTIFICATION_ID2, builder.build());
	}

	//creating a channel
	private void createNotificationChannel1(Context context) {
		if (VERSION.SDK_INT >= VERSION_CODES.O) {
			String channelName = "Event remainder";
			int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

			NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, channelName, channelImportance);
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			if (notificationManager != null) {
				notificationManager.createNotificationChannel(channel);
			}
		}
	}

	private void createNotificationChannel2(Context context) {
		if (VERSION.SDK_INT >= VERSION_CODES.O) {
			String channelName = "Event resetter";
			int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

			NotificationChannel channel = new NotificationChannel(CHANNEL_ID2, channelName, channelImportance);
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			if (notificationManager != null) {
				notificationManager.createNotificationChannel(channel);
			}
		}
	}
}
