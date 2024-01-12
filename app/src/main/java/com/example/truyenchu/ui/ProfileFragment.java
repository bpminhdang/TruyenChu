package com.example.truyenchu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.truyenchu.HomeActivity;
import com.example.truyenchu.R;
import com.example.truyenchu.adapter.BlankFragment;
import com.example.truyenchu.features.ProfilePanelFragment;
import com.example.truyenchu.features.UpdateStoryActivity;
import com.example.truyenchu.features.UploadStoryActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2)
    {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();

        ProfilePanelFragment fragment = new ProfilePanelFragment();
        transaction.replace(R.id.fragmentProfileContainerView_profile, fragment); // R.id.container là id của viewgroup trong Fragment cha
        transaction.commit();

        view.findViewById(R.id.btSignOut).setOnClickListener(v ->
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("users_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid", null);
            editor.apply();
            startActivity(intent);
            getActivity().finish();
        });

        view.findViewById(R.id.pro_upload).setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), UploadStoryActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.pro_update).setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), UpdateStoryActivity.class);
            startActivity(intent);
        });
        return view;
    }
}