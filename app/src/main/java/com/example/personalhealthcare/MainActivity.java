package com.example.personalhealthcare;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
	homeFragment homeFragment = new homeFragment();
	FirebaseAuth login;
	dochomepageFragment docFragment = new dochomepageFragment();
	settings settings = new settings();
	DatabaseReference reference;
	FirebaseUser users_firebase;
	String userid = "", name = "", user = "";
	String address;
	String country;
	String city;
	String longitude;
	String latitude;
	CountDownLatch latch = new CountDownLatch(1);
	notificationFragment notefra = new notificationFragment();
	BottomNavigationView bottomnav;
	FusedLocationProviderClient flpc;
	Executor executor = Executors.newFixedThreadPool(4);
	patient_form patient_form = new patient_form();
	Calendar calendar;
	SwipeRefreshLayout swipeRefreshLayout;
	ValueEventListener appointlistner;
	ValueEventListener fixappinlistner;
	private FirebaseAuth mAuth;
	private DatabaseReference databaseReference;
	private DatabaseReference databaseReferenc;
	private LocationRequest locationRequest;
	private LocationCallback locationCallback;
	private ValueEventListener eventdown2;
	private ValueEventListener down;
	private ValueEventListener eventdown;
	private ValueEventListener getdata;

	@SuppressLint("NonConstantResourceId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
		InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		setContentView(R.layout.activity_main);
		calendar = Calendar.getInstance();
		FirebaseMessaging.getInstance().subscribeToTopic("notification");
		try {

			SharedPreferences sh = MainActivity.this.getSharedPreferences("details", Context.MODE_PRIVATE);
			name = sh.getString("Names", "");
			user = sh.getString("User", "");
			users_firebase = FirebaseAuth.getInstance().getCurrentUser();
			if (user.isEmpty()) {
				if (users_firebase != null) {
					reference = FirebaseDatabase.getInstance().getReference("User");
					userid = users_firebase.getUid();
					getdata = new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							User userprofile = snapshot.getValue(User.class);
							if (userprofile != null) {
								String namess = userprofile.Fullname + " " + userprofile.Lastname;
								if (namess.isEmpty()) {
									getSupportFragmentManager().beginTransaction().replace(R.id.container, patient_form).commit();
									getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
								}
								String userphone = userprofile.Phone;
								name = namess;
								user = userprofile.user;
								SharedPreferences.Editor edit = MainActivity.this.getSharedPreferences("details", Context.MODE_PRIVATE).edit();
								edit.putString("Names", namess);
								edit.putString("User", userprofile.user);
								edit.putString("Phoneno", userphone);
								edit.putString("email", userprofile.email);
								edit.apply();
								Toast.makeText(MainActivity.this, "" + user, Toast.LENGTH_SHORT).show();
								reference.removeEventListener(getdata);
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {
//							Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
						}
					};
					reference.child(userid).addListenerForSingleValueEvent(getdata);

				}
			}  //                Toast.makeText(this, "slowing", Toast.LENGTH_SHORT).show();

//			getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//			if (user.equalsIgnoreCase("Doctor")) {
//				datadocevent();
//				getSupportFragmentManager().beginTransaction().replace(R.id.container, docFragment).commit();
//			} else if (user.equalsIgnoreCase("Patient")) {
//				datadowload();
//				datadowloadevent();
//				getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//			}
//			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			bottomnav = findViewById(R.id.bottomnavigation);
			if (user.equalsIgnoreCase("Doctor")) {
				datadocevent();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, docFragment).commit();
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			} else if (user.equalsIgnoreCase("admin")) {
				startActivity(new Intent(MainActivity.this, adminpage.class));
				finish();
			} else {
				datadowload();
				datadowloadevent();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
			bottomnav.setOnItemSelectedListener((MenuItem item) -> {
				if (item.getItemId() == R.id.home && user.equalsIgnoreCase("Doctor")) {
					getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					getSupportFragmentManager().beginTransaction().replace(R.id.container, docFragment).commit();
					return true;
				}
				if (item.getItemId() == R.id.home && user.equalsIgnoreCase("Patient")) {
					getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).addToBackStack(null).commit();
					return true;
				}
				if (item.getItemId() == R.id.notification) {
					getSupportFragmentManager().beginTransaction().replace(R.id.container, notefra).addToBackStack(null).commit();
					return true;
				}
				if (item.getItemId() == R.id.settings) {
					getSupportFragmentManager().beginTransaction().replace(R.id.container, settings).addToBackStack(null).commit();
					return true;
				}
				return false;
			});
		} catch (Exception e) {
			Log.d("main", "botttom nav " + e);
		}
		login = FirebaseAuth.getInstance();
//			}
//		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseApp.initializeApp(this);
		InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		FirebaseUser user = login.getCurrentUser();
		if (user == null || !user.isEmailVerified()) {
			startActivity(new Intent(MainActivity.this, Log_in.class));
			finish();
		}
		executor.execute(new Runnable() {
			@Override
			public void run() {
				InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
				Looper.prepare();
				permission1();
				Looper.loop();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
				Looper.prepare();
				permission2();
				Looper.loop();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
				Looper.prepare();
				permission3();
				Looper.loop();
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				InputMethodManager in = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
				Looper.prepare();
				permission4();
				Looper.loop();
			}
		});
		//locationrefresh();
		location();

	}


	private void datadocevent() {

		mAuth = FirebaseAuth.getInstance();
		int today = calendar.get(Calendar.DATE - 1);
		int todaymth = calendar.get(Calendar.MONTH) + 1;
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			eventdown = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					Map<String, String> allentris = (Map<String, String>) snapshot.getValue();
					SharedPreferences.Editor editor = getSharedPreferences("docevent", Context.MODE_PRIVATE).edit();
					editor.clear();
					editor.apply();
					if (allentris != null) {
						String date = "";
						String events = "";
						for (Map.Entry<String, String> entry : allentris.entrySet()) {
							String total = entry.getValue();
							String[] totaldate = total.split("\\+\\+");
							String gotdate = totaldate[2];
							String[] datee = gotdate.split("-");
							if (date.equals(datee[0] + datee[1] + datee[2])) {
								events = events + "name :" + totaldate[0] + "\n" + "age :" + totaldate[1] + "\n" + "Problem :" + totaldate[3] + "\n";
							} else {
								events = "name :" + totaldate[0] + "\n" + "age :" + totaldate[1] + "\n" + "Problem :" + totaldate[3] + "\n";
							}
							if (Integer.parseInt(datee[0]) < today) {
								if (Integer.parseInt(datee[1]) <= todaymth) {
									snapshot.getRef().child(entry.getKey()).removeValue();
								}
							}
							if (Integer.parseInt(datee[1]) < todaymth) {
								snapshot.getRef().child(entry.getKey()).removeValue();
							}
							date = datee[0] + datee[1] + datee[2];
							editor.putString(date, events);
						}

						editor.apply();
					} else {
//						Toast.makeText(MainActivity.this, "no events to downoload", Toast.LENGTH_SHORT).show();
					}
					editor.apply();

					databaseReference.removeEventListener(eventdown);
					DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("appoinment");
					String uid = currentUser.getUid();
					appointlistner = new ValueEventListener() {
						@SuppressLint("ScheduleExactAlarm")
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							if (snapshot.exists()) {
								Map<String, String> appoin = (Map<String, String>) snapshot.getValue();
								if (appoin != null) {
									for (Map.Entry<String, String> entry : appoin.entrySet()) {
										String[] date = entry.getValue().split("\\+\\+");
										String[] datee = date[2].split("-");
										if (Integer.parseInt(datee[0]) < today) {
											if (Integer.parseInt(datee[1]) <= todaymth) {
												snapshot.getRef().child(entry.getKey()).removeValue();
											}
										}
										if (Integer.parseInt(datee[1]) < todaymth) {
											snapshot.getRef().child(entry.getKey()).removeValue();
										}
									}
								}

							} else {
//								Toast.makeText(MainActivity.this, "no old appoinments", Toast.LENGTH_SHORT).show();
							}
							databaseReference1.removeEventListener(appointlistner);
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {
							Log.e("database error", error.getMessage());
						}
					};
					databaseReference1.addValueEventListener(appointlistner);
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					Log.d("databse eroor", error.getMessage());
				}

			};
			databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("fixedappoitnments");
			databaseReference.addValueEventListener(eventdown);
		} else {
			Log.d("no user", String.valueOf(currentUser));
		}
	}

	//to dowload events and appointments
	private void datadowloadevent() {
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			// User is signed in
			SharedPreferences.Editor editor = getSharedPreferences("events", Context.MODE_PRIVATE).edit();
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

							}
						}
						editor.apply();

					} else {
//						Toast.makeText(MainActivity.this, "first time user", Toast.LENGTH_SHORT).show();
					}
					// for accessing the appointment set by doctors
					// User is signed in
//            Log.d("user", String.valueOf(currentUser));
					databaseReferenc = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("fixappoinment");

					fixappinlistner = new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							Map<String, String> allEntries = (Map<String, String>) dataSnapshot.getValue();
							if (allEntries != null) {
								SharedPreferences.Editor editor = getSharedPreferences("events", Context.MODE_PRIVATE).edit();
								for (Map.Entry<String, String> entry : allEntries.entrySet()) {
									String key1 = String.valueOf(entry.getKey()).replace("-", "");
									String content[] = String.valueOf(entry.getValue()).split("\\+\\+");
									FirebaseDatabase.getInstance().getReference("User").child(content[1]).addListenerForSingleValueEvent(new ValueEventListener() {
										@Override
										public void onDataChange(@NonNull DataSnapshot snapshot) {
											if (snapshot.exists()) {
												String docname = snapshot.child("Fullname").getValue(String.class);
												if (docname.isEmpty()) {
//													Toast.makeText(MainActivity.this, "docname is emty", Toast.LENGTH_SHORT).show();
												} else {
													if (!content[0].isEmpty()) {
														String total = content[0] + " By Dr." + docname;
														editor.putString(key1, total);
														editor.apply();
//                                                                Log.d("dowload data", String.valueOf(total));
													}
												}
											} else {
//												Toast.makeText(MainActivity.this, "empty", Toast.LENGTH_SHORT).show();
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

									}
								}
								editor.apply();
//								Toast.makeText(MainActivity.this, "dowloaded succesfully", Toast.LENGTH_SHORT).show();
							} else {
//								Toast.makeText(MainActivity.this, "first time user", Toast.LENGTH_SHORT).show();
							}
							databaseReference.removeEventListener(eventdown2);
							databaseReferenc.removeEventListener(fixappinlistner);
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							// Handle error
							Log.d("database error", String.valueOf(databaseError));
						}
					};
					databaseReferenc.addValueEventListener(fixappinlistner);
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
				Docdatahelper dao = Docdatahelper.getdoc(MainActivity.this);
				dao.clearAllTables();
				ArrayList<Docdatabase> data = new ArrayList<>();
				for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
					if (childsnapshot.exists()) {
						Docdatabase dataa = childsnapshot.getValue(Docdatabase.class);
						if (dataa != null) {
							data.add(dataa);
						} else {
//							Toast.makeText(MainActivity.this, "no doctors available", Toast.LENGTH_SHORT).show();
						}
					} else {
//						Toast.makeText(MainActivity.this, "no childsnapshot is available", Toast.LENGTH_SHORT).show();
					}
				}
				Log.d("data", String.valueOf(data));
				dao.DocdataDao().insertall(data);
//				Toast.makeText(MainActivity.this, "docdata updated", Toast.LENGTH_SHORT).show();
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

	public void location() {
		locationrefresh();
		if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			flpc = LocationServices.getFusedLocationProviderClient(MainActivity.this);
			flpc.requestLocationUpdates(locationRequest, locationCallback, null);

			flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
				@Override
				public void onSuccess(Location location) {
					if (location != null) {
						Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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
			ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
		}
	}

	public void permission1() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
		}
	}

	public void permission2() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 200);
		}
	}

	public void permission3() {
		if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
			ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
		}
	}

	public void permission4() {
		if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 3000);
			}
		}
	}

	public String findphno(String uid) {
		final String[] phn = new String[1];
		FirebaseDatabase.getInstance().getReference("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					phn[0] = snapshot.child("Phone").getValue(String.class);
				} else {
//					Toast.makeText(context, "no data snapshot", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.d("phone", error.getMessage());
			}
		});
		return phn[0];
	}
}
