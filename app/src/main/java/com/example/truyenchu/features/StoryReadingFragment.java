package com.example.truyenchu.features;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryReadingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoryReadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryReadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryReadingFragment newInstance(String param1, String param2) {
        StoryReadingFragment fragment = new StoryReadingFragment();
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

    boolean isHidden = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_reading, container, false);

        View top = view.findViewById(R.id.top_navigation_custom_avs);
        View bot = view.findViewById(R.id.bottom_navigation_custom_avs);
        ImageButton exit = top.findViewById(R.id.r_exit);
        ImageButton reload = top.findViewById(R.id.r_reloading);
        ImageButton pre = bot.findViewById(R.id.previous_chapter);
        ImageButton ne = bot.findViewById(R.id.nextchapter);
        ImageButton set = bot.findViewById(R.id.setting_read);
        ImageButton ml = bot.findViewById(R.id.mucluc_read);
        TextView textView = view.findViewById(R.id.readingnehihi);
        NestedScrollView nestedScrollView = view.findViewById(R.id.readingkone);

        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra hướng cuộn và ẩn/hiện top và bot tùy thuộc vào hướng cuộn
                if (scrollY > oldScrollY && !isHidden) {
                    top.setVisibility(View.GONE);
                    bot.setVisibility(View.GONE);
                    isHidden = true;
                } else if (scrollY < oldScrollY && isHidden) {
                    top.setVisibility(View.VISIBLE);
                    bot.setVisibility(View.VISIBLE);
                    isHidden = false;
                }
            }
        });

        // Xử lý sự kiện khi TextView được nhấp vào
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đổi trạng thái của View khi được nhấp vào
                if (isHidden) {
                    top.setVisibility(View.VISIBLE);
                    bot.setVisibility(View.VISIBLE);
                } else {
                    top.setVisibility(View.GONE);
                    bot.setVisibility(View.GONE);
                }

                // Đảo ngược giá trị của biến flag
                isHidden = !isHidden;
            }
        });
        exit.setOnClickListener(v -> requireActivity().onBackPressed());
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();
            }

            private void refreshUI() {
                // Lấy TextView từ layout
                TextView textView = view.findViewById(R.id.readingnehihi);

                // Thực hiện các bước cập nhật UI dựa trên dữ liệu mới
                // Ví dụ: Cập nhật nội dung TextView
                textView.setText("Nội dung mới từ Firebase");
            }

            private void fetchDataFromFirebase() {
                // Thực hiện lấy dữ liệu mới từ Firebase
                // Đây là nơi bạn sẽ thực hiện các thao tác để lấy dữ liệu từ Firebase
                // Sau khi lấy được dữ liệu mới, gọi hàm cập nhật giao diện người dùng
                updateDataAndUI();
            }

            public void updateDataAndUI() {
                // Gọi hàm để lấy dữ liệu mới từ Firebase
                fetchDataFromFirebase();

                // Cập nhật giao diện người dùng sau khi có dữ liệu mới
                refreshUI();
            }

            private void reloadFragment() {
                updateDataAndUI();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingReadingFragment settingReadingFragment = new SettingReadingFragment();
                // Thực hiện chuyển Fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_avs, settingReadingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


// Todo:chuyen chapter va capnhat muc luc
        int currentChapter = 2;
        pre.setOnClickListener(new View.OnClickListener() {
            private void goToPreviousChapter() {
                if (currentChapter > 1) {
                    int previousChapter = currentChapter - 1;
                    StoryReadingFragment newFragment = StoryReadingFragment.newInstance("Chapter " + previousChapter, "YourParam2");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_avs, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(requireContext(), "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClick(View v) {
                // Gọi phương thức để chuyển đến chương trước đó
                goToPreviousChapter();
            }
        });
        ne.setOnClickListener(new View.OnClickListener() {
            private void goToNextChapter() {
                // Giả sử bạn có tổng số chương là maxChapter
                int maxChapter = getMaxChapter();

                if (currentChapter < maxChapter) {
                    int nextChapter = currentChapter + 1;
                    StoryReadingFragment newFragment = StoryReadingFragment.newInstance("Chapter " + nextChapter, "YourParam2");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_avs, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(requireContext(), "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }

            private int getMaxChapter() {
                // Hàm này trả về số chương tối đa, bạn có thể thay đổi cách lấy dữ liệu tùy vào ứng dụng của bạn
                // Ví dụ: nếu bạn lấy từ Firebase, có thể làm một truy vấn để lấy tổng số chương
                return 10; // Đây chỉ là ví dụ, bạn cần thay đổi theo ứng dụng của bạn
            }

            @Override
            public void onClick(View v) {
                // Gọi phương thức để chuyển đến chương tiếp theo
                goToNextChapter();
            }
        });

        ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }


}