package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class Full_branch extends AppCompatActivity {
    ActionBar actionBar;
    Intent intent;
    TextView n_title,n_desc,n_year,u_name,u_time;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_branch);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        final String nid=intent.getStringExtra("nid");
        String ntitle=intent.getStringExtra("ntitle");
        String nnotice=intent.getStringExtra("branch");
        actionBar.setTitle(nnotice + " Notice");
        actionBar.setSubtitle(ntitle);
        n_title=(TextView)findViewById(R.id.b_title);
        n_desc=(TextView)findViewById(R.id.b_desc);
        n_year=(TextView)findViewById(R.id.b_year);
        u_name=(TextView)findViewById(R.id.u_name);
        u_time=(TextView)findViewById(R.id.upload_time);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Branch_Notices");
        Query query=databaseReference.orderByChild("bid").equalTo(nid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String title="" + ds.child("btitle").getValue();
                    String desc="" + ds.child("bdesc").getValue();
                    String year="" + ds.child("byear").getValue();
                    String uname="" + ds.child("uname").getValue();
                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(nid));
                    String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
                    n_title.setText(title);
                    n_desc.setText(desc);
                    n_year.setText(year);
                    u_name.setText(uname);
                    u_time.setText(pTime);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
