package com.example.moon.firebasetest;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import android.provider.DocumentsContract;

import id.zelory.compressor.Compressor;

public class Registration extends AppCompatActivity {

    Button Open_gallary,Upload;
    Uri Path_URi,final_PATH;
    ImageView imageView;


    EditText et_UserName;
    EditText et_Email;
    EditText et_password;
    EditText et_ConformPassword;

    private static int REQUEST_CODE = 1;
    StorageReference storageReference ;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    File compressor;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Open_gallary = (Button)findViewById(R.id.upload);
        Upload = (Button)findViewById(R.id.upload_image);
        et_UserName = (EditText)findViewById(R.id.et_file);
        et_Email = (EditText)findViewById(R.id.et_mail);
        et_password = (EditText)findViewById(R.id.et_password);
        et_ConformPassword = (EditText)findViewById(R.id.et_conformPass);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);


        imageView = (ImageView)findViewById(R.id.iv_uploaded_image);
        storageReference = FirebaseStorage.getInstance().getReference();


        Open_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                final String UserName = et_UserName.getText().toString().trim();
                final String Email = et_Email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String Conform_Pass = et_ConformPassword.getText().toString().trim();





                if(TextUtils.equals(password,Conform_Pass) && password.length()>=6) {

                    firebaseAuth = FirebaseAuth.getInstance();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Profiles").child(UserName);


                    firebaseAuth.createUserWithEmailAndPassword(Email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                              Toast.makeText(getApplicationContext(),"New User Authenticated",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Authentication", String.valueOf(e));
                            Toast.makeText(getApplicationContext(),"Failed to Autheticate User",Toast.LENGTH_SHORT).show();
                        }
                    });



                    StorageReference filePath = storageReference.child(et_UserName.getText().toString().trim() + "/" + "Images.jpg");

                    filePath.putFile(final_PATH).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            databaseReference.setValue(new ObjectForUser(Email,UserName,downloadUrl));
                            Intent intent = new Intent(Registration.this,LoginActivity.class);
                            startActivity(intent);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    et_password.setText("");
                    et_ConformPassword.setText("");
                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);

                    Toast.makeText(getApplicationContext(),"PassWord Confromation Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData()!=null){
            Path_URi = data.getData();

//           String path =  Path_URi.toString();
//           Log.i("PATHA",path);

            File file = new File(getRealPathFromURI_API19(Path_URi));
            String realPath = file.getAbsolutePath();
            Log.i("PATHB",realPath);

//
//            if(file.exists()){
//               Log.d("PATHC","EXISTS");
//            }else {
//                Log.d("PATHD","NOT EXISTS");
//            }
            try {
                compressor = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(40)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(file);
                final_PATH = Uri.fromFile(compressor);


            } catch (IOException e) {
                e.printStackTrace();
            }


//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            compressedImageFile.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
//            byte[] c_image = byteArrayOutputStream.toByteArray();
//            Log.i("SIZEOFTHEBYTEARRAY",Integer.toString(c_image.length));

            imageView.setImageURI(Path_URi);
        }
    }








    public String getRealPathFromURI_API19( Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }




}
