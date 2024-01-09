package com.example.truyenchu.features;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper
{
    public static void updateCount(int storyId, String path, int value)
    {
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storyRef = database.child("stories").child("story_" + storyId).child(path);
        storyRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Integer viewsValue = dataSnapshot.getValue(Integer.class);
                    if (viewsValue != null)
                    {
                        storyRef.setValue(viewsValue + value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
            }
        });
    }

    public static void getWatchingCountAndSetText(StoryClass receivedStory, TextView tvWatching, int valueToAdd)
    {
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storyRef = database.child("stories").child("story_" + receivedStory.getId()).child("watching");
        storyRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Integer watchingValue = dataSnapshot.getValue(Integer.class);
                    if (watchingValue != null)
                    {
                        StoryClass.SetText(tvWatching, "Đang xem: " + (watchingValue + valueToAdd));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
            }
        });
    }


}
