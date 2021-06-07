package com.example.dhaneshchappidi.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button bt,bt2;
    ImageButton im,im2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=(Button)findViewById(R.id.submit2);
        bt2=(Button)findViewById(R.id.submit);
        im=(ImageButton) findViewById(R.id.imageButton);
        im2=(ImageButton)findViewById(R.id.imageButton2);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Admin_login.class);
                startActivity(i);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Admin_login.class);
                startActivity(i);
            }
        });
    }
    public void btnClickAct1(View V){
        Intent i=new Intent(MainActivity.this,Main2Activity.class);
        startActivity(i);
    }
}
