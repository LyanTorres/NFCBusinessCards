package com.example.lyantorres.torreslyan_pp6.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;

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
import java.util.Collections;
import java.util.Comparator;

public class ExpandedListFragment extends ListFragment {

    private FirebaseAuth mAuth;
    private ExpandedListFragmentInterface mInterface;
    private ArrayList<String> mSavedCardsUUID = new ArrayList<>();
    private final ArrayList<User> mSavedCards = new ArrayList<>();
    private int mSpinnerItem = 0;
    private ArrayList<User> mListToDisplay = new ArrayList<>();

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
            // get firebase instance
            mAuth = FirebaseAuth.getInstance();
            mInterface = (ExpandedListFragmentInterface) context;
        }
    }

    public interface ExpandedListFragmentInterface{
        void itemClicked(User _user);
        void profileClicked();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            // make sure they are signed in before doing anything else
            setUpSpinner();

            // getting their saved cards from the internet using the database helper keys
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userReference = database.getReference(currentUser.getUid());
            DatabaseReference userContactInfo = userReference.child(DatabaseHelper.SAVEDCARDS_REF);

            userContactInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // we saved them as an array list
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    ArrayList<String> savedCards = dataSnapshot.getValue(t);

                    if (savedCards != null) {
                        mSavedCardsUUID.clear();
                        mSavedCardsUUID = savedCards;
                    }

                    // update the spinner which will sort them alphabetically by default
                    getSavedCardsData();
                    spinnerChanged();
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

        // setting up the search func
        SearchManager searchManager =
                (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.homescreen_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        // modify the item's being displayed by the user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.isEmpty()){
                    // make sure to update the list we are displaying to the user (this one has all of them )
                    mListToDisplay = mSavedCards;
                    updateUI();

                } else {

                    // only display the ones that meet the search criteria
                    ArrayList<User> filteredCards = new ArrayList<>();

                    for(int i = 0; i < mSavedCards.size(); i ++){

                        // make sure that everything is lower case so that doesn't become an issue when the user is trying to search through their cards
                        if(mSavedCards.get(i).getName().toLowerCase().contains(newText.toLowerCase())){
                            filteredCards.add(mSavedCards.get(i));
                        }
                    }

                    // update list being displayed
                    mListToDisplay = filteredCards;
                    updateUI();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.homescreen_profile){

            if(mInterface != null){
                mInterface.profileClicked();
            }
        }

        return true;
    }

    // this func gets the actual data from the user's contact info and converts them to an ArrayList<User>
    private void getSavedCardsData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i = 0; i < mSavedCardsUUID.size(); i ++){

            // getting database reference
            DatabaseReference userReference = database.getReference(mSavedCardsUUID.get(i));
            DatabaseReference userInfo= userReference.child(DatabaseHelper.CONTACTINFO_REF);

            final int index = i;
            userInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userData = dataSnapshot.getValue(String.class);

                    if(userData != null) {
                        try {
                            JSONObject userJson = new JSONObject(userData);

                            if (userJson != null) {
                                User newUser = new User();
                                newUser.setUUID(mSavedCardsUUID.get(index));
                                // use our user class to convert the user json to our user object
                                newUser.readInJson(userJson);

                                // update the array
                                if(!mSavedCards.contains(newUser)) {
                                    mSavedCards.add(newUser);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinnerChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            spinnerChanged();
        }
    }

    private void updateUI(){

        checkForDuplicatesInSavedCards();

        if (getActivity() != null){

            // set up the expandable list and it's listeners
            ExpandableListAdapter adapter = new ExpandableListAdapter(getContext(), mListToDisplay);
            ExpandableListView lv = getActivity().findViewById(android.R.id.list);
            lv.setAdapter(adapter);
            lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    if(mInterface != null){
                        mInterface.itemClicked(mListToDisplay.get(groupPosition));
                    }

                    return true;
                }
            });
        }
    }

    // this func is to help clear out duplicates that would happen when updating the current user's profile
    private void checkForDuplicatesInSavedCards(){
        for (int i = 0; i < mSavedCards.size(); i ++){

            for (int k = 0; k < mSavedCards.size(); k ++){

                if(i != k) {
                    if (mSavedCards.get(i).getUUID().equals(mSavedCards.get(k).getUUID())){
                        mSavedCards.remove(i);
                    }
                }
            }
        }
    }

    // this updates the list depending on what the user select to sort them by
    private void spinnerChanged(){
        if(mSpinnerItem == 0){
            Collections.sort(mSavedCards, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    return user1.getName().compareTo(user2.getName());
                }
            });
        } else {

            Collections.sort(mSavedCards, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    return user1.getJobTitle().compareTo(user2.getJobTitle());
                }
            });
        }

        mListToDisplay = mSavedCards;
        // since the list has now been sorted update the UI
        updateUI();
    }

    // make sure the spinner knows who to report to
    private void setUpSpinner(){

        if (getActivity()!= null) {
            Spinner spinner = getActivity().findViewById(R.id.sort_by_spinner);
            String[] values = getActivity().getResources().getStringArray(R.array.spinner_values);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,values);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSpinnerItem = position;
                    spinnerChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }


}