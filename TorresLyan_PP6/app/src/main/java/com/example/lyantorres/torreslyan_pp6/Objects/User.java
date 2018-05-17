package com.example.lyantorres.torreslyan_pp6.Objects;
// Lyan Torres Labiosa

// 1802

// com.example.lyantorres.torreslyan_pp6.Objects

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class User implements Serializable{

    public String name;
    public String jobTitle;
    public String phoneNumber;
    public String contactEmail;
    public String smallCard;
    public String largeCard;

    public User(){
    }

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

    public JSONArray getSavedCardsArray(String[] _array){

        JSONArray cards = new JSONArray();

        for(int i = 0; i < _array.length; i ++) {
            cards.put(_array[i]);
        }

        return cards;

    }

    public ArrayList<String> convertSavedCardsJson(JSONArray _jsonArray) throws JSONException {

        ArrayList<String> cards = new ArrayList<>();

        for(int i = 0; i < _jsonArray.length(); i ++) {
            cards.add(String.valueOf(_jsonArray.getInt(i)));
        }

        return cards;
    }

    public void setUserInfo(String _name, String _jobTitle, String _phone, String _email, String _smallCard, String _largeCard){
        name = _name;
        jobTitle = _jobTitle;
        phoneNumber = _phone;
        contactEmail = _email;
        smallCard = _smallCard;
        largeCard = _largeCard;
    }

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
}
