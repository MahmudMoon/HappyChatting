package com.example.moon.firebasetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    EditText et_msg;
    ImageView ib_send;
    String value;
    String Current_User="NOT";
    String url = "";
    ArrayList<ObjectForMessage> arrayList;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter ;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_msg = (EditText)findViewById(R.id.editText);
        ib_send = (ImageView) findViewById(R.id.imageView);
        recyclerView = (RecyclerView) findViewById(R.id.list1);
        arrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(arrayList);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
        final String user = intent.getStringExtra("user");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference= firebaseDatabase.getReference("Message");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                arrayList.clear();
                     for(DataSnapshot data:dataSnapshot.getChildren()){
                                ObjectForMessage objectForMessage =  data.getValue(ObjectForMessage.class);
                                arrayList.add(objectForMessage);

                     }

                     progressBar.setVisibility(View.INVISIBLE);

                     Log.d("Length",Integer.toString(arrayList.size()));

                recyclerView.setAdapter(messageAdapter);
                recyclerView.scrollToPosition(arrayList.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










        final DatabaseReference databaseReference1 = firebaseDatabase.getReference("Profiles");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                       ObjectForUser objectForUser = data.getValue(ObjectForUser.class);
                       if(objectForUser!=null) {
                           //Log.i("User", objectForUser.getUserName_());

                           if(user.equals(objectForUser.getEmail_())){
                               Current_User = objectForUser.getUserName_();
                               url = objectForUser.getDownloadUrl();
                               Log.i("Current",Current_User);
                               break;
                           }
                       }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



       Log.i("CurrentUser",Current_User);



        ////////////


        //databaseReference.setValue("HELLO");


        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               String new_mes =  et_msg.getText().toString();
//               value+="\n";
//               value+=Current_User+"--> "+new_mes;
//               databaseReference.setValue(value);
//               et_msg.setText("");

                String newMessage = et_msg.getText().toString().trim();
                et_msg.setText("");
                //arrayList.add(new ObjectForMessage(newMessage,Current_User,url));
                databaseReference.push().setValue(new ObjectForMessage(newMessage,Current_User,url));


            }
        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialogbuilder =  new AlertDialog.Builder(this);
        alertDialogbuilder.setMessage("END CHAT . . . . ");
        alertDialogbuilder.setPositiveButton("Continue . . . ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogbuilder.setNegativeButton("CLOSE CHAT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.show();

    }
}
