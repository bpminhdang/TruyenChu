package com.example.truyenchu.features;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.truyenchu.R;
import com.example.truyenchu.adapter.BlankFragment;
import com.example.truyenchu._interface.DataListener;
import com.example.truyenchu.ui.SearchFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DataListener dataListener;

    public GenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            dataListener = (DataListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context + " must implement DataListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        Button[] buttons = new Button[22];
        int[] buttonIds = {
                R.id.dis_bt1, R.id.dis_bt2, R.id.dis_bt3, R.id.dis_bt4, R.id.dis_bt5,
                R.id.dis_bt6, R.id.dis_bt7, R.id.dis_bt8, R.id.dis_bt9, R.id.dis_bt10,
                R.id.dis_bt11, R.id.dis_bt12, R.id.dis_bt13, R.id.dis_bt14, R.id.dis_bt15,
                R.id.dis_bt16, R.id.dis_bt17, R.id.dis_bt18, R.id.dis_bt19, R.id.dis_bt20,
                R.id.dis_bt21, R.id.dis_bt22
        };

        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = view.findViewById(buttonIds[i]);
            buttons[i].setOnClickListener(v ->
                    switchToSearch(((Button) v).getText().toString(), "genres")
            );
        }

        return view;
    }

    private void switchToSearch(String input, String type)
    {
        SearchFragment fragment = SearchFragment.newInstance(input, type);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.home_fragment_container, new BlankFragment()) // Use blank fragment to hide all the content, smoother the animation
                .commit();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in_and_slide, R.anim.fade_out)
                .add(R.id.home_fragment_container, fragment, "YOUR_FRAGMENT_TAG")
                .commit();

        sendDataToActivity("Click Search");
    }

    private void sendDataToActivity(String data)
    {
        // Gửi dữ liệu tới Activity thông qua Interface
        if (dataListener != null)
        {
            Log.i("Data Listener", "Send data to activity: " + data);
            dataListener.onDataReceived(data);
        }
    }
}