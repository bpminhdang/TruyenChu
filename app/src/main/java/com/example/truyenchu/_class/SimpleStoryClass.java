package com.example.truyenchu._class;

import java.util.ArrayList;

public class SimpleStoryClass
{
    int id;
    ArrayList<Integer> readChapter;
    ArrayList<Integer> favoriteChapter;

    public SimpleStoryClass(int id, ArrayList<Integer> readChapter, ArrayList<Integer> favoriteChapter)
    {
        this.id = id;
        this.readChapter = readChapter;
        this.favoriteChapter = favoriteChapter;
    }

    public SimpleStoryClass()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ArrayList<Integer> getReadChapter()
    {
        return readChapter;
    }

    public void setReadChapter(ArrayList<Integer> readChapter)
    {
        this.readChapter = readChapter;
    }

    public ArrayList<Integer> getFavoriteChapter()
    {
        return favoriteChapter;
    }

    public void setFavoriteChapter(ArrayList<Integer> favoriteChapter)
    {
        this.favoriteChapter = favoriteChapter;
    }
}
