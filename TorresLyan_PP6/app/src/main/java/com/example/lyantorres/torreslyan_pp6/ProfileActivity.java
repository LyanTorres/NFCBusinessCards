package com.example.lyantorres.torreslyan_pp6;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Objects.DatabaseHelper;
import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.fragments.EditProfileFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.ProfilePreviewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements ProfilePreviewFragment.ProfilePreviewFragmentInterface, EditProfileFragment.EditProfileInterface{

    private boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, ProfilePreviewFragment.newInstance()).commit();
    }

    // ===================================== NFC HANDLING  =====================================

    @Override
    protected void onNewIntent(Intent intent) {

        FirebaseUser user = mAuth.getCurrentUser();

        // make sure this intent is to read in an NFC before doing anything
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime( "text/plain", user.getUid().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] { record });
            if (writeTag(message, detectedTag)) {
                Toast.makeText(this, "Success: Wrote "+user.getUid()+" to nfc tag", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {

                // connecting to the NFC
                ndef.connect();

                // make sure it's writable
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                // make sure it's capable of holding what we want to write to it
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                // if it passed all the tests then you can write to it
                ndef.writeNdefMessage(message);
                return true;
            } else {

                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    // make sure you're not writing to nfc getting close to the device by turning it on/ off

    // turning it on
    private void enableTagWriteMode() {
        // enable NFC writing
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    // turning it off
    private void disableTagWriteMode() {
        // disable NFC writing
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
        mNfcAdapter.disableReaderMode(this);
    }
    


    // ===================================== PROFILE PREVIEW FRAGMENT INTERFACE CALLBACKS  =====================================

    @Override
    public void editWasClicked(User _user) {
        getSupportFragmentManager().beginTransaction().addToBackStack("editing").replace(R.id.profile_frame, EditProfileFragment.newInstance(_user)).commit();
    }

    @Override
    public void saveToNFCWasClicked() {

        // make sure that the adapter knows to send it's data to you
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        final AlertDialog.Builder touchTagMessage = new AlertDialog.Builder(this);

        enableTagWriteMode();

        // show the user some visual stuff so that they know it's ok to tap their NFC now
        touchTagMessage.setIcon(R.drawable.nfc_icon);
        touchTagMessage.setTitle("Write to NFC tag").setNegativeButton("Cancel", null).setMessage("Place the NFC tag where your device's NFC reader is to write your information to that tag.")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        disableTagWriteMode();
                    }
                }).create().show();

    }

    @Override
    public void signOutWasClicked() {
        Intent intent = new Intent();
        setResult(200, intent);
        finish();
    }

    // ===================================== EDIT PROFILE FRAGMENT INTERFACE CALLBACKS  =====================================

    @Override
    public void saveWasPressed(User _user) {

        // make sure to push their changes to the database
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference userReference = database.getReference(currentUser.getUid());
        DatabaseReference userInfo = userReference.child(DatabaseHelper.CONTACTINFO_REF);

        userInfo.setValue(_user.getUserJSON());

        // let them know that everything went fine
        Toast.makeText(this, "Your changes have been saved", Toast.LENGTH_SHORT).show();

        // tidy up back stack before piling them up
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, ProfilePreviewFragment.newInstance()).commit();
    }

}
