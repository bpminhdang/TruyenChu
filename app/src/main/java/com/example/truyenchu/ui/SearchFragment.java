package com.example.truyenchu.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.truyenchu.R;
import com.example.truyenchu.StoryActivity;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.VerticalContentAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EditText editTextInput;
    private ScrollView scrollView3;
    private ChipGroup chipGroup;
    private RecyclerView searchRecyclerView;

    boolean[] checked = {true, false, false};
    ArrayList<StoryClass> storyClasses = new ArrayList<>();
    private Chip searchChipName;
    private Chip searchChipAuthor;
    private Chip searchChipGenre;

    public SearchFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(String param1, String param2)
    {
        SearchFragment fragment = new SearchFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        editTextInput = view.findViewById(R.id.editTextText);
        scrollView3 = view.findViewById(R.id.scrollView3);
        chipGroup = view.findViewById(R.id.chipGroup);
        searchChipName = view.findViewById(R.id.search_chip_name);
        searchChipAuthor = view.findViewById(R.id.search_chip_author);
        searchChipGenre = view.findViewById(R.id.search_chip_genre);

        searchRecyclerView = view.findViewById(R.id.search_recyclerview);

        searchChipName.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            ClearChipCheck();
            checked[0] = isChecked;
            searchChipName.setChecked(isChecked);
        });
        searchChipAuthor.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            ClearChipCheck();
            checked[1] = isChecked;
            searchChipAuthor.setChecked(isChecked);

        });
        searchChipGenre.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            ClearChipCheck();
            checked[2] = isChecked;
            searchChipGenre.setChecked(isChecked);

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        VerticalContentAdapter adapter = new VerticalContentAdapter(getActivity(), storyClasses, story ->
        {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            intent.putExtra("storyData", story);
            startActivity(intent);
        });
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(layoutManager);
        TextView tv_notFound = view.findViewById(R.id.tv_not_found);

        view.findViewById(R.id.iv_search).setOnClickListener(v ->
        {
            tv_notFound.setVisibility(View.GONE);
            String dataToQuery = String.valueOf(editTextInput.getText());
            dataToQuery = StoryClass.NormalizedData(dataToQuery);
            DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            DatabaseReference storyRef = database.child("stories");


            if (searchChipName.isChecked())
            {
//                Query query = storyRef.orderByChild("queryName")
//                        .startAt(dataToQuery)
//                        .endAt(dataToQuery + "\uf8ff");
                String finalDataToQuery1 = dataToQuery;
                storyRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        storyClasses.clear();
                        for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                        {
                            // Lấy dữ liệu của truyện từ DataSnapshot
                            Long id = (Long) storySnapshot.child("id").getValue();
                            String name = (String) storySnapshot.child("queryName").getValue();
                            assert name != null;
                            if (name.contains(finalDataToQuery1))
                                storyClasses.add(StoryClass.loadStoryFromFile(getActivity(), String.valueOf(id)));
                        }
                        if (storyClasses.size() == 0)
                            tv_notFound.setVisibility(View.VISIBLE);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                    }
                });
            } else if (searchChipAuthor.isChecked())
            {
//                Query query = storyRef.orderByChild("queryAuthor")
//                        .startAt(dataToQuery)
//                        .endAt(dataToQuery + "\uf8ff");
                String finalDataToQuery2 = dataToQuery;
                storyRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        storyClasses.clear();
                        for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                        {
                            // Lấy dữ liệu của truyện từ DataSnapshot
                            Long id = (Long) storySnapshot.child("id").getValue();
                            String author = (String) storySnapshot.child("queryAuthor").getValue();
                            assert author != null;
                            if (author.contains(finalDataToQuery2))
                                storyClasses.add(StoryClass.loadStoryFromFile(getActivity(), String.valueOf(id)));
                        }
                        if (storyClasses.size() == 0)
                            tv_notFound.setVisibility(View.VISIBLE);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                    }
                });
            } else
            {
                String finalDataToQuery = dataToQuery;
                storyRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        storyClasses.clear();
                        for (DataSnapshot storySnapshot : dataSnapshot.getChildren())
                        {
                            Long id = (Long) storySnapshot.child("id").getValue();
                            List<String> genres = (List<String>) storySnapshot.child("genresList").getValue();
                            StringBuilder genresString = new StringBuilder();
                            if (genres != null)
                            {
                                //boolean isEqual = false;
                                for (String genre : genres)
                                {
                                    genre = StoryClass.NormalizedData(genre);
                                    if (genre == null)
                                        continue;
                                    genresString.append(genre);
                                }
                                if (genresString.toString().contains(finalDataToQuery))
                                    storyClasses.add(StoryClass.loadStoryFromFile(getActivity(), String.valueOf(id)));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (storyClasses.size() == 0)
                            tv_notFound.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });



        return view;
    }

    private void ClearChipCheck()
    {
        searchChipName.setChecked(false);
        searchChipAuthor.setChecked(false);
        searchChipGenre.setChecked(false);
    }
}