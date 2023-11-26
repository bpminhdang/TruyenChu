package com.example.truyenchu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_content);

        ArrayList<Story> Stories = new ArrayList<>();
        Story story_1 = new Story(1,"Khi hơi thở hóa thinh không", "Paul Kat", "Full","20 giờ trước",  2, new String[]{"Self Help"});
        String chapter_1 = "Trong một thung lũng xa xôi, nơi tình khúc hòa với tiếng gió, có một lão già tên là Oren. Ông là người duy nhất trong làng biết về bí mật của \"Hơi Thở Hóa Thinh Không\".\n" +
                "Lão Oren từng nói rằng mỗi sinh linh đều có khả năng biến hơi thở của mình thành một sức mạnh vô hình, tạo nên những điều kỳ diệu. Người ta cười chê và coi ông như một người mơ mộng.\n" +
                "Trong một buổi chiều trời rực rỡ, Dara - một cô bé tinh nghịch, đầy tò mò đã đến thăm lão Oren. Cô bé vừa học được về bí mật này và muốn hiểu rõ hơn.\n" +
                "\"Mỗi hơi thở là một phép màu, Dara,\" ông Oren nói. \"Nếu ta biết cách tập trung, ta có thể làm bất cứ điều gì.\"\n" +
                "Dara đã quyết định thử sức. Cô bé đóng một quả bóng và nhả hơi thở của mình vào đó. Rồi, kì diệu đã xảy ra. Quả bóng bắt đầu nhấp nhổm, bay lên và sáng rực như ngọn đèn lồng.\n" +
                "Với sức mạnh của hơi thở, Dara tạo ra những hình ảnh phong phú: một con rồng nhỏ bay quanh, một bông hoa màu sắc lấp lánh, và thậm chí là một cánh buồm trắng nhẹ nhàng trên mặt nước.\n" +
                "Từ ngày đó, tin đồn về \"Hơi Thở Hóa Thinh Không\" lan tỏa khắp làng. Mọi người hiểu rõ hơn về sức mạnh tiềm ẩn trong từng hơi thở. Họ đã học cách tin vào điều không tưởng và thấy rằng, trong mỗi chúng ta, đều có khả năng tạo ra kỳ diệu từ những điều tưởng chừng nhỏ nhất.";
        story_1.addChapter(new Chapter(1, chapter_1));
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);
        Stories.add(story_1);

        RecyclerView recyclerView = findViewById(R.id.vertical_content_recyclerview);
        VerticalContentAdapter adapter = new VerticalContentAdapter(Stories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
//        user = auth.getCurrentUser();
//        if (user == null)
//        {
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else
//        {
//            textView.setText(user.getEmail());
//        }
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
}