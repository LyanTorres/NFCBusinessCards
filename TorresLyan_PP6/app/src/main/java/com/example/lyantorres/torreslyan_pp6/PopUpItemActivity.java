package com.example.lyantorres.torreslyan_pp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class PopUpItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_item);
        setUpPopUpWindow();
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
}
