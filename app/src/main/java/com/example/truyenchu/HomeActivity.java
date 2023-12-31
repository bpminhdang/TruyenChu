package com.example.truyenchu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.truyenchu.features.UploadStoryFragment;
import com.example.truyenchu.ui.DownloadFragment;
import com.example.truyenchu.ui.HomeFragment;
import com.example.truyenchu.ui.ProfileFragment;
import com.example.truyenchu.ui.discovery.DiscoveryNewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null)
        {
            // Nếu không có fragment đã được thêm, thêm vào
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, "YOUR_FRAGMENT_TAG")
                    .commit();
        }
        bottomNav.setSelectedItemId(R.id.navigation_home);
        bottomNav.setOnItemSelectedListener(item ->
        {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_discovery)
            {
                selectedFragment = new DiscoveryNewFragment();
            } else if (item.getItemId() == R.id.navigation_download)
            {
                selectedFragment = new DownloadFragment();
            } else if (item.getItemId() == R.id.navigation_home)
            {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_profile)
            {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navigation_search)
            {
                selectedFragment = new UploadStoryFragment();
            }

            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null) // Để thêm Fragment vào Backstack
                        .commit();
                return true;
            }

            return false;
        });


    }
}