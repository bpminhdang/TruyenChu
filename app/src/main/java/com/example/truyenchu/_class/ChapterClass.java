package com.example.truyenchu._class;
public class ChapterClass
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
}