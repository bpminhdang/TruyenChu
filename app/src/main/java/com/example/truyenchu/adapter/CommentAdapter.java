package com.example.truyenchu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truyenchu.R;
import com.example.truyenchu._class.CommentClass;
import com.example.truyenchu._class.UserClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.widget.RatingBar;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentClass> commentList;

    public CommentAdapter(List<CommentClass> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_nhanxet, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentClass CommentClass = commentList.get(position);
        holder.bind(CommentClass);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void updateData( List<CommentClass> commentClasses)
    {
        this.commentList = commentClasses;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentTextView;
        TextView nameTextView;
        RatingBar ratingBar;
        ToggleButton btLike;
        TextView tvLiked;
        ImageView ivProfilePic;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.cm_comment);
            nameTextView = itemView.findViewById(R.id.cm_name);
            ratingBar = itemView.findViewById(R.id.cm_rating_bar);
            btLike = itemView.findViewById(R.id.cm_button_liked);
            tvLiked = itemView.findViewById(R.id.cm_liked);
            ivProfilePic = itemView.findViewById(R.id.cm_profile_image);
            // Xử lý sự kiện khi nút được nhấn
            btLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {

                            tvLiked.setText(String.valueOf(Integer.parseInt((String) tvLiked.getText()) + 1));
                            // Todo: Update liked
                            //DatabaseHelper.updateLiked();
                        }
                    } else
                    {
                        tvLiked.setText(String.valueOf(Integer.parseInt((String) tvLiked.getText()) - 1));
                    }
                }
            });

        }

        public void bind(CommentClass CommentClass) {
            commentTextView.setText(CommentClass.getComment());
            String uuid =  CommentClass.getUsername();
            DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            DatabaseReference storyRef = database.child("users").child(uuid);
            storyRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        UserClass user = dataSnapshot.getValue(UserClass.class);
                        if (user != null)
                        {
                            nameTextView.setText(user.getName());
                            Glide.with(itemView.getContext()).load(user.getProfile()).into(ivProfilePic);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    // Xử lý khi có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
                }
            });
            ratingBar.setRating((float) CommentClass.getRating());
            // Thiết lập giá trị cho button
            tvLiked.setText(String.valueOf(CommentClass.GetNumLike()));

        }
    }
}
