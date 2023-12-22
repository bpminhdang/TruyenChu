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


public class HorizontalSmallImageAdapter extends RecyclerView.Adapter<HorizontalSmallImageAdapter.ViewHolder>
{
    private Context context;
    static ArrayList<StoryClass> arr;

    public HorizontalSmallImageAdapter(Context context, ArrayList<StoryClass> dataSet)
    {
        this.context = context;
        arr = dataSet;
    }

    public static void updateData(StoryClass e)
    {
        arr.add(e);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView storyImage;
        private TextView tvName;

        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View
            storyImage = view.findViewById(R.id.item_horizontal_small_image_storyImage);
            tvName = view.findViewById(R.id.item_horizontal_small_image_tvName);
        }


        public ImageView getStoryImage()
        {
            return storyImage;
        }

        public TextView getTvName()
        {
            return tvName;
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
                .inflate(R.layout.horizontal_small_image_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the images of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {

        // Get element from your dataset at this position and replace the
        // images of the view with that element


        StoryClass story = arr.get(position);
        String temp;

        Glide.with(viewHolder.itemView.getContext())
                .load(story.getUri())
                .into(viewHolder.getStoryImage());
        viewHolder.getTvName().setText(story.getName(13));
        Log.i("ABC", "onBindViewHolder: " + " " + position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return arr.size();
    }
}
