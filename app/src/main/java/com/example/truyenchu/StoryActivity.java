package com.example.truyenchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.DataListener;
import com.example.truyenchu.features.DatabaseHelper;
import com.example.truyenchu.features.StoryDescriptionFragment;
import com.example.truyenchu.features.StoryReadingFragment;
import com.example.truyenchu.ui.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StoryActivity extends AppCompatActivity implements DataListener
{
    StoryClass receivedStory;
    private boolean isRead = false;
    List<Boolean> readList = new ArrayList<>();
    List<Boolean> favList = new ArrayList<>();
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent = getIntent();
        if (intent == null)
            return;
        receivedStory = (StoryClass) intent.getSerializableExtra("storyData");
        DatabaseHelper.updateCount(receivedStory.getId(), "watching", 1);

        Log.i("Life cycle story", "OnCre");


        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);

        ConstraintLayout bottomNav = findViewById(R.id.bottom_navigation_custom_avs);


        if (savedInstanceState == null)
        {
            // Nếu không có fragment đã được thêm, thêm vào
            StoryDescriptionFragment fragment = new StoryDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("receivedStoryID", receivedStory.getId());
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_avs, fragment, "YOUR_FRAGMENT_TAG")
                    .commit();
        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, selectedFragment)
//                .addToBackStack(null) // Để thêm Fragment vào Backstack
//                .commit();
//        return true;
        ImageView bt_tai = findViewById(R.id.btDown);
        bt_tai.setOnClickListener(v ->
        {
            String uuid = UserClass.GetUserInfoFromPref(this, "uuid");
            if (uuid != null)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("users_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String recent = UserClass.GetUserInfoFromPref(this, "saved");
                if (recent == null)
                    editor.putString("saved", receivedStory.GetIdString());
                else if (recent.contains(receivedStory.GetIdString()))
                {
                    // Nếu có, tạo chuỗi mới không chứa số đó
                    String[] numbers = recent.split("_");
                    StringBuilder resultBuilder = new StringBuilder();

                    for (String number : numbers)
                    {
                        int currentNumber = Integer.parseInt(number);
                        if (currentNumber != receivedStory.getId())
                        {
                            resultBuilder.append(number).append("_");
                        }
                    }

                    // Loại bỏ dấu '_' cuối cùng nếu có
                    if (resultBuilder.length() > 0)
                        resultBuilder.deleteCharAt(resultBuilder.length() - 1);
                    String resultString = resultBuilder.toString();
                    editor.putString("saved", resultString);
                    DatabaseHelper.GetCurrentUserReference(this).child("saved").setValue(resultString);
                    Toast.makeText(getApplicationContext(), "Đã xóa khỏi danh sách truyện!", Toast.LENGTH_SHORT).show();
                } else
                {
                    recent = receivedStory.GetIdString() + "_" + recent;
                    String[] numbers = recent.split("_");

                    // Chuyển mảng thành Set để loại bỏ số trùng lặp (sử dụng LinkedHashSet để duy trì thứ tự)
                    Set<String> uniqueNumbersSet = new LinkedHashSet<>(Arrays.asList(numbers));
                    String[] resultArray = uniqueNumbersSet.toArray(new String[0]);
                    recent = String.join("_", resultArray);
                    editor.putString("saved", recent);
                    DatabaseHelper.GetCurrentUserReference(this).child("saved").setValue(recent);
                    Toast.makeText(getApplicationContext(), "Đã lưu vào danh sách truyện!", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });

        Button bt_read = findViewById(R.id.btRead);
        bt_read.setOnClickListener(v ->
        {
            //Todo: màu
            // Chỉ tăng view mỗi khi tạo activity mới
            // Todo: Chỉ tăng view khi người dùng chưa từng đọc truyện (Optional)
            if (!isRead)
            {
                DatabaseHelper.updateCount(receivedStory.getId(), "views", 1);
                isRead = true;
                String uuid = UserClass.GetUserInfoFromPref(this, "uuid");
                if (uuid != null)
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("users_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String recent = UserClass.GetUserInfoFromPref(this, "recent");
                    if (recent == null)
                        editor.putString("recent", receivedStory.GetIdString());
                    else
                    {
                        recent = receivedStory.GetIdString() + "_" + recent;
                        String[] numbers = recent.split("_");

                        // Chuyển mảng thành Set để loại bỏ số trùng lặp (sử dụng LinkedHashSet để duy trì thứ tự)
                        Set<String> uniqueNumbersSet = new LinkedHashSet<>(Arrays.asList(numbers));
                        String[] resultArray = uniqueNumbersSet.toArray(new String[0]);
                        recent = String.join("_", resultArray);


                        int countRecent = 0;
                        int maxRecentStory = 20;
                        for (char c : recent.toCharArray())
                        {
                            if (c == '_')
                                countRecent++;
                        }
                        if (countRecent > maxRecentStory)
                        {
                            String[] parts = recent.split("_");
                            recent = String.join("_", Arrays.copyOf(parts, maxRecentStory));
                        }
                        editor.putString("recent", recent);
                        DatabaseHelper.GetCurrentUserReference(this).child("recentString").setValue(recent);

                    }

                    editor.apply();
                }

            }

            StoryReadingFragment storyReadingFragment = StoryReadingFragment.newInstance(receivedStory.getId(), readList, favList, isLoggedIn, 1);
            storyReadingFragment.setDataListener(StoryActivity.this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_avs, storyReadingFragment)
                    .setCustomAnimations(R.anim.fade_in_300, R.anim.fade_out)
                    .addToBackStack(null) // Để thêm Fragment vào Backstack
                    .commit();
            FrameLayout frameLayoutNavigation = findViewById(R.id.frameLayout_avs);
            frameLayoutNavigation.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );

        });


        String uuid = UserClass.GetUserInfoFromPref(this, "uuid");
        if (uuid != null)
        {
            DatabaseReference currentUsersRef = DatabaseHelper.GetCurrentUserReference(this);

            // Lấy giá trị của readCount
            DatabaseReference readCountRef = currentUsersRef.child("readCount");
            readCountRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        int readCount = dataSnapshot.getValue(Integer.class);
                        String recentStoryReadPath = "recentStoryRead/" + receivedStory.getId();
                        DatabaseReference recentStoryReadRef = currentUsersRef.child(recentStoryReadPath);


                        // Kiểm tra xem nút tồn tại hay không
                        recentStoryReadRef.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (!dataSnapshot.exists())
                                {
                                    // Nếu nút không tồn tại, thực hiện các hành động tạo mới
                                    Map<String, Object> updateMap = new HashMap<>();
                                    for (int i = 1; i <= receivedStory.getNumberOfChapter(); i++)
                                    {
                                        updateMap.put("fav/" + i, false);
                                        updateMap.put("read/" + i, false);
                                    }
                                    recentStoryReadRef.updateChildren(updateMap);
                                    // Lưu giá trị count để tìm được truyện gần nhất đưa vào home
                                    recentStoryReadRef.child("count").setValue(readCount);
                                    // Gọi incrementReadCount() nếu cần
                                    incrementReadCount(readCountRef);
                                } else if (dataSnapshot.getChildrenCount() < receivedStory.getNumberOfChapter())
                                {
                                    for (int i = (int) dataSnapshot.getChildrenCount(); i <= receivedStory.getNumberOfChapter(); i++)
                                    {
                                        recentStoryReadRef.child("fav").child(String.valueOf(i)).setValue(false);
                                        recentStoryReadRef.child("read").child(String.valueOf(i)).setValue(false);
                                    }
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
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                // Xử lý lỗi nếu cần thiết
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    // Xử lý lỗi nếu cần thiết
                }
            });
            findViewById(R.id.btMucluc).setOnClickListener(v ->
                    ChapterChooseClick());
        } else
            findViewById(R.id.btMucluc).setOnClickListener(v ->
                    ChapterChooseClickGuest());

    }

    private void ChapterChooseClickGuest()
    {
        ArrayList<String> optionsList = new ArrayList<>();
        optionsList.add("  Chương đã đọc được tô màu xám");
        for (int i = 0; i < receivedStory.getNumberOfChapter(); i++)
        {
            optionsList.add("  Chương " + (i + 1));
        }
        String[] options = optionsList.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.RoundBorderDialog);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        builder.setTitle("Chọn chương: ");
        builder.setAdapter(adapter, (dialog, chapterPos) ->
        {
            if (chapterPos == 0)
                return;
            SwitchToChapter(chapterPos);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ChapterChooseClick()
    {
        ArrayList<String> optionsList = new ArrayList<>();
        optionsList.add("  Chương đã đọc được tô màu xám");
        for (int i = 0; i < receivedStory.getNumberOfChapter(); i++)
        {
            optionsList.add("  Chương " + (i + 1));
        }
        String[] options = optionsList.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.RoundBorderDialog);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent)
            {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                boolean noError = false;
                while (!noError) {
                    try {
                        if (readList.get(position))
                            textView.setTextColor(Color.GRAY);
                        if (favList.get(position))
                            textView.setText(textView.getText() + " ⭐");
                        noError = true;
                    } catch (Exception e) {
                        readList.add(false);
                        favList.add(false);
                       Log.i("Story Activity set text", "add false");
                    }
                }



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
    }

    private void SwitchToChapter(int chapter)
    {
        StoryReadingFragment storyReadingFragment = StoryReadingFragment.newInstance(receivedStory.getId(), readList, favList, isLoggedIn, chapter);
        storyReadingFragment.setDataListener(StoryActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_avs, storyReadingFragment)
                .setCustomAnimations(R.anim.fade_in_300, R.anim.fade_out)
                .addToBackStack(null) // Để thêm Fragment vào Backstack
                .commit();
        FrameLayout frameLayoutNavigation = findViewById(R.id.frameLayout_avs);
        frameLayoutNavigation.setVisibility(View.GONE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
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

    @Override
    public void onDataReceived(String data)
    {
        Log.i("Data Listener", "rev: " + data);
        if (data.equals("Exit reading"))
        {
            findViewById(R.id.frameLayout_avs).setVisibility(View.VISIBLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
            getWindow().setNavigationBarColor(Color.WHITE);
        }
    }

    @Override
    public void onBooleanListReceived(List<Boolean> readList, List<Boolean> favList)
    {
        this.readList = readList;
        this.favList = favList;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("Life cycle story", "OnStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("Life cycle story", "OnDes");
        DatabaseHelper.updateCount(receivedStory.getId(), "watching", -1);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("Life cycle story", "onPause");

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("Life cycle story", "onrs");

    }
}