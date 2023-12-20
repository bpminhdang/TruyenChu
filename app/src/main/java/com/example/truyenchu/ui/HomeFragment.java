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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
//        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
//        HorizontalSmallImageAdapter adapter = new HorizontalSmallImageAdapter(getActivity(), Stories);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        // Inflate the layout for this fragment
//
//        RecyclerView recyclerView1=view.findViewById(R.id.home_recycler_view_2);
//        HorizontalContentAdapter adapter1 = new HorizontalContentAdapter(getActivity(),Stories);
//        recyclerView1.setAdapter(adapter1);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        RecyclerView recyclerView2=view.findViewById(R.id.home_recycler_view_3);
//        HorizontalImageAdapter adapter2 = new HorizontalImageAdapter(getActivity(),Stories);
//        recyclerView2.setAdapter(adapter2);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return view;


    }
}