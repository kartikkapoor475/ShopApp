package com.kb7.durgaenterprises;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.kb7.durgaenterprises.Model.Users;
import com.kb7.durgaenterprises.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
private EditText phno,pass;
private Button loginbtn;
private ProgressDialog loadingbar;
private String parentDb="Users";
private CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn=(Button)findViewById(R.id.login_btn);
       phno=(EditText) findViewById(R.id.phoneNo_login);
        pass=(EditText)findViewById(R.id.login_pass);
        loadingbar= new ProgressDialog(this);
        checkbox=(CheckBox)findViewById(R.id.remember_me);
        Paper.init(this);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    private void loginUser() {

        String phn=phno.getText().toString();
        String pas= pass.getText().toString();
         if(TextUtils.isEmpty(phn))
        {
            Toast.makeText(this, "Please enter your number..!", Toast.LENGTH_SHORT).show();
        }
        else   if(TextUtils.isEmpty(pas))
        {
            Toast.makeText(this, "Please enter your password..!", Toast.LENGTH_SHORT).show();
        }
        else {
             loadingbar.setTitle("Login");
             loadingbar.setMessage("Please Wait..while we are checking your credentials!");
             loadingbar.setCanceledOnTouchOutside(false);
             loadingbar.show();
             AllowAccess(phn,pas);
         }

    }

    private void AllowAccess(final String phn, final String pas) {
        if(checkbox.isChecked())
        {
            Paper.book().write(Prevalent.phnKey,phn);
            Paper.book().write(Prevalent.passwordKey,pas);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDb).child(phn).exists())
                {

                        Users userdata=dataSnapshot.child(parentDb).child(phn).getValue(Users.class);
                        if(userdata.getPhone().equals(phn))
                        {
                            if(userdata.getPassword().equals(pas))
                            {
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loadingbar.dismiss();
                                Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                            }
                        }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this "+phn+"Does not exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
