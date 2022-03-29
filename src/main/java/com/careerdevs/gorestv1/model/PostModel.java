package com.careerdevs.gorestv1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostModel {

    private int id;
    @JsonProperty("user_id")
    private int user_id;
    private String title;
    private String body;

    @Override
    public String toString() {
        return "PostModel{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String generateReport(){
        return id + "title is " + title +".Body is " + body;
    }


    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }



        public PostModel(){

        }
        public PostModel(String title,String body){
            this.title = title;
            this.body = body;

        }





}
