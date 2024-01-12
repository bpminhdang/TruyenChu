package com.example.truyenchu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.truyenchu._class.UserClass;
import com.example.truyenchu.adapter.ImageUploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    TextInputEditText editTextRepassword;
    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;


    TextInputEditText editTextName;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button bt_chooseImage;
    ImageView iv_Profile;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(Color.parseColor("#fdedee"));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.parseColor("#fdedee"));
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.reg_name);
        editTextEmail = findViewById(R.id.email_rg);
        editTextPassword = findViewById(R.id.password_rg);
        editTextRepassword = findViewById(R.id.repassword);
        buttonReg = findViewById(R.id.bt_rg);
        progressBar = findViewById(R.id.progressBar);
        bt_chooseImage = findViewById(R.id.bt_chooseImage_register);
        iv_Profile = findViewById(R.id.iv_profile_register);
        //Ẩn mật khẩu
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextRepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Điều hướng từ Đăng ký sang Đăng nhập
        findViewById(R.id.iv_Back).setOnClickListener(v ->
        {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        bt_chooseImage.setOnClickListener(v ->
                openFileChooser());

        //Thao tác đăng ký
        buttonReg.setOnClickListener(v ->
        {
            progressBar.setVisibility(View.VISIBLE);
            String email, password, repassword, name;

            name = String.valueOf(editTextName.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());
            repassword = String.valueOf(editTextRepassword.getText());

          //  Kiểm tra mật khẩu nhập 2 lần có giống nhau
            if (!TextUtils.equals(password, repassword)) {
                progressBar.setVisibility(View.GONE);
                editTextPassword.setText("");
                editTextRepassword.setText("");
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            //Check Empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri == null)
            {
                Toast.makeText(this, "Please choose profile picture!", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User registered successfully. Please verify your email",
                                                    Toast.LENGTH_SHORT).show();

                                            //Lưu vào user
                                            UserClass userClass = new UserClass();
                                            userClass.setUuid(mAuth.getCurrentUser().getUid());
                                            userClass.setName(name);
                                            userClass.setEmail(email);
                                            userClass.setPassword(password);

                                            uploadUserToFirebase(userClass);
                                            editTextName.setText("");
                                            editTextEmail.setText("");
                                            editTextPassword.setText("");
                                            editTextRepassword.setText("");
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Đăng ký thất bại.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }

    private void uploadUserToFirebase(UserClass user) {
        DatabaseReference database = FirebaseDatabase.getInstance("https://truyenchu-89dd1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference usersRef = database.child("users");
        usersRef.child(user.getUuid()).setValue(user);
        usersRef.child(user.getUuid()).child("readCount").setValue(0);
        uploadImageToFirebase(user.getUuid(), imageUriStringFB->
                usersRef.child(user.getUuid()).child("profile").setValue(imageUriStringFB));

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
            iv_Profile.setImageURI(imageUri); // Set the selected image to the ImageView
        }
    }


    private void uploadImageToFirebase(String imageName, ImageUploadCallback callback)
    {
        if (imageUri != null)
        {
            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://truyenchu-89dd1.appspot.com");
            StorageReference storageRef = storage.getReference().child("usersImage");

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