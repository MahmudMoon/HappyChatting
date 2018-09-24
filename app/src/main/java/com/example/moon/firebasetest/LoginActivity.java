package com.example.moon.firebasetest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth firebaseAuth;
    TextView textView;
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        textView = (TextView)findViewById(R.id.registration);
      //  sharedPreferences = this.getSharedPreferences("LOGOPTION",MODE_PRIVATE);
      //  boolean isLogin = sharedPreferences.getBoolean("isLogin",false);
//
//        if(isLogin){
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//        }



        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailSignInButton.setVisibility(View.INVISIBLE);
                String email = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();
                     firebaseAuth.signInWithEmailAndPassword(email,pass)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
//                                 SharedPreferences.Editor editor = sharedPreferences.edit();
//                                 editor.putBoolean("isLogin",true);
//                                 editor.apply();

                                 String Valid_email = firebaseAuth.getCurrentUser().getEmail();
                                 Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                 intent.putExtra("user",Valid_email);
                                 startActivity(intent);

                             }else
                             {
                                 Toast.makeText(getApplicationContext(),"Not valid User",Toast.LENGTH_SHORT).show();
                                 mEmailSignInButton.setVisibility(View.VISIBLE);
                             }
                         }
                     });


            }
        });


        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Registration.class);
                startActivity(intent);
            }
        });

    }

}

