package com.example.buyme2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button joinButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton=(Button) findViewById((R.id.main_join_now_btn));
        loginButton=(Button) findViewById((R.id.main_login_btn));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
               startActivity(loginIntent);
                //Intent ProductIntent=new Intent(MainActivity.this, ProductActivity.class);
                //startActivity(ProductIntent);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}