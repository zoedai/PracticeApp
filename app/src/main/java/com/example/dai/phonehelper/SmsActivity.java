package com.example.dai.phonehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Button smsList = (Button) findViewById(R.id.smsListBtn);
        Button sendSms = (Button) findViewById(R.id.writeSmsBtn);

        smsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SMSListActivity.class
                ));
            }
        });
    }
}
