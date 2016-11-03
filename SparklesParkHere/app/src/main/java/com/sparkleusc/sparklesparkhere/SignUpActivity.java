package com.sparkleusc.sparklesparkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        TextView firstNameTV = (TextView)  findViewById(R.id.FirstNameET);
        TextView lastNameTV = (TextView)  findViewById(R.id.LastNameET);
        TextView phoneNumberTV = (TextView)  findViewById(R.id.PhoneNumberET);
        TextView emailTV = (TextView)  findViewById(R.id.EmailAddressET);
        if (emailTV.getText().toString() != null) {
            String email = emailTV.getText().toString();
        }

        TextView passwordTV = (TextView)  findViewById(R.id.passwordET);
        if ( passwordTV.getText().toString() != null) {
            String password = passwordTV.getText().toString();
            if (password.length() >= 10) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher("I am a string");
                boolean b = m.find();

                if (b){
                    //then encrypt
                }

            }
        }


        final RadioButton robotRadioButton = (RadioButton) findViewById(R.id.RobotButton);

        addCreateProfileListener();

    }

    public void addCreateProfileListener(){
        Button createProfileButton = (Button) findViewById(R.id.CreateProfileButton);
        createProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //               System.out.println("Hello");
                Intent intent;
                intent = new Intent(SignUpActivity.this, SeekerOrLenderActivity.class);
                startActivity(intent);
            }
        });

    }
}
