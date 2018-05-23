package com.example.lyantorres.torreslyan_pp6;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Objects.DatabaseHelper;
import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.fragments.ExpandedListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity implements ExpandedListFragment.ExpandedListFragmentInterface{

    private FirebaseAuth mAuth;
    private NfcAdapter mNfcAdapter;
    private Tag mDetectedTag;
    private IntentFilter[] mReadTagFilters;
    private PendingIntent mPendingIntent;
    private ArrayList<String> mSavedCardsStrings = new ArrayList<>();

    public String mUSER_EXTRA = "USER";
    public int mPOPUP_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();

        // making sure that the adapter knows who to talk to when reading an NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mDetectedTag =getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        mAuth = FirebaseAuth.getInstance();

        getData();

        // Setting up the NFC handling
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this,getClass()).
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter filter2     = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mReadTagFilters = new IntentFilter[]{tagDetected,filter2};

        // ADD LOADING PAGE


        // ========== NOTE: adding a new user from outside of app overwrites their existing saved cards so im deacivated it for now ==========

        // this handles when an NFC is used to open the application
//        if(getIntent() != null){
//            if(getIntent().getAction() != null){
//
//                if (getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
//                    mDetectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//                    setIntent(getIntent());
//                    readTag(getIntent());
//                }
//            }
//        }

    }


    // ===================================== NFC HANDLING =====================================


    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mReadTagFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null) {
            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                mDetectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                setIntent(intent);
                readTag(getIntent());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == mPOPUP_REQUEST_CODE){

            if(data.hasExtra("UUID")){

                for(int i = 0; i < mSavedCardsStrings.size(); i ++){

                    if(mSavedCardsStrings.get(i).equals(data.getStringExtra("UUID"))){
                        mSavedCardsStrings.remove(i);
                        saveToDatabase();
                        Toast.makeText(this, "Card was deleted", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();
                    }
                }

            }

        }
    }

    private void readTag(Intent _intent){
        mDetectedTag =getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(mDetectedTag);

        try{
            ndef.connect();

            Parcelable[] messages = _intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String result = new String(payload);

                if(!mSavedCardsStrings.contains(result)) {
                    mSavedCardsStrings.add(result);
                    saveToDatabase();
                    Toast.makeText(this, "New card has been added", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "You have already saved this contact", Toast.LENGTH_SHORT).show();
                }


                ndef.close();

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }

    // ===================================== DATABASE  =====================================

    private void saveToDatabase(){

        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(user.getUid());
        final DatabaseReference savedCards = userRef.child(DatabaseHelper.SAVEDCARDS_REF);

        savedCards.setValue(mSavedCardsStrings);

    }

    private void getData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference userReference = database.getReference(currentUser.getUid());
        DatabaseReference userContactInfo = userReference.child(DatabaseHelper.SAVEDCARDS_REF);

        userContactInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> savedCards = dataSnapshot.getValue(t);

                mSavedCardsStrings = new ArrayList<>();

                if (savedCards != null) {
                    mSavedCardsStrings = savedCards;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // ===================================== EXPANDABLE LIST FRAGMENT INTERFACE CALLBACKS =====================================

    @Override
    public void itemClicked(User _user) {

        Intent popUpIntent = new Intent(this, PopUpItemActivity.class);
        popUpIntent.putExtra(mUSER_EXTRA, _user);
        startActivityForResult(popUpIntent, mPOPUP_REQUEST_CODE);

    }

    @Override
    public void profileClicked() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


}
