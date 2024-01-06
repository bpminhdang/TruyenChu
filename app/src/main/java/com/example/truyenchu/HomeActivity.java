package com.example.truyenchu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.features.DataListener;
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
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements DataListener
{
    FirebaseAuth mAuth;
    boolean isLoggedIn = false;
    ArrayList<String> storyListAll = new ArrayList<>();  // listOfStoryLists 0
    ArrayList<String> storyListNew = new ArrayList<>(); // listOfStoryLists 1
    ArrayList<String> storyListUpdate = new ArrayList<>(); // listOfStoryLists 2
    ArrayList<String> storyListRecent = new ArrayList<>();
    /**
     * 0: storyListAll - ID tất cả truyện _____________________________________________________________
     * 1: storyListNew - ID 13 truyện mới nhất để lấy dữ liệu nhanh và đưa vào HomeFragment
     * 2: storyListUpdate - ID 8 truyện mới cập nhật để lấy dữ liệu nhanh và đưa vào HomeFragment
     */
    ArrayList<ArrayList<String>> listOfStoryLists = new ArrayList<>();

    private HomeFragment homeFragment;
    BottomNavigationView bottomNav;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // region Init
        listOfStoryLists.add(storyListAll);
        listOfStoryLists.add(storyListNew);
        listOfStoryLists.add(storyListUpdate);
        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);
        // endregion Init

        // region Profile Panel
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
        // endregion Profile Panel

        // region Get data from Firebase
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storiesRef = database.child("stories");

        // region New: get ID to send to home fragment
        storiesRef.orderByChild("time").limitToLast(13).addListenerForSingleValueEvent(new ValueEventListener()
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
                        storyListNew.add(String.valueOf(story.getId()));
                    }
                    homeFragment = HomeFragment.newInstance(listOfStoryLists);
                    homeFragment.setDataListener(HomeActivity.this);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in_1000, R.anim.fade_out)
                            .add(R.id.home_fragment_container, homeFragment)
                            .commit();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }

        });
        // endregion New: get ID to send to home fragment


        // region Update: get ID to send to home fragment
        storiesRef.orderByChild("updateTime").limitToLast(8).addListenerForSingleValueEvent(new ValueEventListener()
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
                        storyListUpdate.add(String.valueOf(story.getId()));
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }

        });
        // endregion Update: get ID to send to home fragment


        // region All: Save to local to send all fragment
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
                        storyListAll.add(String.valueOf(story.getId()));
                        saveStoryToFile(story.getId(), story);
                    }
//                    homeFragment = HomeFragment.newInstance(listOfStoryLists);
//                    getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .add(R.id.fragment_container, homeFragment, "YOUR_FRAGMENT_TAG")
//                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }

        });
        // endregion All: Save to local to send all fragment

        // endregion get data from Firebase

        // region Load Fragment
        if (savedInstanceState == null)
        {
            // Nếu không có fragment đã được thêm, thêm vào
            homeFragment = HomeFragment.newInstance(listOfStoryLists);
            homeFragment.setDataListener(this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_1000, R.anim.fade_out)
                    .add(R.id.home_fragment_container, homeFragment, "YOUR_FRAGMENT_TAG")
                    .commit();

        }
        bottomNav.setOnItemSelectedListener(item ->
                SetOnItemClick(item));
        // endregion Load Fragment


    }

    public boolean SetOnItemClick(MenuItem item)
    {
        Fragment selectedFragment = null;
        if (item.getItemId() == R.id.navigation_discovery)
        {
            selectedFragment = DiscoveryFragment.newInstance(listOfStoryLists.get(0));
        } else if (item.getItemId() == R.id.navigation_download)
        {
            selectedFragment = new DownloadFragment();
        } else if (item.getItemId() == R.id.navigation_home)
        {
            selectedFragment = HomeFragment.newInstance(listOfStoryLists);
            homeFragment.setDataListener(HomeActivity.this);

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
                    .replace(R.id.home_fragment_container, selectedFragment)
                    .setCustomAnimations(R.anim.fade_in_1000, R.anim.fade_out)
                    //.addToBackStack(null) // Để thêm Fragment vào Backstack
                    .commit();
            return true;
        }

        return false;
    }

    public boolean SetNotOnItemClick()
    {
        return true;
    }


    public void saveStoryToFile(int storyId, StoryClass story)
    {
        String fileName = storyId + ".json"; // Tên file là ID của truyện + ".json"

        // Convert đối tượng StoryClass thành chuỗi JSON
        Gson gson = new Gson();
        String storyJson = gson.toJson(story);

        // Lấy đường dẫn đến thư mục "stories" trong internal storage
        File directory = new File(getFilesDir() + "/stories");
        if (!directory.exists())
            directory.mkdirs(); // Tạo thư mục nếu nó không tồn tại

        // Ghi chuỗi dữ liệu vào file trong thư mục "stories"
        File file = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(file))
        {
            fos.write(storyJson.getBytes());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onDataReceived(String data)
    {
        Log.i("Data Listener","Receive data from fragment: " +  data);
        if (data.equals("Click Discovery"))
        {
            bottomNav.setOnItemSelectedListener(item ->
                    SetNotOnItemClick());

            bottomNav.setSelectedItemId(R.id.navigation_discovery);

            bottomNav.setOnItemSelectedListener(this::SetOnItemClick);
        }
    }
}