package com.example.truyenchu.features;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.truyenchu.R;
import com.example.truyenchu.story.SettingReadingFragment;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: accent 1_0
        getWindow().setNavigationBarColor(getColor(R.color.accent_1_10));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingReadingFragment newFragment = new SettingReadingFragment();
        fragmentTransaction.replace(R.id.av_setting_fragment_containter, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);

                        // Kiểm tra xem fragment có bị xóa hoàn toàn khỏi FragmentManager không
                        if (!f.isInLayout()) {
                            finish();

                        }
                    }
                }, false);
    }
}