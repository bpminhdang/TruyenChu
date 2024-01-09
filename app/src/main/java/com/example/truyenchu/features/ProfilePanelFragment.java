package com.example.truyenchu.features;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.truyenchu.R;
import com.example.truyenchu._class.UserClass;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePanelFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfilePanelFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePanelFragment.
     */
    public static ProfilePanelFragment newInstance(String param1, String param2)
    {
        ProfilePanelFragment fragment = new ProfilePanelFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_panel, container, false);

        ImageView profilePic = view.findViewById(R.id.cm_profile_image);
        TextView profileName = view.findViewById(R.id.profiile_panel_name);
        TextView welcome = view.findViewById(R.id.profiile_panel_tvWellCome);

//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("users_prefs", MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", "Guest");
//        profileName.setText(username);
//
//        if (username.equals("Guest"))
//            welcome.setText("WELCOME");
//        else
//            welcome.setText("WELCOME BACK!");
//
//        String profilePictureString = sharedPreferences.getString("profilePicture", "https://firebasestorage.googleapis.com/v0/b/truyenchu-89dd1.appspot.com/o/images%2Fprofile_picture.jpg?alt=media&token=bc33064a-23aa-4236-aa3a-b3e3b43eccbc");
        String uuid = UserClass.GetUserInfoFromPref(getActivity(), "uuid");
        if (uuid == null)
        {
            welcome.setText("CHÀO MỪNG");
            profileName.setText("Người dùng khách");
        } else
        {
            welcome.setText("CHÀO MỪNG QUAY TRỞ LẠI!");
            profileName.setText(UserClass.GetUserInfoFromPref(getActivity(), "name"));
            Glide.with(this).load(UserClass.GetUserInfoFromPref(getActivity(), "uri")).into(profilePic);
        }

//        editor.putString("name", user.getName());
//        editor.putString("email", user.getEmail());
//        editor.putString("uri", user.getProfile());
//        editor.putString("background", user.getBackgroundColor());
//        editor.putString("font", user.getFont());
//        editor.putString("fontSize", String.valueOf(user.getFontSize()));
//        editor.putString("textColor", String.valueOf(user.getTextColor()));
            //Glide.with(this).load(profilePictureString).into(profilePic);


            return view;
    }
}