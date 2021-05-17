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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAdmin extends AppCompatActivity {

    private EditText email,password,confirm;
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
        setContentView(R.layout.activity_new_admin);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.newadminemail);
        password = findViewById(R.id.newadminpassword);
        confirm = findViewById(R.id.newadminpasswordconfirm);
        progressBar = findViewById(R.id.newadminprogressbar);
        pwdcb = findViewById(R.id.passwordcheckboxnewadmin);
        confirmcb = findViewById(R.id.passwordcheckboxnewadminconfirm);
        register = findViewById(R.id.registerbtnadmin);
        back = findViewById(R.id.newadminback);
        info = findViewById(R.id.infoadmin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString().toLowerCase().trim();
                String txt_password = password.getText().toString().trim();
                String txt_confirm = confirm.getText().toString().trim();
                Matcher m = p.matcher(txt_password);
                if(!m.matches() && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)){
                    Toast.makeText(NewAdmin.this, "Weak Password", Toast.LENGTH_SHORT).show();
                }
                else if(txt_email.contains("admin") && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)
                        && m.matches() && txt_password.equals(txt_confirm)){
                    progressBar.setVisibility(View.VISIBLE);
                    register.setEnabled(false);
                    back.setVisibility(View.GONE);
                    email.getText().clear();
                    password.getText().clear();
                    confirm.getText().clear();
                    registerUser(txt_email,txt_password);
                }
                else if(txt_email.contains("admin") && !TextUtils.isEmpty(txt_email)
                        && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm)
                        && m.matches() && !txt_password.equals(txt_confirm)){
                    Toast.makeText(NewAdmin.this, "Confirm Password not matches Password field", Toast.LENGTH_LONG).show();
                }
                else if(!txt_email.contains("admin") && !TextUtils.isEmpty(txt_email)){
                    Toast.makeText(NewAdmin.this, "Username must be of type \"name@admin.suas.ac.in\"", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm)){
                    Toast.makeText(NewAdmin.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if((TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))
                            || (TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))
                            || (TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))){
                    Toast.makeText(NewAdmin.this, "Empty Username Field", Toast.LENGTH_SHORT).show();
                }
                else if((!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && !TextUtils.isEmpty(txt_confirm))
                        || (!TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))
                        || (!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password) && TextUtils.isEmpty(txt_confirm))){
                    Toast.makeText(NewAdmin.this, "Empty Password Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(NewAdmin.this, AdminLoggedIn.class));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(NewAdmin.this);
                dialog.setTitle("General Instructions");
                dialog.setMessage("1. Password must be atleast 6 characters long." +
                        "\n2. There must be atleast 1 special character among @#$%^&+=" +
                        "\n3. The password must be alphanumeric." +
                        "\n4. Admin email must be of type adminname@admin.suas.ac.in");
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

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewAdmin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewAdmin.this, "New Admin Added Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            register.setEnabled(true);
                            back.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(NewAdmin.this, "Registration Failed, Please try again later", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            register.setEnabled(true);
                            back.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}