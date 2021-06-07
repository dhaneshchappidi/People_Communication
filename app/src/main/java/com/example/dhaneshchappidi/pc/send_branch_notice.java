package com.example.dhaneshchappidi.pc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class send_branch_notice extends Fragment {

    EditText N_title,N_desc;
    Spinner N_year;
    Button button;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference userdbref;
    ProgressDialog progressDialog;
    String name,email,uid,description,year,branch;

    public send_branch_notice(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.send_branch, container, false);
        N_title=view.findViewById(R.id.editText2);
        N_desc=(EditText)view.findViewById(R.id.edit_text);
        N_year=view.findViewById(R.id.spinner);
        progressDialog =new ProgressDialog(getActivity());

        button=view.findViewById(R.id.exit);
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        uid=user.getUid();
        userdbref= FirebaseDatabase.getInstance().getReference("Users");
        Query query=userdbref.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    name=""+ds.child("name").getValue();
                    email=""+ds.child("email").getValue();
                    branch=""+ds.child("branch").getValue();
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

                HashMap<Object, String> hashMap =new HashMap<>();
                hashMap.put("uid",uid);
                hashMap.put("uname",name);
                hashMap.put("uemail",email);
                hashMap.put("bid",timestamp);
                hashMap.put("btitle",title);
                hashMap.put("bdesc",description);
                hashMap.put("byear",year);
                hashMap.put("branch",branch);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Branch_Notices");
                reference.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(send_branch_notice.this.getActivity(),"Notice published",Toast.LENGTH_SHORT).show();
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

}
