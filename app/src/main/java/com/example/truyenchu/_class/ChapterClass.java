package com.example.truyenchu._class;
public class ChapterClass
{
    private int chapterId;
    private String content;

    public ChapterClass(int chapterId, String content)
    {
        this.chapterId = chapterId;
        this.content = content;
    }

    public int getChapterId()
    {
        return chapterId;
    }

    public void setChapterId(int chapterId)
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
}