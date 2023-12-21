package com.example.truyenchu.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.HorizontalContentAdapter;
import com.example.truyenchu.adapter.HorizontalImageAdapter;
import com.example.truyenchu.adapter.HorizontalSmallImageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        //TODO: Thay vì tự tạo Story, lấy dữ liệu từ Firebase và đưa nó vào nhiều object, sau đó đưa vào recyclerView

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storiesRef = database.child("stories");

        // Lấy dữ liệu từ Firebase
        ArrayList<StoryClass> storyList = new ArrayList<>();

        storiesRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                // Tạo ArrayList để lưu trữ các đối tượng Story
//
////                // Duyệt qua từng node truyện và lưu vào ArrayList
////                for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
////                {
////                    Map<String, Object> dataMap = (Map<String, Object>) dataSnapshot.getValue();
////
////                   StoryClass story = createStoryFromMap(dataMap);
//////                    storyList.add(story);
////                }
//
//
//                // Khi đã lấy được dữ liệu, bạn có thể làm gì đó với storyList ở đây
//                // Ví dụ: in thông tin của mỗi truyện trong storyList
//                for (StoryClass story : storyList)
//                {
//                    Log.i("DB", "Tên truyện: " + story.getName());
//                    // In các thông tin khác của truyện tương ứng
//                }
//            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<StoryClass> storyList = new ArrayList<>();

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
                        }
                    }
                }

                // Ở đây, storyList chứa tất cả các StoryClass đã được lấy từ Firebase
                // Bạn có thể sử dụng storyList trong ứng dụng của mình
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("DB", "Lỗi: " + databaseError.getMessage());
            }
        });

//        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
//        HorizontalSmallImageAdapter adapter = new HorizontalSmallImageAdapter(getActivity(), storyList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        // Inflate the layout for this fragment
//
//        RecyclerView recyclerView1 = view.findViewById(R.id.home_recycler_view_2);
//        HorizontalContentAdapter adapter1 = new HorizontalContentAdapter(getActivity(), storyList);
//        recyclerView1.setAdapter(adapter1);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        RecyclerView recyclerView2 = view.findViewById(R.id.home_recycler_view_3);
//        HorizontalImageAdapter adapter2 = new HorizontalImageAdapter(getActivity(), storyList);
//        recyclerView2.setAdapter(adapter2);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
//        DatabaseReference usersRef = database.child("users");
//        Map<String, Object> users = new HashMap<>();
//        users.put("userId1", new UserClass("user1", "User One", "user1@example.com", "hashedpassword1", "Arial", 12, "#FFFFFF"));
//        users.put("userId2", new UserClass("user2", "User Two", "user2@example.com", "hashedpassword2", "Times New Roman", 14, "#F0F0F0"));
//        usersRef.setValue(users, (databaseError, databaseReference) ->
//        {
//            if (databaseError != null)
//            {
//                System.out.println("Data could not be saved " + databaseError.getMessage());
//            } else
//            {
//                System.out.println("Users data saved successfully.");
//            }
//        });


//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//


        return view;


    }
}