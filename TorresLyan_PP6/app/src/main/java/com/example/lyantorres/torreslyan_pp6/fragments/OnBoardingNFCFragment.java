package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lyantorres.torreslyan_pp6.R;

public class OnBoardingNFCFragment extends Fragment {


    private OnBoardingNFCFragmentInterface mInterface;

    public OnBoardingNFCFragment() {
        // Required empty public constructor
    }


    public static OnBoardingNFCFragment newInstance() {

        Bundle args = new Bundle();

        OnBoardingNFCFragment fragment = new OnBoardingNFCFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnBoardingNFCFragmentInterface){
            mInterface = (OnBoardingNFCFragmentInterface) context;
        }
    }

    public interface OnBoardingNFCFragmentInterface{
        void getStartedClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_nfc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity()!= null){

            Button btn = getActivity().findViewById(R.id.getStarted_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mInterface!= null){
                        mInterface.getStartedClicked();
                    }
                }
            });
        }
    }
}
