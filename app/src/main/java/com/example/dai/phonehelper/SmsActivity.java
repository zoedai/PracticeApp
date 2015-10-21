package com.example.dai.phonehelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SmsActivity extends AppCompatActivity {

    Button smsList, composeSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsList = (Button) findViewById(R.id.smsListBtn);
        composeSms = (Button) findViewById(R.id.writeSmsBtn);

        smsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SMSListActivity.class
                ));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            // App is not default.
            // Show the "not currently set as the default SMS app" interface

            composeSms.setVisibility(View.INVISIBLE);

            askToBeDefault();
            // Set up a button that allows the user to change the default SMS app

        } else {
            composeSms.setVisibility(View.VISIBLE);
        }
    }

    private void askToBeDefault() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Phone helper is not your default sms app")
                .setMessage("To get full functionality, do you want to set phone helper as your" +
                        " default sms app?").setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                getPackageName());
                        startActivity(intent);
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
