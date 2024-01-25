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
import com.example.truyenchu.story.StoryActivity;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.Horizontal_2_ImageAdapter;
import com.example.truyenchu.adapter.VerticalContentAdapter;
import com.example.truyenchu._interface.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SavedFragment extends Fragment
{
    ArrayList<StoryClass> storyClassesRecent = new ArrayList<>();
    ArrayList<StoryClass> storyClassesSaved = new ArrayList<>();


    public SavedFragment()
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser == null)
            return view;


        String recentString = UserClass.GetUserInfoFromPref(getActivity(), "recent");
        if (!Objects.equals(recentString, "") && recentString!= null)
        {
                String[] recentStringArray = recentString.split("_");
                for (String id : recentStringArray)
                    storyClassesRecent.add(StoryClass.loadStoryFromFile(getActivity(), id));
        }

        String savedString = UserClass.GetUserInfoFromPref(getActivity(), "saved");
        if (!Objects.equals(savedString, "") && savedString != null)
        {
            String[] savedStringArray = savedString.split("_");
            for (String id : savedStringArray)
                storyClassesSaved.add(StoryClass.loadStoryFromFile(getActivity(), id));
        }


        RecyclerView recyclerViewRecent = view.findViewById(R.id.download_recycler_view_recent);
        RecyclerView recyclerViewSaved = view.findViewById(R.id.download_recycler_view_saved);

        recyclerViewRecent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSaved.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Horizontal_2_ImageAdapter adapter = new Horizontal_2_ImageAdapter(getActivity(), storyClassesRecent, story ->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData", story);
            startActivity(intent);
        });

        VerticalContentAdapter adapter1 = new VerticalContentAdapter(getActivity(), storyClassesSaved, story ->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData", story);
            startActivity(intent);
        });

        recyclerViewRecent.setAdapter(adapter);
        recyclerViewSaved.setAdapter(adapter1);


        String uuid = UserClass.GetUserInfoFromPref(getActivity(), "uuid");
        if (uuid == null)
            return view;

        DatabaseReference currentUserRef = DatabaseHelper.GetCurrentUserReference(getActivity());

        currentUserRef.child("recentString").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String recentString = dataSnapshot.getValue(String.class);
                    storyClassesRecent.clear();
                    if (!Objects.equals(recentString, ""))
                    {
                        String[] recentStringArray = recentString.split("_");
                        for (String id : recentStringArray)
                            storyClassesRecent.add(StoryClass.loadStoryFromFile(getActivity(), id));
                    }
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        currentUserRef.child("saved").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String savedString = dataSnapshot.getValue(String.class);
                    storyClassesSaved.clear();
                    if (!Objects.equals(savedString, ""))
                    {
                        String[] savedStringArray = savedString.split("_");
                        for (String id : savedStringArray)
                            storyClassesSaved.add(StoryClass.loadStoryFromFile(getActivity(), id));
                    }

                    adapter1.notifyDataSetChanged();
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
