package com.example.truyenchu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu.StoryActivity;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.Horizontal_3_ContentAdapter;
import com.example.truyenchu.adapter.Horizontal_2_ImageAdapter;
import com.example.truyenchu.adapter.Horizontal_1_SmallImageAdapter;
import com.example.truyenchu.adapter.BlankFragment;
import com.example.truyenchu.adapter.DataListener;
import com.example.truyenchu.features.DatabaseHelper;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment// implements RecyclerViewItemClickListener
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // private static final String ARG_STORY_LIST = "storyList";
    private static final String ARG_LIST_STORY_LIST = "listStoryList";

    private String mParam1;
    private String mParam2;
    /**
     * 0: storyListAll - ID tất cả truyện ____________________________________________________________________
     * 1: storyListNew - ID 13 truyện mới nhất để lấy dữ liệu nhanh và đưa vào HomeFragment ______
     * 2: storyListUpdate - ID 8 truyện mới cập nhật để lấy dữ liệu nhanh và đưa vào HomeFragment
     */
    private ArrayList<ArrayList<String>> mListStoryList = new ArrayList<>();
    private ArrayList<StoryClass> mListStoryNew = new ArrayList<>();
    private ArrayList<StoryClass> mListStoryUpdate = new ArrayList<>();
    private ArrayList<StoryClass> mListStoryRecent = new ArrayList<>();
    private DataListener dataListener;
    ImageButton profilePic;
    TextView profileName;

    // region Init

    public HomeFragment()
    {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     * @storyList A list of story
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(ArrayList<ArrayList<String>> listOfStoryLists)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_STORY_LIST, listOfStoryLists);
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

    // endregion Init
    public void setDataListener(DataListener listener)
    {
        this.dataListener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataListener = (DataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement DataListener");
        }
    }
    private void sendDataToActivity(String data)
    {
        // Gửi dữ liệu tới Activity thông qua Interface
        if (dataListener != null)
        {
            Log.i("Data Listener","Send data to activity: " +  data);
            dataListener.onDataReceived(data);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getArguments() != null)
            mListStoryList = (ArrayList<ArrayList<String>>) getArguments().getSerializable(ARG_LIST_STORY_LIST);
        for (String storyID : mListStoryList.get(1))
        {
            mListStoryNew.add(StoryClass.loadStoryFromFile(getActivity() ,storyID));
        }
        for (String storyID : mListStoryList.get(2))
        {
            mListStoryUpdate.add(StoryClass.loadStoryFromFile(getActivity() ,storyID));
        }

        String recentString = UserClass.GetUserInfoFromPref(getActivity(), "recent");
        if (recentString!= null)
        {
            String[] recentStringArray = recentString.split("_");
            for (String id : recentStringArray)
            {
                mListStoryRecent.add(StoryClass.loadStoryFromFile(getActivity(), id));
            }
        }

        // Khoảng mã lệnh trong Fragment cha để thêm Fragment con
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        ProfilePanelFragment fragment = new ProfilePanelFragment();
        transaction.replace(R.id.home_profile_panel_container, fragment); // R.id.container là id của viewgroup trong Fragment cha
        transaction.commit();

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

        Horizontal_1_SmallImageAdapter adapter = new Horizontal_1_SmallImageAdapter(getActivity(), mListStoryNew, this::StartStoryDescriptionActivity);
        Horizontal_3_ContentAdapter adapter1 = new Horizontal_3_ContentAdapter(getActivity(), mListStoryUpdate, this::StartStoryDescriptionActivity);
        Horizontal_2_ImageAdapter adapter2 = new Horizontal_2_ImageAdapter(getActivity(), mListStoryRecent, this::StartStoryDescriptionActivity);
        rcViewNew.setAdapter(adapter);
        rcViewNew.setLayoutManager(layoutManager);
        rcViewUpdate.setAdapter(adapter1);
        rcViewUpdate.setLayoutManager(layoutManager2);
        rcViewRecent.setAdapter(adapter2);
        rcViewRecent.setLayoutManager(layoutManager3);


        view.findViewById(R.id.home_bt_more_new).setOnClickListener(v ->
                switchToDiscoveryFromButton(0));
        view.findViewById(R.id.home_bt_more_update).setOnClickListener(v ->
                switchToDiscoveryFromButton(1));
        view.findViewById(R.id.home_bt_more_recent).setOnClickListener(v ->
                switchToDiscoveryFromButton(2));


        String uuid = UserClass.GetUserInfoFromPref(getActivity(),"uuid");
        if (uuid != null)
        {
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
                        mListStoryRecent.clear();
                        for (String id : recentStringArray)
                        {
                            mListStoryRecent.add(StoryClass.loadStoryFromFile(getActivity(), id));
                        }
                        adapter2.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }

        return view;


    }

    private void switchToDiscoveryFromButton(int btID)
    {
        if (btID == 2)
        {
            Fragment downloadFragment = new DownloadFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, new BlankFragment()) // Use blank fragment to hide all the content, smoother the animation
                    .commit();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_and_slide, R.anim.fade_out)
                    .add(R.id.home_fragment_container, downloadFragment, "YOUR_FRAGMENT_TAG")
                    .commit();
            return;
        }

        Fragment updateFragment = DiscoveryFragment.newInstance(mListStoryList.get(0), btID);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.home_fragment_container, new BlankFragment()) // Use blank fragment to hide all the content, smoother the animation
                .commit();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in_and_slide, R.anim.fade_out)
                .add(R.id.home_fragment_container, updateFragment, "YOUR_FRAGMENT_TAG")
                .commit();

        sendDataToActivity("Click Discovery");
    }


    private void StartStoryDescriptionActivity(StoryClass story)
    {
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra("storyData", story);
        startActivity(intent);

    }


}