package com.example.truyenchu.features;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class StoryDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoryDescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryDescriptionFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static StoryDescriptionFragment newInstance(String param1, String param2) {
        StoryDescriptionFragment fragment = new StoryDescriptionFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_description, container, false);

        TextView tvNameDes = view.findViewById(R.id.tvName_des);
        ImageView ivBack = view.findViewById(R.id.des_ivback);
        ImageView ivPicture = view.findViewById(R.id.des_iv_picture);
        TextView tvAuthor = view.findViewById(R.id.des_tv_author);
        TextView tvStatus = view.findViewById(R.id.des_tv_status);
        TextView tvNumChapter = view.findViewById(R.id.des_tv_num_chapter);
        TextView tvLiked = view.findViewById(R.id.des_tv_liked);
        TextView tvView = view.findViewById(R.id.des_tv_view);
        TextView tvWatching = view.findViewById(R.id.des_tv_watching);
        ImageView ivLike = view.findViewById(R.id.des_iv_like);
        ImageView ivComment = view.findViewById(R.id.des_iv_comment);
        TextView tvDescription = view.findViewById(R.id.des_tv_description);


        Bundle bundle = getArguments();
        if (bundle != null) {
            StoryClass receivedStory = (StoryClass) bundle.getSerializable("receivedStory");
            StoryClass.SetText(tvNameDes, receivedStory.getName());
            StoryClass.SetImage(requireContext(), receivedStory.getUri(), ivPicture);
            StoryClass.SetText(tvAuthor, receivedStory.getAuthor());
            String receivedDateString = receivedStory.getUpdateTime(); // Chuỗi ngày từ receivedStory
            String stt = receivedStory.getStatus();
            if ("Đang cập nhật".equals(stt)) {
                stt = "Đang ra - ";
            }
            StoryClass.SetText(tvStatus, stt + formattedDateString(receivedDateString));
            StoryClass.SetText(tvNumChapter, String.valueOf(receivedStory.getNumberOfChapter()));
            StoryClass.SetText(tvWatching, "Đang xem: " + "40");
            StoryClass.SetText(tvLiked, "100");
            // Todo: tvWatching tvLiked
            StoryClass.SetText(tvView, String.valueOf(receivedStory.getViews()));
            StoryClass.SetText(tvDescription, receivedStory.getDescription());


        }
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private String formattedDateString(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("'ngày' d 'th' M, yyyy", Locale.getDefault());

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Trả về chuỗi ban đầu nếu có lỗi
        }
    }
}