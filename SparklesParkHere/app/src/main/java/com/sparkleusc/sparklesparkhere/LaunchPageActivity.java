package com.sparkleusc.sparklesparkhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by josep on 10/29/2016.
 */


public class LaunchPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_page);

        TextView parkHereTV = (TextView) findViewById(R.id.parkHereTV);

        addLoginListener();
        addSignUpListener();

    }

    public void addLoginListener(){
        Button loginButton = (Button) findViewById(R.id.LaunchLoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LaunchPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addSignUpListener(){

        //final Context context = this;

        Button signUpButton = (Button) findViewById(R.id.LaunchSignupButton);
        System.out.println("Before onclicke");
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                System.out.println("in onclick");
                if(LaunchPageActivity.this == null){System.out.println("Boots");}
                else{System.out.println("not null");}


                intent = new Intent(LaunchPageActivity.this, SignUpActivity.class);
                System.out.println("after intent");
                startActivity(intent);
                System.out.println("SLUTS");
            }
        });
    }
}


