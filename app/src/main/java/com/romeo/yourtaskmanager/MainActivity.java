package com.romeo.yourtaskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView register;
    EditText email;
    EditText password;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =(EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.password);
        btn_login =(Button) findViewById(R.id.btn_login);

        final ProgressDialog mdialog = new ProgressDialog(this);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                if(mEmail == "" || mPass ==""){
                    email.setError("Enter email address");
                    password.setError("Enter password");
                    return;
                }
                else {
                    mdialog.setMessage("Processing...");
                    mdialog.show();
                    mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"successfull",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                finish();
                                mdialog.dismiss();
                            }
                            else {

                                mdialog.dismiss();
                            }

                        }
                    });

                }
            }
        });
         TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, RegActivity.class);
                startActivity(i);
                finish();
            }

        });
    }
    //enter back button twice
    int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    long mBackPressed;
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }


}