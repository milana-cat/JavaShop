package com.example.buyme2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RegisterActivity extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    public static String password;
    public static String hashpassword;
    public static String login;
    public static String username;

    private Button loginButton;
    private EditText loginInput;
    private Button registerBtn;

    private ProgressDialog loadingBar;
    private EditText usernameInput, passwordInput;
    //private String login;

    public RegisterActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn =(Button) findViewById(R.id.register_btn);
        usernameInput =(EditText) findViewById(R.id.register_username_input);
        loginInput =(EditText) findViewById(R.id.register_login_input);

        passwordInput =(EditText) findViewById(R.id.register_password_input);
        loadingBar= new ProgressDialog(this);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    CreateAccount();
            }
        });
        Button backbtn =(Button)findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(MainIntent);;
            }
        });
    }

    private void CreateAccount() {
        username=usernameInput.getText().toString();
        login=loginInput.getText().toString();
        password=passwordInput.getText().toString();
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Введите имя",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(login)){
            Toast.makeText(this,"Введите логин",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Введите пароль",Toast.LENGTH_SHORT).show();
        }
        else{
            // Создаем объект MessageDigest с использованием алгоритма SHA-256
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
                byte[] hash = md.digest(password.getBytes());

                // Кодируем хэш-значение в Base64 и выводим на экран
                hashpassword = Base64.getEncoder().encodeToString(hash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
                try{login();}
                catch (Exception e) {
                    Toast.makeText(this,"Не удалось создать аккаунт",Toast.LENGTH_SHORT).show();
                    loadingBar.hide();
                } ;
            loadingBar.hide();
            Intent MainIntent=new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(MainIntent);



        }
    }
    private void login() {
//Тут работает
        Thread thrd =new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress sa = new InetSocketAddress(host, port);
                try {
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("insert into public.clients (login, clientname, pass) values ('" + login + "','" + username + "','" + hashpassword + "')");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    //String response2 = buf.readLine();
                    if(in.toString()=="-1"){
                        throw new RuntimeException() ;
                    }
                    socket.close();

                } catch (Exception ex) {
                    ex.printStackTrace();

                }

            }


        }); thrd.start();
                        //BufferedWriter buf = new BufferedWriter(out);
                        //out.write("insert into public.clients (login, clientname, pass) values ('" + login + "','" + username + "','" + hashpassword + "')");
                        //out.flush();
                        //socket.close();
                    //} catch (Exception ex) {
                        //Toast.makeText(RegisterActivity.this,"Не удалось создать пользователя", Toast.LENGTH_SHORT).show();


                    //}

                //}
            //}).start();
        }
   }

