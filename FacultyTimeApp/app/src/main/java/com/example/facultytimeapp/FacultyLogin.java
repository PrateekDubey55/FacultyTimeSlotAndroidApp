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

public class FacultyLogin extends AppCompatActivity {

    private ImageButton back;
    private Button login;
    private ProgressBar progress;
    private CheckBox checkBox;
    private EditText email,password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        back = findViewById(R.id.facultyback);
        login = findViewById(R.id.loginbtnfaculty);
        progress = findViewById(R.id.facultyprogressbar);
        checkBox = findViewById(R.id.passwordcheckboxfaculty);
        email = findViewById(R.id.facultyemail);
        password = findViewById(R.id.facultypassword);

        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                startActivity(new Intent(FacultyLogin.this, MainActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString().toLowerCase();
                String txt_password = password.getText().toString();
                if(!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password)
                        && !txt_email.contains("admin") && !txt_email.contains("student")){
                    back.setEnabled(false);
                    back.setVisibility(View.GONE);
                    login.setEnabled(false);
                    progress.setVisibility(View.VISIBLE);
                    loginUser(txt_email, txt_password);
                }
                else if(txt_email.contains("admin") || txt_email.contains("student")){
                    Toast.makeText(FacultyLogin.this, "Invalid Faculty Username", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password)){
                    Toast.makeText(FacultyLogin.this, "Empty Username Field", Toast.LENGTH_SHORT).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
                else if(!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)){
                    Toast.makeText(FacultyLogin.this, "Empty Password Field", Toast.LENGTH_SHORT).show();
                    back.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    login.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)){
                    Toast.makeText(FacultyLogin.this, "Empty Credentials...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FacultyLogin.this, "Login Successful!!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FacultyLogin.this, FacultyLoggedIn.class));
                        finish();
                    }
                });
        auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FacultyLogin.this, "Wrong Credentials...", Toast.LENGTH_LONG).show();
                        back.setEnabled(true);
                        back.setVisibility(View.VISIBLE);
                        login.setEnabled(true);
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}