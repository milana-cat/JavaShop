package com.example.buyme2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

//import javax.swing.*;
public class RegisterService extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    public static String password;
    public static String hashpassword;
    public static AtomicReference<String> message=new AtomicReference<>(" ");
    public static String login;
    public static String username;

    private Button loginButton;
    private EditText loginInput;
    private Button registerBtn;

    private ProgressDialog loadingBar;
    private EditText usernameInput, passwordInput;
    //private String login;

    public RegisterService(){
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
                Intent MainIntent=new Intent(RegisterService.this, MainActivity.class);
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
                try{login();Intent MainIntent=new Intent(RegisterService.this, MainActivity.class);
                    startActivity(MainIntent);}
                catch (Exception e) {
                    //if(message.get().contentEquals(""))
                      //  message.set("Не удалось создать аккаунт");
                    Toast.makeText(this, "Не удалось создать аккаунт."+message,Toast.LENGTH_SHORT);

                } ;
            loadingBar.hide();





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
                    long count=buf.lines().count();
                    String response = buf.readLine();
                    if(count>=2){
                       response=buf.readLine();

                    }
                    if(response.contains("-1")){
                        message=new AtomicReference<>("Такой логин уже существует");
                        throw new RuntimeException() ;
                    }
                    out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("CREATE TABLE "+login+"_order (" +
                            "product_id varchar(48) NOT null primary key," +
                            "title varchar(256) NOT NULL," +
                            "price float4 NULL DEFAULT 0.00," +
                            "lot int4 NULL DEFAULT 0" +
                            ");");
                    out.flush();
                    in = new InputStreamReader(socket.getInputStream());
                    buf = new BufferedReader(in);
                    String response2 = buf.readLine();
                    //String response2 = buf.readLine();
                    if(response2.contentEquals("-1")){
                        message=new AtomicReference<>("Такой логин уже существует");
                        throw new RuntimeException() ;
                    }
                    socket.close();

                } catch (Exception ex) {
                    message.set("Нет соединения с сервером");
                    throw new RuntimeException();
                    //ex.printStackTrace();

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

