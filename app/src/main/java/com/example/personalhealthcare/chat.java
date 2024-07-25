package com.example.personalhealthcare;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chat extends AppCompatActivity {
	private final int User_key = 0;
	private final int Bot_key = 1;
	protected ArrayList<Messagemodel> messagemodelArrayList;
	protected RecyclerView chatRv;
	protected ImageButton sendbtn;
	protected CardView cardView;
	protected EditText usermessage;
	boolean ans = false;
	int i = 0;
	String answerslist[] = new String[100];
	boolean appointment = false;
	boolean selectdoc = false;
	reply pushmessage;
	private MessageRVAdapter messageRVAdapter;
	private set_appointment set_appointment = new set_appointment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		pushmessage = new reply();
		chatRv = findViewById(R.id.recyclerView);
		sendbtn = findViewById(R.id.sendbutn);
		usermessage = findViewById(R.id.usermessagebox);
		cardView = findViewById(R.id.card);
		messagemodelArrayList = new ArrayList<>();
		messageRVAdapter = new MessageRVAdapter(messagemodelArrayList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chat.this, RecyclerView.VERTICAL, false);
		chatRv.setLayoutManager(linearLayoutManager);
		chatRv.setAdapter(messageRVAdapter);
		sendbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (usermessage.getText().toString().isEmpty()) {
					Toast.makeText(chat.this, "Please Enter proper message", Toast.LENGTH_SHORT).show();
					return;
				}
				sendMessage(usermessage.getText().toString().trim());
				usermessage.setText("");
			}
		});
		String botresponse = "Hello welcome to chatbot Service.\n How may I help You?";
		messagemodelArrayList.add(new Messagemodel(botresponse, Bot_key));
		messageRVAdapter.notifyDataSetChanged();
		usermessage.setHint("Set Appointment/Chat");
	}

	public void sendMessage(String userMsg) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chat.this, RecyclerView.VERTICAL, false);
		chatRv.setLayoutManager(linearLayoutManager);
		chatRv.setAdapter(messageRVAdapter);
		messagemodelArrayList.add(new Messagemodel(userMsg, User_key));
		messageRVAdapter.notifyDataSetChanged();
		String botresponse = pushmessage.replymessage(userMsg, ans);
		usermessage.setHint("");
		if (userMsg.equalsIgnoreCase("chat")) {

			appointment = false;
			ans = true;
		}
		if (userMsg.equalsIgnoreCase("set appointment")) {
			appointment = true;
		}
		if (appointment) {
			ans = false;
			pushmessage.setappointment(userMsg);

		}

		if (i == 3) {
			selectdoc = true;
			appointment = false;
		}
		if (userMsg.equalsIgnoreCase("yes") && appointment) {
			chatRv.setVisibility(View.GONE);
			sendbtn.setVisibility(View.GONE);
			usermessage.setVisibility(View.GONE);
			cardView.setVisibility(View.GONE);
			getSupportFragmentManager().beginTransaction().replace(R.id.chat, set_appointment).addToBackStack("homeFragment").commit();
			appointment = false;
			messagemodelArrayList.clear();
			messagemodelArrayList.add(new Messagemodel("Hello welcome to chatbot Service.\n How may I help You?", Bot_key));
			usermessage.setHint("Set Appointment/ Chat");

		} else if (userMsg.equalsIgnoreCase("no")) {
			appointment = false;
			messagemodelArrayList.add(new Messagemodel("How may I help you", Bot_key));
			messageRVAdapter.notifyDataSetChanged();
			chatRv.scrollToPosition(messageRVAdapter.getItemCount() - 1);

		} else {
			messagemodelArrayList.add(new Messagemodel(botresponse, Bot_key));
			messageRVAdapter.notifyDataSetChanged();
			chatRv.scrollToPosition(messageRVAdapter.getItemCount() - 1);
		}


	}
}