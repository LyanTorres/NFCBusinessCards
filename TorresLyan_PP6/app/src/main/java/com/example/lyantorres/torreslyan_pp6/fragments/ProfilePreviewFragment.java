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

import com.example.lyantorres.torreslyan_pp6.R;

public class ProfilePreviewFragment extends android.support.v4.app.Fragment {


    private ProfilePreviewFragmentInterface mInterface;

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
            mInterface = (ProfilePreviewFragmentInterface) context;
        }
    }

    public interface ProfilePreviewFragmentInterface{
        void editWasClicked();
        void saveToNFCWasClicked();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        if(view != null){

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
