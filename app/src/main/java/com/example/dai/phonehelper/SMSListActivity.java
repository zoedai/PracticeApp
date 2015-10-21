package com.example.dai.phonehelper;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;


import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class SMSListActivity extends AppCompatActivity {

    private ListView listView;
    private final Uri INBOXURI = Uri.parse("content://sms/inbox");
    final String[] fromColumns = {"_id", "address", "body"};
//    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};
    int[] toViews = {0, android.R.id.text1, android.R.id.text2 };
    ContentResolver cr;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    BroadcastReceiver receiver;
    private boolean selected = false;
    private int selectedId = -1;
    private TextToSpeech t1;
    ToggleButton readSMS, delSMS;
    long mSmsId;
    Uri mSmsUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smslist);

        listView = (ListView) findViewById(R.id.listView);

        cr = getContentResolver();
        cursor = cr.query(INBOXURI, fromColumns, null, null, null);

        //adapter = new SimpleCursorAdapter(this,
               // R.layout.sms_row, cursor, fromColumns, toViews, 0);

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, fromColumns, toViews, 0);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (delSMS.isChecked()) {
                    Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
                    // Move to the selected contact
                    cursor.moveToPosition(position);
                    // Get the _ID value
                    mSmsId = cursor.getLong(cursor.getColumnIndex(fromColumns[0]));
                    getContentResolver().delete(Uri.parse("content://sms/"+mSmsId), null, null);
                    updateSMS();
                } else if (readSMS.isChecked()) {
                    read(view);
                }


            }
        });


        //tts
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        readSMS = (ToggleButton) findViewById(R.id.readBtn);
        delSMS = (ToggleButton) findViewById(R.id.delBtn);

        readSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    delSMS.setChecked(false);
                }

            }
        });

        delSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                readSMS.setChecked(false);
            }
        });

    }


    private void updateSMS() {
        // sometimes not working
        cursor = cr.query(INBOXURI, fromColumns, null, null, null);
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Message!", Toast.LENGTH_LONG).show();
                updateSMS();
            }
        };

        registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));


    }

    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }



    public void read(View row) {
//        ViewGroup row = (ViewGroup) view.getParent().getParent();
        StringBuffer toSpeak = new StringBuffer("Message sent from ");
        toSpeak.append(((TextView) row.findViewById(toViews[1])).getText());
        toSpeak.append(". ");
        toSpeak.append(((TextView) row.findViewById(toViews[2])).getText());
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void delete(View view) {

    }


}


/*class SMSData {

    public SMSData(String id, String number, String body) {
        this.id = id;
        this.number = number;

        if (body != null && body.length() > 20) {
            this.body = body.substring(0, 20);
        } else {
            this.body = "";
        }

    }

    // Number from witch the sms was send
    public final String number;
    // SMS text body
    public final String body;

    public final String id;


}*/
