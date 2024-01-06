package com.example.truyenchu._class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentClass implements Serializable
{
    public CommentClass(String username, double rating, String comment, ArrayList<String> uuidLikedUsers)
    {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.uuidLikedUsers = uuidLikedUsers;
    }

    public CommentClass()
    {
    }

    private String username;
    private double rating;
    private String comment;

    private ArrayList<String> uuidLikedUsers = new ArrayList<>();

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public ArrayList<String> getUuidLikedUsers()
    {
        return uuidLikedUsers;
    }

    public void setUuidLikedUsers(ArrayList<String> uuidLikedUsers)
    {
        this.uuidLikedUsers = uuidLikedUsers;
    }
}
