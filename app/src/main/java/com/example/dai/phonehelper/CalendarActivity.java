package com.example.dai.phonehelper;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity implements AddReminder.OnFragmentInteractionListener {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mContext = getApplicationContext();
//        CalendarView cv = (CalendarView) findViewById(R.id.calendarView);
//        cv.setDate(System.currentTimeMillis());

        Button addReminder = (Button) findViewById(R.id.addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new AddReminder();
                dialogFragment.show(getFragmentManager(), "Add a reminder");
            }
        });
    }


    @Override
    public void onFragmentInteraction(GregorianCalendar startDate, CharSequence note) {
        ContentResolver cr = getContentResolver();

        // add event
        long calID = 3;
        Long startMillis = startDate.getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, startMillis+3600);
        values.put(CalendarContract.Events.TITLE, note.toString());
        values.put(CalendarContract.Events.DESCRIPTION, note.toString());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        // add reminder
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "Error when adding event", Toast.LENGTH_LONG).show();
            return;
        }

        long eventID = Long.parseLong(uri.getLastPathSegment());
        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.MINUTES, 15);
        reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder);

        if (uri != null) {
            Toast.makeText(getApplicationContext(), "Reminder added", Toast.LENGTH_LONG).show();
        }
    }
}
