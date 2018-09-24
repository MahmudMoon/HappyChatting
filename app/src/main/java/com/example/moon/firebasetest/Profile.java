package com.example.moon.firebasetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {


    ImageView profile_iv;
    TextView user,pass,email_id;
    String imageLink;
    Button Update;
   // SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile_iv = (ImageView)findViewById(R.id.imagePro);
        user = (EditText)findViewById(R.id.username_);
        pass = (EditText)findViewById(R.id.password_);
        email_id = (EditText)findViewById(R.id.email_);
        Update = (Button)findViewById(R.id.UpdateProfile_);


        Intent intent = getIntent();
        String User = intent.getStringExtra("User");



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles").child(User);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObjectForUser objectForUser = dataSnapshot.getValue(ObjectForUser.class);
                String userName = objectForUser.getUserName_();
                String Email = objectForUser.getEmail_();
                String Password = "DEMO PASS";

                imageLink = objectForUser.getDownloadUrl();
                Picasso.get().load(imageLink).into(profile_iv);
                user.setText(userName);
                pass.setText(Password);
                email_id.setText(Email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout){
//            sharedPreferences = this.getSharedPreferences("LOGOPTION",MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isLogin",false);
//            editor.apply();
            Intent intent = new Intent(Profile.this,LoginActivity.class);
            startActivity(intent);

            return true;
        }
       return false;
    }
}
