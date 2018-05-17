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

import com.example.lyantorres.torreslyan_pp6.Objects.DatabaseHelper;
import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

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
        void editWasClicked(User _user);
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
                DatabaseReference userContactInfo = userReference.child(DatabaseHelper.CONTACTINFO_REF);

                userContactInfo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userData = dataSnapshot.getValue(String.class);

                        if(userData != null) {
                            try {
                                JSONObject userJson = new JSONObject(userData);

                                if (userJson != null) {
                                    mUser.readInJson(userJson);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


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

                    TextView emptyInfo = getActivity().findViewById(R.id.no_info_TV);
                    LinearLayout contactInfo = getActivity().findViewById(R.id.contact_information_layout);

                    TextView userName = getActivity().findViewById(R.id.profile_name);
                    TextView userJob = getActivity().findViewById(R.id.profile_job_title);
                    TextView userPhone = getActivity().findViewById(R.id.profile_phone);
                    TextView userEmail = getActivity().findViewById(R.id.profile_email);
                    ImageView userSmall = getActivity().findViewById(R.id.small_card_IV);
                    ImageView userLarge = getActivity().findViewById(R.id.large_card_IV);


                    if(emptyInfo != null && contactInfo !=null) {

                        if (mUser.name != null) {

                            contactInfo.setVisibility(View.VISIBLE);
                            emptyInfo.setVisibility(View.INVISIBLE);

                            userName.setText(mUser.getName());
                            userJob.setText(mUser.getJobTitle());
                            userPhone.setText(mUser.getPhoneNumber());
                            userEmail.setText(mUser.getContactEmail());

                            // TODO: ADD THE ABILITY TO VIEW IMAGES THEY HAVE SELECTED
                            userSmall.setVisibility(View.INVISIBLE);
                            userLarge.setVisibility(View.INVISIBLE);
                        } else {
                            contactInfo.setVisibility(View.INVISIBLE);
                            emptyInfo.setVisibility(View.VISIBLE);

                        }
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
                mInterface.editWasClicked(mUser);
            }
        } else if (item.getItemId() ==  R.id.save_to_nfc_profile){

            if(mInterface != null){
                mInterface.saveToNFCWasClicked();
            }
        }

        return true;
    }
}
