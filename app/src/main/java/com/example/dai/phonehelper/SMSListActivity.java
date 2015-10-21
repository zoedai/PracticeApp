package com.example.dai.phonehelper;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.database.Cursor;
import android.net.Uri;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SMSListActivity extends AppCompatActivity {

    private ListView listView;
    private final Uri INBOXURI = Uri.parse("content://sms/inbox");
    String[] fromColumns = {"_id", "address", "body"};
    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};
//    int[] toViews = {0, android.R.id.text1, android.R.id.text2 };
    ContentResolver cr;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    BroadcastReceiver receiver;
    private boolean selected = false;
    private int selectedId = -1;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smslist);

        listView = (ListView) findViewById(R.id.listView);

        cr = getContentResolver();
        cursor = cr.query(INBOXURI, fromColumns, null, null, null);

        adapter = new SimpleCursorAdapter(this,
                R.layout.sms_row, cursor, fromColumns, toViews, 0);

        listView.setAdapter(adapter);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = true;
                selectedId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = false;
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


//        Button readSMS = (Button) findViewById(R.id.readBtn);
//        Button delSMS = (Button) findViewById(R.id.delBtn);

      /*  readSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected) {
                    CharSequence cs = cursor.getString(cursor.getColumnIndex(fromColumns[2]));
                    Log.i("TTTTTTTTTTTT",
                         cs.toString());

                    t1.speak(cs, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
*/
    }


    private void updateSMS() {
        // not working
        cursor = cr.query(INBOXURI, fromColumns, null, null, null);
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
//        listView.setAdapter(adapter);
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

    public void read(View view) {
        ViewGroup row = (ViewGroup) view.getParent().getParent();
        StringBuffer toSpeak = new StringBuffer("Message sent from ");
        toSpeak.append(((TextView) row.findViewById(R.id.sms_contact)).getText());
        toSpeak.append(". ");
        toSpeak.append(((TextView) row.findViewById(R.id.sms_body)).getText());
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
