package com.example.buyme2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private Button RemoveButton, AddButton, ExitButton, OrderButton;
    private TextView TextName, TextLot, TextPrice;
    Product product = new Product();
    ImageView Image;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Product product = new Product();
        id=1;
        //product.GetProductByID(id);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AddButton=(Button) findViewById((R.id.add_btn));
        RemoveButton=(Button) findViewById((R.id.remove_btn));
        OrderButton=(Button) findViewById((R.id.final_order_btn));
        ExitButton=(Button) findViewById((R.id.exit_btn));
        TextName=(TextView) findViewById((R.id.product_name));
        TextLot=(TextView) findViewById((R.id.product_count));
        TextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //При нажатии на текст переход к записи продукта
                    //ProductActivity.id=
                    Intent ExitIntent=new Intent(OrderActivity.this,ProductActivity.class);
                    startActivity(ExitIntent);

            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.Lot==0){
                    ProductActivity.BuyList.add(product);}

                product.Add();
                TextLot.setText(product.Lot);
            }
        });
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ExitIntent=new Intent(OrderActivity.this,ProductActivity.class);
                startActivity(ExitIntent);
            }
        });
        RemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.Lot==1){
                    ProductActivity.BuyList.remove(product);

                }else{
                    product.Remove();
                }
                TextLot.setText(product.Lot);
                //Intent registerIntent=new Intent(ProductActivity.this, RegisterActivity.class);
                //startActivity(registerIntent);
            }
        });

    }
    private void ProductsList(){
    }

    float fromPosition=0;

    public void ListShow(List<Product> order)
    {
        for (int i=0;i<=order.size();i++)
        {
            TextView text = new TextView(TextName.getContext());
            //text.setTop(80);

        }
                    //Получаем данные о новом продукте и меняем содержание
                    id+=1;
                    TextName=(TextView)findViewById((R.id.product_name));
                    TextName.setText(product.Name);
                    TextPrice=(TextView)findViewById((R.id.product_price));
                    TextPrice.setText("Цена: "+product.Price+"мяукоин");
                    Image = (ImageView)findViewById((R.id.product_pic));
                    String name = product.GetPicName(id);
                    int id = getResources().getIdentifier(name, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    Image.setImageDrawable(drawable);


    }
}
