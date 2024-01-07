package com.example.truyenchu.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu.R;
import com.example.truyenchu._class.CommentClass;
import com.example.truyenchu.features.DatabaseHelper;


import android.widget.Button;
import android.widget.RatingBar;

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
        Button button;
        TextView liked;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.cm_comment);
            nameTextView = itemView.findViewById(R.id.cm_name);
            ratingBar = itemView.findViewById(R.id.cm_rating_bar);
            button = itemView.findViewById(R.id.cm_button_liked);
            liked = itemView.findViewById(R.id.cm_liked);
            // Xử lý sự kiện khi nút được nhấn
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý khi nút được nhấn ở đây
                    // Ví dụ: Lấy vị trí của item trong RecyclerView
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Xử lý sự kiện click tại vị trí này
                        Log.i("Comment", "Like" + position);
                        // Todo: Update liked
                        //DatabaseHelper.updateLiked();
                    }
                }
            });
        }

        public void bind(CommentClass CommentClass) {
            commentTextView.setText(CommentClass.getComment());
            nameTextView.setText(CommentClass.getUsername());
            ratingBar.setRating((float) CommentClass.getRating());
            // Thiết lập giá trị cho button
            liked.setText(String.valueOf(CommentClass.GetNumLike()));
        }
    }
}
