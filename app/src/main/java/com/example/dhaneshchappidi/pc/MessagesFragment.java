package com.example.dhaneshchappidi.pc;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterUsersChat adapterUsers;
    List<Modelchat> modelchats;
    FirebaseAuth firebaseAuth;

    public MessagesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_messages, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.usersrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        modelchats = new ArrayList<>();
        getAllUsers();
        return view;


    }

    private void getAllUsers() {
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelchats.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Modelchat modelchat=ds.getValue(Modelchat.class);
                    if(!modelchat.getUid().equals(fuser.getUid())){
                        modelchats.add(modelchat);
                    }
                    adapterUsers=new AdapterUsersChat(getActivity(),modelchats);
                    recyclerView.setAdapter(adapterUsers);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
