package com.example.truyenchu.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.DataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryReadingFragment extends Fragment {
    private static final String ARG_STORY = "story";
    boolean isHidden = false;
    int currentChapter = 1;
    private static int mStoryID;
    List<Boolean> readList = new ArrayList<>();
    List<Boolean> favList = new ArrayList<>();
    boolean isLoggedIn = false;
    private StoryClass story;
    private DataListener dataListener;
    TextView tvName;
    TextView tvContent;
    int readCount = -1;

    public StoryReadingFragment() {
        // Required empty public constructor
    }

//    public static StoryReadingFragment newInstance(List<Boolean> readList, List<Boolean> favList, boolean isLoggedIn) {
//        StoryReadingFragment fragment = new StoryReadingFragment();
//
//        // Truyền dữ liệu vào fragment thông qua Bundle
//        Bundle args = new Bundle();
//
//        // Chuyển List<Boolean> thành boolean[] để sử dụng putBooleanArray
//        boolean[] readArray = convertBooleanListToArray(readList);
//        boolean[] favArray = convertBooleanListToArray(favList);
//
//        args.putBooleanArray("readList", readArray);
//        args.putBooleanArray("favList", favArray);
//        args.putBoolean("isLoggedIn", isLoggedIn);
//
//        // Đặt các tham số vào fragment
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    private static boolean[] convertBooleanListToArray(List<Boolean> booleanList) {
        boolean[] booleanArray = new boolean[booleanList.size()];
        for (int i = 0; i < booleanList.size(); i++) {
            booleanArray[i] = booleanList.get(i);
        }
        return booleanArray;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public static StoryReadingFragment newInstance(int storyID, List<Boolean> readList, List<Boolean> favList, boolean isLoggedIn, int chapter) {
        StoryReadingFragment fragment = new StoryReadingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STORY, storyID);

        // Chuyển đổi List<Boolean> thành boolean[] để đặt vào Bundle
        boolean[] readArray = convertBooleanListToArray(readList);
        boolean[] favArray = convertBooleanListToArray(favList);

        args.putBooleanArray("readList", readArray);
        args.putBooleanArray("favList", favArray);
        args.putBoolean("isLoggedIn", isLoggedIn);
        args.putInt("chapter", chapter);

        fragment.setArguments(args);
        return fragment;
    }

    // Hàm để lấy màu nền từ SharedPreferences
    private int getSavedBackgroundColor() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getInt("background_color", getResources().getColor(android.R.color.white));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoryID = getArguments().getInt(ARG_STORY);
            story = loadStoryFromFile(String.valueOf(mStoryID));
            story.sortChaptersById();

            boolean[] readArray = getArguments().getBooleanArray("readList");
            boolean[] favArray = getArguments().getBooleanArray("favList");
            isLoggedIn = getArguments().getBoolean("isLoggedIn");
            currentChapter = getArguments().getInt("chapter");
            // Chuyển đổi mảng thành List<Boolean>
            readList = convertBooleanArrayToList(readArray);
            favList = convertBooleanArrayToList(favArray);
        }
    }

    private List<Boolean> convertBooleanArrayToList(boolean[] booleanArray) {
        List<Boolean> booleanList = new ArrayList<>();
        for (boolean value : booleanArray) {
            booleanList.add(value);
        }
        return booleanList;
    }

    private void applySavedTextColor(TextView mauchu) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int textColor = preferences.getInt("text_color", Color.BLACK);
        mauchu.setTextColor(textColor);
    }

    private float ourFontsize = 16f; //bien' size text

    private float getSavedTextSize() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getFloat("text_size", ourFontsize); //
    }

    private float getSavedLineSpacingMultiplier() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getFloat("line_spacing_multiplier", 1.0f);
    }

    private void applyLineSpacingMultiplier(TextView textView, float multiplier) {
        textView.setLineSpacing(0, multiplier);
    }

    private static final String PREF_SELECTED_FONT_PATH = "selected_font_path";

    private String getSelectedFontPath() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getString(PREF_SELECTED_FONT_PATH, "");
    }

    // Hàm áp dụng font từ đường dẫn đã chọn
    private void applySavedFont(TextView textView) {
        String fontPath = getSelectedFontPath();

        if (!fontPath.isEmpty()) {
            // Tạo Typeface từ đường dẫn đã lưu
            Typeface savedFont = Typeface.createFromAsset(requireActivity().getAssets(), fontPath);
            textView.setTypeface(savedFont);
        }
    }

    private void fadeOutView(View view) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        alphaAnimator.setDuration(500); // Set the duration in milliseconds
        alphaAnimator.setInterpolator(new DecelerateInterpolator()); // Use DecelerateInterpolator for a gradual fade-out
        alphaAnimator.start();
        view.setVisibility(View.GONE);
    }

    private void fadeInView(View view) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        alphaAnimator.setDuration(300); // Set the duration of the animation in milliseconds
        alphaAnimator.setInterpolator(new DecelerateInterpolator()); // You can adjust the interpolator
        alphaAnimator.start();
        view.setVisibility(View.VISIBLE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_reading, container, false);
        View top = view.findViewById(R.id.top_navigation_custom_avs);
        View bot = view.findViewById(R.id.bottom_navigation_custom_avs);
        ImageButton exit = top.findViewById(R.id.r_exit);
        ImageButton mark = top.findViewById(R.id.r_reloading);
        ImageButton pre = bot.findViewById(R.id.previous_chapter);
        ImageButton ne = bot.findViewById(R.id.nextchapter);
        ImageButton set = bot.findViewById(R.id.setting_read);
        ImageButton ml = bot.findViewById(R.id.mucluc_read);
        tvContent = view.findViewById(R.id.readingnehihi);
        tvName = view.findViewById(R.id.r_name);
        NestedScrollView nestedScrollView = view.findViewById(R.id.readingkone);

        // Áp dụng các setting
        int backgroundColor = getSavedBackgroundColor();

        nestedScrollView.setBackgroundColor(backgroundColor);

        applySavedTextColor(tvContent);

        float textSize = getSavedTextSize();
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        float lineSpacingMultiplier = getSavedLineSpacingMultiplier();
        applyLineSpacingMultiplier(tvContent, lineSpacingMultiplier);

        applySavedFont(tvContent);

        //hihi

        SwitchToChapter(currentChapter);

        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !isHidden) {
                fadeOutView(top);
                fadeOutView(bot);
                isHidden = true;
            } else if (scrollY < oldScrollY && isHidden) {
                fadeInView(top);
                fadeInView(bot);
                isHidden = false;
            }
        });

        // Xử lý sự kiện khi TextView được nhấp vào
        tvContent.setOnClickListener(v -> {
            // Chuyển đổi trạng thái của View khi được nhấp vào
            if (isHidden) {
                fadeInView(top);
                fadeInView(bot);
            } else {
                fadeOutView(top);
                fadeOutView(bot);
            }

            // Đảo ngược giá trị của biến flag
            isHidden = !isHidden;
        });

        exit.setOnClickListener(v ->
        {
            requireActivity().onBackPressed();
            sendDataToActivity("Exit reading");
        });

        mark.setOnClickListener(v ->
        {
            if (isLoggedIn) {
                DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(getActivity());
                currentUsersRef.child("recentStoryRead")
                        .child(story.GetIdString())
                        .child("fav")
                        .child(String.valueOf(currentChapter)).setValue(true);
                favList.set(currentChapter, true);
                if (dataListener != null) {
                    dataListener.onBooleanListReceived(readList, favList);

                    Toast.makeText(getContext(), "Đã đánh dấu chương", Toast.LENGTH_SHORT).show();

                }
            }
//            @Override
//            public void onClick(View v)
//            {
//                reloadFragment();
//            }
//
//            private void refreshUI()
//            {
//                // Lấy TextView từ layout
//                TextView textView = view.findViewById(R.id.readingnehihi);
//
//                // Thực hiện các bước cập nhật UI dựa trên dữ liệu mới
//                // Ví dụ: Cập nhật nội dung TextView
//                textView.setText("Nội dung mới từ Firebase");
//            }
//
//            private void fetchDataFromFirebase()
//            {
//                // Thực hiện lấy dữ liệu mới từ Firebase
//                // Đây là nơi bạn sẽ thực hiện các thao tác để lấy dữ liệu từ Firebase
//                // Sau khi lấy được dữ liệu mới, gọi hàm cập nhật giao diện người dùng
//                updateDataAndUI();
//            }
//
//            public void updateDataAndUI()
//            {
//                // Gọi hàm để lấy dữ liệu mới từ Firebase
//                fetchDataFromFirebase();
//
//                // Cập nhật giao diện người dùng sau khi có dữ liệu mới
//                refreshUI();
//            }
//
//            private void reloadFragment()
//            {
//                updateDataAndUI();
//            }
        });

        set.setOnClickListener(v ->
        {
            SettingReadingFragment settingReadingFragment = new SettingReadingFragment();
            // Thực hiện chuyển Fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_avs, settingReadingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        pre.setOnClickListener(v ->
        {
            if (currentChapter > 1) {
                currentChapter--;
                SwitchToChapter(currentChapter);
            } else
                Toast.makeText(requireContext(), "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();

        });

        ne.setOnClickListener(v ->
        {
            if (currentChapter < story.getNumberOfChapter()) {
                currentChapter++;
                SwitchToChapter(currentChapter);
            } else
                Toast.makeText(requireActivity(), "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();

        });

        if (isLoggedIn) {
            ml.setOnClickListener(v ->
            {
                ArrayList<String> optionsList = new ArrayList<>();
                optionsList.add("  Chương đã đọc được tô màu xám");
                for (int i = 0; i < story.getNumberOfChapter(); i++) {
                    optionsList.add("  Chương " + (i + 1));
                }
                String[] options = optionsList.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundBorderDialog);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, options) {
                    @Override
                    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        if (readList.get(position))
                            textView.setTextColor(Color.GRAY);
                        if (favList.get(position))
                            textView.setText(textView.getText() + " ⭐");
                        return textView;
                    }
                };
                builder.setTitle("Chọn chương: ");
                builder.setAdapter(adapter, (dialog, chapterPos) ->
                {
                    if (chapterPos == 0)
                        return;
                    SwitchToChapter(chapterPos);
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        } else {
            ml.setOnClickListener(v ->
            {
                ArrayList<String> optionsList = new ArrayList<>();
                optionsList.add("  Chương đã đọc được tô màu xám");
                for (int i = 0; i < story.getNumberOfChapter(); i++) {
                    optionsList.add("  Chương " + (i + 1));
                }
                String[] options = optionsList.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundBorderDialog);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, options);
                builder.setTitle("Chọn chương: ");
                builder.setAdapter(adapter, (dialog, chapterPos) ->
                {
                    if (chapterPos == 0)
                        return;
                    SwitchToChapter(chapterPos);
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }

        return view;
    }


    private void SwitchToChapter(int chapter) {
        currentChapter = chapter;
        tvContent.setText(story.GetChapterContent(currentChapter));
        tvName.setText(story.getName(13) + " | C" + (currentChapter));
        if (isLoggedIn) {
            DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(getActivity());
            currentUsersRef.child("recentStoryRead")
                    .child(story.GetIdString())
                    .child("read")
                    .child(String.valueOf(currentChapter)).setValue(true);
            readList.set(currentChapter, true);
            // Chuyển dữ liệu sang Activity
            Log.i("Reading", "gui dl");

            if (dataListener != null) {
                Log.i("Reading", "gui dl that");
                dataListener.onBooleanListReceived(readList, favList);
            }
        }
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

    private void sendDataToActivity(String data) {
        Log.i("Data Listener", "Send data to activity1: " + data);

        // Gửi dữ liệu tới Activity thông qua Interface
        if (dataListener != null) {
            Log.i("Data Listener", "Send data to activity: " + data);
            dataListener.onDataReceived(data);
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
            }
        }
        return loadedStory;
    }
}