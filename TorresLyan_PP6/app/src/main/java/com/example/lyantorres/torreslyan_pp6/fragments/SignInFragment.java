package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lyantorres.torreslyan_pp6.R;

public class SignInFragment extends android.support.v4.app.Fragment {

    private static SignInInterface mInterface;

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof SignInInterface){
            mInterface = (SignInInterface) context;
        }
    }

    public interface SignInInterface{
        void signInWasPressed();
        void signUpWasPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
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

            if(mInterface != null){
                mInterface.signUpWasPressed();
            }
        }
    };

    // ===================================== =====================================

    private void setUpListeners(){
        Button signUpBTN =  getActivity().findViewById(R.id.sign_up_BTN);
        signUpBTN.setOnClickListener(signUpWasClicked);

    }
}
