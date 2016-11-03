package com.sparkleusc.sparklesparkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SeekerOrLenderActivity extends AppCompatActivity {


//    Intent seekerIntent = new Intent(this, SeekerProfilePageActivity.class);
//    Intent lenderIntent = new Intent(this, LenderProfilePageActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_or_lender);

        addLenderListener();
        addSeekerListener();
    }

    public void addLenderListener(){
        Button lenderButton = (Button) findViewById(R.id.LenderButton);

        lenderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Hello");
                Intent intent;
                intent = new Intent(SeekerOrLenderActivity.this, LenderSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addSeekerListener(){
        Button seekerButton = (Button) findViewById(R.id.SeekerButton);
        seekerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Hello");
                Intent intent;
                intent = new Intent(SeekerOrLenderActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }




}
