package com.example.lyantorres.torreslyan_pp6;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.fragments.SignInFragment;
import com.example.lyantorres.torreslyan_pp6.fragments.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SignInFragment.SignInInterface, SignUpFragment.SignUpInterface{

    private FirebaseAuth mAuth;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, SignInFragment.newInstance()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // TODO: do something if there is one I guess??

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


    // ===================================== SIGN UP FRAGMENT INTERFACE CALLBACKS =====================================
    @Override
    public void signUpWasPressed(String _email, String _password) {

        mUser = new User(_email, _password);

        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(getBaseContext(), "You have created a new account", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getBaseContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

}
