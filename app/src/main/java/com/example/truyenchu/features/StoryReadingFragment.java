package com.example.truyenchu.features;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryReadingFragment extends Fragment
{

    private static final String ARG_STORY = "story";
    boolean isHidden = false;
    int currentChapter = 1;
    boolean isLoggedIn = false;
    private static int mStoryID;
    List<Boolean> readList = new ArrayList<>();
    List<Boolean> favList = new ArrayList<>();
    private StoryClass story;
    private DataListener dataListener;
    TextView tvName;
    TextView tvContent;
    int readCount = -1;

    public StoryReadingFragment()
    {
        // Required empty public constructor
    }

    public StoryReadingFragment(int currentChapter)
    {
        this.currentChapter = currentChapter;
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public static StoryReadingFragment newInstance(int storyID)
    {
        StoryReadingFragment fragment = new StoryReadingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STORY, storyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mStoryID = getArguments().getInt(ARG_STORY);
            story = loadStoryFromFile(String.valueOf(mStoryID));
            story.sortChaptersById();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
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
        tvContent = view.findViewById(R.id.readingnehihi);
        tvName = view.findViewById(R.id.r_name);
        NestedScrollView nestedScrollView = view.findViewById(R.id.readingkone);

        String uuid = UserClass.GetUserInfoFromPref(getActivity(), "uuid");
        if (uuid != null) {
            DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(getActivity());

            // Lấy giá trị của readCount
            DatabaseReference readCountRef = currentUsersRef.child("readCount");
            readCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int readCount = dataSnapshot.getValue(Integer.class);
                        String recentStoryReadPath = "recentStoryRead/" + story.getId();
                        DatabaseReference recentStoryReadRef = currentUsersRef.child(recentStoryReadPath);


                        // Kiểm tra xem nút tồn tại hay không
                        recentStoryReadRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    // Nếu nút không tồn tại, thực hiện các hành động tạo mới
                                    Map<String, Object> updateMap = new HashMap<>();
                                    for (int i = 1; i <= story.getNumberOfChapter(); i++) {
                                        updateMap.put("fav/" + i, false);
                                        updateMap.put("read/" + i, false);
                                    }
                                    updateMap.put("read/1", true);
                                    recentStoryReadRef.updateChildren(updateMap);
                                    // Lưu giá trị count để tìm được truyện gần nhất đưa vào home
                                    recentStoryReadRef.child("count").setValue(readCount);
                                    // Gọi incrementReadCount() nếu cần
                                    incrementReadCount(readCountRef);
                                }

                                recentStoryReadRef.child("read").addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.exists())
                                        {
                                            // Chuyển dữ liệu từ Firebase thành mảng
                                            readList.add(true);
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                            {
                                                Boolean value = snapshot.getValue(Boolean.class);
                                                readList.add(value);
                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {
                                        // Xử lý lỗi nếu cần thiết
                                    }
                                });

                                recentStoryReadRef.child("fav").addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.exists())
                                        {
                                            // Chuyển dữ liệu từ Firebase thành mảng
                                            favList.add(false);
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                            {
                                                Boolean value = snapshot.getValue(Boolean.class);
                                                favList.add(value);
                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {
                                        // Xử lý lỗi nếu cần thiết
                                    }
                                });

                                isLoggedIn = true;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi nếu cần thiết
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu cần thiết
                }
            });
        }




        SwitchToChapter(currentChapter);
        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
        {
            // Kiểm tra hướng cuộn và ẩn/hiện top và bot tùy thuộc vào hướng cuộn
            if (scrollY > oldScrollY && !isHidden)
            {
                top.setVisibility(View.GONE);
                bot.setVisibility(View.GONE);
                isHidden = true;
            } else if (scrollY < oldScrollY && isHidden)
            {
                top.setVisibility(View.VISIBLE);
                bot.setVisibility(View.VISIBLE);
                isHidden = false;
            }
        });

        // Xử lý sự kiện khi TextView được nhấp vào
        tvContent.setOnClickListener(v ->
        {
            // Chuyển đổi trạng thái của View khi được nhấp vào
            if (isHidden)
            {
                top.setVisibility(View.VISIBLE);
                bot.setVisibility(View.VISIBLE);
            } else
            {
                top.setVisibility(View.GONE);
                bot.setVisibility(View.GONE);
            }

            // Đảo ngược giá trị của biến flag
            isHidden = !isHidden;
        });

        exit.setOnClickListener(v ->
        {
            requireActivity().onBackPressed();
            sendDataToActivity("Exit reading");
        });

        reload.setOnClickListener(v ->
        {
            if (isLoggedIn)
            {
                DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(getActivity());
                currentUsersRef.child("recentStoryRead")
                        .child(story.GetIdString())
                        .child("fav")
                        .child(String.valueOf(currentChapter)).setValue(true);
                favList.set(currentChapter, true);
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
            if (currentChapter > 1)
            {
                currentChapter--;
                SwitchToChapter(currentChapter);
            } else
                Toast.makeText(requireContext(), "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();

        });

        ne.setOnClickListener(v ->
        {
            if (currentChapter < story.getNumberOfChapter())
            {
                currentChapter++;
                SwitchToChapter(currentChapter);
            } else
                Toast.makeText(requireActivity(), "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();

        });

        ml.setOnClickListener(v ->
        {
            ArrayList<String> optionsList = new ArrayList<>();
            optionsList.add("  Chương đã đọc được tô màu xám");
            for (int i = 0; i < story.getNumberOfChapter(); i++)
            {
                optionsList.add("  Chương " + (i + 1));
            }
            String[] options = optionsList.toArray(new String[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundBorderDialog);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, options)
            {
                @Override
                public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent)
                {
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

        return view;
    }

    private void incrementReadCount(DatabaseReference readCountRef)
    {
        readCountRef.runTransaction(new Transaction.Handler()
        {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData)
            {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null)
                {
                    // Nếu giá trị hiện tại là null, set giá trị là 1
                    mutableData.setValue(1);
                } else
                {
                    // Ngược lại, tăng giá trị hiện tại lên 1
                    mutableData.setValue(currentValue + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot)
            {
                // Xử lý khi giao dịch hoàn tất (hoặc xảy ra lỗi)
                if (committed)
                {
                } else
                {
                    // Xảy ra lỗi trong quá trình thực hiện giao dịch
                }
            }
        });
    }

    private void SwitchToChapter(int chapter)
    {
        currentChapter = chapter;
        tvContent.setText(story.GetChapterContent(currentChapter));
        tvName.setText(story.getName(13) + " | C" + (currentChapter));
        if (isLoggedIn)
        {
            DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(getActivity());
            currentUsersRef.child("recentStoryRead")
                    .child(story.GetIdString())
                    .child("read")
                    .child(String.valueOf(currentChapter)).setValue(true);
            readList.set(currentChapter, true);
        }
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

    private void sendDataToActivity(String data)
    {
        Log.i("Data Listener", "Send data to activity1: " + data);

        // Gửi dữ liệu tới Activity thông qua Interface
        if (dataListener != null)
        {
            Log.i("Data Listener", "Send data to activity: " + data);
            dataListener.onDataReceived(data);
        }
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