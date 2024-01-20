package com.example.truyenchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu._class.UserClass;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity
{
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    TextView textView;


  /*  @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }*/

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Đóng LoginActivity sau khi chuyển đến HomeActivity
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String uuid = UserClass.GetUserInfoFromPref(getApplicationContext(), "uuid");
        if (uuid != null)
        {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Status bar icon: Black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Status bar: accent 1_0
        getWindow().setStatusBarColor(Color.parseColor("#fdedee"));
        // Navigation pill: White
        getWindow().setNavigationBarColor(Color.parseColor("#fdedee"));

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_lg);
        editTextPassword = findViewById(R.id.password_lg);
        buttonLogin = findViewById(R.id.bt_lg);
        progressBar = findViewById(R.id.progressBar);
        Button btSwitch = findViewById(R.id.btSwitchlg);

        //Ẩn mật khẩu
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        btSwitch.setOnClickListener(v ->
        {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.iv_Back).setOnClickListener(v ->
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        });


        buttonLogin.setOnClickListener(v ->
        {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            //Check Empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task ->
                    {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified())
                            {
                                Toast.makeText(getApplicationContext(), "LoginActivity successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("users_info", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uuid", mAuth.getCurrentUser().getUid());
                                editor.remove("recent");
                                editor.remove("saved");
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong password/Wrong email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}