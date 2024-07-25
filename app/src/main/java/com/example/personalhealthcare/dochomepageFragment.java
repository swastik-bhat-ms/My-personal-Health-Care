package com.example.personalhealthcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class dochomepageFragment extends Fragment {
	LocationRequest locationRequest;
	LocationCallback locationCallback;
	String address;
	String country;
	String city;
	String longitude;
	String latitude;
	CountDownLatch latch = new CountDownLatch(1);
	LocationManager locmanager;
	FusedLocationProviderClient flpc;
	ImageButton dbot;
	ImageButton dappoint;
	ImageButton cancelappoint;
	FloatingActionButton doc100;
	FloatingActionButton doc108;
	FloatingActionButton doc112;
	FirebaseUser currentuser;
	SwipeRefreshLayout swipeRefreshLayout;
	DatabaseReference databaseReference;
	private docappoint docappoint;
	private TextView User_name;
	private TextView user;
	private TextView phone;
	private FirebaseUser users_firebase;
	private String name;
	private DatabaseReference reference;
	private String userid;
	private TextView desig;
	private FirebaseAuth mAuth;
	private ValueEventListener eventdown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dochomepage, container, false);
		InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), 0);
		doc108 = view.findViewById(R.id.doc108);
		doc100 = view.findViewById(R.id.doc100);
		doc112 = view.findViewById(R.id.doc112);

		swipeRefreshLayout = view.findViewById(R.id.docrefresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						datadocevent();
//						Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 2000);

			}
		});
		// code starts here
		Thread imp1 = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				permission1();
				Looper.loop();
			}
		});
		imp1.setPriority(5);
		imp1.start();
		Thread imp2 = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				permission2();
				Looper.loop();
			}
		});
		imp2.setPriority(5);
		imp2.start();
		Thread imp3 = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				permission3();
				Looper.loop();
			}
		});
		imp3.setPriority(5);
		imp3.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				locationrefresh();
			}
		}).start();
		location();
		User_name = (TextView) view.findViewById(R.id.docname);
		user = (TextView) view.findViewById(R.id.user);
		phone = (TextView) view.findViewById(R.id.docphn);
		users_firebase = FirebaseAuth.getInstance().getCurrentUser();
		desig = (TextView) view.findViewById(R.id.docdes);
		SharedPreferences sh = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
		name = sh.getString("Names", "");
		User_name.setText(name);
		phone.setText(sh.getString("Phoneno", ""));
		user.setText(sh.getString("User", ""));
		desig.setText(sh.getString("desig", ""));
		String users = sh.getString("User", "");
		if (name.isEmpty()) details_add();
		dbot = view.findViewById(R.id.docbotbutton);
		dbot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				appointmentrequest appointments = new appointmentrequest();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, appointments).addToBackStack("dochomepageFragment").commit();
			}
		});
		dappoint = view.findViewById(R.id.docappointment);
		dappoint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docappoint = new docappoint();
				requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, docappoint).addToBackStack("docappoint").commit();
			}
		});
		cancelappoint = view.findViewById(R.id.doccancel);
		cancelappoint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				view_appointmentfrag view_appointmentsfrag = new view_appointmentfrag();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, view_appointmentsfrag).addToBackStack("dochomepageFragment").commit();
			}
		});

		doc108.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					doc100.setEnabled(true);
					doc108.setEnabled(true);
					doc112.setEnabled(true);

					String who = "108";
					String pno = "Phone No";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
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
					doc100.setEnabled(false);
					doc108.setEnabled(false);
					doc112.setEnabled(false);
				}
			}
		});
		doc100.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					doc100.setEnabled(true);
					doc108.setEnabled(true);
					doc112.setEnabled(true);
					String pno = "Phone No";
					String who = "100";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
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
					doc100.setEnabled(false);
					doc108.setEnabled(false);
					doc112.setEnabled(false);
				}
			}
		});
		doc112.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
					doc100.setEnabled(true);
					doc108.setEnabled(true);
					doc112.setEnabled(true);
					String pno = "Phone No";
					String who = "112";
					locmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					if (!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						Thread imp = new Thread(new Runnable() {
							@Override
							public void run() {
								Looper.prepare();
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
					doc100.setEnabled(false);
					doc108.setEnabled(false);
					doc112.setEnabled(false);
				}
			}
		});

		return view;
	}

	// modlues for location
	public void locationinfo() {
		try {
			if ((ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
				ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
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
		OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<>() {
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
					Log.d("Location ", "not fetched");
				}
			}
		};
		locationrefresh();
		if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			flpc = LocationServices.getFusedLocationProviderClient(getActivity());
			flpc.requestLocationUpdates(locationRequest, locationCallback, null);

			flpc.getLastLocation().addOnSuccessListener(onSuccessListener);
			flpc.getLastLocation().addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.d("Failure", e.getMessage());
				}
			});
		} else {
			ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
		}
	}

	// module for sms
	public void smssender(String pno, String txtxmsg) {
		if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.SEND_SMS}, 1);
		}
		SmsManager.getDefault().sendTextMessage(pno, null, txtxmsg, null, null);
//		Toast.makeText(getActivity(), "SMS Sent", Toast.LENGTH_SHORT).show();
	}

	// module for phone call
	public void phonecall(String who, String pno, String txtmsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Are you sure, You want to call " + who);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
				if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
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
//				Toast.makeText(getActivity(), "canceled", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void permission1() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
		}
	}

	public void permission2() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
		}
	}

	public void permission3() {
		if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
		}
	}

	private void datadocevent() {
		mAuth = FirebaseAuth.getInstance();
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DATE - 1);
		int todaymth = calendar.get(Calendar.MONTH) + 1;
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			eventdown = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					Map<String, String> allentris = (Map<String, String>) snapshot.getValue();
					SharedPreferences.Editor editor = getActivity().getSharedPreferences("docevent", Context.MODE_PRIVATE).edit();
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
							if (date == (datee[0] + datee[1] + datee[2])) {
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
//						Toast.makeText(getActivity(), "no events to downoload", Toast.LENGTH_SHORT).show();
					}
					editor.apply();

					databaseReference.removeEventListener(eventdown);
					DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("appoinment");
					databaseReference1.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							if (snapshot.exists()) {
								Map<String, String> appoin = (Map<String, String>) snapshot.getValue();
								if (appoin != null) {
									for (Map.Entry<String, String> entry : appoin.entrySet()) {
										String[] date = entry.getValue().split("\\+\\+");
										String[] datee = date[2].split("-");
										if (Integer.parseInt(datee[0]) < today && Integer.parseInt(datee[1]) <= todaymth) {
											snapshot.getRef().removeValue();
										}
									}
								}

							} else {
//								Toast.makeText(getActivity(), "no old appoinments", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {
							Log.e("database error", error.getMessage());
						}
					});
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

	public void details_add() {
		if (users_firebase != null) {
			reference = FirebaseDatabase.getInstance().getReference("User");
			userid = users_firebase.getUid();
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
						desig.setText(userprofile.designation);
						name = namess;
						SharedPreferences.Editor edit = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE).edit();
						edit.putString("Names", namess);
						edit.putString("User", userprofile.user);
						edit.putString("Phoneno", userphone);
						edit.putString("email", userprofile.email);
						edit.putString("desig", userprofile.designation);
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