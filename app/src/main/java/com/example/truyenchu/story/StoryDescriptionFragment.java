package com.example.truyenchu.story;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._interface.DatabaseHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isSwitchedToAnotherFragment = false;

    private String mParam1;
    private String mParam2;
    StoryClass receivedStory;

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
        ToggleButton tgbtLike = view.findViewById(R.id.des_iv_like);
        TextView tvView = view.findViewById(R.id.des_tv_view);
        TextView tvWatching = view.findViewById(R.id.des_tv_watching);
        ImageView ivComment = view.findViewById(R.id.des_iv_comment);
        TextView tvDescription = view.findViewById(R.id.des_tv_description);
        TextView tvID = view.findViewById(R.id.des_tv_id);


        Bundle bundle = getArguments();
        if (bundle != null) {
            int receivedStoryID = bundle.getInt("receivedStoryID");
            receivedStory = loadStoryFromFile(String.valueOf(receivedStoryID));
            StoryClass.SetText(tvNameDes, receivedStory.getName());
            StoryClass.SetText(tvAuthor, receivedStory.getAuthor());
            StoryClass.SetText(tvID, "ID: " + receivedStory.getId());
            StoryClass.SetImage(requireContext(), receivedStory.getUri(), ivPicture);
            ivPicture.setOnClickListener(v->
            {

            });

            String receivedDateString = receivedStory.getUpdateTime(); // Chuỗi ngày từ receivedStory
            String stt = receivedStory.getStatus();
            if ("Đang cập nhật".equals(stt)) {
                stt = "Đang ra";
            }

            try {
                StoryClass.SetText(tvStatus, stt + " - " + formattedDateString(receivedDateString));
            } catch (Exception e) {
                StoryClass.SetText(tvStatus, stt + " - " + receivedDateString);

            }
            StoryClass.SetText(tvNumChapter, String.valueOf(receivedStory.getNumberOfChapter()));
            StoryClass.SetText(tvLiked, String.valueOf(receivedStory.getUuidLikedUsers().size()));
            StoryClass.SetText(tvView, String.valueOf(receivedStory.getViews()));
            StoryClass.SetText(tvDescription, receivedStory.getDescription());

            // Tránh tình trạng watching bị tăng lên sau khi chuyển trở về từ reading fragment
            if (!isSwitchedToAnotherFragment)
                DatabaseHelper.getWatchingCountAndSetText(receivedStory, tvWatching, 1);
            else
                DatabaseHelper.getWatchingCountAndSetText(receivedStory, tvWatching, 0);

            ivComment.setOnClickListener(v ->
            {
                Intent intent = new Intent(getActivity(), StoryRatingActivity.class);
                intent.putExtra("storyData", receivedStory.getId());
                startActivity(intent);
            });
        }
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
        tgbtLike.setOnClickListener(v -> {
            int num = Integer.parseInt(tvLiked.getText().toString());
            if (tgbtLike.isChecked()) {

                tvLiked.setText(String.valueOf(num + 1));
            } else
                tvLiked.setText(String.valueOf(num - 1));


        });

        return view;
    }

    private String formattedDateString(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("'ng' d 'th' M, yyyy", Locale.getDefault());

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Trả về chuỗi ban đầu nếu có lỗi
        }
    }

    public StoryClass loadStoryFromFile(String storyId) {
        StoryClass loadedStory = null;
        String fileName = storyId + ".json"; // Tên file là ID của truyện + ".json"

        // Lấy đường dẫn đến thư mục "stories" trong internal storage
        File directory = new File(getActivity().getFilesDir() + "/stories");
        File file = new File(directory, fileName);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                String storyJson = new String(buffer);

                // Chuyển đổi chuỗi JSON thành đối tượng StoryClass
                Gson gson = new Gson();
                loadedStory = gson.fromJson(storyJson, StoryClass.class);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Story loading", "Error1");

            }
        } else {
            Toast.makeText(getActivity(), "Can't find story", Toast.LENGTH_SHORT).show();
            Log.i("Story loading", "Error");
        }
        return loadedStory;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Life cycle", "OnPause");
        isSwitchedToAnotherFragment = true;
    }
}