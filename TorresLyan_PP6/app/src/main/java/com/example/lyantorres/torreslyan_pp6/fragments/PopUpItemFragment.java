package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;


public class PopUpItemFragment extends Fragment implements ImageButton.OnClickListener{


    private static User mUser;
    private PopUpFragmentInterface mInterface;

    public PopUpItemFragment() {
        // Required empty public constructor
    }

    public static PopUpItemFragment newInstance(User _user) {

        mUser = _user;

        Bundle args = new Bundle();

        PopUpItemFragment fragment = new PopUpItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface PopUpFragmentInterface{
        void deleteWasPressed(String _UUID);
        void callWasPressed(String _phone);
        void emailWasPressed(String _email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PopUpFragmentInterface){
            mInterface = (PopUpFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        if(getActivity() != null){

            if(mUser != null){
                TextView nameTv = getActivity().findViewById(R.id.detail_name);
                TextView jobTv = getActivity().findViewById(R.id.detail_jobtitle);

                nameTv.setText(mUser.getName());
                jobTv.setText(mUser.getJobTitle());

                //TODO: UPDATE IMAGEVIEW
                ImageView iv = getActivity().findViewById(R.id.detail_largeCard);

                ImageButton phone = getActivity().findViewById(R.id.detail_phone);
                ImageButton email = getActivity().findViewById(R.id.detail_email);

                phone.setOnClickListener(this);
                email.setOnClickListener(this);
            }
        }
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.detail_phone){

            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mUser.getPhoneNumber(), null)));

        } else if (v.getId() == R.id.detail_email){

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mUser.getContactEmail()});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Potential Job Offer");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Write out your email here!");


            startActivity(Intent.createChooser(emailIntent, "Send mail"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_pop_up_item, container, false);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.details_delete){


        }
        return true;
    }
}
