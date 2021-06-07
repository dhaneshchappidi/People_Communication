package com.example.dhaneshchappidi.pc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Add_Notice extends Fragment {

    EditText N_title,N_desc;
    Spinner N_year,N_branch;
    Button button;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference userdbref;
    ProgressDialog progressDialog;
    String name,email,uid,description,year,branch;

    public Add_Notice() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_add__notice, container, false);


        N_title=view.findViewById(R.id.editText2);
        N_desc=(EditText)view.findViewById(R.id.edit_text);
        N_year=view.findViewById(R.id.spinner);
        N_branch=view.findViewById(R.id.spinner1);
        progressDialog =new ProgressDialog(getActivity());

        button=view.findViewById(R.id.exit);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        uid=user.getUid();
        userdbref=FirebaseDatabase.getInstance().getReference("Users");
        Query query=userdbref.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    name=""+ds.child("name").getValue();
                    email=""+ds.child("email").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Notice publishing");
                progressDialog.show();
                String timestamp=String.valueOf(System.currentTimeMillis());
                String title=N_title.getText().toString();
                description=N_desc.getText().toString();
                year=N_year.getSelectedItem().toString();
                branch=N_branch.getSelectedItem().toString();

                HashMap<Object, String> hashMap =new HashMap<>();
                hashMap.put("uid",uid);
                hashMap.put("uname",name);
                hashMap.put("uemail",email);
                hashMap.put("nid",timestamp);
                hashMap.put("ntitle",title);
                hashMap.put("ndesc",description);
                hashMap.put("nyear",year);
                hashMap.put("nbranch",branch);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notice");
                reference.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(Add_Notice.this.getActivity(),"Notice published",Toast.LENGTH_SHORT).show();
                                N_title.setText("");
                                N_desc.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });

        return view;


    }
    private void checkuserstatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(user!=null){

        }
        else{
            Intent myIntent = new Intent(Add_Notice.this.getActivity(),MainActivity.class);
            startActivity(myIntent);

        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        android.view.MenuItem item=menu.findItem(R.id.action_search);
        MenuItem item1=menu.findItem(R.id.action_add_post).setVisible(false);
        MenuItem item2=menu.findItem(R.id.action_updatepassword).setVisible(false);
        if(item!=null){
            item.setVisible(false);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }

}
