package com.example.truyenchu.story;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu.R;
import com.example.truyenchu._class.CommentClass;
import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.CommentAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class StoryRatingActivity extends AppCompatActivity
{
    ArrayList<CommentClass> commentClasses = new ArrayList<>();
    DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    DatabaseReference storyRef;
    DatabaseReference commentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_rating);

        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(Color.WHITE);
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.WHITE);


        Intent intent = getIntent();
        if (intent == null)
            return;
        String receivedStoryID = String.valueOf(intent.getSerializableExtra("storyData"));
        storyRef = database.child("stories").child("story_" + receivedStoryID);
        commentRef = storyRef.child("comments");

        ImageView but = findViewById(R.id.xemnhanxet_back);
        but.setOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.xemnhanxet_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CommentAdapter adapter = new CommentAdapter(commentClasses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        TextView tvRating = findViewById(R.id.asr_hint_rating);
        RatingBar ratingBar = findViewById(R.id.asr_ratingBar);
        Button btComment = findViewById(R.id.asr_write_comment);
        commentRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                commentClasses.clear();
                float ratingAVG = 0;
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren())
                {
                    CommentClass comment = commentSnapshot.getValue(CommentClass.class);
                    commentClasses.add(comment);
                    ratingAVG += comment.getRating();
                }
                if (commentClasses.size() != 0)
                {
                    ratingAVG = ratingAVG / commentClasses.size();
                    storyRef.child("avgRating").setValue(ratingAVG);
                }
                ratingBar.setRating(ratingAVG);

                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.DOWN);
                tvRating.setText( df.format(ratingAVG));

                adapter.updateData(commentClasses);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
            }
        });

        btComment.setOnClickListener(v->
                showCustomDialog());

    }

    private void showCustomDialog() {
        if (UserClass.GetUserInfoFromPref(this, "uuid") == null)
        {
            Toast.makeText(this, "Vui lòng đăng nhập để bình luận!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo dialog mới và set layout từ file XML
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_write_rating);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);

        // Cài đặt các thành phần trong dialog
        RatingBar ratingBar = dialog.findViewById(R.id.dgc_ratingBar);
        TextInputEditText commentEditText = dialog.findViewById(R.id.dgc_comment);
        Button sendButton = dialog.findViewById(R.id.dgc_send);
        Button cancelButton = dialog.findViewById(R.id.dgc_cancel);

        // Xử lý sự kiện khi nhấn nút "Gửi nhận xét"
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ RatingBar và TextInputEditText
                float rating = ratingBar.getRating();
                String comment = commentEditText.getText().toString();
                CommentClass commentClass = new CommentClass();
                commentClass.setComment(comment);
                commentClass.setRating(rating);
                commentClass.setUsername(UserClass.GetUserInfoFromPref(getApplicationContext(), "uuid"));
                commentRef.child(String.valueOf(commentClasses.size())).setValue(commentClass);
                dialog.dismiss(); // Đóng dialog sau khi xử lý
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng dialog
            }
        });

        dialog.show();
    }
}