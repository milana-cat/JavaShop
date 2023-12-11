package com.example.buyme2;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.PendingIntentCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    private Button joinButton, loginButton;

    private static AtomicBoolean HasAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton=(Button) findViewById((R.id.main_join_now_btn));
        loginButton=(Button) findViewById((R.id.main_login_btn));
        //HasAnswer=new AtomicBoolean(false);
         new Thread(new Runnable() {
                @Override
                public void run() {
                    InetSocketAddress sa = new InetSocketAddress(host, port);
                    try {
                        Socket socket = new Socket();
                        socket.connect(sa, 5000);
                        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                        out.write("");
                        out.flush();
                        InputStreamReader in = new InputStreamReader(socket.getInputStream());
                        BufferedReader buf = new BufferedReader(in);
                        String i = buf.readLine();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } catch(Exception e){
                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics.
                                builder.setMessage("Нет связи с сервером")
                                        .setTitle("Ошибка!");

// 3. Get the AlertDialog.
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        joinButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics.
                                builder.setMessage("Нет связи с сервером")
                                        .setTitle("Ошибка!");

// 3. Get the AlertDialog.
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                //Toast.makeText(MainActivity.this, "Нет связи с сервером", Toast.LENGTH_LONG);
                            }
                        });
                        throw new RuntimeException();}
                }
            });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent=new Intent(MainActivity.this, LoginService.class);
                startActivity(loginIntent);
                //Intent ProductIntent=new Intent(MainActivity.this, ProductActivity.class);
                //startActivity(ProductIntent);
            }
        });
        HasAnswer=new AtomicBoolean(true);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent=new Intent(MainActivity.this, RegisterService.class);
                startActivity(registerIntent);
            }
        });

    }

}
