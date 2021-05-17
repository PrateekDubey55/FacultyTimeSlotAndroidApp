package com.example.facultytimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentLoggedIn extends AppCompatActivity {

    private ImageButton backbtn;
    private Button logout,search;
    private EditText facultyname;
    private TextView deleteaccount;
    private Spinner days;
    private ProgressBar progressBar;
    String dayselected,name;

    FirebaseAuth auth;
    FirebaseUser user;

    String daysArray[] = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_logged_in);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        backbtn = findViewById(R.id.studentbackloggedin);
        progressBar = findViewById(R.id.studentloggedinprogress);
        logout = findViewById(R.id.studentlogout);
        search = findViewById(R.id.searchstudent);
        facultyname = findViewById(R.id.facultynamestudent);
        days = findViewById(R.id.dayspinnerstudent);
        deleteaccount = findViewById(R.id.deletestudent);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daysArray);
        days.setAdapter(adapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentLoggedIn.this);
                dialog.setTitle("Do you wish to logout?");
                dialog.setMessage("By logging out all your previous work will be autosaved");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backbtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        logout.setEnabled(false);
                        search.setEnabled(false);
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(StudentLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StudentLoggedIn.this, StudentLogin.class);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentLoggedIn.this);
                dialog.setTitle("Do you wish to logout?");
                dialog.setMessage("By logging out all your previous work will be autosaved");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backbtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        logout.setEnabled(false);
                        search.setEnabled(false);
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(StudentLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StudentLoggedIn.this, StudentLogin.class);
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
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayselected = daysArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentLoggedIn.this);
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
                                    Toast.makeText(StudentLoggedIn.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StudentLoggedIn.this, StudentLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(StudentLoggedIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = facultyname.getText().toString().trim().toUpperCase();
                backbtn.setVisibility(View.GONE);
                logout.setEnabled(false);
                search.setEnabled(false);
                days.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(facultyname.getText().toString())){
                    ArrayList<String> list = new ArrayList<String>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(name).child(dayselected);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            list.add(snapshot.child("Slot1").getValue().toString());
                            list.add(snapshot.child("Slot2").getValue().toString());
                            list.add(snapshot.child("Slot3").getValue().toString());
                            list.add(snapshot.child("Slot4").getValue().toString());
                            list.add(snapshot.child("Slot5").getValue().toString());
                            list.add(snapshot.child("Slot6").getValue().toString());
                            AlertDialog.Builder dialog = new AlertDialog.Builder(StudentLoggedIn.this);
                            dialog.setTitle("Time Slots");
                            dialog.setMessage("Slot 1 : "+list.get(0) + "\nSlot 2 : "+list.get(1)
                                    + "\nSlot 3 : "+list.get(2) + "\nSlot 4 : "+list.get(3)
                                    + "\nSlot 5 : "+list.get(4) + "\nSlot 6 : "+list.get(5));
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    backbtn.setVisibility(View.VISIBLE);
                                    logout.setEnabled(true);
                                    search.setEnabled(true);
                                    days.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    backbtn.setVisibility(View.VISIBLE);
                    logout.setEnabled(true);
                    search.setEnabled(true);
                    days.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(StudentLoggedIn.this, "Empty Faculty Name Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}