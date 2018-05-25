package com.example.lyantorres.torreslyan_pp6.Objects;
// Lyan Torres Labiosa

// 1802

// com.example.lyantorres.torreslyan_pp6.Objects

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable{

    private String UUID;
    public String name;
    private String jobTitle;
    private String phoneNumber;
    private String contactEmail;
    private String smallCard;
    private String largeCard;

    public User(){
    }

    // easy way to get a JSON to save to database
    public String getUserJSON(){

        JSONObject userJSON = new JSONObject();

        try {
            userJSON.put(DatabaseHelper.NAME_KEY, name);
            userJSON.put(DatabaseHelper.JOBTITLE_KEY, jobTitle);
            userJSON.put(DatabaseHelper.PHONENUMBER_KEY, phoneNumber);
            userJSON.put(DatabaseHelper.EMAIL_KEY, contactEmail);
            userJSON.put(DatabaseHelper.SMALLCARD_KEY, smallCard);
            userJSON.put(DatabaseHelper.LARGECARD_KEY, largeCard);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userJSON.toString();
    }

    // this is so that we have an easy was to convert JSON (way it is stored in database) to our USER object
    public void readInJson(JSONObject _jsonObj){

        if(_jsonObj != null){

            try {
                name = (String) _jsonObj.get(DatabaseHelper.NAME_KEY);
                jobTitle = (String) _jsonObj.get(DatabaseHelper.JOBTITLE_KEY);
                phoneNumber = (String) _jsonObj.get(DatabaseHelper.PHONENUMBER_KEY);
                contactEmail = (String) _jsonObj.get(DatabaseHelper.EMAIL_KEY);
                smallCard = (String) _jsonObj.get(DatabaseHelper.SMALLCARD_KEY);
                largeCard = (String) _jsonObj.get(DatabaseHelper.LARGECARD_KEY);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("=== LYAN ===", " ========== \n readInJson: SOMETHING WENT WRONG WHEN READING IN USER INFO FROM DATABASE \n ==========");
            }

        }

    }


    // this is to set all information as to not have to set them one by one or have a constructor that makes you have all values from the start (which works against us)
    public void setUserInfo( String _name, String _jobTitle, String _phone, String _email, String _smallCard, String _largeCard){
        name = _name;
        jobTitle = _jobTitle;
        phoneNumber = _phone;
        contactEmail = _email;
        smallCard = _smallCard;
        largeCard = _largeCard;
    }


    // GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getSmallCard() {
        return smallCard;
    }

    public String getLargeCard() {
        return largeCard;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }
}
