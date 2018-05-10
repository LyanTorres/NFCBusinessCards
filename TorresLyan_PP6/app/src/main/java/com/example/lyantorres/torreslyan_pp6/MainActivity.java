package com.example.lyantorres.torreslyan_pp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lyantorres.torreslyan_pp6.fragments.SignInFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.SignUpFragment;

public class MainActivity extends AppCompatActivity implements SignInFragment.SignInInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, SignInFragment.newInstance()).commit();
    }


    // ===================================== SIGN IN FRAGMENT INTERFACE CALLBACKS =====================================
    @Override
    public void signInWasPressed() {

    }

    @Override
    public void signUpWasPressed() {
        //TODO: ===================================== ADD CUSTOM ANIMATIONS =====================================
        getSupportFragmentManager().beginTransaction().addToBackStack("signUp").replace(R.id.main_frame, SignUpFragment.newInstance()).commit();
    }
    //  ===================================== ===================================== =====================================


}
