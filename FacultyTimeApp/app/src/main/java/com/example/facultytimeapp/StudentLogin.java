package com.example.facultytimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StudentLogin extends AppCompatActivity {

    private ImageButton back;
    private Button login;
    private ProgressBar progress;
    private CheckBox checkBox;
    private EditText email,password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        back = findViewById(R.id.studentback);
        login = findViewById(R.id.loginbtnstudent);
        progress = findViewById(R.id.studentprogressbar);
        checkBox = findViewById(R.id.passwordcheckboxstudent);
        email = findViewById(R.id.studentemail);
        password = findViewById(R.id.studentpassword);

        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                startActivity(new Intent(StudentLogin.this, MainActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setEnabled(false);
                back.setVisibility(View.GONE);
                login.setEnabled(false);
                progress.setVisibility(View.VISIBLE);
                String txt_email = email.getText().toString().toLowerCase();
                String txt_password = password.getText().toString();
                if(!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && txt_email.contains("student")){
                    loginUser(txt_email, txt_password);
                }
                else if(!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && !txt_email.contains("student")){
                    Toast.makeText(StudentLogin.this, "Invalid Student Username", Toast.LENGTH_LONG).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password)){
                    Toast.makeText(StudentLogin.this, "Empty Username Field", Toast.LENGTH_SHORT).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
                else if(!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)){
                    Toast.makeText(StudentLogin.this, "Empty Password Field", Toast.LENGTH_SHORT).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)){
                    Toast.makeText(StudentLogin.this, "Empty Credentials...", Toast.LENGTH_SHORT).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(null);
                }
                else{
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(StudentLogin.this, "Login Successful!!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentLogin.this, StudentLoggedIn.class));
                        finish();
                    }
                });
        auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentLogin.this, "Wrong Credentials...", Toast.LENGTH_LONG).show();
                        back.setEnabled(true);
                        back.setVisibility(View.VISIBLE);
                        login.setEnabled(true);
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}