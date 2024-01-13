package com.example.truyenchu.dev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class AddDataFirebase_dev extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_firebase_dev);
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
// Truy cập vào một node cụ thể (ví dụ: "users")
        DatabaseReference usersRef = database.child("stories");
// Duyệt qua tất cả các node con và thêm trường dữ liệu mới (ví dụ: "newField")
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    String name = (String) snapshot.child("name").getValue();
                    snapshot.getRef().child("queryName").setValue(StoryClass.NormalizedData(name));

//                    String author = (String) snapshot.child("author").getValue();
//                    snapshot.getRef().child("queryAuthor").setValue(StoryClass.NormalizedData(author));
//
//                    String genresString = "";
//                    List<String> genres = (List<String>) snapshot.child("genresList").getValue();
//                    if (genres != null)
//                    {
//                        for (String genre : genres)
//                            genresString += genre;
//                    }
                   Log.i("DEV",StoryClass.NormalizedData(name));
//
//                    snapshot.getRef().child("queryGenres").setValue(StoryClass.NormalizedData(genresString));

                   // break;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}