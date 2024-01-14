package com.example.truyenchu.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.truyenchu.HomeActivity;
import com.example.truyenchu.Login;
import com.example.truyenchu.R;
import com.example.truyenchu.adapter.BlankFragment;
import com.example.truyenchu.features.CSBM;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.example.truyenchu.features.SettingActivity;
import com.example.truyenchu.features.SettingReadingFragment;
import com.example.truyenchu.features.UpdateStoryActivity;
import com.example.truyenchu.features.UploadStoryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        view.findViewById(R.id.btSignOut).setOnClickListener(v ->
        {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("users_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid", null);
            editor.remove("recent");
            editor.remove("saved");
            editor.apply();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.pro_upload).setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), UploadStoryActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.pro_update).setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), UpdateStoryActivity.class);
            startActivity(intent);
            //requireActivity().finish();
        });

        view.findViewById((R.id.csbm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CSBM.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.pro_recent).setOnClickListener(v ->
        {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_download);
        });
        view.findViewById(R.id.pro_download).setOnClickListener(v ->
        {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_download);
        });

        view.findViewById(R.id.giaodien).setOnClickListener(v->
        {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.gopy).setOnClickListener(v ->
        {
            String emailAddress = "antruyenne@gmail.com";

            // Get the clipboard manager
            ClipboardManager clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);

            // Create a ClipData object to store the copied data
            ClipData clipData = ClipData.newPlainText("email", emailAddress);

            // Set the ClipData to the clipboard
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
            }
            Toast.makeText(requireContext(), "Email copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}