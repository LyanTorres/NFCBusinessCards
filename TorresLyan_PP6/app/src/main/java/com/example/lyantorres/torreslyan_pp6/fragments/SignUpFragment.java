package com.example.lyantorres.torreslyan_pp6.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lyantorres.torreslyan_pp6.R;
public class SignUpFragment extends android.support.v4.app.Fragment {

    private static SignUpInterface mInterface;
    private static String mValidPassword;
    private static String mValidEmail;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof SignUpInterface){
            mInterface = (SignUpInterface) context;
        }
    }

    public interface SignUpInterface {
        void signUpWasPressed(String _email, String _password);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(view != null){
            setUpListeners();
        }
    }

    //  ===================================== LISTENERS =====================================
    private View.OnClickListener signUpWasClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mValidEmail != null && mValidPassword != null) {

                if (mInterface != null) {
                    mInterface.signUpWasPressed(mValidEmail, mValidPassword);
                }
            }
        }
    };

    // ===================================== =====================================

    private void setUpListeners(){

        Button signUpBTN = getActivity().findViewById(R.id.sign_up_sign_up_BTN);
        signUpBTN.setOnClickListener(signUpWasClicked);

        EditText email = getActivity().findViewById(R.id.sign_up_email_ET);
        EditText password = getActivity().findViewById(R.id.sign_up_password_ET);

        email.addTextChangedListener(new TextWatcher() {

            final EditText emailET = (EditText)getActivity().findViewById(R.id.sign_up_email_ET);

            ImageView emailFeedback = (ImageView)getActivity().findViewById(R.id.sign_up_email_feedback_IV);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.length() > 0) {
                    // is valid
                    updateFeedback(emailET, emailFeedback, true);
                    mValidEmail = s.toString();
                } else  {
                    // Isn't valid
                    updateFeedback(emailET, emailFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            EditText passwordET = (EditText)getActivity().findViewById(R.id.sign_up_password_ET);
            ImageView passwordFeedback = (ImageView)getActivity().findViewById(R.id.sign_up_password_feedback_IV);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 6){
                    // is valid
                    updateFeedback(passwordET, passwordFeedback, true);
                    mValidPassword = s.toString();
                } else {
                    updateFeedback(passwordET, passwordFeedback, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void updateFeedback(EditText _editText, ImageView _imageView, Boolean _isValid){

        if(_isValid){
            _editText.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_24dp));
        } else {
            _editText.setTextColor(getResources().getColor(R.color.red, getActivity().getTheme()));
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_red_24dp));
        }
    }

}
