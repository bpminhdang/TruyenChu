package com.example.truyenchu._class;

import java.io.Serializable;

public class ChapterClass implements Serializable
{
    private String chapterId;
    private String content;

    public ChapterClass(String chapterId, String content)
    {
        this.chapterId = chapterId;
        this.content = content;
    }

    public ChapterClass()
    {
    }

    public String getChapterId()
    {
        return chapterId;
    }

    public void setChapterId(String chapterId)
    {
        this.chapterId = chapterId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
    public int GetChapterIDInt()
    {
        // Tách chuỗi thành mảng sử dụng dấu _
        String[] parts = chapterId.split("_");
        int id;
        try
        {
            id = Integer.parseInt(parts[1]);
        }
        catch (Exception e)
        {
            id = -1;
        }
        return id;
    }
}