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

import com.example.lyantorres.torreslyan_pp6.Objects.User;
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
        void saveWasPressed(User _user);
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

            String name = nameET.getText().toString();
            String jobTitle = jobTitleET.getText().toString();
            String phone = phoneET.getText().toString();
            String email = emailET.getText().toString();
            String smallCard = smallET.getText().toString();
            String largeCard = largeET.getText().toString();

            if(name != null && jobTitle != null && phone != null && email != null && smallCard != null && largeCard != null){

                if(mInterface != null){

                    User userInfo = new User();
                    userInfo.setUserInfo(name, jobTitle, phone,email,smallCard,largeCard);

                    mInterface.saveWasPressed(userInfo);
                }
            } else {

                Toast.makeText(getContext(), "Please make sure all fields are filled out", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }
}
