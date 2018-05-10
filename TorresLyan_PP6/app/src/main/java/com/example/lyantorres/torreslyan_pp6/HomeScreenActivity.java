package com.example.lyantorres.torreslyan_pp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyantorres.torreslyan_pp6.fragments.ExpandedListFragment;

public class HomeScreenActivity extends AppCompatActivity implements ExpandedListFragment.ExpandedListFragmentInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.homescreen_frame, ExpandedListFragment.newInstance()).commit();
    }

    @Override
    public void itemClicked() {

    }

    @Override
    public void profileClicked() {

    }
}
