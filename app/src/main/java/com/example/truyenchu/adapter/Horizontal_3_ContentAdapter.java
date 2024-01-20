package com.example.truyenchu.adapter;

import android.content.Context;
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
import com.example.truyenchu._interface.RecyclerViewItemClickListener;

import java.util.ArrayList;


public class Horizontal_3_ContentAdapter extends RecyclerView.Adapter<Horizontal_3_ContentAdapter.ViewHolder>
{
    private Context context;
    static ArrayList<StoryClass> arr;
    private final RecyclerViewItemClickListener listener;

    public Horizontal_3_ContentAdapter(Context context, ArrayList<StoryClass> dataSet, RecyclerViewItemClickListener listener)
    {
        this.context = context;
        arr = dataSet;
        this.listener = listener;
    }

    public static void updateData(StoryClass e)
    {
        arr.add(e);
    }

    public void updateList(ArrayList<StoryClass> newList)
    {
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
                .inflate(R.layout.horizontal_3_content_item, viewGroup, false);

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
        String temp;

        Glide.with(viewHolder.itemView.getContext())
                .load(story.getUri())
                .into(viewHolder.getStoryImage());
        viewHolder.getTvName().setText(story.getName(20));

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

       // Log.i("ABC", "onBindViewHolder: " + " " + position);
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
        private TextView tvAuthor;
        private TextView tvChapter;
        private TextView tvChapter2;
        private TextView tvStatus;
        private TextView tvView;
        private ToggleButton btFavorite;

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
            btFavorite = view.findViewById(R.id.item_horizontal_content_button_favorite);
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

        public void bind(StoryClass storyClass, RecyclerViewItemClickListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(storyClass);
                }
            });

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

}
