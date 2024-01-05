package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.truyenchu.StoryActivity;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.VerticalContentAdapter;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    private static final String ARG_LIST_STORY_ID = "listStoryID";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<String> storyListString = new ArrayList<>();
    ArrayList<StoryClass> storyListObject = new ArrayList<>();
    VerticalContentAdapter adapter = new VerticalContentAdapter(getActivity(), storyListObject, story ->
    {
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra("storyData", story);
        startActivity(intent);
    });;

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
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance(ArrayList<String> storyList)
    {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_STORY_ID, storyList);
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
        if (getArguments() != null)
            storyListString = (ArrayList<String>)getArguments().getSerializable(ARG_LIST_STORY_ID);
       for (String storyID : storyListString)
       {
           storyListObject.add(loadStoryFromFile(storyID));
       }

        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();

        ProfilePanelFragment fragment = new ProfilePanelFragment();
        transaction.replace(R.id.dis_profile_panel_container, fragment);
        transaction.commit();


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



        for (int i = 0; i < btList.size(); i++) {
            final int index = i; // Create a final variable to capture the value of i
            btList.get(i).setOnClickListener(v ->
            {
                ResetColorAndSet(btList, index);
                if (index == 2)
                {
                    Toast.makeText(getActivity(), "Change", Toast.LENGTH_SHORT).show();
                    storyListObject = new ArrayList<>();
                    for (String storyID : storyListString)
                    {
                        StoryClass story = loadStoryFromFile(storyID);
                        if (story.getStatus().equals("Full"))
                            storyListObject.add(story);
                    }
                    adapter.updateData(storyListObject);
                }
            });
        }

//        btnNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ResetColorAndSet(btList, 0);
//            }
//        });
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ResetColorAndSet(btList, 1);
//            }
//        });




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