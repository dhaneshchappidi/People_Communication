package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Registered_students extends Fragment {
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> userList;
    FirebaseAuth firebaseAuth;

    public Registered_students(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registered, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.usersrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();
        getAllUsers();
        return view;
    }

    private void getAllUsers() {
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelUser modelUser=ds.getValue(ModelUser.class);
                    if(modelUser.getType().equals("student")){
                        userList.add(modelUser);
                    }
                    adapterUsers=new AdapterUsers(getActivity(),userList);
                    recyclerView.setAdapter(adapterUsers);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        android.view.MenuItem item_disable=menu.findItem(R.id.action_add_post);
        if(item_disable!=null){
            item_disable.setVisible(false);
        }

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    SearchUsers(s);
                }
                else {
                    getAllUsers();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    SearchUsers(s);
                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void SearchUsers(final String s) {
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelUser modelUser=ds.getValue(ModelUser.class);
                    if(modelUser.getName().toLowerCase().contains(s.toLowerCase()) ||modelUser.getIdno().toLowerCase().contains(s.toLowerCase())){
                        userList.add(modelUser);
                    }

                    adapterUsers=new AdapterUsers(getActivity(),userList);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    private void checkuserstatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }
}
