package com.example.personalhealthcare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class notificationFragment extends Fragment {
	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	TextView textView5;
	Intent intent;
	//    SharedPreferences sh=getActivity().getSharedPreferences("details",Context.MODE_PRIVATE);
//    String user= sh.getString("User","");
	String alrmevent = "";
	String alrmevent2 = "";

	public String fetchevent(String currentdate) {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("docevent", Context.MODE_PRIVATE);
		String docevent = sharedPreferences.getString(currentdate, "");
		return docevent;
	}

	private String alreadysavedevent(String saveddate) {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE);
		String event = sharedPreferences.getString(saveddate, "");
		return event;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_notification, container, false);
		textView1 = view.findViewById(R.id.textView2);
		textView2 = view.findViewById(R.id.textView3);
		textView3 = view.findViewById(R.id.textView4);
		textView4 = view.findViewById(R.id.textView5);
		textView5 = view.findViewById(R.id.textView6);
		Calendar calendar = Calendar.getInstance();
		String tonotevent;
		int noofevent = 1;
		int eventcount = 0;
		SharedPreferences edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		String user = edit.getString("User", "");
		if (user.equals("Patient")) {
			for (int i = 0; i <= noofevent; i++, noofevent++) {
				int day = calendar.get(Calendar.DATE);
				int mth = calendar.get(Calendar.MONTH);
				int yer = calendar.get(Calendar.YEAR);
				long maxdate = calendar.getMaximum(Calendar.DATE);
				if ((day + i) > maxdate) {
					break;
				}
				String events = String.valueOf((day + i)) + (mth + 1) + (yer);
				tonotevent = alreadysavedevent(events);

//                    Toast.makeText(getActivity(), events, Toast.LENGTH_SHORT).show();

				String eventday = (day + i) + "/" + (mth + 1) + "/" + yer + "\n" + tonotevent;

				intent = new Intent(requireActivity(), notfication.class);

				if (!tonotevent.isEmpty()) {
					eventcount++;
					if (eventcount == 1) {
						alrmevent = eventday;
						SharedPreferences.Editor editor = (SharedPreferences.Editor) getActivity().getSharedPreferences("eventss", Context.MODE_PRIVATE).edit();
						editor.putString("event", eventday).apply();
						textView1.setText(eventday);
					} else if (eventcount == 2) {
						alrmevent2 = eventday;
						SharedPreferences.Editor editor = (SharedPreferences.Editor) getActivity().getSharedPreferences("eventss", Context.MODE_PRIVATE).edit();
						editor.putString("event2", eventday).apply();
						textView2.setText(eventday);
					} else if (eventcount == 3) {
						textView3.setText(eventday);
					} else if (eventcount == 4) {
						textView4.setText(eventday);
					} else {
						textView5.setText(eventday);
					}
				}

				if (eventcount >= 5) {
					break;
				}

			}
		} else {
			for (int i = 0; i <= noofevent; i++, noofevent++) {
				int day = calendar.get(Calendar.DATE);
				int mth = calendar.get(Calendar.MONTH);
				int yer = calendar.get(Calendar.YEAR);
				long maxdate = calendar.getMaximum(Calendar.DATE);
				if ((day + i) > maxdate) {
					break;
				}
				String events = String.valueOf((day + i)) + (mth + 1) + (yer);
				tonotevent = fetchevent(events);
				String eventday = (day + i) + "/" + (mth + 1) + "/" + yer + "\n" + tonotevent;

				intent = new Intent(requireActivity(), notfication.class);

				if (!tonotevent.isEmpty()) {
					eventcount++;
					if (eventcount == 1) {
						alrmevent = eventday;
						SharedPreferences.Editor editor = (SharedPreferences.Editor) getActivity().getSharedPreferences("eventss", Context.MODE_PRIVATE).edit();
						editor.putString("event", eventday).apply();
						textView1.setText(eventday);
					} else if (eventcount == 2) {
						alrmevent2 = eventday;
						SharedPreferences.Editor editor = (SharedPreferences.Editor) getActivity().getSharedPreferences("eventss", Context.MODE_PRIVATE).edit();
						editor.putString("event2", eventday).apply();
						textView2.setText(eventday);
					} else if (eventcount == 3) {
						textView3.setText(eventday);
					} else if (eventcount == 4) {
						textView4.setText(eventday);
					} else {
						textView5.setText(eventday);
					}
				}

				if (eventcount >= 5) {
					break;
				}
			}

		}
		Calendar now = Calendar.getInstance();
		Calendar next = Calendar.getInstance();
		if (!alrmevent.isEmpty()) {
			String[] ev = alrmevent.split("/");
			now.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ev[0]) - 1);
			next.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ev[0]));
			now.set(Calendar.MONTH, Integer.parseInt(ev[1]) - 1);
			next.set(Calendar.MONTH, Integer.parseInt(ev[1]) - 1);
			now.set(Calendar.HOUR_OF_DAY, 19);
			now.set(Calendar.MINUTE, 0);
			next.set(Calendar.HOUR_OF_DAY, 8);
			next.set(Calendar.MINUTE, 0);
			Calendar cal = Calendar.getInstance();
//          Specify the desired time for the notification (in milliseconds)
//          Toast.makeText(getActivity(), ev[1], Toast.LENGTH_SHORT).show();
			long notificationTime = now.getTimeInMillis(); // Example: 19th hour
			long notificationTime2 = next.getTimeInMillis();
			long abcd = cal.getTimeInMillis();
			// Example: 8 o clock
			AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
			intent = new Intent(getActivity(), notfication.class);
			Intent intents = new Intent(getActivity(), notfication.class);
			intent.putExtra("action", "1st");
			intents.putExtra("action", "1st");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 5, intents, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

			if (alarmManager != null) {
				if (System.currentTimeMillis() < notificationTime) {
					alarmManager.set(AlarmManager.RTC_WAKEUP, abcd, pendingIntent);
				}
				Calendar now2 = Calendar.getInstance();
				Calendar next2 = Calendar.getInstance();
				if (!alrmevent2.isEmpty()) {
					String[] ev2 = alrmevent2.split("/");
					now2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ev2[0]) - 1);
					next2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ev2[0]));
					now2.set(Calendar.MONTH, Integer.parseInt(ev2[1]) - 1);
					next2.set(Calendar.MONTH, Integer.parseInt(ev2[1]) - 1);
					now2.set(Calendar.HOUR_OF_DAY, 19);
					now2.set(Calendar.MINUTE, 0);
					next2.set(Calendar.HOUR_OF_DAY, 8);
					next2.set(Calendar.MINUTE, 0);
					long notificationTime3 = now2.getTimeInMillis(); // Example: 19th hour
					long notificationTime4 = next2.getTimeInMillis();
//					long abcde = cal.getTimeInMillis();
					// Example: 8 o clock
					AlarmManager alarmManager2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
					Intent intent2 = new Intent(getActivity(), notfication.class);
					Intent intent3 = new Intent(getActivity(), notfication.class);
					intent2.putExtra("action", "2nd");
					intent3.putExtra("action", "2nd");
					PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getActivity(), 6, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
					PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getActivity(), 7, intent3, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

					if (alarmManager != null) {
						if (System.currentTimeMillis() < notificationTime3) {
							alarmManager2.set(AlarmManager.RTC_WAKEUP, abcd, pendingIntent3);
//							Toast.makeText(getActivity(), "notification", Toast.LENGTH_SHORT).show();
						}
						if (System.currentTimeMillis() < notificationTime4) {
							alarmManager2.set(AlarmManager.RTC_WAKEUP, abcd, pendingIntent4);
//							Toast.makeText(getActivity(), "notification2", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Log.w("string", "empty");
				}
				if (System.currentTimeMillis() < notificationTime2) {
					alarmManager.set(AlarmManager.RTC_WAKEUP, abcd, pendingIntent2);
				}
//              Toast.makeText(getActivity(), "notification", Toast.LENGTH_SHORT).show();
			}
		} else {
//			Toast.makeText(getActivity(), "no event", Toast.LENGTH_SHORT).show();
		}


		return view;
	}
}
