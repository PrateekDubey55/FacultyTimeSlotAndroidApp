package com.example.facultytimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;

import java.util.ArrayList;
import java.util.HashMap;

public class FacultyLoggedIn extends AppCompatActivity {

    private ImageButton backbtn;
    private Button logout,search,edit,editfinal;
    private EditText facultyname;
    private TextView deleteaccount;
    private Spinner days;
    private ProgressBar progressBar;
    private RadioGroup grp1,grp2,grp3,grp4,grp5,grp6;
    private CardView c1,c2,c3,c4,c5,c6;
    String dayselected="";

    FirebaseAuth auth;
    FirebaseUser user;

    String daysArray[] = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_logged_in);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        backbtn = findViewById(R.id.facultybackloggedin);
        progressBar = findViewById(R.id.facultyloggedinprogress);
        logout = findViewById(R.id.facultylogout);
        search = findViewById(R.id.searchfaculty);
        facultyname = findViewById(R.id.facultyname);
        days = findViewById(R.id.dayspinnerfaculty);
        deleteaccount = findViewById(R.id.deletefaculty);
        grp1 = findViewById(R.id.radiogrp1);
        grp2 = findViewById(R.id.radiogrp2);
        grp3 = findViewById(R.id.radiogrp3);
        grp4 = findViewById(R.id.radiogrp4);
        grp5 = findViewById(R.id.radiogrp5);
        grp6 = findViewById(R.id.radiogrp6);
        edit = findViewById(R.id.editdata);
        editfinal = findViewById(R.id.editfinal);
        c1 = findViewById(R.id.slot1faculty);
        c2 = findViewById(R.id.slot2faculty);
        c3 = findViewById(R.id.slot3faculty);
        c4 = findViewById(R.id.slot4faculty);
        c5 = findViewById(R.id.slot5faculty);
        c6 = findViewById(R.id.slot6faculty);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daysArray);
        days.setAdapter(adapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FacultyLoggedIn.this);
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
                        Toast.makeText(FacultyLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FacultyLoggedIn.this, FacultyLogin.class);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(FacultyLoggedIn.this);
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
                        Toast.makeText(FacultyLoggedIn.this, "Logged Out Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FacultyLoggedIn.this, FacultyLogin.class);
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
                dayselected = daysArray[position].trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(facultyname.getText().toString())){
                    backbtn.setVisibility(View.GONE);
                    logout.setEnabled(false);
                    search.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    days.setEnabled(false);
                    ArrayList<String> list = new ArrayList<String>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child(facultyname.getText().toString().toUpperCase().trim()).
                            child(dayselected);
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(FacultyLoggedIn.this);
                            dialog.setTitle("Current Time Slots");
                            dialog.setMessage("Slot 1 : "+list.get(0) + "\nSlot 2 : "+list.get(1)
                                    + "\nSlot 3 : "+list.get(2) + "\nSlot 4 : "+list.get(3)
                                    + "\nSlot 5 : "+list.get(4) + "\nSlot 6 : "+list.get(5));
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            backbtn.setVisibility(View.VISIBLE);
                            logout.setEnabled(true);
                            search.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            days.setEnabled(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    backbtn.setVisibility(View.VISIBLE);
                    logout.setEnabled(true);
                    search.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    days.setEnabled(true);
                    Toast.makeText(FacultyLoggedIn.this, "Faculty name field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FacultyLoggedIn.this);
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
                                    Toast.makeText(FacultyLoggedIn.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(FacultyLoggedIn.this, AdminLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(FacultyLoggedIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(facultyname.getText().toString())){
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c5.setVisibility(View.VISIBLE);
                    c6.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    editfinal.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(FacultyLoggedIn.this, "Empty Faculty Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backbtn.setVisibility(View.GONE);
                logout.setEnabled(false);
                search.setEnabled(false);
                editfinal.setEnabled(false);
                deleteaccount.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                int checked1 = grp1.getCheckedRadioButtonId();
                int checked2 = grp2.getCheckedRadioButtonId();
                int checked3 = grp3.getCheckedRadioButtonId();
                int checked4 = grp4.getCheckedRadioButtonId();
                int checked5 = grp5.getCheckedRadioButtonId();
                int checked6 = grp6.getCheckedRadioButtonId();
                String data1 = getDataChecked1(checked1);
                String data2 = getDataChecked2(checked2);
                String data3 = getDataChecked3(checked3);
                String data4 = getDataChecked4(checked4);
                String data5 = getDataChecked5(checked5);
                String data6 = getDataChecked6(checked6);
                insertData(facultyname.getText().toString().trim().toUpperCase(),data1,data2,data3,data4,data5,data6,dayselected);
            }
        });

    }

    private String getDataChecked1(int checked) {
        switch (checked){
            case R.id.availableslot1:
                return "Available";
            case R.id.lectureslot1:
                return "Lecture";
            case R.id.labslot1:
                return "Lab";
            case R.id.meetingslot1:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private String getDataChecked2(int checked) {
        switch (checked){
            case R.id.availableslot2:
                return "Available";
            case R.id.lectureslot2:
                return "Lecture";
            case R.id.labslot2:
                return "Lab";
            case R.id.meetingslot2:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private String getDataChecked3(int checked) {
        switch (checked){
            case R.id.availableslot3:
                return "Available";
            case R.id.lectureslot3:
                return "Lecture";
            case R.id.labslot3:
                return "Lab";
            case R.id.meetingslot3:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private String getDataChecked4(int checked) {
        switch (checked){
            case R.id.availableslot4:
                return "Available";
            case R.id.lectureslot4:
                return "Lecture";
            case R.id.labslot4:
                return "Lab";
            case R.id.meetingslot4:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private String getDataChecked5(int checked) {
        switch (checked){
            case R.id.availableslot5:
                return "Available";
            case R.id.lectureslot5:
                return "Lecture";
            case R.id.labslot5:
                return "Lab";
            case R.id.meetingslot5:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private String getDataChecked6(int checked) {
        switch (checked){
            case R.id.availableslot6:
                return "Available";
            case R.id.lectureslot6:
                return "Lecture";
            case R.id.labslot6:
                return "Lab";
            case R.id.meetingslot6:
                return "Meeting";
            default:
                return "Null";
        }
    }

    private void insertData(String name, String slot1, String slot2, String slot3, String slot4, String slot5, String slot6, String dayselected) {
        HashMap<String, String> slotmap = new HashMap<>();
        slotmap.put("Slot1", slot1);
        slotmap.put("Slot2", slot2);
        slotmap.put("Slot3", slot3);
        slotmap.put("Slot4", slot4);
        slotmap.put("Slot5", slot5);
        slotmap.put("Slot6", slot6);

        HashMap<String, Object> days = new HashMap<>();
        days.put(dayselected, slotmap);

        FirebaseDatabase.getInstance().getReference()
                .child(name)
                .updateChildren(days)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(FacultyLoggedIn.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        backbtn.setVisibility(View.VISIBLE);
                        logout.setEnabled(true);
                        search.setEnabled(true);
                        editfinal.setEnabled(true);
                        deleteaccount.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FacultyLoggedIn.this, "Data Not Updated...", Toast.LENGTH_SHORT).show();
                        backbtn.setVisibility(View.VISIBLE);
                        logout.setEnabled(true);
                        search.setEnabled(true);
                        editfinal.setEnabled(true);
                        deleteaccount.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}