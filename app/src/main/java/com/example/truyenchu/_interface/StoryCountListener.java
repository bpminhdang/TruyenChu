package com.example.truyenchu._interface;

// Use to get number of story from firebase to create ID for the next Story
public interface StoryCountListener
{
    void onStoryCountReceived(int storyCount);
}