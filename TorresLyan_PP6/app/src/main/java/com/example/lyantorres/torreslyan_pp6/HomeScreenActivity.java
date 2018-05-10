package com.example.lyantorres.torreslyan_pp6;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Tasks.NdefReaderTask;
import com.example.lyantorres.torreslyan_pp6.fragments.ExpandedListFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.ProfilePreviewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity implements ExpandedListFragment.ExpandedListFragmentInterface, NdefReaderTask.NDEFAsyncTaskInterface{

    private NfcAdapter mNfcAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mAuth = FirebaseAuth.getInstance();

        handleIntent(getIntent());
    }

    @Override
    public void itemClicked() {

    }

    @Override
    public void profileClicked() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("text/plain".equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.i("=== LYAN ===", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void finishedReading(String _result) {

        final ArrayList<String> savedCardsStrings = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference(currentUser.getUid());

        final DatabaseReference savedCards = userReference.child("savedCards");

        savedCards.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                savedCardsStrings.add(value);
                Toast.makeText(getBaseContext(), "You have added: "+ value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        savedCards.setValue(savedCardsStrings);


        Toast.makeText(this, "Your changes have been saved", Toast.LENGTH_SHORT).show();
    }
}
