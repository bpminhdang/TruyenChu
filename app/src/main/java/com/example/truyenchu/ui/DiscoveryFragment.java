package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.truyenchu.StoryActivity;
import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.VerticalContentAdapter;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<StoryClass> storyList = new ArrayList<>();

    public DiscoveryFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance(String param1, String param2)
    {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void ResetColorAndSet(ArrayList<Button> btList, int position)
    {
        for (Button button : btList)
        {
            button.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.accent_1_10));
            button.setTextColor(getResources().getColor(R.color.accent_1_700));
        }
        btList.get(position).setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.accent_1_600));
        btList.get(position).setTextColor(getResources().getColor(R.color.accent_1_10));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_discovery_new, container, false);



        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();

        ProfilePanelFragment fragment = new ProfilePanelFragment();
        transaction.replace(R.id.dis_profile_panel_container, fragment);
        transaction.commit();

        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storiesRef = database.child("stories");

        RecyclerView recyclerView = view.findViewById(R.id.dis_recycler_view);
        Button btnNew = view.findViewById(R.id.dis_bt_new);
        Button btnUpdate = view.findViewById(R.id.dis_bt_update);
        Button btnFull = view.findViewById(R.id.dis_bt_full);
        Button btnRating = view.findViewById(R.id.dis_bt_rating);
        Button btnView = view.findViewById(R.id.dis_bt_view);
        Button btnFavorite = view.findViewById(R.id.dis_bt_favorite);
        ArrayList<Button> btList = new ArrayList<>();
        btList.add(btnNew);
        btList.add(btnUpdate);
        btList.add(btnFull);
        btList.add(btnRating);
        btList.add(btnView);
        btList.add(btnFavorite);
        Button btnFilter = view.findViewById(R.id.dis_fillter_button);


        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColorAndSet(btList, 0);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColorAndSet(btList, 1);
            }
        });

        VerticalContentAdapter adapter = new VerticalContentAdapter(getActivity(), storyList, story ->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData",(Serializable) story);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        storiesRef.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                    {
                        if (storySnapshot.getKey().startsWith("story_"))
                        {
                            // Lấy dữ liệu của từng story từ dataSnapshot
                            Map<String, Object> storyData = (Map<String, Object>) storySnapshot.getValue();

                            // Tạo đối tượng StoryClass từ dữ liệu của mỗi story
                            StoryClass story = new StoryClass((int) (long) storyData.get("id"));
                            story.setName((String) storyData.get("name"));
                            story.setTime((String) storyData.get("time"));
                            story.setAuthor((String) storyData.get("author"));
                            story.setStatus((String) storyData.get("status"));
                            story.setDescription((String) storyData.get("description"));
                            story.setNumberOfChapter((int) (long) storyData.get("numberOfChapter"));
                            story.setViews((int) (long) storyData.get("views"));
                            story.setUri((String) storyData.get("uri"));

                            // Lấy danh sách genres
                            List<String> genres = (List<String>) storyData.get("genres");
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
                            Log.i("DBValue", story.toString());
                            // Thêm story vào danh sách storyList
                            storyList.add(story);
                            adapter.notifyDataSetChanged();
                        }
                    }
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