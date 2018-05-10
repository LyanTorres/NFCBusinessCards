package com.example.lyantorres.torreslyan_pp6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.fragments.EditProfileFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.ProfilePreviewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements ProfilePreviewFragment.ProfilePreviewFragmentInterface, EditProfileFragment.EditProfileInterface{

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, ProfilePreviewFragment.newInstance()).commit();
    }

    @Override
    public void editWasClicked() {
        getSupportFragmentManager().beginTransaction().addToBackStack("editing").replace(R.id.profile_frame, EditProfileFragment.newInstance()).commit();
    }

    @Override
    public void saveToNFCWasClicked() {
        
    }

    @Override
    public void signOutWasClicked() {
        Intent intent  = new Intent(this, MainActivity.class);
        intent.setAction("SIGNOUT");
        startActivity(intent);
        finish();
    }

    @Override
    public void saveWasPressed(String _name, String _jobTitle, String _phone, String _email, String _smallCard, String _largeCard) {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference(currentUser.getUid());

        DatabaseReference userName = userReference.child("name");
        DatabaseReference userJob = userReference.child("jobTitle");
        DatabaseReference userPhone = userReference.child("phone");
        DatabaseReference userEmail = userReference.child("email");
        DatabaseReference userSmallCard = userReference.child("smallCard");
        DatabaseReference userLargeCard = userReference.child("largeCard");

        userName.setValue(_name);
        userJob.setValue(_jobTitle);
        userPhone.setValue(_phone);
        userEmail.setValue(_email);
        userSmallCard.setValue(_smallCard);
        userLargeCard.setValue(_largeCard);

        Toast.makeText(this, "Your changes have been saved", Toast.LENGTH_SHORT).show();

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, ProfilePreviewFragment.newInstance()).commit();
    }
}
