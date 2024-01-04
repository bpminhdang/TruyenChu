package com.example.truyenchu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.Horizontal_1_SmallImageAdapter;
import com.example.truyenchu.features.UploadStoryFragment;
import com.example.truyenchu.ui.DownloadFragment;
import com.example.truyenchu.ui.HomeFragment;
import com.example.truyenchu.ui.ProfileFragment;
import com.example.truyenchu.ui.DiscoveryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    boolean isLoggedIn = false;
    ArrayList<StoryClass> storyList = new ArrayList<>();
    ArrayList<StoryClass> storyList1 = new ArrayList<>();
    ArrayList<StoryClass> storyList2 = new ArrayList<>();
    ArrayList<ArrayList<StoryClass>> listOfStoryLists = new ArrayList<>();

    private HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listOfStoryLists.add(storyList);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser != null)
        {
            String name = mUser.getDisplayName();
            SharedPreferences sharedPreferences = getSharedPreferences("users_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", name);
            String photoURL;
            try
            {
                photoURL = mUser.getPhotoUrl().toString();
            } catch (Exception e)
            {
                photoURL = null; // Todo: Image mac dinh
                Toast.makeText(this, "Cant retrive profile image", Toast.LENGTH_SHORT).show();
            }
            editor.putString("profilePicture", photoURL);
            editor.putString("isLoggedIn", "true");
            isLoggedIn = true;
            editor.apply();
        } else
        {
            SharedPreferences sharedPreferences = getSharedPreferences("users_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "Guest");
            editor.putString("profilePicture", "R.drawable.guest_profile");
            editor.putString("isLoggedIn", "false");
            editor.apply();
        }


        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);

        //FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").setPersistenceEnabled(true);
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storiesRef = database.child("stories");
//        storiesRef.orderByChild("time").limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener()

        storiesRef.orderByChild("time").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                    {
                            // Lấy dữ liệu của từng story từ dataSnapshot
                            Map<String, Object> storyData = (Map<String, Object>) storySnapshot.getValue();

                            // Tạo đối tượng StoryClass từ dữ liệu của mỗi story
                            StoryClass story = new StoryClass((int) (long) storyData.get("id"));
                            story.setName((String) storyData.get("name"));
                            story.setTime((String) storyData.get("time"));
                            story.setUpdateTime((String) storyData.get("updateTime"));
                            story.setAuthor((String) storyData.get("author"));
                            story.setStatus((String) storyData.get("status"));
                            story.setDescription((String) storyData.get("description"));
                            story.setNumberOfChapter((int) (long) storyData.get("numberOfChapter"));
                            story.setViews((int) (long) storyData.get("views"));
                            story.setUri((String) storyData.get("uri"));

                            // Lấy danh sách genres
                            List<String> genres = (List<String>) storyData.get("genresList");
                            if (genres != null)
                            {
                                story.setGenres(genres);
                            }

                            // Lấy danh sách các chương
                            Map<String, Map<String, Object>> chaptersMap = (Map<String, Map<String, Object>>) storyData.get("chapters");
                            if (chaptersMap != null)
                            {
                                for (Map.Entry<String, Map<String, Object>> entry : chaptersMap.entrySet())
                                {
                                    ChapterClass chapter = new ChapterClass();
                                    Map<String, Object> chapterData = entry.getValue();
                                    chapter.setChapterId((String) chapterData.get("chapterId"));
                                    chapter.setContent((String) chapterData.get("content"));
                                    story.getChapters().add(chapter);
                                }
                            }
                            // Thêm story vào danh sách storyList
                            storyList.add(story);

                    }
                    //listOfStoryLists.set(0, storyList);
                    homeFragment = HomeFragment.newInstance(storyList);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .add(R.id.fragment_container, homeFragment, "YOUR_FRAGMENT_TAG")
                            .commit();


//                    adapter.notifyDataSetChanged();
//                    adapter1.notifyDataSetChanged();
//                    adapter2.notifyDataSetChanged();

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }

        });


        if (savedInstanceState == null)
        {
            // Nếu không có fragment đã được thêm, thêm vào
            homeFragment = HomeFragment.newInstance(storyList);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.fragment_container, homeFragment, "YOUR_FRAGMENT_TAG")
                    .commit();

        }
        bottomNav.setOnItemSelectedListener(item ->
        {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_discovery)
            {
                selectedFragment = new DiscoveryFragment();
            } else if (item.getItemId() == R.id.navigation_download)
            {
                selectedFragment = new DownloadFragment();
            } else if (item.getItemId() == R.id.navigation_home)
            {
                selectedFragment = HomeFragment.newInstance(storyList);
            } else if (item.getItemId() == R.id.navigation_profile)
            {
                if (!isLoggedIn)
                {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    return false;
                } else
                    selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navigation_search)
            {
                selectedFragment = new UploadStoryFragment();
            }

            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null) // Để thêm Fragment vào Backstack
                        .commit();
                return true;
            }

            return false;
        });



    }

    public static void sortStoryListByTime(ArrayList<StoryClass> storyList)
    {
        Collections.sort(storyList, (s1, s2) -> s2.getTime().compareTo(s1.getTime()));
    }

    public void updateData(Horizontal_1_SmallImageAdapter adapter, ArrayList<StoryClass> newDataList) {
        // Update data trong adapter hoặc RecyclerView của bạn với newDataList
        // Ví dụ:
        adapter.setData(newDataList);
        adapter.notifyDataSetChanged(); // Sau khi thiết lập dữ liệu mới, thông báo cho adapter cập nhật giao diện
    }
}