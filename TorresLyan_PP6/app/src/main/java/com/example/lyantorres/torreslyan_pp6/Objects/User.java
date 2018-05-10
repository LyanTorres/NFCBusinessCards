package com.example.lyantorres.torreslyan_pp6.Objects;
// Lyan Torres Labiosa

// 1802

// com.example.lyantorres.torreslyan_pp6.Objects

public class User {

    private String name;
    private String jobTitle;
    private String phoneNumber;
    private String contactEmail;
    private String smallCard;
    private String largeCard;

    public User(){
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

    public void setSmallCard(String smallCard) {
        this.smallCard = smallCard;
    }

    public void setLargeCard(String largeCard) {
        this.largeCard = largeCard;
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
