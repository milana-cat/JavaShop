package com.example.buyme2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProductActivity extends AppCompatActivity {


    public static List<Product> BuyList;
    private Button BuyButton, ListButton, ExitButton;
    private TextView TextName, TextDesc, TextPrice;
    Product product = new Product();
    private String Response;
    ImageView Image;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Product product = new Product();
        id=1;
        GetProduct();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        BuyButton=(Button) findViewById((R.id.buy_btn));
        ListButton=(Button) findViewById((R.id.list_btn));
        ExitButton=(Button) findViewById((R.id.exit_btn));

        ListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent OrderIntent=new Intent(ProductActivity.this,OrderActivity.class);
               startActivity(OrderIntent);
            }
        });
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ExitIntent=new Intent(ProductActivity.this,MainActivity.class);
                startActivity(ExitIntent);
            }
        });
        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.Lot==0){
                    BuyList.add(product);
                }else{
                    product.Add();
                }
                //Intent registerIntent=new Intent(ProductActivity.this, RegisterActivity.class);
                //startActivity(registerIntent);
            }
        });

    }
    private void ProductsList(){
    }

    float fromPosition=0;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения
                // fromPosition - координата по оси X начала выполнения операции
                fromPosition = event.getX();
                break;
            case MotionEvent.ACTION_UP: // Пользователь отпустил экран, т.е. окончание движения
                float toPosition = event.getX();
                if (fromPosition > toPosition){
                    //Получаем данные о новом продукте и меняем содержание
                    id+=1;
                    GetProduct();
                    String name = product.GetPicName(id);
                    int id = getResources().getIdentifier(name, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    Image.setImageDrawable(drawable);

                }
                    //
                else if (fromPosition < toPosition){
                    //Получаем данные о прошлом продукте и меняем содержание страницы
                    if(id>=2){
                        id-=1;
                        GetProduct();
                        String name = product.GetPicName(id);
                        int id = getResources().getIdentifier(name, "drawable", getPackageName());
                        Drawable drawable = getResources().getDrawable(id);
                        Image.setImageDrawable(drawable);
                    }
                }
                    //flipper.showPrevious();
            default:
                break;
        }
        //product.GetProductByID(id);

        return true;
    }
    public void GetProduct(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress sa = new InetSocketAddress(LoginActivity.host, LoginActivity.port);
                try {
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());


                    //BufferedWriter buf = new BufferedWriter(out);
                    out.write("select * from public.product where product_id='" + id +"'");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    Response = buf.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String [] log = Response.split("\t");
                            Product product = new Product();
                            product.Name=log[1];
                            product.Descriptor=log[2];
                            product.Price=Float.parseFloat(log[3]);
                            product.Count =Integer.parseInt(log[4]);
                            TextName=(TextView)findViewById((R.id.product_name));
                            TextName.setText(product.Name);
                            TextDesc=(TextView)findViewById((R.id.product_descript));
                            TextDesc.setText(product.Descriptor);
                            TextPrice=(TextView)findViewById((R.id.product_price));
                            TextPrice.setText("Цена: "+product.Price+"мяукоин");
                            Image = (ImageView)findViewById((R.id.product_pic));

                            //int id = getResources().getIdentifier(name, "drawable", getPackageName());
                            //Drawable drawable = getResources().getDrawable(id);
                            //Image.setImageDrawable(drawable);
                        }
                    });

                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();

                }

            }
        }).start();
    }
}
