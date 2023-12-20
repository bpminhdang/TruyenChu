package com.example.truyenchu._class;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

// Lớp Truyen (Truyện)
public class StoryClass
{
    private final int id;
    private String name;
    private String time;
    private String author;
    private String status;
    private String description;

    private int numberOfChapter;
    //private Image image;
    private List<ChapterClass> chapters = new ArrayList<>(); // List để lưu danh sách các chương
    private List<String> genres;
    private int views;

    private String uri; // Retrive data from Firebase



    // Constructor
    public StoryClass(int id, String name, String time, String author, String status, String description, int numberOfChapter, List<String> genres, int views)
    {
        this.id = id;
        this.name = name;
        this.time = time;
        this.author = author;
        this.status = status;
        this.description = description;
        this.numberOfChapter = numberOfChapter;
        this.genres = genres;
        this.views = views;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getName(int cutOffWhenMoreThan)
    {
        if (name.length() > cutOffWhenMoreThan)
        {
            int lastIndex = name.lastIndexOf(' ', cutOffWhenMoreThan);
            if (lastIndex != -1)
            {
                return name.substring(0, lastIndex) + "...";
            }
        }
        return name;
    }


    public String getAuthor()
    {
        return author;
    }

    public String getStatus()
    {
        return status;
    }

    public int getNumberOfChapter()
    {
        return numberOfChapter;
    }

    public List<String> getGenres()
    {
        return genres;
    }

    public List<ChapterClass> getChapters()
    {
        return chapters;
    }

    public int getViews()
    {
        return views;
    }

    // Thêm chương vào danh sách của truyện
    public void addChapter(ChapterClass chapterClass)
    {
        chapters.add(chapterClass);
    }


    public String getTime()
    {
        return time;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUri()
    {
        return uri;
    }
}