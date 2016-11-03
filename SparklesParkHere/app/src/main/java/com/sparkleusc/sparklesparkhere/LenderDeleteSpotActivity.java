package com.sparkleusc.sparklesparkhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LenderDeleteSpotActivity extends AppCompatActivity {
    EditText timeAvailableEditTV = (EditText) findViewById(R.id.editTimeAvailableED);
    EditText priceEditTV = (EditText) findViewById(R.id.editPriceET);
    Button pictureEditButton = (Button) findViewById(R.id.editPictureButton);
    EditText descriptionEditTV = (EditText) findViewById(R.id.editDescriptionET);

    //get text in edittext and and change the backend to these "Edit texts"
    //do something about the edit picture button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_delete_spot);

        String timeAvailableString = timeAvailableEditTV.getText().toString();
        if (timeAvailableString != "Edit Times Available"){
            //pass time available string to backend
        }
        String priceString = priceEditTV.getText().toString();
        if (priceString != "Edit Price"){
            //pass price  string to backend
        }
        String descriptionEditString = descriptionEditTV.getText().toString();

        if (descriptionEditString != "Edit Description"){
            //pass description string to backend
        }

        pictureEditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });



    }
}
