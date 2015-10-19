package com.example.dai.phonehelper;

import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button callBtn, calBtn, browserBtn, addrBtn, locBtn, smsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callBtn = (Button) findViewById(R.id.callBtn);
        calBtn = (Button) findViewById(R.id.calBtn);
        browserBtn = (Button) findViewById(R.id.browserBtn);
        addrBtn = (Button) findViewById(R.id.addrBtn);
        locBtn = (Button) findViewById(R.id.locBtn);
        smsBtn = (Button) findViewById(R.id.smsBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL));
            }
        });
    }
}
