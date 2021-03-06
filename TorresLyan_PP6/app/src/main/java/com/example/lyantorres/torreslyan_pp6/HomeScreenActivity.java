package com.example.lyantorres.torreslyan_pp6;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.provider.Settings;
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

public class HomeScreenActivity extends AppCompatActivity implements ExpandedListFragment.ExpandedListFragmentInterface {

    private FirebaseAuth mAuth;
    private NfcAdapter mNfcAdapter;
    private Tag mDetectedTag;
    private IntentFilter[] mReadTagFilters;
    private PendingIntent mPendingIntent;
    private ArrayList<String> mSavedCardsStrings = new ArrayList<>();

    private ProgressDialog mDialog;

    public final String mUSER_EXTRA = "USER";
    public final int mPOPUP_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null) {

            if(mAuth.getCurrentUser() != null) {

                // making sure that the adapter knows who to talk to when reading an NFC
                mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
                checkIfNFCIsEnabled();

                mDetectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

                mDialog = new ProgressDialog(this);
                mDialog.setTitle("Downloading");
                mDialog.setMessage("Getting your saved cards");
                mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mDialog.show();


                getData();

                // Setting up the NFC handling
                mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        new Intent(this, getClass()).
                                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
                IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
                mReadTagFilters = new IntentFilter[]{tagDetected, filter2};
            } else {

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Not Signed in");
                dialog.setMessage("You have to be signed in to add a card");
                dialog.setPositiveButton("GO TO SIGN IN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent signInIntent = new Intent(HomeScreenActivity.this, MainActivity.class);
                        startActivity(signInIntent);
                        finish();
                    }
                });

                dialog.show();
            }

        }


    }


    // ===================================== NFC HANDLING =====================================


    private void checkIfNFCIsEnabled() {
        if (!mNfcAdapter.isEnabled()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NFC not enabled");
            builder.setMessage("Some features of this application require your NFC settings to be enabled. Please turn NFC data exchange on and try again.");
            builder.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent setNFC = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(setNFC);
                }
            });
            builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setIcon(R.drawable.nfc_icon);
            builder.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mReadTagFilters, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
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

        if (resultCode == mPOPUP_REQUEST_CODE) {

            if (data.hasExtra("UUID")) {

                for (int i = 0; i < mSavedCardsStrings.size(); i++) {

                    if (mSavedCardsStrings.get(i).equals(data.getStringExtra("UUID"))) {
                        mSavedCardsStrings.remove(i);
                        saveToDatabase();
                        Toast.makeText(this, "Card was deleted", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();
                    }
                }

            }
        } else if(resultCode == 200){
            Intent intent  = new Intent(this, MainActivity.class);
            intent.setAction("SIGNOUT");
            startActivity(intent);
            finish();
        }
    }

    private void readTag(Intent _intent) {
        mDetectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(mDetectedTag);

        try {
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

                if (!mSavedCardsStrings.contains(result)) {
                    mSavedCardsStrings.add(result);
                    saveToDatabase();
                    Toast.makeText(this, "New card has been added", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "You have already saved this contact", Toast.LENGTH_SHORT).show();
                }


                ndef.close();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }

    // ===================================== DATABASE  =====================================

    private void saveToDatabase() {

        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(user.getUid());
        final DatabaseReference savedCards = userRef.child(DatabaseHelper.SAVEDCARDS_REF);

        savedCards.setValue(mSavedCardsStrings);

    }

    private void getData() {


        // don't do anything if they aren't connected to the internet
        if (isNetworkAvailable()) {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userReference = database.getReference(currentUser.getUid());
            DatabaseReference userContactInfo = userReference.child(DatabaseHelper.SAVEDCARDS_REF);

            userContactInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                    };
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

            userContactInfo.setPriority(null, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (mDialog != null) {
                        mDialog.dismiss();

                        if (getIntent() != null) {
                            if (getIntent().getAction() != null) {

                                if (getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
                                    mDetectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                                    setIntent(getIntent());
                                    readTag(getIntent());
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    // making sure they have a network connectivity and let them know if they dont
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean results = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!results){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("No Internet Connection");
            dialog.setMessage("Please check your connection and try again.");
            dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getData();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return results;
    }

    // ===================================== EXPANDABLE LIST FRAGMENT INTERFACE CALLBACKS =====================================

    @Override
    public void itemClicked(User _user) {
        // open up pop up window
        Intent popUpIntent = new Intent(this, PopUpItemActivity.class);
        popUpIntent.putExtra(mUSER_EXTRA, _user);
        startActivityForResult(popUpIntent, mPOPUP_REQUEST_CODE);

    }

    @Override
    public void profileClicked() {
        // take them to profile preview
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivityForResult(intent, 200);
    }


}
