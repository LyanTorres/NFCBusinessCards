package com.example.lyantorres.torreslyan_pp6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.fragments.PopUpItemFragment;

public class PopUpItemActivity extends AppCompatActivity implements PopUpItemFragment.PopUpFragmentInterface {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_item);
        setUpPopUpWindow();

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra("USER")) {

            mUser = (User) intent.getSerializableExtra("USER");

            getSupportFragmentManager().beginTransaction().replace(R.id.popUp_frame, PopUpItemFragment.newInstance(mUser)).commit();
        }

    }

    private void setUpPopUpWindow(){

        // making this activity smaller according to the device measurements
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        // actually assigning the values
        getWindow().setLayout((int) (screenWidth * .8), (int) (screenHeight * .8));

        WindowManager.LayoutParams parameters = getWindow().getAttributes();
        parameters.gravity = Gravity.CENTER;
        parameters.x = 0;
        parameters.x = 0;

        getWindow().setAttributes(parameters);

    }

    @Override
    public void deleteWasPressed(String _UUID) {
        Intent intent = new Intent();
        intent.putExtra("UUID", mUser.getUUID());
        setResult(101, intent);
        finish();
    }
}
