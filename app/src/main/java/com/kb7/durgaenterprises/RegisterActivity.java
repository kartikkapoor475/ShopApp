package com.kb7.durgaenterprises;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

   private Button createAcc;
   private EditText Inputname,Inputphno,Inputpass;
   private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAcc=(Button)findViewById(R.id.register_btn);
        Inputname=(EditText) findViewById(R.id.name_register);
        Inputphno=(EditText)findViewById(R.id.phoneNo_register);
        Inputpass=(EditText)findViewById(R.id.register_pass);
        loadingbar=new ProgressDialog(this);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name=Inputname.getText().toString();
        String phno=Inputphno.getText().toString();
        String pass= Inputpass.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name..!", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phno))
        {
            Toast.makeText(this, "Please enter your number..!", Toast.LENGTH_SHORT).show();
        }
     else   if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please enter your password..!", Toast.LENGTH_SHORT).show();
        }

     else
        {
    loadingbar.setTitle("Create Account");
    loadingbar.setMessage("Please Wait..while we are checking your credentials!");
    loadingbar.setCanceledOnTouchOutside(false);
    loadingbar.show();
    ValidatePhno(name,phno,pass);
        }
    }

    private void ValidatePhno(final String name, final String phno, final String pass) {

            final DatabaseReference RootRef;
            RootRef= FirebaseDatabase.getInstance().getReference();
            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!(dataSnapshot.child("Users").child(phno).exists()))
                    {
                        HashMap<String,Object> userhashmap=new HashMap<>();
                        userhashmap.put("phone",phno);
                        userhashmap.put("password",pass);
                       userhashmap.put("name",name);
                       RootRef.child("Users").child(phno).updateChildren(userhashmap)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful())
                                       {
                                           Toast.makeText(RegisterActivity.this, "Your Account Has Been Created", Toast.LENGTH_SHORT).show();
                                           loadingbar.dismiss();
                                           Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                           startActivity(intent);

                                       }
                                       else
                                       {
                                           Toast.makeText(RegisterActivity.this, "Network Error!!Please Try Again", Toast.LENGTH_SHORT).show();
                                           loadingbar.dismiss();
                                       }
                                   }
                               });
                    }


                    else
                    {
                        Toast.makeText(RegisterActivity.this, "This"+phno+"already exists", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try again using different phone number", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
