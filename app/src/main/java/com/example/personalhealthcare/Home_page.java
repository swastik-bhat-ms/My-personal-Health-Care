package com.example.personalhealthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_page extends AppCompatActivity {
	FirebaseAuth login;
	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main3);
		login = FirebaseAuth.getInstance();
		logout = findViewById(R.id.button);
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login.signOut();
				startActivity(new Intent(Home_page.this, chat.class));
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseUser user = login.getCurrentUser();

		if (user == null || !user.isEmailVerified()) {
			startActivity(new Intent(Home_page.this, Log_in.class));
			finish();
		}
	}
}