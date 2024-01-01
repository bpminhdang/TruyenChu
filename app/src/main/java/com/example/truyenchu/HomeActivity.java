package com.example.truyenchu;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.example.truyenchu.features.UploadStoryFragment;
import com.example.truyenchu.ui.DownloadFragment;
import com.example.truyenchu.ui.HomeFragment;
import com.example.truyenchu.ui.ProfileFragment;
import com.example.truyenchu.ui.discovery.DiscoveryNewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            }
            catch (Exception e)
            {
                photoURL = null; // Todo: Image mac dinh
                Toast.makeText(this, "Cant retrive profile image", Toast.LENGTH_SHORT).show();
            }
            editor.putString("profilePicture", photoURL);
            editor.putString("isLoggedIn", "true");
            isLoggedIn = true;
            editor.apply();
        }
        else
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
                if (!isLoggedIn)
                {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    return false;
                }
                else
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