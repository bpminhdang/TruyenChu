package com.example.truyenchu._class;

import java.util.ArrayList;

// Lớp Truyen (Truyện)
public class StoryClass
{
    private int id;
    private String name;
    private String time;
    private String author;
    private String status;
    private int numberOfChapter;
    //private Image image;
    private ArrayList<ChapterClass> chapterClasses; // ArrayList để lưu danh sách các chương
    private String[] genres;
    private int views;

    // Constructor
    public StoryClass(int id, String name, String author, String status, String time, int numberOfChapter, String[] genres, int views)
    {
        this.id = id;
        this.name = name;
        this.author = author;
        this.status = status;
        this.time = time;
        this.numberOfChapter = numberOfChapter;
        this.views = views;
        this.chapterClasses = new ArrayList<>();
        this.genres = genres;
    }

    public StoryClass(int id, String name, String author, String status, String time, int numberOfChapter,ArrayList<ChapterClass> chapterClasses, String[] genres, int views)
    {
        this.id = id;
        this.name = name;
        this.author = author;
        this.status = status;
        this.time = time;
        this.numberOfChapter = numberOfChapter;
        this.views = views;
        this.chapterClasses = new ArrayList<>();
        this.chapterClasses.addAll(chapterClasses);
        this.genres = genres;
    }

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

    public String[] getGenres()
    {
        return genres;
    }

    public ArrayList<ChapterClass> getChapterClasses()
    {
        return chapterClasses;
    }

    public int getViews()
    {
        return views;
    }

    // Thêm chương vào danh sách của truyện
    public void addChapter(ChapterClass chapterClass)
    {
        chapterClasses.add(chapterClass);
    }

    public ArrayList<ChapterClass> getChapters()
    {
        return chapterClasses;
    }

    public String getTime()
    {
        return time;
    }
}