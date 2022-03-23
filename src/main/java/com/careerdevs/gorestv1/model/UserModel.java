package com.careerdevs.gorestv1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserModel {

    //@JsonProperty("id")
    private int id;


    private String name;
    private String email;
    private String gender;
    private String status;

    //DO NOT DELETE OR CHANE
    public UserModel(){

    }
    public UserModel(String name, String email, String gender, String status) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }
    public String generateReport(){
        return name + "is currently " + status +".You can contact them at: " +email;
    }
    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
