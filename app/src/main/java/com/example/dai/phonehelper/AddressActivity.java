package com.example.dai.phonehelper;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddressActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {



    ListView mContactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;
    String[] fromColumns = {Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME};
    int[] toViews = {android.R.id.text1};
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getLoaderManager().initLoader(0, null, this);
        mContactsList = (ListView) findViewById(R.id.contactList);


        mCursorAdapter = new SimpleCursorAdapter(
                AddressActivity.this,
                android.R.layout.simple_list_item_1,
                null,
                fromColumns, toViews,
                0);
        // Sets the adapter for the ListView
        mContactsList.setAdapter(mCursorAdapter);


        mContactsList.setOnItemClickListener(this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Starts the query
        return new CursorLoader(
                AddressActivity.this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the Cursor
            Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
            // Move to the selected contact
            cursor.moveToPosition(position);
            // Get the _ID value
            mContactId = cursor.getLong(CONTACT_ID_INDEX);
            // Get the selected LOOKUP KEY
            mContactKey = cursor.getString(LOOKUP_KEY_INDEX);
            // Create the contact's content Uri
            mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);
        /*
         * You can use mContactUri as the content URI for retrieving
         * the details for a contact.
         */
        Intent openContact = new Intent(Intent.ACTION_VIEW, mContactUri);
        startActivity(openContact);
    }
}
