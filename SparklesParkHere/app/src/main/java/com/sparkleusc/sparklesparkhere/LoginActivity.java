package com.sparkleusc.sparklesparkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView emailTV = (TextView)  findViewById(R.id.email);
//        String email = emailTV.getText().toString();

        TextView passwordTV = (TextView)  findViewById(R.id.password);
//        String password = passwordTV.getText().toString();

        //send it back to the backend and see if the email & password are present.
        //if the email isnt present in the database then send the user to the Sign Up Activity
        //otherwise, send the user to the Seeker or Lender page

        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, SeekerOrLenderActivity.class);
                startActivity(intent);
            }
        });
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//            }
//        });
    }
}

