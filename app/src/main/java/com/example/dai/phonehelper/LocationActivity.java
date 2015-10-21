package com.example.dai.phonehelper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.GregorianCalendar;

public class LocationActivity extends AppCompatActivity implements
        EnterDestination.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    public void showMyLocation(View view) {
        startActivity(new Intent(getApplicationContext(), MyLocationActivity.class));
    }

    public void goSomewhere(View view) {
        EnterDestination askForDestination = new EnterDestination();
        askForDestination.show(getFragmentManager(), "");


    }


    @Override
    public void onFragmentInteraction(CharSequence destination) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+destination));
        startActivity(intent);
    }
}
