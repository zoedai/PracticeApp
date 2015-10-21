package com.example.dai.phonehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setDate(System.currentTimeMillis());
    }
}
