package com.example.truyenchu.features;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu.R;
import com.example.truyenchu._class.ChapterClass;
import com.example.truyenchu._class.NetworkUtil;
import com.example.truyenchu._class.StoryClass;
import com.example.truyenchu.adapter.ImageUploadCallback;
import com.example.truyenchu.adapter.StoryCountListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateStoryFragment extends Fragment implements StoryCountListener
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private Uri imageUri;

    private String mParam1;
    private String mParam2;
    private int storyCount;
    private ImageView imageView;
    ArrayList<StoryClass> usersStory = new ArrayList<>();
    ArrayList<String> usersStoryString = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private int selectedStoryPosition = 0;


    @Override
    public void onStoryCountReceived(int storyCount)
    {
        this.storyCount = storyCount;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStoryFragment.
     */
    public static UpdateStoryFragment newInstance(String param1, String param2)
    {
        UpdateStoryFragment fragment = new UpdateStoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateStoryFragment()
    {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_story, container, false);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, usersStoryString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp_name_update = view.findViewById(R.id.spiner_update);
        sp_name_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                selectedStoryPosition = position;
                TextInputLayout textInputLayout = view.findViewById(R.id.update_input_layout_chapter_to_update);
                textInputLayout.setHint("Cập nhật chương: " + usersStory.get(position).getNumberOfChapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        sp_name_update.setAdapter(adapter);

        TextInputEditText et_genres_update = view.findViewById(R.id.genres_update);
        SwitchMaterial sw_final = view.findViewById(R.id.update_switch_final);
        Button bt_chooseImage = view.findViewById(R.id.update_bt_chooseImage);
        TextInputEditText et_description_update = view.findViewById(R.id.et_description);
        TextInputEditText et_chapter_content_update = view.findViewById(R.id.chapter_content_update);
        Button bt_Upload = view.findViewById(R.id.btUpload_update);
        Button bt_Draft = view.findViewById(R.id.btDraft_update);
        Button bt_Reload = view.findViewById(R.id.btReload_update);
        ImageView bt_Back = view.findViewById(R.id.ivBack_update);
        imageView = view.findViewById(R.id.update_iv_cover1);
        TextView tv_Author = view.findViewById(R.id.tvUsername_update);
        SharedPreferences usersInfoPreference = getActivity().getSharedPreferences("users_info", Context.MODE_PRIVATE);
        String uuid = usersInfoPreference.getString("uuid", null);
        if (uuid != null)
            tv_Author.setText(usersInfoPreference.getString("name", null));

        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference storyRef = database.child("stories");
        storyRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // Iterate through the results
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Map<String, Object> storyData = (Map<String, Object>) snapshot.getValue();
                    if (storyData.get("userUUID") != null)
                    {
                        if (!storyData.get("userUUID").equals(uuid))
                            continue;
                        StoryClass story = new StoryClass((int) (long) storyData.get("id"));
                        story.setName((String) storyData.get("name"));
                        story.setNumberOfChapter((int) (long) storyData.get("numberOfChapter"));
                        TextInputLayout textInputLayout = view.findViewById(R.id.update_input_layout_chapter_to_update);
                        textInputLayout.setHint("Cập nhật chương: " + story.getNumberOfChapter());
                        usersStory.add(story);
                        usersStoryString.add(story.getId() + " - " + story.getName());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        bt_Back.setOnClickListener(v ->
        {
            requireActivity().onBackPressed();
        });
        bt_chooseImage.setOnClickListener(v ->

                openFileChooser());

        bt_Draft.setOnClickListener(v ->
        {
            // String name = et_name_update.getText().toString();
            String genres = et_genres_update.getText().toString();
            String description = et_description_update.getText().toString();
            String content = et_chapter_content_update.getText().toString();

            // Saving other details to SharedPreferences
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("StoryDraft2", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.putString("name", name);
            editor.putString("genres", genres);
            editor.putString("description", description);
            editor.putString("imageUri", imageUri.toString());
            editor.apply();

            // Save story content to a text file
            try
            {
                FileOutputStream fileOutputStream = requireContext().openFileOutput("story2.txt", Context.MODE_PRIVATE);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();

                Drawable drawable = imageView.getDrawable();
                Bitmap bitmap;

                if (drawable instanceof BitmapDrawable)
                {
                    bitmap = ((BitmapDrawable) drawable).getBitmap();

                    String fileName = "story2.jpg";
                    FileOutputStream imageOutputStream = null;

                    try
                    {
                        imageOutputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutputStream); // Compress and save the bitmap
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        try
                        {
                            if (imageOutputStream != null)
                            {
                                imageOutputStream.close(); // Close the file output stream
                            }
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        bt_Reload.setOnClickListener(v ->
        {

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("StoryDraft2", Context.MODE_PRIVATE);
            String savedName = sharedPreferences.getString("name", "");
            String savedGenres = sharedPreferences.getString("genres", "");
            String savedDescription = sharedPreferences.getString("description", "");
            String imageUriString = sharedPreferences.getString("imageUri", "");
            String fileName = "story2.jpg";
            Bitmap loadedBitmap = null;


            //et_name_update.setText(savedName);
            et_genres_update.setText(savedGenres);
            et_description_update.setText(savedDescription);

            // Load story content from the text file
            try
            {
                FileInputStream fileInputStream = requireContext().openFileInput("story2.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    contentBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                et_chapter_content_update.setText(contentBuilder.toString());
                Toast.makeText(getContext(), "Loaded successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                Toast.makeText(getContext(), "Error loading content!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            try
            {
                if (!imageUriString.isEmpty())
                {
                    imageUri = Uri.parse(imageUriString);
                    imageView.setImageURI(imageUri); // Set the selected image to the ImageView
                }
                FileInputStream fileInputStream = requireContext().openFileInput(fileName);
                loadedBitmap = BitmapFactory.decodeStream(fileInputStream); // Decode the stored file into a Bitmap
                fileInputStream.close(); // Close the file input stream
                imageView.setImageBitmap(loadedBitmap);
            } catch (Exception e)
            {
                Toast.makeText(getContext(), "Can't retrieve image, please choose it manually!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


        bt_Upload.setOnClickListener(v ->
        {
            if (!NetworkUtil.isNetworkConnected(getContext()))
            {
                Toast.makeText(getContext(), "Not connected to the internet. Check your connection and try again!", Toast.LENGTH_LONG).show();
                return;
            }

            StoryClass story = usersStory.get(selectedStoryPosition);
            int id = story.getId();
            DatabaseReference StoryUpdateRef = storyRef.child("story_" + id);

            if (!TextUtils.isEmpty(et_chapter_content_update.getText()))
            {
                String content = et_chapter_content_update.getText().toString();
                EditText et_chapter_num = view.findViewById(R.id.update_et_chapter_to_update);
                int numberOfChapter;
                if (TextUtils.isEmpty(et_chapter_num.getText()))
                {
                    story.setNumberOfChapter(story.getNumberOfChapter() + 1);
                    numberOfChapter = story.getNumberOfChapter();
                    TextInputLayout textInputLayout = view.findViewById(R.id.update_input_layout_chapter_to_update);
                    textInputLayout.setHint("Cập nhật chương: " + numberOfChapter);
                } else
                    numberOfChapter = Integer.parseInt(et_chapter_num.getText().toString());

                StoryUpdateRef.child("chapters").child("chapter_" + id + "_" + numberOfChapter).setValue(new ChapterClass(id + "_" + numberOfChapter, content));
                StoryUpdateRef.child("numberOfChapter").setValue(numberOfChapter);
            }


            String status;
            if (sw_final.isChecked())
                status = "Full";
            else
                status = "Đang cập nhật";
            StoryUpdateRef.child("status").setValue(status);

            String updateTime = LocalDate.now().toString();
            StoryUpdateRef.child("updateTime").setValue(updateTime);

            if (!TextUtils.isEmpty(et_description_update.getText()))
                StoryUpdateRef.child("description").setValue(et_description_update.getText().toString());

            if (!TextUtils.isEmpty(et_genres_update.getText()))
            {
                List<String> genres;
                String[] splittedStrings = Objects.requireNonNull(et_genres_update.getText()).toString().split(",\\s*");
                genres = new ArrayList<>(Arrays.asList(splittedStrings));
                StoryUpdateRef.child("genresList").setValue(genres);
            }

            // Handle upload Story
            if (imageUri != null)
            {
                uploadImageToFirebase("story_" + id, imageUriStringFB ->
                        storyRef.child("story_" + id).child("uri").setValue(imageUriStringFB));
            }

            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Set the selected image to the ImageView
        }
    }

    private void uploadImageToFirebase(String imageName, ImageUploadCallback callback)
    {
        if (imageUri != null)
        {
            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://truyenchu-89dd1.appspot.com");
            StorageReference storageRef = storage.getReference().child("images");

            // Create a reference to the specific image file name
            StorageReference imageRef = storageRef.child(imageName + ".jpg");
            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putFile(imageUri);

            // Monitor the upload task
            uploadTask.addOnSuccessListener(taskSnapshot ->
            {
                Log.i("DB", "uploadImageToFirebase: Success");
                imageRef.getDownloadUrl().addOnSuccessListener(uri ->
                {
                    String imageUriStringFB = uri.toString();
                    Log.i("DB", "Image URL: " + imageUriStringFB);
                    if (callback != null)
                    {
                        callback.onImageUploaded(imageUriStringFB);
                    }
                });
            }).addOnFailureListener(exception ->
            {
                // Handle unsuccessful uploads
            });
        }
    }
}

