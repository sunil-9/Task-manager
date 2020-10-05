package com.romeo.yourtaskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity extends AppCompatActivity {
    TextView login;
    EditText email;
    EditText password;
    EditText repassword;
    RadioButton terms;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
         login =(TextView) findViewById(R.id.login);
         email =(EditText) findViewById(R.id.email);
         password =(EditText) findViewById(R.id.password);
         repassword =(EditText) findViewById(R.id.repassword);
         terms =(RadioButton) findViewById(R.id.terms);
         signup =(Button) findViewById(R.id.btn_signup);
        final ProgressDialog mdialog = new ProgressDialog(this);

        signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String mEmail =email.getText().toString().trim();
                 String mPass =password.getText().toString().trim();
                 FirebaseAuth mAuth = FirebaseAuth.getInstance();
                 mdialog.setMessage("Processing...");
                 mdialog.show();
                 if(TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPass)) {
                     Toast.makeText(getApplicationContext(), "User or password cant be empty", Toast.LENGTH_SHORT);
                 }
                 else {
                     if (password.getText().toString().equals(repassword.getText().toString())) {

                         mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT);
                                 startActivity(new Intent(RegActivity.this,HomeActivity.class));
                                 finish();
                                 mdialog.dismiss();
                             }
                         });
                    } else {
                         Toast.makeText(getApplicationContext(), "password don't match", Toast.LENGTH_SHORT);
                         repassword.setError("password don't match");
                         return;
                     }
                 }
             }
         });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(RegActivity.this,MainActivity.class));
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
