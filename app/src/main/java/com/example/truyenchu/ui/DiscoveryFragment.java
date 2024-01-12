package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.Toast;

import com.example.truyenchu.StoryActivity;
import com.example.truyenchu.R;
import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.VerticalContentAdapter;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LIST_STORY_ID = "listStoryID";
    private static final String ARG_BUTTON_ID = "btID";
    private int buttonID = 0;
    ArrayList<Button> btList = new ArrayList<>();



    private String mParam1;
    private String mParam2;
    ArrayList<String> storyListString = new ArrayList<>();
    ArrayList<String> storyListStringUpdate = new ArrayList<>();
    ArrayList<StoryClass> storyListObject = new ArrayList<>();
    VerticalContentAdapter adapter = new VerticalContentAdapter(getActivity(), storyListObject, story ->
    {
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra("storyData", story);
        startActivity(intent);
    });
    ;

    public DiscoveryFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoveryNewFragment.
     */
    public static DiscoveryFragment newInstance(ArrayList<String> storyList)
    {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_STORY_ID, storyList);
        fragment.setArguments(args);
        return fragment;
    }

    public static DiscoveryFragment newInstance(ArrayList<String> storyList, int buttonID)
    {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_STORY_ID, storyList);
        args.putInt(ARG_BUTTON_ID, buttonID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            storyListString = (ArrayList<String>) getArguments().getSerializable(ARG_LIST_STORY_ID);
            assert storyListString != null;
            for (String storyID : storyListString)
                storyListObject.add(loadStoryFromFile(storyID));
            if (getArguments().containsKey(ARG_BUTTON_ID)) {
                buttonID = getArguments().getInt(ARG_BUTTON_ID);
            }
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
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storiesRef = database.child("stories");

        storiesRef.orderByChild("updateTime").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
               // storyListStringUpdate.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                    {
                        // Lấy dữ liệu của từng story từ dataSnapshot
                        Map<String, Object> storyData = (Map<String, Object>) storySnapshot.getValue();

                        // Tạo đối tượng StoryClass từ dữ liệu của mỗi story
                        StoryClass story = new StoryClass((int) (long) storyData.get("id"));
                        story.setName((String) storyData.get("name"));
                        story.setTime((String) storyData.get("time"));
                        story.setUpdateTime((String) storyData.get("updateTime"));
                        story.setAuthor((String) storyData.get("author"));
                        story.setStatus((String) storyData.get("status"));
                        story.setDescription((String) storyData.get("description"));
                        story.setNumberOfChapter((int) (long) storyData.get("numberOfChapter"));
                        story.setViews((int) (long) storyData.get("views"));
                        story.setUri((String) storyData.get("uri"));

                        // Lấy danh sách genres
                        List<String> genres = (List<String>) storyData.get("genresList");
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
                        // Thêm story vào danh sách storyList
                        storyListStringUpdate.add(String.valueOf(story.getId()));
                    }
                }
                if (buttonID != 0 )
                {
                    Button btToClick = view.findViewById(R.id.dis_bt_update);
                    btToClick.performClick();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.dis_recycler_view);
        Button btnNew = view.findViewById(R.id.dis_bt_new);
        Button btnUpdate = view.findViewById(R.id.dis_bt_update);
        Button btnFull = view.findViewById(R.id.dis_bt_full);
        Button btnRating = view.findViewById(R.id.dis_bt_rating);
        Button btnView = view.findViewById(R.id.dis_bt_view);
        Button btnFavorite = view.findViewById(R.id.dis_bt_favorite);
        btList.add(btnNew);
        btList.add(btnUpdate);
        btList.add(btnFull);
        btList.add(btnRating);
        btList.add(btnView);
        btList.add(btnFavorite);
        if (buttonID != 0)
        {
            ResetColorAndSet(btList, buttonID);
            storyListObject.clear();
        }

        for (int i = 0; i < btList.size(); i++)
        {
            final int index = i; // Create a final variable to capture the value of i
            btList.get(i).setOnClickListener(v ->
            {
                ResetColorAndSet(btList, index);

                if (index == 0)
                {
                    storyListObject.clear();
                    for (String storyID : storyListString)
                    {
                        storyListObject.add(loadStoryFromFile(storyID));
                    }
                    adapter.updateData(storyListObject);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                }
                if (index == 2)
                {
                    storyListObject.clear();
                    for (String storyID : storyListString)
                    {
                        StoryClass story = loadStoryFromFile(storyID);
                        if (story.getStatus().equals("Full"))
                            storyListObject.add(story);
                    }
                    adapter.updateData(storyListObject);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                }

                if (index == 1)
                {
                    storyListObject.clear();
                    for (String storyID : storyListStringUpdate)
                    {
                        storyListObject.add(loadStoryFromFile(storyID));
                    }

                    adapter.updateData(storyListObject);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }

            });
        }

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        return view;
    }

    public StoryClass loadStoryFromFile(String storyId)
    {
        StoryClass loadedStory = null;
        String fileName = storyId + ".json"; // Tên file là ID của truyện + ".json"

        // Lấy đường dẫn đến thư mục "stories" trong internal storage
        File directory = new File(getActivity().getFilesDir() + "/stories");
        File file = new File(directory, fileName);

        if (file.exists())
        {
            try (FileInputStream fis = new FileInputStream(file))
            {
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                String storyJson = new String(buffer);

                // Chuyển đổi chuỗi JSON thành đối tượng StoryClass
                Gson gson = new Gson();
                loadedStory = gson.fromJson(storyJson, StoryClass.class);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return loadedStory;
    }
}