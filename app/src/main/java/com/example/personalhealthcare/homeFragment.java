package com.example.personalhealthcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class homeFragment extends Fragment {
	LocationRequest locationRequest;
	LocationCallback locationCallback;
	Calendar calendar;
	String address;
	String country;
	String city;
	String longitude;
	String latitude;
	TextView User_name, user, phone;
	FirebaseUser users_firebase;

	CountDownLatch latch = new CountDownLatch(1);
	LocationManager locmanager;
	ImageButton cancelappoint;

	ImageButton availdoc;
	String name = "";
	Executor executor = Executors.newFixedThreadPool(4);
	SwipeRefreshLayout swipeRefreshLayout;
	DatabaseReference databaseReferenc;
	DatabaseReference databaseReference;
	ValueEventListener fireapp;
	private availdoctor avildocfragment;
	private appointmentFragment appointmentFragment;
	private patient_appointment patient_appointmen;
	private FusedLocationProviderClient flpc;
	private FirebaseAuth mAuth;
	private ValueEventListener eventdown2;
	private ValueEventListener down;


	//module for phno
	public void findphno(String uid) {
		final String[] phn = new String[1];
		SharedPreferences.Editor editor = getActivity().getSharedPreferences("secondnot", Context.MODE_PRIVATE).edit();
		FirebaseDatabase.getInstance().getReference("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					phn[0] = snapshot.child("Phone").getValue(String.class);
					editor.putString("phno", phn[0]).apply();
				} else {
//					Toast.makeText(context, "no data snapshot", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.d("phone", error.getMessage());
			}
		});
	}

	//module appointment firing
	public void fireappoint() {
		calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DATE);
		int todaymth = calendar.get(Calendar.MONTH) + 1;
		SharedPreferences sh = getActivity().getSharedPreferences("secondnot", Context.MODE_PRIVATE);
		String docid = sh.getString("docuid", "");
		Log.d("I am at", " starting fireapp");
		if (!docid.isEmpty()) {
			findphno(docid);
			DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(docid).child("appoinment");
			fireapp = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					Log.d("I am at", " inside ");
					if (snapshot.exists()) {
						Map<String, String> appoin = (Map<String, String>) snapshot.getValue();
						if (appoin != null) {
							for (Map.Entry<String, String> entry : appoin.entrySet()) {
								Log.d("I am at", " loop");
								String[] date = entry.getValue().split("\\+\\+");
								String[] datee = date[2].split("-");
								if (Integer.parseInt(datee[1]) > todaymth) {
									String finalAll = entry.getValue();
									Log.d("appoint", finalAll);
									AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
									Intent intent = new Intent(getActivity(), notfication.class);
									intent.putExtra("action", "appoint");
									SharedPreferences.Editor editor = getActivity().getSharedPreferences("secondnot", Context.MODE_PRIVATE).edit();
									editor.putString("date", finalAll);
									editor.apply();

									PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
									Calendar calendernot = Calendar.getInstance();
									calendernot.set(Calendar.HOUR_OF_DAY, 22);
									String[] cutdate = date[2].split("-");
									int numb = Integer.parseInt(cutdate[1]);
									int val = numb - 1;
									calendernot.set(Integer.parseInt(cutdate[2]), val, Integer.parseInt(cutdate[0]) - 1);
									long time = calendernot.getTimeInMillis();
									Log.d("I am at", " alrm manager1");
									alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
								}
								if (Integer.parseInt(datee[1]) == todaymth) {
									if (Integer.parseInt(datee[0]) == (today + 1)) {
										String finalAll = entry.getValue();

										AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
										Intent intent = new Intent(getActivity(), notfication.class);
										intent.putExtra("action", "appoint");
										SharedPreferences.Editor editor = getActivity().getSharedPreferences("secondnot", Context.MODE_PRIVATE).edit();
										editor.putString("date", finalAll);
										editor.apply();

										PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
										Calendar calendernot = Calendar.getInstance();
										calendernot.set(Calendar.HOUR_OF_DAY, 22);
										String[] cutdate = date[2].split("-");
										int numb = Integer.parseInt(cutdate[1]);
										int val = numb - 1;
										calendernot.set(Integer.parseInt(cutdate[2]), val, Integer.parseInt(cutdate[0]) - 1);
										long time = calendernot.getTimeInMillis();
										Log.d("I am at", " alrm manager");
										alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
									}
								}
							}
						}
					}
					databaseReference1.removeEventListener(fireapp);
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			};
			databaseReference1.addValueEventListener(fireapp);
		} else {
			Log.w("not setted any appointment", "not yet");
		}
	}

	// modlues for location
	public void locationinfo() {
		try {
			if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
				ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
			}
			locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Enable GPS").setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						latch.countDown();
					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						Toast.makeText(getActivity(), "canceled", Toast.LENGTH_SHORT).show();
						latch.countDown();
					}
				}).setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						latch.countDown();
					}
				});
				final AlertDialog alertDialog = builder.create();
				alertDialog.show();
			} else {
				latch.countDown();
			}
		} catch (Exception e) {
			Log.println(Log.ERROR, null, " exception" + e);
		}
	}

	// location updation for ever 5 sec
	public void locationrefresh() {
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(5000); // Update interval in milliseconds (e.g., every 5 seconds)
		locationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				if (locationResult != null) {
					Location location = locationResult.getLastLocation();
					// Process the received location
				}
			}
		};

	}

	//location accaess
	public void location() {
		locationrefresh();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			flpc = LocationServices.getFusedLocationProviderClient(getActivity());
			flpc.requestLocationUpdates(locationRequest, locationCallback, null);

			flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
				@Override
				public void onSuccess(Location location) {
					if (location != null) {
						Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
						try {
							List<Address> loclist = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
							latitude = String.valueOf(loclist.get(0).getLatitude());
							longitude = String.valueOf(loclist.get(0).getLongitude());
							address = String.valueOf(loclist.get(0).getAddressLine(0));
							country = String.valueOf(loclist.get(0).getCountryName());
							city = String.valueOf(loclist.get(0).getLocality());
							latch.countDown();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}

					} else {
//                        Toast.makeText(getActivity(), "null location", Toast.LENGTH_SHORT).show();
					}
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.d(" location", "location failed reason  " + e);

				}
			});
		} else {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
		}
	}

	// module for sms
	public void smssender(String pno, String txtxmsg) {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
		}
		SmsManager.getDefault().sendTextMessage(pno, null, txtxmsg, null, null);
//        Toast.makeText(getActivity(), "SMS Sent", Toast.LENGTH_SHORT).show();
	}

	// module for phone call
	public void phonecall(String who, String pno, String txtmsg) {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Are you sure, You want to call " + who);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

//                Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
				if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
				}
				Intent phone = new Intent(Intent.ACTION_CALL);
				phone.setData(Uri.parse("tel:" + pno));
				startActivity(phone);
				smssender(pno, txtmsg);
			}
		});
		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Toast.makeText(getActivity(), "canceled", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);
		FloatingActionButton actionButton112 = view.findViewById(R.id.floatingActionButton2);
		FloatingActionButton actionButton108 = view.findViewById(R.id.floatingActionButton);
		FloatingActionButton actionButton100 = view.findViewById(R.id.floatingActionButton3);
		InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), 0);
//		if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
//			actionButton100.setEnabled(true);
//			actionButton112.setEnabled(true);
//			actionButton108.setEnabled(true);
//		} else {
//			actionButton100.setEnabled(false);
//			actionButton108.setEnabled(false);
//			actionButton112.setEnabled(false);
//		}
		swipeRefreshLayout = view.findViewById(R.id.refresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						datadowloadevent();
						datadowload();
						Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 3000);

			}
		});
		fireappoint();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                datadowload();
//                datadowloadevent();
//                locationrefresh();
//            }
//        }).start();
//        location();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				User_name = (TextView) view.findViewById(R.id.name);
				user = (TextView) view.findViewById(R.id.user);
				phone = (TextView) view.findViewById(R.id.inphonenumber);
				users_firebase = FirebaseAuth.getInstance().getCurrentUser();
				SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
				name = sh.getString("Names", "");
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						User_name.setText(name);
						phone.setText(sh.getString("Phoneno", ""));
						user.setText(sh.getString("User", ""));
					}
				});

			}
		});


		ImageButton botbutton = view.findViewById(R.id.botbutton);
		botbutton.setOnClickListener(view1 -> {
//            Toast.makeText(getActivity(), "bot button pressed", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getActivity(), chat.class));
		});
		ImageButton appointment = view.findViewById(R.id.appointment);
		appointment.setOnClickListener(view12 -> {
//            Toast.makeText(getActivity(), "appointment button pressed", Toast.LENGTH_SHORT).show();
			appointmentFragment = new appointmentFragment();
			requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, appointmentFragment).addToBackStack("appointment").commit();
		});
		availdoc = view.findViewById(R.id.availdoc);
		availdoc.setOnClickListener(v -> {
			avildocfragment = new availdoctor();
			requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, avildocfragment).addToBackStack("available doctor").commit();
		});
		cancelappoint = view.findViewById(R.id.cancelappoint);
		cancelappoint.setOnClickListener(v -> {
			patient_appointmen = new patient_appointment();
			requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, patient_appointmen).addToBackStack("cancel appointment").commit();
		});
		actionButton108.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					actionButton100.setEnabled(true);
					actionButton112.setEnabled(true);
					actionButton108.setEnabled(true);

					String who = "108";
					String pno = "Phone No";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
								locationinfo();
								Looper.loop();
							}
						});
						imp.setPriority(7);
						imp.start();
						try {
							latch.await();

						} catch (Exception e) {
							Log.d("thread", String.valueOf(e));
						}
					} else {
						Thread next = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								location();
								String txtmsg = latitude + " " + longitude + "\n " + address + " " + city + " " + country;
								phonecall(who, pno, txtmsg);
								Looper.loop();
							}
						});
						next.setPriority(3);
						next.start();
					}
				} else {
					actionButton100.setEnabled(false);
					actionButton108.setEnabled(false);
					actionButton112.setEnabled(false);
				}
			}
		});


		actionButton112.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					actionButton100.setEnabled(true);
					actionButton112.setEnabled(true);
					actionButton108.setEnabled(true);

					String who = "112";
					String pno = "Phone No";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
								locationinfo();
								Looper.loop();
							}
						});
						imp.setPriority(7);
						imp.start();
						try {
							latch.await();

						} catch (Exception e) {
							Log.d("thread", String.valueOf(e));
						}
					} else {
						Thread next = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								location();
								String txtmsg = latitude + " " + longitude + "\n " + address + " " + city + " " + country;
								phonecall(who, pno, txtmsg);
								Looper.loop();
							}
						});
						next.setPriority(3);
						next.start();
					}
				} else {
					actionButton100.setEnabled(false);
					actionButton108.setEnabled(false);
					actionButton112.setEnabled(false);
				}
			}
		});


		actionButton100.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					actionButton100.setEnabled(true);
					actionButton112.setEnabled(true);
					actionButton108.setEnabled(true);
					String who = "100";
					String pno = "Phone No";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
								locationinfo();
								Looper.loop();
							}
						});
						imp.setPriority(7);
						imp.start();
						try {
							latch.await();

						} catch (Exception e) {
							Log.d("thread", String.valueOf(e));
						}
					} else {
						Thread next = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
								location();
								String txtmsg = latitude + " " + longitude + "\n " + address + " " + city + " " + country;
								phonecall(who, pno, txtmsg);
								Looper.loop();
							}
						});
						next.setPriority(3);
						next.start();
					}
				} else {
					actionButton100.setEnabled(false);
					actionButton108.setEnabled(false);
					actionButton112.setEnabled(false);
				}
			}
		});

		return view;
	}

	private void datadowloadevent() {
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			// User is signed in
			SharedPreferences.Editor editor = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE).edit();
			Calendar calendar = Calendar.getInstance();
			int today = calendar.get(Calendar.DATE);
			int todaymth = calendar.get(Calendar.MONTH) + 1;

			eventdown2 = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					Map<String, String> allEntries = (Map<String, String>) dataSnapshot.getValue();
//                    Log.d("dowload data", String.valueOf(allEntries));
					if (allEntries != null) {
						editor.clear();
						editor.apply();
						for (Map.Entry<String, String> entry : allEntries.entrySet()) {
							editor.putString(entry.getKey(), String.valueOf(entry.getValue()));
							editor.apply();
							String v = entry.getKey();
							if (v.length() == 8) {
								int date1 = Integer.parseInt(v.substring(0, 2));
								int mont = Integer.parseInt(v.substring(2, 4));
//								Toast.makeText(getActivity(), String.valueOf(date1), Toast.LENGTH_SHORT).show();
								if (date1 < today) {
									if (mont <= todaymth) {
										dataSnapshot.getRef().child(v).removeValue();
									}
								}
								if (mont < todaymth) {
									dataSnapshot.getRef().child(v).removeValue();
								}
							} else if (v.length() == 7) {
								int date1 = Integer.parseInt(v.substring(0, 2));
								int mont = Integer.parseInt(v.substring(2, 3));
//								Toast.makeText(getActivity(), String.valueOf(mont), Toast.LENGTH_SHORT).show();
								if (date1 < today) {
									if (mont <= todaymth) {
										dataSnapshot.getRef().child(v).removeValue();
									}
								}
								if (mont < todaymth) {
									dataSnapshot.getRef().child(v).removeValue();
								}
							} else if (v.length() == 6) {
								int date1 = Integer.parseInt(v.substring(0, 1));
								int mont = Integer.parseInt(v.substring(1, 2));
								Toast.makeText(getActivity(), String.valueOf(mont), Toast.LENGTH_SHORT).show();
								if (date1 < today) {
									if (mont <= todaymth) {
										dataSnapshot.getRef().child(v).removeValue();
									}
								}
								if (mont < todaymth) {
									dataSnapshot.getRef().child(v).removeValue();
								}
							} else {
//								Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
							}
						}
						editor.apply();

					} else {
//						Toast.makeText(getActivity(), "first time user", Toast.LENGTH_SHORT).show();
					}
					// for accessing the appointment set by doctors
					// User is signed in
//            Log.d("user", String.valueOf(currentUser));
					databaseReferenc = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("fixappoinment");
					databaseReferenc.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							Map<String, String> allEntries = (Map<String, String>) dataSnapshot.getValue();
							if (allEntries != null) {
								SharedPreferences.Editor editor = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE).edit();
								for (Map.Entry<String, String> entry : allEntries.entrySet()) {
									String key1 = String.valueOf(entry.getKey()).replace("-", "");
									String content[] = String.valueOf(entry.getValue()).split("\\+\\+");
									FirebaseDatabase.getInstance().getReference("User").child(content[1]).addListenerForSingleValueEvent(new ValueEventListener() {
										@Override
										public void onDataChange(@NonNull DataSnapshot snapshot) {
											if (snapshot.exists()) {
												String docname = snapshot.child("Fullname").getValue(String.class);
												if (docname.isEmpty()) {
//													Toast.makeText(getActivity(), "docname is emty", Toast.LENGTH_SHORT).show();
												} else {
													if (!content[0].isEmpty()) {
														String total = content[0] + " By Dr." + docname;
														editor.putString(key1, total);
														editor.apply();
//                                                                Log.d("dowload data", String.valueOf(total));
													}
												}
											} else {
//												Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
											}
										}

										@Override
										public void onCancelled(@NonNull DatabaseError error) {
											Log.d("data error", String.valueOf(error));
										}
									});
									String v = entry.getKey();
									if (v.length() == 8) {
										int date1 = Integer.parseInt(v.substring(0, 2));
										int mont = Integer.parseInt(v.substring(2, 4));
										if (date1 < today) {
											if (mont <= todaymth) {
												dataSnapshot.getRef().child(v).removeValue();
											}
										}
										if (mont < todaymth) {
											dataSnapshot.getRef().child(v).removeValue();
										}
									} else if (v.length() == 7) {
										int date1 = Integer.parseInt(v.substring(0, 2));
										int mont = Integer.parseInt(v.substring(2, 3));
//								Toast.makeText(getActivity(), String.valueOf(mont), Toast.LENGTH_SHORT).show();
										if (date1 < today) {
											if (mont <= todaymth) {
												dataSnapshot.getRef().child(v).removeValue();
											}
										}
										if (mont < todaymth) {
											dataSnapshot.getRef().child(v).removeValue();
										}
									} else if (v.length() == 6) {
										int date1 = Integer.parseInt(v.substring(0, 1));
										int mont = Integer.parseInt(v.substring(1, 2));
										if (date1 < today) {
											if (mont <= todaymth) {
												dataSnapshot.getRef().child(v).removeValue();
											}
										}
										if (mont < todaymth) {
											dataSnapshot.getRef().child(v).removeValue();
										}
									} else {
//										Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
									}
								}
								editor.apply();
//                                        Map<String, ?> a=getSharedPreferences("events",MODE_PRIVATE).getAll();
//                                        Log.d("all data", a.toString());
//								Toast.makeText(getActivity(), "dowloaded succesfully", Toast.LENGTH_SHORT).show();
							} else {
//								Toast.makeText(getActivity(), "first time user", Toast.LENGTH_SHORT).show();
							}
							databaseReference.removeEventListener(eventdown2);
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							// Handle error
							Log.d("database error", String.valueOf(databaseError));
						}
					});

				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					// Handle error
					Log.d("database error", String.valueOf(databaseError));
				}
			};
			Log.d("user", String.valueOf(currentUser));
			databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("total event");
			databaseReference.addValueEventListener(eventdown2);
//
		} else {
			Log.d("no user", String.valueOf(currentUser));
		}

	}


	private void datadowload() {
		databaseReference = FirebaseDatabase.getInstance().getReference();
		Query query = databaseReference.child("User").orderByChild("vaild").equalTo(true);
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			// User is signed in
			Log.d("user", String.valueOf(currentUser));
		} else {
			Log.d("null user", String.valueOf(currentUser));
		}
		down = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Docdatahelper dao = Docdatahelper.getdoc(getActivity());
				dao.clearAllTables();
				ArrayList<Docdatabase> data = new ArrayList<>();
				for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
					if (childsnapshot.exists()) {
						Docdatabase dataa = childsnapshot.getValue(Docdatabase.class);
						if (dataa != null) {
							data.add(dataa);

						} else {
//							Toast.makeText(getActivity(), "no doctors available", Toast.LENGTH_SHORT).show();
						}
					} else {
//						Toast.makeText(getActivity(), "no childsnapshot is available", Toast.LENGTH_SHORT).show();
					}
				}
				Log.d("data", String.valueOf(data));
				dao.DocdataDao().insertall(data);
//				Toast.makeText(getActivity(), "docdata updated", Toast.LENGTH_SHORT).show();
				query.removeEventListener(down);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Handle error
				Log.d("database error", String.valueOf(databaseError));
			}
		};

		query.addValueEventListener(down);
	}

	public void abc() {
		if (users_firebase != null) {
			DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
			String userid = users_firebase.getUid();
			reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					User userprofile = snapshot.getValue(User.class);

					if (userprofile != null) {
						String namess = userprofile.Fullname + " " + userprofile.Lastname;
						String userphone = userprofile.Phone;
						User_name.setText(namess);
						phone.setText(userphone);
						user.setText(userprofile.user);
						name = namess;
						SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
						edit.putString("Names", namess);
						edit.putString("User", userprofile.user);
						edit.putString("Phoneno", userphone);
						edit.putString("email", userprofile.email);
						edit.apply();
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
				}
			});

		}
	}
}





