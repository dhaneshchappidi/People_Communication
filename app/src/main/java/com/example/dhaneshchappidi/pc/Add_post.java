package com.example.dhaneshchappidi.pc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Add_post extends AppCompatActivity {
    ActionBar actionBar;
    //permission constraints
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String[] cameraPermissions;
    String[] storagePermissions;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference userDbRef;
    EditText titleEt,description;
    ImageView image;
    Button upload;
    Uri image_uri=null;
    String name,email,uid,dp;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        uid=user.getUid();
        userDbRef= FirebaseDatabase.getInstance().getReference("Users");
        Query query=userDbRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        name=""+ds.child("name").getValue();
                        email=""+ds.child("email").getValue();
                        dp=""+ds.child("image").getValue();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        actionBar=getSupportActionBar();
        actionBar.setTitle("Add New Post");
        actionBar.setSubtitle(user.getEmail());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        titleEt=findViewById(R.id.ptitle);
        description=findViewById(R.id.pdescription);
        image=findViewById(R.id.pimagetv);
        upload=findViewById(R.id.puploadbtn);
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=titleEt.getText().toString().trim();
                String pdescription=description.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(Add_post.this,"Enter title",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pdescription)){
                    Toast.makeText(Add_post.this,"Enter Description",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(image_uri==null){
                        uploadData(title,pdescription,"noImage");
                }
                else{
                        uploadData(title,pdescription,String.valueOf(image_uri));
                }
            }
        });
    }

    private void uploadData(final String title, final String pdescription, String uri) {
          pd.setMessage("Waiting for publishing...");
          pd.show();
          final String timeStamp=String.valueOf(System.currentTimeMillis());
          String filePathAndName="Posts/"+"post_"+timeStamp;
          if(!uri.equals("noImage")){
              StorageReference ref= FirebaseStorage.getInstance().getReference().child(filePathAndName);
              ref.putFile(Uri.parse(uri))
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                 Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                                 while (!uriTask.isSuccessful());

                                 String downloadUri=uriTask.getResult().toString();
                                 if(uriTask.isSuccessful()){
                                     HashMap<Object,String> hashMap=new HashMap<>();
                                     hashMap.put("uid",uid);
                                     hashMap.put("uName",name);
                                     hashMap.put("uEmail",email);
                                     hashMap.put("uDp",dp);
                                     hashMap.put("pId",timeStamp);
                                     hashMap.put("pTitle",title);
                                     hashMap.put("pDescr",pdescription);
                                     hashMap.put("pImage",downloadUri);
                                     hashMap.put("pTime",timeStamp);

                                     DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                                     ref.child(timeStamp).setValue(hashMap)
                                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     pd.dismiss();
                                                     Toast.makeText(Add_post.this,"Post Published",Toast.LENGTH_SHORT).show();
                                                     titleEt.setText("");
                                                     description.setText("");
                                                     image.setImageURI(null);
                                                     image_uri=null;
                                                 }
                                             })
                                             .addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                    pd.dismiss();
                                                    Toast.makeText(Add_post.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                 }
                                             });

                                 }
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(Add_post.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                          }
                      });
          }
          else{
              HashMap<Object,String> hashMap=new HashMap<>();
              hashMap.put("uid",uid);
              hashMap.put("uName",name);
              hashMap.put("uEmail",email);
              hashMap.put("uDp",dp);
              hashMap.put("pId",timeStamp);
              hashMap.put("pTitle",title);
              hashMap.put("pDescr",pdescription);
              hashMap.put("pImage","noImage");
              hashMap.put("pTime",timeStamp);

              DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
              ref.child(timeStamp).setValue(hashMap)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              pd.dismiss();
                              Toast.makeText(Add_post.this,"Post Published",Toast.LENGTH_SHORT).show();
                              titleEt.setText("");
                              description.setText("");
                              image.setImageURI(null);
                              image_uri=null;
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              pd.dismiss();
                              Toast.makeText(Add_post.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                          }
                      });
          }
    }

    private void showImagePickDialog() {
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if(which==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera(){

        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        android.view.MenuItem item_disable=menu.findItem(R.id.action_add_post);
        if(item_disable!=null){
            item_disable.setVisible(false);
        }
        android.view.MenuItem item_disable2=menu.findItem(R.id.action_search);
        if(item_disable2!=null){
            item_disable2.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkuserstatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            startActivity(new Intent(Add_post.this,MainActivity.class));
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this,"Camera and Storage persmissions are neccessary",Toast.LENGTH_SHORT);
                    }
                }
                else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                    if(grantResults.length>0){
                        boolean storageAccepted=grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if(storageAccepted){
                            pickFromGallery();
                        }
                        else {
                            Toast.makeText(this,"Storage persmissions are neccessary",Toast.LENGTH_SHORT);
                        }
                    }
                    else {

                    }
                    }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                image_uri=data.getData();
                image.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                image.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
