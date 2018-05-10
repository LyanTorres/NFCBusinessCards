package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.R;

public class EditProfileFragment extends android.support.v4.app.Fragment {


    private EditProfileInterface mInterface;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public static EditProfileFragment newInstance() {

        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof EditProfileInterface){
            mInterface = (EditProfileInterface) context;
        }
    }

    public interface EditProfileInterface{
        void saveWasPressed(String _name, String _jobTitle, String _phone, String _email, String _smallCard, String _largeCard);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save_profile){

            EditText nameET = getActivity().findViewById(R.id.edit_name);
            EditText jobTitleET = getActivity().findViewById(R.id.edit_job_title);
            EditText phoneET = getActivity().findViewById(R.id.edit_phone_number);
            EditText emailET = getActivity().findViewById(R.id.edit_email);
            EditText smallET = getActivity().findViewById(R.id.edit_small_card);
            EditText largeET = getActivity().findViewById(R.id.edit_large_card);


            // TODO: Make this nicer
            if(nameET.getText() != null && jobTitleET.getText() != null && phoneET.getText() != null && emailET.getText() != null && smallET.getText() != null && largeET.getText() != null){

                if(mInterface != null){
                    mInterface.saveWasPressed(nameET.getText().toString(), jobTitleET.getText().toString(), phoneET.getText().toString(), emailET.getText().toString(), smallET.getText().toString(), largeET.getText().toString());
                }
            } else {

                Toast.makeText(getContext(), "Please make sure all fields are filled out", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }
}
