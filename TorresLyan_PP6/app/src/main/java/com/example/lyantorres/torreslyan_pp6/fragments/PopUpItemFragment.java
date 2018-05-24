package com.example.lyantorres.torreslyan_pp6.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;


public class PopUpItemFragment extends Fragment implements ImageButton.OnClickListener{


    private static User mUser;
    private PopUpFragmentInterface mInterface;

    private VideoView mVideoView;
    private MediaController mMediacontroller;
    private ProgressBar mProgressBar;
    private Boolean mPaused = false;

    public PopUpItemFragment() {
        // Required empty public constructor
    }

    public static PopUpItemFragment newInstance(User _user) {

        mUser = _user;

        Bundle args = new Bundle();

        PopUpItemFragment fragment = new PopUpItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface PopUpFragmentInterface{
        void deleteWasPressed(String _UUID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PopUpFragmentInterface){
            mInterface = (PopUpFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setHasOptionsMenu(true);

        if(getActivity() != null){

            if(mUser != null){
                TextView nameTv = getActivity().findViewById(R.id.detail_name);
                TextView jobTv = getActivity().findViewById(R.id.detail_jobtitle);

                nameTv.setText(mUser.getName());
                jobTv.setText(mUser.getJobTitle());

                ImageButton phone = getActivity().findViewById(R.id.detail_phone);
                ImageButton email = getActivity().findViewById(R.id.detail_email);
                Button delete = getActivity().findViewById(R.id.detail_button_delete);
                mProgressBar = getActivity().findViewById(R.id.popup_progress);
                mProgressBar.setVisibility(View.GONE);

                phone.setOnClickListener(this);
                email.setOnClickListener(this);
                delete.setOnClickListener(this);

                WebView webView = getActivity().findViewById(R.id.popup_wv);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setSupportZoom(true);

                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);


                String videoId = getVideoId();
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                String frameVideo = "<iframe width=\""+dm.widthPixels/3.5+"\" height=\"250\" src=\"https://www.youtube.com/embed/"+videoId+"\" frameborder=\"0\" allowfullscreen></iframe>";
                webView.loadData(frameVideo, "text/html", "utf-8");

                if (Build.VERSION.SDK_INT > 8) {
                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                }


                // load in video
                //setUpVideoViewer();
            }
        }
    }

    private String getVideoId(){
        String[] string = mUser.getLargeCard().split("=");

        return string[string.length-1];
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.detail_phone){

            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mUser.getPhoneNumber(), null)));

        } else if (v.getId() == R.id.detail_email){

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mUser.getContactEmail()});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Potential Job Offer");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Write out your email here!");


            startActivity(Intent.createChooser(emailIntent, "Send mail"));

        } else if(v.getId() == R.id.detail_button_delete){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete this card");
            builder.setMessage("Are you sure you want to delete "+ mUser.getName()+ " forever?");
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterface != null){
                        mInterface.deleteWasPressed(mUser.getUUID());
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // don't do anything
                }
            });

            builder.setIcon(R.drawable.ic_delete_black_24dp);
            builder.show();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_item, container, false);
    }


}
