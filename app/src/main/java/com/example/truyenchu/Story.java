package com.example.truyenchu;

import android.media.Image;

import java.util.ArrayList;

// Lớp Truyen (Truyện)
public class Story
{

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getNumberOfChapter()
    {
        return numberOfChapter;
    }

    public void setNumberOfChapter(int numberOfChapter)
    {
        this.numberOfChapter = numberOfChapter;
    }

    // Todo: Thêm dữ liệu Image
    public void setChapters(ArrayList<Chapter> chapters)
    {
        this.chapters = chapters;
    }

    public String[] getGenres()
    {
        return genres;
    }

    public void setGenres(String[] genres)
    {
        this.genres = genres;
    }

    private int id;
    private String name;

    public void setTime(String time)
    {
        this.time = time;
    }

    private String time;
    private String author;
    private String status;
    private int numberOfChapter;
    //private Image image;
    private ArrayList<Chapter> chapters; // ArrayList để lưu danh sách các chương
    private String[] genres;
    // Constructor
    public Story(int id, String name, String author, String status, String time, int numberOfChapter, String[] genres)
    {
        this.id = id;
        this.name = name;
        this.author = author;
        this.status = status;
        this.time = time;
        this.numberOfChapter = numberOfChapter;
        this.chapters = new ArrayList<>();
        this.genres = genres;
    }

    // Thêm chương vào danh sách của truyện
    public void addChapter(Chapter chapter)
    {
        chapters.add(chapter);
    }

    public ArrayList<Chapter> getChapters()
    {
        return chapters;
    }

    public String getTime()
    {
        return time;
    }
}