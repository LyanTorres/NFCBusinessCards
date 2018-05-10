package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePreviewFragment extends android.support.v4.app.Fragment {


    private FirebaseAuth mAuth;
    private ProfilePreviewFragmentInterface mInterface;
    private User mUser;

    public ProfilePreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_preview, container, false);
    }

    public static ProfilePreviewFragment newInstance() {

        Bundle args = new Bundle();

        ProfilePreviewFragment fragment = new ProfilePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ProfilePreviewFragmentInterface){
            mAuth = FirebaseAuth.getInstance();
            mInterface = (ProfilePreviewFragmentInterface) context;
        }
    }

    public interface ProfilePreviewFragmentInterface{
        void editWasClicked();
        void saveToNFCWasClicked();
        void signOutWasClicked();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        if(view != null){

            Button signOutBTN = getActivity().findViewById(R.id.sign_out);

            signOutBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface != null){
                        mInterface.signOutWasClicked();
                    }
                }
            });

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser != null){

                mUser = new User();

                TextView emptyTV = getActivity().findViewById(R.id.no_info_TV);
                LinearLayout info = getActivity().findViewById(R.id.contact_information_layout);

                emptyTV.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userReference = database.getReference(currentUser.getUid());

                DatabaseReference userName = userReference.child("name");
                DatabaseReference userJob = userReference.child("jobTitle");
                DatabaseReference userPhone = userReference.child("phone");
                DatabaseReference userEmail = userReference.child("email");
                DatabaseReference userSmallCard = userReference.child("smallCard");
                DatabaseReference userLargeCard = userReference.child("largeCard");

                userName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setName(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userJob.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setJobTitle(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userPhone.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setPhoneNumber(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userEmail.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setContactEmail(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userSmallCard.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setSmallCard(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userLargeCard.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        mUser.setLargeCard(value);
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                updateUI();
            }
        }
    }

    private void updateUI(){

            if (mUser != null) {

                if (getActivity() != null) {
                    TextView userName = getActivity().findViewById(R.id.profile_name);
                    TextView userJob = getActivity().findViewById(R.id.profile_job_title);
                    TextView userPhone = getActivity().findViewById(R.id.profile_phone);
                    TextView userEmail = getActivity().findViewById(R.id.profile_email);
                    ImageView userSmall = getActivity().findViewById(R.id.small_card_IV);
                    ImageView userLarge = getActivity().findViewById(R.id.large_card_IV);

                    if (userName != null && userJob != null && userPhone != null && userEmail != null && userSmall != null && userLarge != null) {
                        userName.setText(mUser.getName());
                        userJob.setText(mUser.getJobTitle());
                        userPhone.setText(mUser.getPhoneNumber());
                        userEmail.setText(mUser.getContactEmail());
                        userSmall.setVisibility(View.INVISIBLE);
                        userLarge.setVisibility(View.INVISIBLE);
                    }
                }
            }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_profile){

            if(mInterface != null){
                mInterface.editWasClicked();
            }
        } else if (item.getItemId() ==  R.id.save_to_nfc_profile){

            if(mInterface != null){
                mInterface.saveToNFCWasClicked();
            }
        }

        return true;
    }


}
