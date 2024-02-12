package com.example.truyenchu.features;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.example.truyenchu._interface.ImageUploadCallback;
import com.example.truyenchu._interface.Notification;
import com.example.truyenchu._interface.StoryCountListener;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpdateStoryActivity extends AppCompatActivity implements StoryCountListener
{
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

    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_story);
        Notification.showNotification(this, "Notification Title", "This is the notification message.");

        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(getColor(R.color.accent_1_10));
        // Navigation pill: accent 1_0
        getWindow().setNavigationBarColor(getColor(R.color.accent_1_10));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usersStoryString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp_name_update = findViewById(R.id.spiner_update);
        sp_name_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                selectedStoryPosition = position;
                TextInputLayout textInputLayout = findViewById(R.id.update_input_layout_chapter_to_update);
                textInputLayout.setHint("Cập nhật chương: " + usersStory.get(position).getNumberOfChapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        sp_name_update.setAdapter(adapter);

        TextInputEditText et_genres_update = findViewById(R.id.genres_update);
        SwitchMaterial sw_final = findViewById(R.id.update_switch_final);
        Button bt_chooseImage = findViewById(R.id.update_bt_chooseImage);
        TextInputEditText et_description_update = findViewById(R.id.et_description);
        TextInputEditText et_chapter_content_update = findViewById(R.id.chapter_content_update);
        Button bt_Upload = findViewById(R.id.btUpload_update);
        Button bt_Draft = findViewById(R.id.btDraft_update);
        Button bt_Reload = findViewById(R.id.btReload_update);
        ImageView bt_Back = findViewById(R.id.ivBack_update);
        imageView = findViewById(R.id.update_iv_cover1);
        TextView tv_Author = findViewById(R.id.tvUsername_update);
        SharedPreferences usersInfoPreference = this.getSharedPreferences("users_info", Context.MODE_PRIVATE);
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
                        TextInputLayout textInputLayout = findViewById(R.id.update_input_layout_chapter_to_update);
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
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
            //finish();
            onBackPressed();
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
            SharedPreferences sharedPreferences = getSharedPreferences("StoryDraft2", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.putString("name", name);
            editor.putString("genres", genres);
            editor.putString("description", description);
            editor.putString("imageUri", imageUri.toString());
            editor.apply();

            // Save story content to a text file
            try
            {
                FileOutputStream fileOutputStream = openFileOutput("story2.txt", Context.MODE_PRIVATE);
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
                        imageOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
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

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        bt_Reload.setOnClickListener(v ->
        {

            SharedPreferences sharedPreferences = getSharedPreferences("StoryDraft2", Context.MODE_PRIVATE);
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
                FileInputStream fileInputStream = openFileInput("story2.txt");
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
                Toast.makeText(this, "Loaded successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                Toast.makeText(this, "Error loading content!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            try
            {
                if (!imageUriString.isEmpty())
                {
                    imageUri = Uri.parse(imageUriString);
                    imageView.setImageURI(imageUri); // Set the selected image to the ImageView
                }
                FileInputStream fileInputStream = openFileInput(fileName);
                loadedBitmap = BitmapFactory.decodeStream(fileInputStream); // Decode the stored file into a Bitmap
                fileInputStream.close(); // Close the file input stream
                imageView.setImageBitmap(loadedBitmap);
            } catch (Exception e)
            {
                Toast.makeText(this, "Can't retrieve image, please choose it manually!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


        bt_Upload.setOnClickListener(v ->
        {
            if (!NetworkUtil.isNetworkConnected(this))
            {
                Toast.makeText(this, "Not connected to the internet. Check your connection and try again!", Toast.LENGTH_LONG).show();
                return;
            }

            StoryClass story = usersStory.get(selectedStoryPosition);
            int id = story.getId();
            DatabaseReference StoryUpdateRef = storyRef.child("story_" + id);

            if (!TextUtils.isEmpty(et_chapter_content_update.getText()))
            {
                String content = et_chapter_content_update.getText().toString();
                EditText et_chapter_num = findViewById(R.id.update_et_chapter_to_update);
                int numberOfChapter;
                if (TextUtils.isEmpty(et_chapter_num.getText()))
                {
                    story.setNumberOfChapter(story.getNumberOfChapter() + 1);
                    numberOfChapter = story.getNumberOfChapter();
                    TextInputLayout textInputLayout = findViewById(R.id.update_input_layout_chapter_to_update);
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
            DatabaseReference updateStringRef = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference()
                    .child("updateString");
            updateStringRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String recent = story.GetIdString();
                    if (dataSnapshot.exists())
                    {
                        recent += "_" + dataSnapshot.getValue(String.class);

                        String[] numbers = recent.split("_");

                        // Chuyển mảng thành Set để loại bỏ số trùng lặp (sử dụng LinkedHashSet để duy trì thứ tự)
                        Set<String> uniqueNumbersSet = new LinkedHashSet<>(Arrays.asList(numbers));
                        String[] resultArray = uniqueNumbersSet.toArray(new String[0]);
                        recent = String.join("_", resultArray);

//                        int countRecent = 0;
//                        int maxRecentStory = 20;
//                        for (char c : recent.toCharArray())
//                        {
//                            if (c == '_')
//                                countRecent++;
//                        }
//                        if (countRecent > maxRecentStory)
//                        {
//                            String[] parts = recent.split("_");
//                            recent = String.join("_", Arrays.copyOf(parts, maxRecentStory));
//                        }

                    }
                    updateStringRef.setValue(recent);


                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e("Firebase", "Lỗi khi truy xuất dữ liệu: " + databaseError.getMessage());
                }
            });


            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

        });


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

