package com.example.lyantorres.torreslyan_pp6.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;

public class EditProfileFragment extends android.support.v4.app.Fragment {


    private EditProfileInterface mInterface;
    private static User mUser;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public static EditProfileFragment newInstance(User _user) {
        mUser = _user;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null){
            setUpListeners();
        }

        if(getActivity() != null && mUser.name != null){


            // populating the user's info only if there is any
            EditText nameET = getActivity().findViewById(R.id.edit_name);
            EditText jobTitleET = getActivity().findViewById(R.id.edit_job_title);
            EditText phoneET = getActivity().findViewById(R.id.edit_phone_number);
            EditText emailET = getActivity().findViewById(R.id.edit_email);
            EditText smallET = getActivity().findViewById(R.id.edit_small_card);
            EditText largeET = getActivity().findViewById(R.id.edit_large_card);

            nameET.setText(mUser.getName());
            jobTitleET.setText(mUser.getJobTitle());
            phoneET.setText(mUser.getPhoneNumber());
            emailET.setText(mUser.getContactEmail());
            smallET.setText(mUser.getSmallCard());
            largeET.setText(mUser.getLargeCard());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save_profile && isNetworkAvailable()){

            EditText nameET = getActivity().findViewById(R.id.edit_name);
            EditText jobTitleET = getActivity().findViewById(R.id.edit_job_title);
            EditText phoneET = getActivity().findViewById(R.id.edit_phone_number);
            EditText emailET = getActivity().findViewById(R.id.edit_email);
            EditText smallET = getActivity().findViewById(R.id.edit_small_card);
            EditText largeET = getActivity().findViewById(R.id.edit_large_card);


            // check if they are valid, if not then don't let them save
            if(isValid(nameET) && isValid(jobTitleET) && isValid(phoneET) && isValid(emailET) && isValid(smallET) && isValid(largeET)){

                String name = nameET.getText().toString();
                String jobTitle = jobTitleET.getText().toString();
                String phone = phoneET.getText().toString();
                String email = emailET.getText().toString();
                String smallCard = smallET.getText().toString();
                String largeCard = largeET.getText().toString();

                if(mInterface != null){

                    User userInfo = new User();
                    userInfo.setUserInfo(name, jobTitle, phone,email,smallCard,largeCard);

                    mInterface.saveWasPressed(userInfo);
                }
            } else {

                // not all of their inputs were valid
                Toast.makeText(getContext(), "Please make sure all fields are filled out", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }


    private boolean isNetworkAvailable() {

        // make sure they have an internet connection
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean results = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!results){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("No Internet Connection");
            dialog.setMessage("Please check your connection and try again.");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return results;
    }

    // these keep track of when the edit texts are typed in to provide live feedback
    private void setUpListeners(){

        final EditText nameET = getActivity().findViewById(R.id.edit_name);
        final EditText jobTitleET = getActivity().findViewById(R.id.edit_job_title);
        final EditText phoneET = getActivity().findViewById(R.id.edit_phone_number);
        final EditText emailET = getActivity().findViewById(R.id.edit_email);
        final EditText smallET = getActivity().findViewById(R.id.edit_small_card);
        final EditText largeET = getActivity().findViewById(R.id.edit_large_card);

        final ImageView nameFeedback = getActivity().findViewById(R.id.edit_name_feedback_IV);
        final ImageView jobFeedback = getActivity().findViewById(R.id.edit_jt_feedback_IV);
        final ImageView phoneFeedback = getActivity().findViewById(R.id.edit_pn_feedback_IV);
        final ImageView emailFeedback = getActivity().findViewById(R.id.edit_email_feedback_IV);
        final ImageView smallFeedBack = getActivity().findViewById(R.id.edit_small_feedback_IV);
        final ImageView largeFeedback = getActivity().findViewById(R.id.edit_large_feedback_IV);

        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 0){
                    // VALID
                    updateFeedback(nameET, nameFeedback, true);

                } else {
                    updateFeedback(nameET, nameFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        jobTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    // VALID
                    updateFeedback(jobTitleET, jobFeedback, true);

                } else {
                    updateFeedback(jobTitleET, jobFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 10){
                    // VALID
                    updateFeedback(phoneET, phoneFeedback, true);

                } else {
                    updateFeedback(phoneET, phoneFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.length() > 0) {
                    // VALID
                    updateFeedback(emailET, emailFeedback, true);

                } else {
                    updateFeedback(emailET,emailFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        smallET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(URLUtil.isHttpsUrl(s.toString())) {
                    updateFeedback(smallET, smallFeedBack, true);
                } else {
                    updateFeedback(smallET, smallFeedBack, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        largeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(URLUtil.isHttpsUrl(s.toString()) && s.toString().toLowerCase().contains("youtube.com")) {
                    updateFeedback(largeET, largeFeedback, true);
                } else {
                    updateFeedback(largeET, largeFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void updateFeedback(EditText _editText, ImageView _imageView, Boolean _isValid){

        // we will use the text color to verify if it's valid or not later so this really matters
        if(_isValid){
            _editText.setTextColor(getResources().getColor(R.color.black));
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_24dp));
        } else {
            _editText.setTextColor(getResources().getColor(R.color.red, getActivity().getTheme()));
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_red_24dp));
        }
    }


    private Boolean isValid(EditText _editText){
        // if it's red then it's false (not valid)
        return _editText.getCurrentTextColor() != getResources().getColor(R.color.red);

    }
}
