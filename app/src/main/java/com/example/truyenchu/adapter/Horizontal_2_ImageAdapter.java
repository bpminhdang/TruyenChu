package com.example.truyenchu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;

import java.util.ArrayList;


public class Horizontal_2_ImageAdapter extends RecyclerView.Adapter<Horizontal_2_ImageAdapter.ViewHolder>
{
    private Context context;
    static ArrayList<StoryClass> arr;
    public final RecyclerViewItemClickListener listener;

    public Horizontal_2_ImageAdapter(Context context, ArrayList<StoryClass> dataSet, RecyclerViewItemClickListener listener)
    {
        this.context = context;
        arr = dataSet;
        this.listener = listener;
    }

    public static void updateData(StoryClass e)
    {
        arr.add(e);
    }

    public void updateList(ArrayList<StoryClass> newList) {
        this.arr = newList;
        notifyDataSetChanged();
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
                .inflate(R.layout.horizontal_2_image_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the images of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {

        // Get element from your dataset at this position and replace the
        // images of the view with that element

        viewHolder.bind(arr.get(position), listener);
        StoryClass story = arr.get(position);
        String temp;

        Glide.with(viewHolder.itemView.getContext())
                .load(story.getUri())
                .into(viewHolder.getStoryImage());

        //viewHolder.getTvName().setText(story.getName());
        viewHolder.getTvName().setText("");

        temp = context.getString(R.string.recent_chapter) + "   " + context.getString(R.string.chapter) + " " + story.getNumberOfChapter();
        viewHolder.getTvChapter2().setText(temp);
        //Log.i("ABC", "onBindViewHolder: " + " " + position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return arr.size();
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView storyImage;
        private TextView tvName;
        private TextView tvChapter2;

        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View
            storyImage = view.findViewById(R.id.item_horizontal_image_storyImage);
            tvName = view.findViewById(R.id.item_horizontal_image_tvName);
            tvChapter2 = view.findViewById(R.id.item_horizontal_image_tvChapter2);
        }


        public ImageView getStoryImage()
        {
            return storyImage;
        }

        public TextView getTvName()
        {
            return tvName;
        }

        public TextView getTvChapter2()
        {
            return tvChapter2;
        }

        public void bind(StoryClass story, RecyclerViewItemClickListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(story);
                }
            });
        }
    }
}
