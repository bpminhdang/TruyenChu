package com.example.truyenchu.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.HorizontalContentAdapter;
import com.example.truyenchu.adapter.HorizontalImageAdapter;
import com.example.truyenchu.adapter.HorizontalSmallImageAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
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

        //TODO: Thay vì tự tạo Story, lấy dữ liệu từ Firebase và đưa nó vào nhiều object, sau đó đưa vào recyclerView

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference usersRef = database.child("users");
        Map<String, Object> users = new HashMap<>();
        users.put("userId1", new UserClass("user1", "User One", "user1@example.com", "hashedpassword1", "Arial", 12, "#FFFFFF"));
        users.put("userId2", new UserClass("user2", "User Two", "user2@example.com", "hashedpassword2", "Times New Roman", 14, "#F0F0F0"));
        usersRef.setValue(users, (databaseError, databaseReference) ->
        {
            if (databaseError != null)
            {
                System.out.println("Data could not be saved " + databaseError.getMessage());
            } else
            {
                System.out.println("Users data saved successfully.");
            }
        });

        // Thêm dữ liệu cho stories
        DatabaseReference storiesRef = database.child("stories");
        //ArrayList<StoryClass> Stories = new ArrayList<>();
        StoryClass story_Class_1 = new StoryClass(2,"Khi hơi thở hóa thinh không", "Paul Katy", "Full","19 giờ trước",  10, Arrays.asList("First String", "Second String"), 103);

        String chapter_1ct = "Trong một thung lũng xa xôi, nơi tình khúc hòa với tiếng gió, có một lão già tên là Oren. Ông là người duy nhất trong làng biết về bí mật của \"Hơi Thở Hóa Thinh Không\".\n" +
                "Lão Oren từng nói rằng mỗi sinh linh đều có khả năng biến hơi thở của mình thành một sức mạnh vô hình, tạo nên những điều kỳ diệu. Người ta cười chê và coi ông như một người mơ mộng.\n" +
                "Trong một buổi chiều trời rực rỡ, Dara - một cô bé tinh nghịch, đầy tò mò đã đến thăm lão Oren. Cô bé vừa học được về bí mật này và muốn hiểu rõ hơn.\n" +
                "\"Mỗi hơi thở là một phép màu, Dara,\" ông Oren nói. \"Nếu ta biết cách tập trung, ta có thể làm bất cứ điều gì.\"\n" +
                "Dara đã quyết định thử sức. Cô bé đóng một quả bóng và nhả hơi thở của mình vào đó. Rồi, kì diệu đã xảy ra. Quả bóng bắt đầu nhấp nhổm, bay lên và sáng rực như ngọn đèn lồng.\n" +
                "Với sức mạnh của hơi thở, Dara tạo ra những hình ảnh phong phú: một con rồng nhỏ bay quanh, một bông hoa màu sắc lấp lánh, và thậm chí là một cánh buồm trắng nhẹ nhàng trên mặt nước.\n" +
                "Từ ngày đó, tin đồn về \"Hơi Thở Hóa Thinh Không\" lan tỏa khắp làng. Mọi người hiểu rõ hơn về sức mạnh tiềm ẩn trong từng hơi thở. Họ đã học cách tin vào điều không tưởng và thấy rằng, trong mỗi chúng ta, đều có khả năng tạo ra kỳ diệu từ những điều tưởng chừng nhỏ nhất.";

        story_Class_1.addChapter(new ChapterClass(1, chapter_1ct));
        storiesRef.child("storyId1").setValue(story_Class_1, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.i("DB", "Data could not be saved " + databaseError.getMessage());
            } else {
                Log.i("DB", "Story data saved successfully.");
                Gson gson = new Gson();
                String storyJson = gson.toJson(story_Class_1); // Convert the object to JSON

                Log.i("DB", "Data pushed: " + storyJson);
            }
        });




//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//        Stories.add(story_Class_1);
//
//        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
//        HorizontalSmallImageAdapter adapter = new HorizontalSmallImageAdapter(getActivity(), Stories);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        // Inflate the layout for this fragment
//
//        RecyclerView recyclerView1=view.findViewById(R.id.home_recycler_view_2);
//        HorizontalContentAdapter adapter1 = new HorizontalContentAdapter(getActivity(),Stories);
//        recyclerView1.setAdapter(adapter1);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        RecyclerView recyclerView2=view.findViewById(R.id.home_recycler_view_3);
//        HorizontalImageAdapter adapter2 = new HorizontalImageAdapter(getActivity(),Stories);
//        recyclerView2.setAdapter(adapter2);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return view;


    }
}