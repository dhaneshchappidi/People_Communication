package com.example.dhaneshchappidi.pc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity{

    private EditText editTextEmail,name,phno;
    private EditText editTextPassword,id_no;
    ImageButton buttonSignup;
    TextView tv;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.mail);
        editTextPassword = (EditText) findViewById(R.id.pass);
        phno=(EditText)findViewById(R.id.phone);
        buttonSignup = (ImageButton) findViewById(R.id.submit);
        id_no=(EditText)findViewById(R.id.idno);
        name=(EditText)findViewById(R.id.name);
        tv=(TextView)findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Activity.this,Login_act.class);
                startActivity(i);
            }
        });
    }
    public void btnClickAct1(View V){
        String id = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        final String email2=id_no.getText().toString().trim();
        if(email2.startsWith("N170")|| email2.startsWith("N150") || email2.startsWith("N160") || email2.startsWith("N140")&& email2.length()==7) {
            if (TextUtils.isEmpty(email2)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
                return;
            }

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();
            final String email=email2+"@rguktn.ac.in";
            final String full_name=name.getText().toString();
            final String image="http://intranet.rguktn.ac.in/SMS/usrphotos/user/"+email2+".jpg";
            final String phoneno=phno.getText().toString();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                String uid=user.getUid();
                                String uemail=user.getEmail();
                                HashMap<Object,String> hashMap=new HashMap<>();
                                hashMap.put("uid",uid);
                                hashMap.put("idno",email2);
                                hashMap.put("phone",phoneno);
                                hashMap.put("email",uemail);
                                hashMap.put("name",full_name);
                                hashMap.put("image",image);
                                hashMap.put("type","student");
                                hashMap.put("branch","No_brach");

                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference reference=database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                Toast.makeText(Main2Activity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(Main2Activity.this,Student_home.class);
                                startActivity(i);

                            } else {
                                Toast.makeText(Main2Activity.this, "Registration Error", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

        }
        else {
            Toast.makeText(Main2Activity.this,"Enter id no correctly",Toast.LENGTH_LONG).show();
        }

    }
}
