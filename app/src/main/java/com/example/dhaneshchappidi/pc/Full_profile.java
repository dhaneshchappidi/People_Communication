package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;


public class Full_profile extends AppCompatActivity {
    ActionBar actionBar;
    Intent intent;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView name,mail,idno,phone,branch;
    ImageView imageView;
    String image_data;
    Animation zoomin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        final String uid=intent.getStringExtra("uid");
        String uname=intent.getStringExtra("uname");
        actionBar.setTitle("Profile");
        actionBar.setSubtitle(uname);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        name=(TextView)findViewById(R.id.name);
        mail=(TextView)findViewById(R.id.mail);
        idno=(TextView)findViewById(R.id.idno);
        phone=(TextView)findViewById(R.id.phone);
        imageView=(ImageView)findViewById(R.id.user_dp);
        zoomin= AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        branch=(TextView)findViewById(R.id.branch);
        Query query=databaseReference.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String name_data="" + ds.child("name").getValue();
                    String mail_data = "" + ds.child("email").getValue();
                    String idno_data = "" + ds.child("idno").getValue();
                    image_data= "" + ds.child("image").getValue();
                    String phone_data=""+ds.child("phone").getValue();
                    String brach_data=""+ds.child("branch").getValue();
                    branch.setText(brach_data);
                    name.setText(name_data);
                    mail.setText(mail_data);
                    idno.setText(idno_data);
                    phone.setText(phone_data);
                    Picasso
                            .with(imageView.getContext())
                            .load(image_data)
                            .placeholder(R.drawable.person)
                            .error(R.drawable.person)
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(image_data);
            }
        });
    }

    private void showImage(String image) {
        View view= LayoutInflater.from(this).inflate(R.layout.full_image,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        ImageView imageView=view.findViewById(R.id.image);
        builder.setView(view);
        Bitmap img;
        try {
            URL url = new URL(image);
            img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(img);
        }
        catch(IOException e) {
            System.out.println(e);
        }
        imageView.startAnimation(zoomin);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
