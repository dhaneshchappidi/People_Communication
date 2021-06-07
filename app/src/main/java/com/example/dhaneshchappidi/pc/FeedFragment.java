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

public class FeedFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelNotice> noticeList;
    AdapterNotices adapterNotices;

    public FeedFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feed, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.noticerecyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        noticeList=new ArrayList<>();
        loadNotices();
        return view;
    }

    private void loadNotices() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelNotice modelNotice=ds.getValue(ModelNotice.class);
                    noticeList.add(modelNotice);

                    adapterNotices=new AdapterNotices(getActivity(),noticeList);
                    recyclerView.setAdapter(adapterNotices);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"" +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
        android.view.MenuItem item_2=menu.findItem(R.id.action_add_post);
        MenuItem item2=menu.findItem(R.id.action_updatepassword).setVisible(false);
        if(item_2!=null){
            item_2.setVisible(false);
        }
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    SearchNotices(s);
                }
                else {
                    loadNotices();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    SearchNotices(s);
                }
                else {
                    loadNotices();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void SearchNotices(final String s) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelNotice modelNotice=ds.getValue(ModelNotice.class);
                    if(modelNotice.getNtitle().toLowerCase().contains(s.toLowerCase())|| modelNotice.getNdesc().contains(s.toLowerCase())) {
                        noticeList.add(modelNotice);
                    }
                    adapterNotices=new AdapterNotices(getActivity(),noticeList);
                    adapterNotices.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterNotices);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"" +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
