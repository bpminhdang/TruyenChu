package com.example.truyenchu.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.truyenchu.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.navigation_home) {
                // Handle home action
                return true;
            } else // Handle notifications action
                if (item.getItemId() == R.id.navigation_discovery) {
                    // Handle dashboard action
                    return true;
                } else return item.getItemId() == R.id.navigation_download;
        });
    }
}