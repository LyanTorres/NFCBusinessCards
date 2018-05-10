package com.example.lyantorres.torreslyan_pp6.Objects;
// Lyan Torres Labiosa

// 1802

// com.example.lyantorres.torreslyan_pp6.Objects

public class User {

    private String email;
    private String password;
    private String name;
    private String jobTitle;
    private String phoneNumber;
    private String contactEmail;

    public User(String _email, String _password){
        email = _email;
        password = _password;
    }




    public void setName(String name) {
        this.name = name;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
}
