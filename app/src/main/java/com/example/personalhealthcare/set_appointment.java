package com.example.personalhealthcare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class set_appointment extends Fragment {
	Activity context;
	String uid = "";
	Spinner spinner;
	DatabaseReference reference;
	ArrayList<String> lists = new ArrayList<>();
	AlertDialog dialog;
	AlertDialog.Builder builder;
	AutoCompleteTextView spinnerdis;
	int month, date;
	String docname;
	LinearLayout layout;
	String phno = "";
	Calendar calendar = Calendar.getInstance();
	private EditText answer;
	private EditText appointmentdate;
	private ImageButton calbtn;
	private RadioButton meradio;
	private RadioButton famradio;
	private EditText name_age;
	private EditText pincode;
	private Button request_btn, cancel_btn;
	private ProgressBar progressbar;
	private RadioGroup radiogroup;
	private TextView specify;
	private Button change;
	private TextView doc_name;
	private Button search;
	private View view;
	private ImageButton appointment_btn;
	private String datee;
	private EditText relative, relation_filed;
	private String district;
	private String[] items;
	private ValueEventListener querylister;

	public set_appointment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("MissingInflatedId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		context = getActivity();
		view = inflater.inflate(R.layout.fragment_set_appointment, container, false);
//		hideKeybord();
		in.hideSoftInputFromWindow(view.getWindowToken(), 0);


		spinner = (Spinner) view.findViewById(R.id.body_part);
		spinnerdis = (AutoCompleteTextView) view.findViewById(R.id.district);
		answer = (EditText) view.findViewById(R.id.answer);
		appointmentdate = (EditText) view.findViewById(R.id.dateappointment);
		meradio = (RadioButton) view.findViewById(R.id.radioButton);
		famradio = (RadioButton) view.findViewById(R.id.radioButton2);
		name_age = (EditText) view.findViewById(R.id.relation);
		pincode = (EditText) view.findViewById(R.id.pincode);
		request_btn = (Button) view.findViewById(R.id.request_btn);
		cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
		progressbar = (ProgressBar) view.findViewById(R.id.progressBar3);
		radiogroup = (RadioGroup) view.findViewById(R.id.group);
		specify = (TextView) view.findViewById(R.id.specify);
		relative = view.findViewById(R.id.relative_age);
		layout = (LinearLayout) view.findViewById(R.id.layout);
		doc_name = (TextView) view.findViewById(R.id.doc_name);
		relation_filed = view.findViewById(R.id.relation_filed);
		search = (Button) view.findViewById(R.id.search);
		radiogroup = view.findViewById(R.id.group);
		appointment_btn = (ImageButton) view.findViewById(R.id.appointmentdate_btn);


		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.body_part, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.location, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerdis.setAdapter(adapter2);

		appointmentdate.setEnabled(false);
		//Calendar calendar1= Calendar.getInstance();
		appointmentdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectdate();
			}
		});
		appointment_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectdate();

			}
		});


		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressbar.setVisibility(View.VISIBLE);
				Query query;

				ArrayList<String> lists = new ArrayList<>();
				ArrayList<Docdatabase> arrayList = new ArrayList<>();
				ArrayList<String> ulist = new ArrayList<>();
//                String spinner;
				DatabaseReference reference;
				InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(view.getWindowToken(), 0);

				reference = FirebaseDatabase.getInstance().getReference("User");
				query = reference.orderByChild("vaild").equalTo(true);

				Docdatahelper sort = Docdatahelper.getdoc(context);
				try {

					querylister = new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							Docdatabase data;
							List<Docdatabase> da = new ArrayList<>();
							for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
								data = dataSnapshot.getValue(Docdatabase.class);
								arrayList.add(data);
								lists.add(dataSnapshot.getKey());

							}//for
							sort.clearAllTables();
							sort.DocdataDao().insertall(arrayList);

							da = sort.DocdataDao().getThese();
							//latch.countDown();

							int j = 0;
							int k = 0;
							for (int i = 0; i < da.size(); i++) {
								String sd = spinnerdis.getText().toString();
								String sdDB = da.get(i).getDistrict();
//
								if (sdDB.equals(sd)) {
									j++;
								}


							}//for end

							if (j > 0) {
								items = new String[j];
								for (int i = 0; i < da.size(); i++) {
									String sd = spinnerdis.getText().toString();
									String sdDB = da.get(i).getDistrict();
//
									if (sdDB.equals(sd)) {
										//phonumber is added here
										items[k] = "Name: " + da.get(i).getFullname() + "\nDesignation: " + da.get(i).getDesignation() + "\nAddress: " + da.get(i).getAddress() + "\nPincode: " + da.get(i).getPincode() + "\n";
										ulist.add(lists.get(i));
										k++;
									}
								}
								List<Docdatabase> dab = da;
								sort.clearAllTables();
								if (items[0] != null) {
									builder = new AlertDialog.Builder(getActivity());
									builder.setTitle("Select a Doctor");
									builder.setCancelable(false).setPositiveButton("Select", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											layout.setVisibility(View.VISIBLE);
//                                        doc_name.setText(items[which]);
//                                        handler.removeCallbacks(null);
											dialog.cancel();
											query.removeEventListener(querylister);
										}
									});
									builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											doc_name.setText(items[which]);
											phno = dab.get(which).getPhone();
											//docname = name.get(which);
											uid = ulist.get(which);
										}
									});
									dialog = builder.create();
									progressbar.setVisibility(View.GONE);
									request_btn.setEnabled(true);
									dialog.show();

								}

							}//end of if
							else {
								builder = new AlertDialog.Builder(getActivity());
								builder.setTitle("Not Found!").setMessage("No Doctor Found in your District").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
								dialog = builder.create();
								progressbar.setVisibility(View.GONE);
								dialog.show();
							}//end od else
						}


						@Override
						public void onCancelled(@NonNull DatabaseError error) {

						}
					};
				} catch (Exception e) {
				}
//                runthread();
				query.addValueEventListener(querylister);
			}


		});
		famradio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				specify.setVisibility(View.VISIBLE);
				name_age.setVisibility(View.VISIBLE);
				relative.setVisibility(View.VISIBLE);
				relation_filed.setVisibility(View.VISIBLE);
			}
		});
		meradio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				specify.setVisibility(View.GONE);
				name_age.setVisibility(View.GONE);
				relation_filed.setVisibility(View.GONE);
			}
		});
		request_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select body part") || answer.getText().toString().isEmpty() || appointmentdate.getText().toString().isEmpty() || specify.getText().toString().isEmpty()) { //|| (famradio.isChecked() && name_age.getText().toString().isEmpty()  || relative.getText().toString().isEmpty() || relation_filed.getText().toString().isEmpty())) {
					if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select body part")) {
						spinner.requestFocus();
						Toast.makeText(context, "Spinner left empty", Toast.LENGTH_SHORT).show();
					}
					if (answer.getText().toString().isEmpty()) {
						answer.setError("cannot be empty");
						Toast.makeText(context, "Answer Empty", Toast.LENGTH_SHORT).show();
					}
					if (appointmentdate.getText().toString().isEmpty()) {
						appointmentdate.setError("Select a date");
						Toast.makeText(context, "Date empty", Toast.LENGTH_SHORT).show();
					}
					if (specify.getText().toString().isEmpty()) {
						//spinnerdis.setError("Select doctor");
						Toast.makeText(context, "Doctor filed empty", Toast.LENGTH_SHORT).show();
					}
//                    if(famradio.isChecked())
//                    {
//                        if(name_age.getText().toString().isEmpty()  || relative.getText().toString().isEmpty() || relation_filed.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "Relation Filed not correct ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
				} else {
					String all = "";
					String part = spinner.getSelectedItem().toString();
					String explain = answer.getText().toString();
					String date = appointmentdate.getText().toString();
					reference = null;
					reference = FirebaseDatabase.getInstance().getReference("User").child(uid);
					if (famradio.isChecked()) {
						String relation = name_age.getText().toString();
						String age = relative.getText().toString();
						String relation_type = relation_filed.getText().toString();
						all = part + "++" + explain + "++" + date + "++" + relation + "++" + age + "++" + relation_type + "++";
					} else {
						all = part + "++" + explain + "++" + date + "++";
					}
//                    all = part + "++" + explain + "++" + date + "++";

					String cuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
					String finalAll = all;
					reference.child("appoinment").child(cuid).addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							if (snapshot.exists()) {
								Toast.makeText(getActivity(), "Appointment has already been requested .Please wait for response", Toast.LENGTH_LONG).show();
								context.finish();
							} else {
//                                reference.child("fixedappointment").child(uid).addListenerForSingleValueEvent
								reference.child("appoinment").child(cuid).setValue(finalAll).addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										try {
											if (task.isSuccessful()) {
												Toast.makeText(context, "Appointment has been requested. Please wait for response", Toast.LENGTH_SHORT).show();
												smssender(phno, "There is an Appointment request on the date :" + date + "\n Bodypart :" + part);
												SharedPreferences.Editor editor = getActivity().getSharedPreferences("secondnot", Context.MODE_PRIVATE).edit();
												editor.putString("docuid", uid).apply();
//												Log.d("allarm manger is called", "called");
												context.finish();
											} else {
												Toast.makeText(context, "Error while requesting appointment. Try again later " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
												context.finish();
											}
										} catch (Exception e) {
											Toast.makeText(context, "Appointment Already requested for this date", Toast.LENGTH_SHORT).show();
										}
									}
								});
								//add a node to the user itself
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {

						}
					});
//                    reference.child("appoinment").child(cuid).setValue(all).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            try {
//
//                                if (task.isSuccessful())
//                                {
//                                    Toast.makeText(context, "Appointment has been requested. Please wait for response", Toast.LENGTH_SHORT).show();
//                                    getActivity().finish();
//                                }
//                                else
//                                {
//                                    Toast.makeText(context, "Error will requesting appointment "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            catch (Exception e)
//                            {
//                                Toast.makeText(context, "Appointment Already requested for this date", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
				}

			}
		});
		cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});


		return view;
	}

	private void selectdate() {
		month = calendar.get(Calendar.MONTH);
		date = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
				appointmentdate.setText(String.valueOf(i2) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i));
				//datee = String.valueOf(i2) + String.valueOf(i1) +  String.valueOf(i);
			}
		}, calendar.get(Calendar.YEAR), month, date);
		datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)));
		datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
		datePickerDialog.show();
	}

	private void hideKeybord() {
		InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

		im.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		context.findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
		context.findViewById(R.id.sendbutn).setVisibility(View.VISIBLE);
		context.findViewById(R.id.usermessagebox).setVisibility(View.VISIBLE);
		context.findViewById(R.id.card).setVisibility(View.VISIBLE);
		if ((dialog != null) && dialog.isShowing()) {
			dialog.cancel();
		}

	}

	private void runthread() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
//                synchronized (this) {
//                    try {
//                        handler.wait(3000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
				if (items[0] != null) {
					builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Select a Doctor");
					builder.setCancelable(false).setPositiveButton("Select", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							layout.setVisibility(View.VISIBLE);
//                                        doc_name.setText(items[which]);
//                                        handler.removeCallbacks(null);
							dialog.dismiss();
						}
					});


					ArrayList<String> finalLists = lists;
					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doc_name.setText(items[which]);
							//docname = name.get(which);
							uid = finalLists.get(which);
						}
					});
					dialog = builder.create();
					progressbar.setVisibility(View.GONE);
					request_btn.setEnabled(true);
					dialog.show();

				} else {
					builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Not Found!").setMessage("No Doctor Found in your District").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog = builder.create();
					progressbar.setVisibility(View.GONE);
					dialog.show();
				}//else close

			}
		}, 3000);

	}

	public void smssender(String pno, String txtxmsg) {
		if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
		}
		SmsManager.getDefault().sendTextMessage(pno, null, txtxmsg, null, null);
//        Toast.makeText(getActivity(), "SMS Sent", Toast.LENGTH_SHORT).show();
	}

}