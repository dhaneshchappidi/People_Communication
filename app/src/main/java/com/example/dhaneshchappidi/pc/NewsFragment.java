package com.example.dhaneshchappidi.pc;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_news, container, false);
        firebaseAuth=FirebaseAuth.getInstance();

        recyclerView=view.findViewById(R.id.postsRecyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        postList=new ArrayList<>();
        loadPosts();

        return view;
    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postList.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelPost modelPost=ds.getValue(ModelPost.class);
                        postList.add(modelPost);
                        adapterPosts=new AdapterPosts(getActivity(), postList);
                        recyclerView.setAdapter(adapterPosts);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

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
        android.view.MenuItem item=menu.findItem(R.id.action_search);
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
        if(id==R.id.action_add_post){
            startActivity(new Intent(getActivity(),Add_post.class));
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
