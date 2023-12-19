package com.example.truyenchu._class;

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
    private int numberOfChapter;
    //private Image image;
    private List<ChapterClass> chapters = new ArrayList<>(); // List để lưu danh sách các chương
    private List<String> genres;
    private int views;

    // Constructor
    public StoryClass(int id, String name, String author, String status, String time, int numberOfChapter, List<String> genres, int views)
    {
        this.id = id;
        this.name = name;
        this.author = author;
        this.status = status;
        this.time = time;
        this.numberOfChapter = numberOfChapter;
        this.views = views;
        this.genres = genres;
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

    // Todo: Thêm get dữ liệu Image

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
}