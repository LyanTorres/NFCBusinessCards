package com.example.lyantorres.torreslyan_pp6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.fragments.OnBoardingNFCFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.SignInFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SignInFragment.SignInInterface, SignUpFragment.SignUpInterface, OnBoardingNFCFragment.OnBoardingNFCFragmentInterface{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right).replace(R.id.main_frame, SignInFragment.newInstance()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Checking if the user was already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            if(getIntent() != null) {

                if(getIntent().getAction().equals("SIGNOUT")){
                    mAuth.signOut();
                } else {

                    // they are signed in so don't make them sign in again
                    Intent intent = new Intent(this, HomeScreenActivity.class);
                    startActivity(intent);

                    finish();
                }

            }
        }

    }

    // ===================================== SIGN IN FRAGMENT INTERFACE CALLBACKS =====================================
    @Override
    public void signInWasPressed(String _email, String _password) {

        // authenticating user and making sure they have an account
        mAuth.signInWithEmailAndPassword(_email, _password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
                    startActivity(intent);

                    finish();

                } else {

                    // Something went wrong so let the user know
                    Toast.makeText(getBaseContext(), "Invalid email or password, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void signUpWasPressed() {
        getSupportFragmentManager().beginTransaction().addToBackStack("signUp").setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right).replace(R.id.main_frame, SignUpFragment.newInstance()).commit();
    }


    // ===================================== SIGN UP FRAGMENT INTERFACE CALLBACKS =====================================
    @Override
    public void signUpWasPressed(String _email, String _password) {

        // Add their email and password to firebase's authentication so that they can log in in the future
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right).replace(R.id.main_frame, OnBoardingNFCFragment.newInstance()).commit();

                        } else {
                            // Let the user know that something went wrong with their sign up
                            Toast.makeText(getBaseContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

    @Override
    public void getStartedClicked() {
        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(getBaseContext(), "You have created a new account", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
        startActivity(intent);
        finish();

    }
}
