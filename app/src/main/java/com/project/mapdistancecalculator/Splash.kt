package com.project.mapdistancecalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class Splash : AppCompatActivity() {
    lateinit var start:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        start=findViewById(R.id.start)

        start.setOnClickListener(View.OnClickListener {
            val intent=Intent(this@Splash,SignInActivity::class.java)
            startActivity(intent)
            finish()

        })
    }
}