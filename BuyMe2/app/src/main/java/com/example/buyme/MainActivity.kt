package com.example.buyme

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

internal class MainActivity(var count: Int = 0) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun MyOnClick(view: View?) {
        count =count+ 1
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = count.toString()
        //textView.text("Спасибо");
    }
}