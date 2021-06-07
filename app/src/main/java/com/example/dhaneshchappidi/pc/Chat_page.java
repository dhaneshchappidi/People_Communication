package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chat_page extends AppCompatActivity {
    Intent intent;
    String uid,uname,idno,imageurl;
    String myuid;
    ImageView imageView;
    TextView name;
    EditText editText;
    ImageButton submit;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;

    List<ModelMessage> messageList;
    AdapterChat adapterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();            }
        });

        intent=getIntent();
        uid=intent.getStringExtra("Hisuid");
        uname=intent.getStringExtra("uname");
        idno=intent.getStringExtra("Idno");
        imageurl=intent.getStringExtra("image2");
        imageView=(ImageView)findViewById(R.id.profileIv);
        name=(TextView)findViewById(R.id.nameTv);
        editText=(EditText)findViewById(R.id.chat);
        recyclerView=findViewById(R.id.messagerecyclerview);
        submit=(ImageButton)findViewById(R.id.submit_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myuid=user.getUid();
        name.setText(uname);
        messageList =new ArrayList<>();
        readMessages(user.getUid(),uid,imageurl);
        Picasso
                .with(imageView.getContext())
                .load(imageurl)
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(imageView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editText.getText().toString().trim();
                if(!message.equals("")) {
                    send_msg(uid, message, myuid);
                }
                else {
                    Toast.makeText(Chat_page.this,"Message cannot empty",Toast.LENGTH_SHORT).show();
                }
                editText.setText(null);
            }
        });
    }

    private void readMessages(final String myuid,final String uid,final String imageurl) {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Chats");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageList.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelMessage modelMessage=ds.getValue(ModelMessage.class);
                        if(modelMessage.getReceiver().equals(myuid)&&modelMessage.getSender().equals(uid) ||
                                modelMessage.getReceiver().equals(uid)&&modelMessage.getSender().equals(myuid)
                        ){
                            messageList.add(modelMessage);
                        }
                        adapterMessage=new AdapterChat(Chat_page.this,messageList);
                        recyclerView.setAdapter(adapterMessage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Chat_page.this,"" +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void send_msg(String uid, String message, String myuid) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myuid);
        hashMap.put("receiver",uid);
        hashMap.put("message",message);
        databaseReference.child("Chats").push().setValue(hashMap);

    }
}
