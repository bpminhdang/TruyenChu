package com.example.truyenchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.DataListener;
import com.example.truyenchu.features.DatabaseHelper;
import com.example.truyenchu.features.StoryDescriptionFragment;
import com.example.truyenchu.features.StoryReadingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StoryActivity extends AppCompatActivity implements DataListener
{
    StoryClass receivedStory;
    private boolean isRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent = getIntent();
        if (intent == null)
            return;
        receivedStory = (StoryClass) intent.getSerializableExtra("storyData");
        DatabaseHelper.updateCount(receivedStory.getId(), "watching", 1);

        Log.i("Life cycle", "OnCre");


        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);

        ConstraintLayout bottomNav = findViewById(R.id.bottom_navigation_custom_avs);


        if (savedInstanceState == null)
        {
            // Nếu không có fragment đã được thêm, thêm vào
            StoryDescriptionFragment fragment = new StoryDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("receivedStoryID", receivedStory.getId());
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_avs, fragment, "YOUR_FRAGMENT_TAG")
                    .commit();
        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, selectedFragment)
//                .addToBackStack(null) // Để thêm Fragment vào Backstack
//                .commit();
//        return true;
        ImageView bt_tai = findViewById(R.id.btDown);

        Button bt_read = findViewById(R.id.btRead);
        bt_read.setOnClickListener(v ->
        {
            // Chỉ tăng view mỗi khi tạo activity mới
            // Todo: Chỉ tăng view khi người dùng chưa từng đọc truyện (Optional)
            if (!isRead)
            {
                DatabaseHelper.updateCount(receivedStory.getId(), "views", 1);
                isRead = true;
            }

            StoryReadingFragment storyReadingFragment = StoryReadingFragment.newInstance(receivedStory.getId());
            storyReadingFragment.setDataListener(StoryActivity.this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_avs, storyReadingFragment)
                    .setCustomAnimations(R.anim.fade_in_300, R.anim.fade_out)
                    .addToBackStack(null) // Để thêm Fragment vào Backstack
                    .commit();
            FrameLayout frameLayoutNavigation = findViewById(R.id.frameLayout_avs);
            frameLayoutNavigation.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );

        });
    }

    @Override
    public void onDataReceived(String data)
    {
        Log.i("Data Listener", "rev: " + data);

        if (data.equals("Exit reading"))
        {
            findViewById(R.id.frameLayout_avs).setVisibility(View.VISIBLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
            getWindow().setNavigationBarColor(Color.WHITE);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("Life cycle", "OnStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("Life cycle", "OnDes");
        DatabaseHelper.updateCount(receivedStory.getId(), "watching", -1);

    }
}