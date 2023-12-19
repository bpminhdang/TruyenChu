package com.example.truyenchu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.truyenchu.R;
import com.example.truyenchu._class.StoryClass;

import java.util.ArrayList;


public class HorizontalContentAdapter extends RecyclerView.Adapter<HorizontalContentAdapter.ViewHolder>
{
    private Context context;
    static ArrayList<StoryClass> arr;

    public HorizontalContentAdapter(Context context, ArrayList<StoryClass> dataSet)
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
        private TextView tvAuthor;
        private TextView tvChapter;
        private TextView tvChapter2;
        private TextView tvStatus;
        private TextView tvView;

        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View
            storyImage = view.findViewById(R.id.item_horizontal_content_storyImage);
            tvName = view.findViewById(R.id.item_horizontal_content_tvName);
            tvAuthor = view.findViewById(R.id.item_horizontal_content_tvAuthor);
            tvChapter = view.findViewById(R.id.item_horizontal_content_tvChapter);
            tvChapter2 = view.findViewById(R.id.item_horizontal_content_tvChapter2);
            tvStatus = view.findViewById(R.id.item_horizontal_content_tvStatus);
            tvView = view.findViewById(R.id.item_horizontal_content_tvView);
        }


        public ImageView getStoryImage()
        {
            return storyImage;
        }

        public TextView getTvName()
        {
            return tvName;
        }

        public TextView getTvAuthor()
        {
            return tvAuthor;
        }

        public TextView getTvChapter()
        {
            return tvChapter;
        }

        public TextView getTvChapter2()
        {
            return tvChapter2;
        }

        public TextView getTvStatus()
        {
            return tvStatus;
        }

        public TextView getTvView()
        {
            return tvView;
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
                .inflate(R.layout.horizontal_content_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        StoryClass story = arr.get(position);
        String temp;

        //Todo: Image
        //viewHolder.getStoryImage().setImageDrawable(story.getImage());

        viewHolder.getStoryImage();
        viewHolder.getTvName().setText(story.getName());

        temp = context.getString(R.string.author) + " " + story.getAuthor();
        viewHolder.getTvAuthor().setText(temp);

        temp = context.getString(R.string.number_of_chapter) + " " + story.getNumberOfChapter();
        viewHolder.getTvChapter().setText(temp);

        temp = context.getString(R.string.status) + " " + story.getStatus();
        viewHolder.getTvStatus().setText(temp);

        temp = context.getString(R.string.views) + " " + story.getViews();
        viewHolder.getTvView().setText(temp);

        temp = story.getNumberOfChapter() + " " + context.getString(R.string.chapter);
        viewHolder.getTvChapter2().setText(temp);

        Log.i("ABC", "onBindViewHolder: " + " " + position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return arr.size();
    }
}
