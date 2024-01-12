package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.truyenchu.R;
import com.example.truyenchu.StoryActivity;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.Horizontal_1_SmallImageAdapter;
import com.example.truyenchu.adapter.Horizontal_2_ImageAdapter;
import com.example.truyenchu.adapter.Horizontal_3_ContentAdapter;
import com.example.truyenchu.features.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment
{
    ArrayList<StoryClass> storyClasses = new ArrayList<>();


    public DownloadFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        String recentString = UserClass.GetUserInfoFromPref(getActivity(), "recent");
        String[] recentStringArray = recentString.split("_");
        for (String id : recentStringArray)
        {
            storyClasses.add(StoryClass.loadStoryFromFile(getActivity(), id));
        }

        RecyclerView recyclerView = view.findViewById(R.id.download_recycler_view_recent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        Horizontal_2_ImageAdapter adapter = new Horizontal_2_ImageAdapter(getActivity() ,storyClasses, story->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData", story);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        String uuid = UserClass.GetUserInfoFromPref(getActivity(),"uuid");
        if (uuid == null)
            return view;

        DatabaseReference currentUserRef = DatabaseHelper.GetCurrentUserReference(getActivity())
                .child("recentString");
        currentUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String recentString = dataSnapshot.getValue(String.class);
                    String[] recentStringArray = recentString.split("_");
                    storyClasses.clear();
                    for (String id : recentStringArray)
                    {
                        storyClasses.add(StoryClass.loadStoryFromFile(getActivity(), id));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        return view;
    }
}
