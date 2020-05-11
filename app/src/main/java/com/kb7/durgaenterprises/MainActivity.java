package com.kb7.durgaenterprises;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kb7.durgaenterprises.Model.Users;
import com.kb7.durgaenterprises.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
private Button joinnow,login;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinnow=(Button)findViewById(R.id.button_join);
       login=(Button)findViewById(R.id.button_login);
       loadingbar=new ProgressDialog(this);
        Paper.init(this);
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);


           }
       });
       joinnow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
               startActivity(intent);

           }
       });

       String UserphnKey=Paper.book().read(Prevalent.phnKey);
        String UserpasswordKey=Paper.book().read(Prevalent.passwordKey);
        if (UserphnKey!=""&&UserpasswordKey!="")
        {
            if(!TextUtils.isEmpty(UserphnKey)&&!TextUtils.isEmpty(UserpasswordKey))
            {
                AllowAccessToAcc(UserphnKey,UserpasswordKey);
                loadingbar.setTitle("Already Logged In");
                loadingbar.setMessage("Please Wait....!");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }
        }


    }

    private void AllowAccessToAcc(final String phn, final String pas) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phn).exists())
                {

                    Users userdata=dataSnapshot.child("Users").child(phn).getValue(Users.class);
                    if(userdata.getPhone().equals(phn))
                    {
                        if(userdata.getPassword().equals(pas))
                        {
                            Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with this "+phn+"Does not exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }}

