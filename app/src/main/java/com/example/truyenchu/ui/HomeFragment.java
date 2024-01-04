package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu.StoryActivity;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.Horizontal_3_ContentAdapter;
import com.example.truyenchu.adapter.Horizontal_2_ImageAdapter;
import com.example.truyenchu.adapter.Horizontal_1_SmallImageAdapter;
import com.example.truyenchu.features.ProfilePanelFragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment// implements RecyclerViewItemClickListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LIST_OF_STORY_LIST = "listStoryList";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static ArrayList<ArrayList<StoryClass>> mListOfStoryList = new ArrayList<>();

    ImageButton profilePic;
    TextView profileName;

    public HomeFragment()
    {
    }


//    public static HomeFragment newInstance(String param1, String param2)
//    {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @listStoryList A list of story
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(ArrayList<ArrayList<StoryClass>> listStoryList)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_OF_STORY_LIST, listStoryList);
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<ArrayList<StoryClass>> receivedListOfLists = (ArrayList<ArrayList<StoryClass>>) bundle.getSerializable(ARG_LIST_OF_STORY_LIST);
            if (receivedListOfLists != null) {
               mListOfStoryList = receivedListOfLists;

            }
        }

        // Khoảng mã lệnh trong Fragment cha để thêm Fragment con
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        ProfilePanelFragment fragment = new ProfilePanelFragment();
        transaction.replace(R.id.home_profile_panel_container, fragment); // R.id.container là id của viewgroup trong Fragment cha
        transaction.commit();

        if (mListOfStoryList.size() == 0)
        {
            ArrayList<StoryClass> storyClasses = new ArrayList<>();
            mListOfStoryList.add(storyClasses);
            mListOfStoryList.add(storyClasses);
        }

        RecyclerView rcViewNew = view.findViewById(R.id.home_recycler_view);
        RecyclerView rcViewUpdate = view.findViewById(R.id.home_recycler_view_2);
        RecyclerView rcViewRecent = view.findViewById(R.id.home_recycler_view_3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // Reverse lại vì dữ liệu được lấy về bị sắp xếp ngược
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);

        Horizontal_1_SmallImageAdapter adapter = new Horizontal_1_SmallImageAdapter(getActivity(), mListOfStoryList.get(0), story->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData", (Serializable) story);
            startActivity(intent);
        });
        Horizontal_3_ContentAdapter adapter1 = new Horizontal_3_ContentAdapter(getActivity(), mListOfStoryList.get(1), this::StartStoryDescriptionActivity);
        Horizontal_2_ImageAdapter adapter2 = new Horizontal_2_ImageAdapter(getActivity(), mListOfStoryList.get(1), this::StartStoryDescriptionActivity);
        // Todo: thêm recent
        rcViewNew.setAdapter(adapter);
        rcViewNew.setLayoutManager(layoutManager);
        rcViewUpdate.setAdapter(adapter1);
        rcViewUpdate.setLayoutManager(layoutManager2);
        rcViewRecent.setAdapter(adapter2);
        rcViewRecent.setLayoutManager(layoutManager3);






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

        return view;


    }

    private void StartStoryDescriptionActivity(StoryClass story)
    {
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra("storyData", (Serializable) story);
        startActivity(intent);
    }
}