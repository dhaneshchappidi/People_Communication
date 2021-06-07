package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;
import java.util.ArrayList;
import java.util.List;

public class Admin_home extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    SNavigationDrawer sNavigationDrawer;
    int color1=0;
    Class fragmentClass;
    ImageView im;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        firebaseAuth=FirebaseAuth.getInstance();
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("News Feed",R.drawable.music_bg));
        menuItems.add(new MenuItem("Notice Board",R.drawable.music_bg));
        menuItems.add(new MenuItem("Branch Notices",R.drawable.navigation_2));
        menuItems.add(new MenuItem("Send Notice",R.drawable.navigation_2));
        menuItems.add(new MenuItem("Send branch Notice",R.drawable.navigation_2));
        menuItems.add(new MenuItem("Messages",R.drawable.navigation_2));
        menuItems.add(new MenuItem("Registered students",R.drawable.navigation_2));
        menuItems.add(new MenuItem("My Profile",R.drawable.navigation_2));
        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass =  NewsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }


        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{
                        color1 = R.color.red;
                        fragmentClass = NewsFragment.class;
                        break;
                    }
                    case 1:{
                        color1 = R.color.orange;
                        fragmentClass = FeedFragment.class;
                        break;
                    }
                    case 2:{
                        color1 = R.color.green;
                        fragmentClass = Branch_Notices.class;
                        break;
                    }
                    case 3:{
                        color1 = R.color.green;
                        fragmentClass = Add_Notice.class;
                        break;
                    }
                    case 4:{
                        color1 = R.color.blue;
                        fragmentClass = send_branch_notice.class;
                        break;
                    }
                    case 5:{
                        color1 = R.color.blue;
                        fragmentClass = Received_Msg.class;
                        break;
                    }
                    case 6:{
                        color1 = R.color.blue;
                        fragmentClass = Registered_students.class;
                        break;
                    }
                    case 7:{
                        color1 = R.color.blue;
                        fragmentClass = My_profile.class;
                        break;
                    }

                }
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });

    }
    private void checkuserstatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            startActivity(new Intent(Admin_home.this,MainActivity.class));
            finish();
        }
    }

}

