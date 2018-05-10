package com.example.lyantorres.torreslyan_pp6.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyantorres.torreslyan_pp6.R;

public class ExpandedListFragment extends ListFragment {


    private ExpandedListFragmentInterface mInterface;

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

        // TODO: SET ADAPTER

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
                mInterface.profileClicked();
            }
        }
        return true;
    }
}
