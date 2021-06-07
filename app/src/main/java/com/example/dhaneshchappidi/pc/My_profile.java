package com.example.dhaneshchappidi.pc;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


public class My_profile extends Fragment {

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView dp;
    TextView name,mail,idno,phoneno,branch;
    ProgressDialog progressDialog;
    Animation animation,zoomin;
    String image_data;

    Button bt;


    public My_profile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.my_profile, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        dp=view.findViewById(R.id.user_dp);
        name=view.findViewById(R.id.name);
        mail=view.findViewById(R.id.mail);
        idno=view.findViewById(R.id.idno);
        branch=view.findViewById(R.id.branch);
        progressDialog=new ProgressDialog(getActivity());
        phoneno=view.findViewById(R.id.phone);
        animation= AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_fall_down);
        zoomin=AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_in);

        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name_data = "" + ds.child("name").getValue();
                    String mail_data = "" + ds.child("email").getValue();
                    String idno_data = "" + ds.child("idno").getValue();
                    image_data= "" + ds.child("image").getValue();
                    String phone_data=""+ds.child("phone").getValue();
                    String brach_data=""+ds.child("branch").getValue();
                    name.setText(name_data);
                    mail.setText(mail_data);
                    idno.setText(idno_data);
                    phoneno.setText(phone_data);
                    if(brach_data.equals("No_brach")){
                        branch.setText("Please select branch");
                    }
                    else{
                        branch.setText(brach_data);
                    }
                    Picasso
                            .with(dp.getContext())
                            .load(image_data)
                            .placeholder(R.drawable.person)
                            .fit()
                            .centerCrop()
                            .error(R.drawable.person)
                            .into(dp);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(image_data);
            }
        });
        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBranchDialog();
            }
        });
        bt=view.findViewById(R.id.logout);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkuserstatus();
            }
        });
        return view;
    }

    private void showImage(String image) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.full_image,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final ImageView imageView=view.findViewById(R.id.image);
        builder.setView(view);
        Bitmap img;
        try {
            URL url = new URL(image);
            img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(img);
        }
        catch(IOException e) {
            System.out.println(e);
        }
        imageView.startAnimation(zoomin);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void showBranchDialog() {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.branch_select,null);
        final Spinner spinner=view.findViewById(R.id.sel_branch);
        Button button=view.findViewById(R.id.submit);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Select branch");
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String branch=spinner.getSelectedItem().toString();
                HashMap<String,Object> result=new HashMap<>();
                result.put("branch",branch);
                databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(),"Branch Updated...",Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        });

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
        if(item!=null){
            item.setVisible(false);
        }
        menu.findItem(R.id.action_add_post).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.getInstance().signOut();
            checkuserstatus();
        }
        if(id==R.id.action_updatepassword){
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.update_pass,null);
        final EditText password=view.findViewById(R.id.old_pass);
        final EditText new_opass=view.findViewById(R.id.new_pass);
        final EditText pass=view.findViewById(R.id.conform_pass);
        Button button=view.findViewById(R.id.submit);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        builder.setView(view);
        builder.setTitle("Update Password");
        builder.create().show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_opass.startAnimation(zoomin);
                pass.startAnimation(zoomin);
                password.startAnimation(zoomin);
                progressDialog.show();
                String text=password.getText().toString().trim();
                final String new_pass=new_opass.getText().toString().trim();
                final String conform_pass=pass.getText().toString().trim();
                if (TextUtils.isEmpty(text)){
                    Toast.makeText(getActivity(),"Enter password",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),text);
                    user.reauthenticate(authCredential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(new_pass.length()>=6) {
                                        if (new_pass.equals(conform_pass)) {
                                            user.updatePassword(new_pass)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getActivity(), "Successfully changed password", Toast.LENGTH_SHORT).show();
                                                            new_opass.setText("");
                                                            password.setText("");
                                                            pass.setText("");
                                                            progressDialog.dismiss();
                                                            pass.startAnimation(animation);
                                                            password.startAnimation(animation);
                                                            new_opass.startAnimation(animation);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "failed to change password", Toast.LENGTH_SHORT).show();

                                                            progressDialog.dismiss();

                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getActivity(), "Enter password correctly", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Password length must be minimum length 6",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }

            }
        });

    }

    private void checkuserstatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            Toast.makeText(getActivity(),"Logout",Toast.LENGTH_SHORT).show();

        }
        else{
            Intent myIntent = new Intent(My_profile.this.getActivity(),MainActivity.class);
            startActivity(myIntent);

        }
    }


}