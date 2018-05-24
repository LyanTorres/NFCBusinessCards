package com.example.lyantorres.torreslyan_pp6.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyantorres.torreslyan_pp6.Objects.DatabaseHelper;
import com.example.lyantorres.torreslyan_pp6.Objects.User;
import com.example.lyantorres.torreslyan_pp6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePreviewFragment extends android.support.v4.app.Fragment {


    private FirebaseAuth mAuth;
    private ProfilePreviewFragmentInterface mInterface;
    private User mUser;

    public ProfilePreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_preview, container, false);
    }

    public static ProfilePreviewFragment newInstance() {

        Bundle args = new Bundle();

        ProfilePreviewFragment fragment = new ProfilePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ProfilePreviewFragmentInterface){
            mAuth = FirebaseAuth.getInstance();
            mInterface = (ProfilePreviewFragmentInterface) context;
        }
    }


    public interface ProfilePreviewFragmentInterface{
        void editWasClicked(User _user);
        void saveToNFCWasClicked();
        void signOutWasClicked();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        if(view != null){

            Button signOutBTN = getActivity().findViewById(R.id.sign_out);

            signOutBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface != null){
                        mInterface.signOutWasClicked();
                    }
                }
            });

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser != null) {

                mUser = new User();

                TextView emptyTV = getActivity().findViewById(R.id.no_info_TV);
                LinearLayout info = getActivity().findViewById(R.id.contact_information_layout);

                emptyTV.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference userReference = database.getReference(currentUser.getUid());
                    DatabaseReference userContactInfo = userReference.child(DatabaseHelper.CONTACTINFO_REF);

                    userContactInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userData = dataSnapshot.getValue(String.class);

                            if (userData != null) {
                                try {
                                    JSONObject userJson = new JSONObject(userData);

                                    if (userJson != null) {
                                        mUser.readInJson(userJson);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            updateUI();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    updateUI();
                }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean results = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!results){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("No Internet Connection");
            dialog.setMessage("Please check your connection and try again.");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return results;
    }

    private void updateUI(){

            if (mUser != null) {

                if (getActivity() != null) {

                    TextView emptyInfo = getActivity().findViewById(R.id.no_info_TV);
                    LinearLayout contactInfo = getActivity().findViewById(R.id.contact_information_layout);

                    TextView userName = getActivity().findViewById(R.id.profile_name);
                    TextView userJob = getActivity().findViewById(R.id.profile_job_title);
                    TextView userPhone = getActivity().findViewById(R.id.profile_phone);
                    TextView userEmail = getActivity().findViewById(R.id.profile_email);
                    ImageView userSmall = getActivity().findViewById(R.id.small_card_IV);
                    WebView webView = getActivity().findViewById(R.id.large_card_WV);
                    final ScrollView sv = getActivity().findViewById(R.id.profile_scrollView);
                    final ProgressBar pb = getActivity().findViewById(R.id.profile_progressBar);


                    if(emptyInfo != null && contactInfo !=null) {

                        if (mUser.name != null) {
                            pb.setVisibility(View.VISIBLE);
                            contactInfo.setVisibility(View.VISIBLE);
                            emptyInfo.setVisibility(View.INVISIBLE);

                            userName.setText("Name: " + mUser.getName());
                            userJob.setText("Job title: " + mUser.getJobTitle());
                            userPhone.setText("Phone: " + mUser.getPhoneNumber());
                            userEmail.setText("Email: " + mUser.getContactEmail());
                            sv.setVisibility(View.INVISIBLE);


                            if (isNetworkAvailable()) {

                                Picasso.with(getContext())
                                        .load(mUser.getSmallCard())
                                        .into(userSmall, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                sv.setVisibility(View.VISIBLE);
                                                pb.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {
                                                sv.setVisibility(View.VISIBLE);
                                                pb.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Something went wrong loading in your cards", Toast.LENGTH_SHORT);
                                            }
                                        });

                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.getSettings().setSupportZoom(true);

                                webView.setWebViewClient(new WebViewClient() {
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

                                String frameVideo = "<iframe width=\"" + dm.widthPixels / 3.5 + "\" height=\"250\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>";
                                webView.loadData(frameVideo, "text/html", "utf-8");

                                if (Build.VERSION.SDK_INT > 8) {
                                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                                }
                            }


                            } else {
                                contactInfo.setVisibility(View.INVISIBLE);
                                emptyInfo.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);

                            }
                    }
                }
            }
    }


    private String getVideoId(){
        String[] string = mUser.getLargeCard().split("=");

        return string[string.length-1];
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_profile){

            if(mInterface != null){
                mInterface.editWasClicked(mUser);
            }
        } else if (item.getItemId() ==  R.id.save_to_nfc_profile){

            if(mUser.getName() != null && !mUser.getName().isEmpty()) {
                if (mInterface != null) {
                    mInterface.saveToNFCWasClicked();
                }
            } else {
                Toast.makeText(getContext(), "You have to set up your profile first.", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }
}
