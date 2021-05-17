package com.example.facultytimeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private Button facultylogin,studentlogin,adminlogin;
    private ProgressBar facultypb, studentpb, adminpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facultylogin = findViewById(R.id.facultyloginbtnmain);
        studentlogin = findViewById(R.id.studentloginbtnmain);
        adminlogin = findViewById(R.id.adminloginbtnmain);
        facultypb = findViewById(R.id.pbfaculty);
        studentpb = findViewById(R.id.pbstudent);
        adminpb = findViewById(R.id.pbadmin);

        facultylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facultypb.setVisibility(View.VISIBLE);
                facultylogin.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, FacultyLogin.class));
                finish();
            }
        });

        studentlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentpb.setVisibility(View.VISIBLE);
                studentlogin.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, StudentLogin.class));
                finish();
            }
        });

        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminpb.setVisibility(View.VISIBLE);
                adminlogin.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, AdminLogin.class));
                finish();
            }
        });

    }
}