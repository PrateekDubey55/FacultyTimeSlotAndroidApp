package com.example.facultytimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoggedIn extends AppCompatActivity {

    private ImageButton backbtn, facultybtn, studentbtn, adminbtn;
    private ProgressBar progressBar;
    private Button logout;
    private TextView deleteaccount;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logged_in);

        backbtn = findViewById(R.id.adminbackloggedin);
        facultybtn = findViewById(R.id.newfaculty);
        studentbtn = findViewById(R.id.newstudent);
        adminbtn = findViewById(R.id.newadmin);
        progressBar = findViewById(R.id.adminloggedinprogress);
        logout = findViewById(R.id.adminlogout);
        deleteaccount = findViewById(R.id.deleteadmin);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminLoggedIn.this);
                dialog.setTitle("Do you wish to logout?");
                dialog.setMessage("By logging out all your previous work will be autosaved");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backbtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(AdminLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminLoggedIn.this, AdminLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
        facultybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backbtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                logout.setEnabled(false);
                startActivity(new Intent(AdminLoggedIn.this, NewFaculty.class));
            }
        });
        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backbtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                logout.setEnabled(false);
                startActivity(new Intent(AdminLoggedIn.this, NewStudent.class));
            }
        });
        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backbtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                logout.setEnabled(false);
                startActivity(new Intent(AdminLoggedIn.this, NewAdmin.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminLoggedIn.this);
                dialog.setTitle("Do you wish to logout?");
                dialog.setMessage("By logging out all your previous work will be autosaved");
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backbtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(AdminLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminLoggedIn.this, AdminLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminLoggedIn.this);
                dialog.setTitle("Do you wish to delete your account?");
                dialog.setMessage("By deleting your account, you will loose all your access to this application. " +
                        "\nThis is an irreversible action. \nAre you sure?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backbtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AdminLoggedIn.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminLoggedIn.this, AdminLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(AdminLoggedIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    backbtn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
}