package com.example.dhaneshchappidi.pc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_act extends AppCompatActivity {
    private EditText email2;
    private EditText password2;
    ImageButton btnlogin;
    Button forgot;
    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){

        }

        email2=(EditText)findViewById(R.id.uname);
        password2=(EditText)findViewById(R.id.pass);
        btnlogin=(ImageButton)findViewById(R.id.submit);
        forgot=(Button)findViewById(R.id.forget);
        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRecoverPasswordDialog();
            }
        });

    }

    private void ShowRecoverPasswordDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        LinearLayout linearLayout =new LinearLayout(this);
        final EditText EditText=new EditText(this);
        EditText.setHint("ID NO");
        EditText.setMinEms(16);
        linearLayout.addView(EditText);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    String IDNO=EditText.getText().toString().trim();
                    String idemail=IDNO+"@rguktn.ac.in";
                    beginRecover(idemail);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecover(String idemail) {
        firebaseAuth.sendPasswordResetEmail(idemail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login_act.this,"Email Sent",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login_act.this,"Failed...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login_act.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


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
            String email1=email+"@rguktn.ac.in";
            firebaseAuth.signInWithEmailAndPassword(email1, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                            } else {
                                Toast.makeText(Login_act.this, "Login failed", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Login_act.this, "Login Successfull", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Login_act.this,Student_home.class);
                        startActivity(i);
                    } else if (value.equals("faculty")){
                        startActivity(new Intent(Login_act.this, Admin_home.class));
                        Toast.makeText(Login_act.this, "Login as Faculty", Toast.LENGTH_SHORT).show();
                        finish();
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
