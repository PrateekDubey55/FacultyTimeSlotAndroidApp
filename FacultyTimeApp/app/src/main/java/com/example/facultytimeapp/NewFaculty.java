package com.example.facultytimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewFaculty extends AppCompatActivity {

    private EditText email,password,confirm,name;
    private ProgressBar progressBar;
    private CheckBox pwdcb, confirmcb;
    private Button register;
    private ImageButton back,info;

    FirebaseAuth auth;

    String regex = "^(?=.*[0-9])"
            + "(?=.*[a-z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{6,20}$";
    Pattern p = Pattern.compile(regex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_faculty);

        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.newfacultyname);
        email = findViewById(R.id.newfacultyemail);
        password = findViewById(R.id.newfacultypassword);
        confirm = findViewById(R.id.newfacultypasswordconfirm);
        progressBar = findViewById(R.id.newfacultyprogressbar);
        pwdcb = findViewById(R.id.passwordcheckboxnewfaculty);
        confirmcb = findViewById(R.id.passwordcheckboxnewfacultyconfirm);
        register = findViewById(R.id.registerbtnfaculty);
        back = findViewById(R.id.newfacultyback);
        info = findViewById(R.id.infofaculty);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString().toUpperCase().trim();
                String txt_email = email.getText().toString().toLowerCase().trim();
                String txt_password = password.getText().toString().trim();
                String txt_confirm = confirm.getText().toString().trim();
                Matcher m = p.matcher(txt_password);
                if(!m.matches() && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)){
                    Toast.makeText(NewFaculty.this, "Weak Password", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_email.contains("admin") && !txt_email.contains("student") && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)
                        && m.matches() && txt_password.equals(txt_confirm) && txt_email.contains("suas.ac.in")){
                    progressBar.setVisibility(View.VISIBLE);
                    register.setEnabled(false);
                    back.setVisibility(View.GONE);
                    name.getText().clear();
                    email.getText().clear();
                    password.getText().clear();
                    confirm.getText().clear();
                    registerUser(txt_email,txt_password,txt_name);
                }
                else if(!txt_email.contains("admin") && !txt_email.contains("student") && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)
                        && m.matches() && !txt_password.equals(txt_confirm)){
                    Toast.makeText(NewFaculty.this, "Confirm Password not matches Password field", Toast.LENGTH_LONG).show();
                }
                else if((txt_email.contains("admin") || txt_email.contains("student") || !txt_email.contains("suas.ac.in")) && !TextUtils.isEmpty(txt_email)){
                    Toast.makeText(NewFaculty.this, "Username must be of type \"facultyname@branch.suas.ac.in\"", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm)){
                    Toast.makeText(NewFaculty.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if((TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))
                        || (TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))
                        || (TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))){
                    Toast.makeText(NewFaculty.this, "Empty Username Field", Toast.LENGTH_SHORT).show();
                }
                else if((!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))
                        || (!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))
                        || (!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))){
                    Toast.makeText(NewFaculty.this, "Empty Password Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(NewFaculty.this, AdminLoggedIn.class));
                finish();
            }
        });
        pwdcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        confirmcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    confirm.setTransformationMethod(null);
                }
                else{
                    confirm.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NewFaculty.this);
                dialog.setTitle("General Instructions");
                dialog.setMessage("1. Password must be atleast 6 characters long." +
                        "\n2. There must be atleast 1 special character among @#$%^&+=" +
                        "\n3. The password must be alphanumeric." +
                        "\n4. Faculty email must be of type facultyname@suas.ac.in");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    private void registerUser(String email, String password, String name) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewFaculty.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewFaculty.this, "New Admin Added Successfully", Toast.LENGTH_SHORT).show();
                            insertData(name);
                        }
                        else{
                            Toast.makeText(NewFaculty.this, "Registration Failed, Please try again later", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            register.setEnabled(true);
                            back.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void insertData(String name) {
        HashMap<String, String> slotmap = new HashMap<>();
        slotmap.put("Slot1", "Available");
        slotmap.put("Slot2", "Available");
        slotmap.put("Slot3", "Available");
        slotmap.put("Slot4", "Available");
        slotmap.put("Slot5", "Available");
        slotmap.put("Slot6", "Available");

        HashMap<String, Object> days = new HashMap<>();
        days.put("MONDAY", slotmap);
        days.put("TUESDAY", slotmap);
        days.put("WEDNESDAY", slotmap);
        days.put("THURSDAY", slotmap);
        days.put("FRIDAY", slotmap);
        days.put("SATURDAY", slotmap);

        FirebaseDatabase.getInstance().getReference()
                .child(name)
                .updateChildren(days)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NewFaculty.this, "Initial Data Saved", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        register.setEnabled(true);
                        back.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewFaculty.this, "Initial Data Not Saved...", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        register.setEnabled(true);
                        back.setVisibility(View.VISIBLE);
                    }
                });
    }
}