package com.example.buyme2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    public static String password;
    public String login;
    public static AtomicReference<String> Login;
    public static String username;

    private Button loginButton;
    private EditText loginInput;
    private Button loginBtn;
    private String Response;

    private ProgressDialog loadingBar;
    private EditText usernameInput, passwordInput;
    //private String login;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginInput = (EditText) findViewById(R.id.login_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginAccount();
            }
        });
        Button backbtn = (Button) findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(MainIntent);
                ;
            }
        });
    }

    private void LoginAccount() {
        login =loginInput.getText().toString();
        Login=new AtomicReference<String>(login);
        password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(login)) {
            Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        } else {
            // Создаем объект MessageDigest с использованием алгоритма SHA-256
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
            byte[] hash = md.digest(password.getBytes());

            // Кодируем хэш-значение в Base64 и выводим на экран
            password = Base64.getEncoder().encodeToString(hash);
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            try {
                login();




            } catch (Exception e) {
                Intent ProductIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ProductIntent);
                //throw new RuntimeException(e);
            }

            Intent ProductIntent = new Intent(LoginActivity.this, ProductActivity.class);
            startActivity(ProductIntent);
        }
    }

    //String str ="Select login, pass from public.clients where (login='" + login + "'and pass='" +password + "')";
    private void login() {

        Thread thrd1 = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress sa = new InetSocketAddress(host, port);
                try {
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());


                    out.write("select login, pass from public.clients where (login='" + login + "')");
                    //out.write("select login, pass from public.clients");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    Response = buf.readLine();
                    socket.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] log = Response.split("\t");
                            String logname, logpass;
                            logname = log[0];
                            logpass = log[1];

                            password = passwordInput.getText().toString();
                            MessageDigest md = null;
                            try {
                                md = MessageDigest.getInstance("SHA-256");
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }
                            // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
                            byte[] hash = md.digest(password.getBytes());

                            // Кодируем хэш-значение в Base64 и выводим на экран
                            password = Base64.getEncoder().encodeToString(hash);
                            if (logpass.contentEquals(password)) {

                                //Toast.makeText(this, "Вы ввели неверный пароль", Toast.LENGTH_SHORT).show();
                                Intent ProductIntent = new Intent(LoginActivity.this, ProductActivity.class);
                                startActivity(ProductIntent);
                            }
                            else {
                                Intent ProductIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(ProductIntent);
                                //Toast.makeText(this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } catch (Exception ex) {
                    ex.printStackTrace();

                }

            }


        });thrd1.start();
        loadingBar.hide();
    }
}

