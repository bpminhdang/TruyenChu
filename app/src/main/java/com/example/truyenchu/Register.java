package com.example.truyenchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu._class.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity
{
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    TextInputEditText editTextRepassword;

    TextInputEditText editTextName;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.reg_name);
        editTextEmail = findViewById(R.id.email_rg);
        editTextPassword = findViewById(R.id.password_rg);
        editTextRepassword = findViewById(R.id.repassword);
        buttonReg = findViewById(R.id.bt_rg);
        progressBar = findViewById(R.id.progressBar);


        //Ẩn mật khẩu
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextRepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Điều hướng từ Đăng ký sang Đăng nhập
        findViewById(R.id.iv_Back).setOnClickListener(v ->
        {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        //Thao tác đăng ký
        buttonReg.setOnClickListener(v ->
        {
            progressBar.setVisibility(View.VISIBLE);
            String email, password, repassword, name;

            name = String.valueOf(editTextName.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());
            repassword = String.valueOf(editTextRepassword.getText());

            //Kiểm tra mật khẩu nhập 2 lần có giống nhau
//            if (password != repassword)
//            {
//                progressBar.setVisibility(View.GONE);
//                editTextPassword.setText("");
//                editTextRepassword.setText("");
//                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                return;
//            }

            //Check Empty
            if (TextUtils.isEmpty(email)){
                Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Register.this, "User registered successfully. Please verify your email",
                                                    Toast.LENGTH_SHORT).show();

                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if (currentUser != null && !TextUtils.isEmpty(name)) {
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name)
                                                        .build();
                                            }
                                            //Lưu vào user
                                            UserClass userClass = new UserClass();
                                            userClass.setName(name);
                                            userClass.setEmail(email);
                                            userClass.setPassword(password);

                                            editTextName.setText("");
                                            editTextEmail.setText("");
                                            editTextPassword.setText("");
                                            editTextRepassword.setText("");
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
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
}