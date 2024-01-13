package com.example.truyenchu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;

import java.util.ArrayList;


public class VerticalContentAdapter extends RecyclerView.Adapter<VerticalContentAdapter.ViewHolder>
{
    private Context context;
    static ArrayList<StoryClass> arr;
    private final RecyclerViewItemClickListener listener;

    public VerticalContentAdapter(Context context, ArrayList<StoryClass> dataSet, RecyclerViewItemClickListener listener)
    {
        this.context = context;
        arr = dataSet;
        this.listener = listener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        //private LinearLayout storyInfo;
        private ImageView storyImage;
       // private LinearLayout info;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvAuthor;
        private TextView tvChapter;
        private TextView tvGenre;
        private ToggleButton btFavorite;

        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View

            //storyInfo = view.findViewById(R.id.item_vertical_content_storyInfo);
            storyImage = view.findViewById(R.id.item_vertical_content_storyImage);
            //info = view.findViewById(R.id.item_vertical_content_info);
            tvName = view.findViewById(R.id.item_vertical_content_tvName);
            tvTime = view.findViewById(R.id.item_vertical_content_tvTime);
            tvAuthor = view.findViewById(R.id.item_vertical_content_tvAuthor);
            tvChapter = view.findViewById(R.id.item_vertical_content_tvChapter);
            tvGenre = view.findViewById(R.id.item_vertical_content_tvGenre);
            btFavorite = view.findViewById(R.id.item_vertical_content_toggle_button);
        }



        public ImageView getStoryImage()
        {
            return storyImage;
        }


        public TextView getTvName()
        {
            return tvName;
        }

        public TextView getTvTime()
        {
            return tvTime;
        }

        public TextView getTvAuthor()
        {
            return tvAuthor;
        }

        public TextView getTvChapter()
        {
            return tvChapter;
        }

        public TextView getTvGenre()
        {
            return tvGenre;
        }

        public void bind(StoryClass story, RecyclerViewItemClickListener listener)
        {
            itemView.setOnClickListener(v-> listener.onItemClick(story));
            btFavorite.setOnCheckedChangeListener((buttonView, isChecked) ->
            {
                if (isChecked)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        Toast.makeText(itemView.getContext(), "Added to favorite!", Toast.LENGTH_SHORT).show();
                        //tvLiked.setText(String.valueOf(Integer.parseInt((String) tvLiked.getText()) + 1));
                        // Todo: Update favorite story
                        //DatabaseHelper.updateLiked();
                    }
                } else
                {
                    //tvLiked.setText(String.valueOf(Integer.parseInt((String) tvLiked.getText()) - 1));
                }
            });
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     * <p>
     * param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vertical_0_content_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       viewHolder.bind(arr.get(position), listener);
        StoryClass story = arr.get(position);

        try
        {
            Glide.with(viewHolder.itemView.getContext())
                    .load(story.getUri())
                    .into(viewHolder.getStoryImage());
            viewHolder.getTvName().setText(story.getName(40));
            viewHolder.getTvTime().setText("Ngày đăng: " + story.getTime() + "\n" + "Cập nhật: " + story.getUpdateTime());
            viewHolder.getTvAuthor().setText("Tác giả: " + story.getAuthor());
            viewHolder.getTvChapter().setText("Số chương: " + String.valueOf(story.getNumberOfChapter()));
            viewHolder.getTvGenre().setText(story.getGenres(25));
        }
        catch (Exception e)
        {
            Toast.makeText(context.getApplicationContext(), "Không thể load một vài hình ảnh truyện!", Toast.LENGTH_SHORT).show();


        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return arr.size();
    }

    public void updateData(ArrayList<StoryClass> e)
    {
        arr = e;
        notifyDataSetChanged();
    }
}
