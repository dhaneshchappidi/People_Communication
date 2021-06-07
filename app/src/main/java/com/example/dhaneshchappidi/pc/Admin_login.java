package com.example.dhaneshchappidi.pc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_login extends AppCompatActivity {
    private EditText email2;
    private EditText password2;
    ImageButton btnlogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){

        }

        email2=(EditText) findViewById(R.id.uname);
        password2=(EditText) findViewById(R.id.pass);
        btnlogin=(ImageButton)findViewById(R.id.submit);
        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
    }
    public void btnClickAct1(View V){


        String email= email2.getText().toString().trim();
        String password=password2.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        final String email1=email+"@rguktn.ac.in";
        firebaseAuth.signInWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                                email2.setText(null);
                                password2.setText(null
                                );
                        } else {
                            Toast.makeText(Admin_login.this, "Login failed", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("type");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if(value.equals("student")) {
                        Toast.makeText(Admin_login.this, "Welcome "+email2.getText().toString(), Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Admin_login.this,Student_home.class);
                        startActivity(i);
                    } else if (value.equals("faculty")){
                        Toast.makeText(Admin_login.this, "Welcome "+email2.getText().toString(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Admin_login.this, Admin_home.class));
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

