package com.example.dhaneshchappidi.pc;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Branch_Notices extends Fragment {
    TextView tv;
    Typeface tf;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    List<ModelBranch> branchList;
    AdapterBranch adapterBranch;
    String brach_data;
    public Branch_Notices() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.branch_notices, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        tv=view.findViewById(R.id.branch_notice);
        tf=Typeface.createFromAsset(getActivity().getAssets(), "fonts/Skate_Brand.otf");
        tv.setTypeface(tf);
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    brach_data=""+ds.child("branch").getValue();
                    if(brach_data.equals("No_brach")){
                        tv.setText("Select branch in my profile page");
                    }
                    else{
                        tv.setText(brach_data+" Notice Board");
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView=view.findViewById(R.id.branchnoticerecyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        branchList=new ArrayList<>();
        loadNotices();

        return view;
    }

    private void loadNotices() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Branch_Notices");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                branchList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelBranch modelBranch=ds.getValue(ModelBranch.class);
                    if(modelBranch.getBranch().equals(brach_data)) {
                        branchList.add(modelBranch);
                    }

                    adapterBranch=new AdapterBranch(getActivity(),branchList);
                    recyclerView.setAdapter(adapterBranch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"" +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
