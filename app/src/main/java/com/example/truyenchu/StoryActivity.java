package com.example.truyenchu;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.features.StoryDescriptionFragment;

import java.io.Serializable;
import java.util.Objects;

public class StoryActivity extends AppCompatActivity
{
    StoryClass receivedStory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent = getIntent();
        if (intent == null)
            return;
        receivedStory = (StoryClass) intent.getSerializableExtra("storyData");

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
            bundle.putSerializable("receivedStory", receivedStory);
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
    }
}