package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.lyantorres.torreslyan_pp6.Objects.DatabaseHelper;
import com.example.lyantorres.torreslyan_pp6.Objects.ExpandableListAdapter;
import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpandedListFragment extends ListFragment {

    private FirebaseAuth mAuth;
    private ExpandedListFragmentInterface mInterface;
    private ArrayList<String> mSavedCardsUUID = new ArrayList<>();
    private final ArrayList<User> mSavedCards = new ArrayList<>();

    public ExpandedListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expanded_list, container, false);
    }

    public static ExpandedListFragment newInstance() {

        Bundle args = new Bundle();

        ExpandedListFragment fragment = new ExpandedListFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ExpandedListFragmentInterface){
            mAuth = FirebaseAuth.getInstance();
            mInterface = (ExpandedListFragmentInterface) context;
        }
    }

    public interface ExpandedListFragmentInterface{
        void itemClicked();
        void profileClicked();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userReference = database.getReference(currentUser.getUid());
            DatabaseReference userContactInfo = userReference.child(DatabaseHelper.SAVEDCARDS_REF);

            userContactInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    ArrayList<String> savedCards = dataSnapshot.getValue(t);

                    if (savedCards != null) {
                        mSavedCardsUUID.clear();
                        mSavedCards.clear();

                        mSavedCardsUUID = savedCards;
                        Log.i("=== LYAN ===", "========== \n onDataChange: ARRAY SIZE: "+mSavedCardsUUID.size()+" \n ==========");

                    }

                    getSavedCardsData();
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_screen_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.homescreen_profile){

            if(mInterface != null){
                mSavedCardsUUID.clear();
                mInterface.profileClicked();
            }
        }
        return true;
    }

    private void getSavedCardsData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i = 0; i < mSavedCardsUUID.size(); i ++){
            DatabaseReference userReference = database.getReference(mSavedCardsUUID.get(i));
            DatabaseReference userInfo= userReference.child(DatabaseHelper.CONTACTINFO_REF);

            userInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userData = dataSnapshot.getValue(String.class);

                    if(userData != null) {
                        try {
                            JSONObject userJson = new JSONObject(userData);

                            if (userJson != null) {
                                User newUser = new User();
                                newUser.readInJson(userJson);
                                mSavedCards.add(newUser);
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

    private void updateUI(){

        if (getActivity() != null){
            ExpandableListAdapter adapter = new ExpandableListAdapter(getContext(), mSavedCards);
            ExpandableListView lv = getActivity().findViewById(android.R.id.list);
            lv.setAdapter(adapter);
        }
    }
}
