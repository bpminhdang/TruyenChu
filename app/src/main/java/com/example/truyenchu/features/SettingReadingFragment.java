package com.example.truyenchu.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.truyenchu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingReadingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //cuu

    public interface OnColorChangedListener {
        void onColorChanged(int newColor);

        void setOnColorChangedListener(Object saveBackgroundColor);
    }

    private OnColorChangedListener colorChangedListener;

    //... rest of the code

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.colorChangedListener = listener;
    }


    //cuuuu
    public SettingReadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingReadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingReadingFragment newInstance(String param1, String param2) {
        SettingReadingFragment fragment = new SettingReadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void saveBackgroundColor(int color) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("background_color", color);
        editor.apply();
    }

    private int getSavedBackgroundColor() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getInt("background_color", getResources().getColor(android.R.color.white));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_reading, container, false);

        View back = view.findViewById(R.id.setting_back);
        back.setOnClickListener(v -> requireActivity().onBackPressed());

        View cwhite = view.findViewById(R.id.col_white);
        cwhite.setOnClickListener(v -> {
            saveBackgroundColor(Color.WHITE);
            view.setBackgroundColor(Color.WHITE);
        });

        View cblack = view.findViewById(R.id.col_black);
        cblack.setOnClickListener(v -> {
            saveBackgroundColor(Color.BLACK);
            view.setBackgroundColor(Color.BLACK);
            if (colorChangedListener != null) {
                colorChangedListener.onColorChanged(Color.BLACK);
            }
            StoryReadingFragment storyReadingFragment2 = new StoryReadingFragment();
            int newColor = getSavedBackgroundColor();
          //  storyReadingFragment2.onColorChanged(newColor);

            // Thực hiện chuyển Fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.reading_hihi, storyReadingFragment2);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View clgrey = view.findViewById(R.id.col_lightgrey);
        clgrey.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#E2E2E2"));
            view.setBackgroundColor(Color.parseColor("#E2E2E2"));
        });

        View cwbeige = view.findViewById(R.id.col_warmbeige);
        cwbeige.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#f3dcba"));
            view.setBackgroundColor(Color.parseColor("#f3dcba"));
        });

        View cowhite = view.findViewById(R.id.col_offwhite);
        cowhite.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#fff9ef"));
            view.setBackgroundColor(Color.parseColor("#fff9ef"));
        });

        int backgroundColor = getSavedBackgroundColor();
        view.setBackgroundColor(backgroundColor);

        return view;
    }
}