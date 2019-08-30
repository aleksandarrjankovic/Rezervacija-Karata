package com.example.kupovinakarata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val ip = intent.getStringExtra("ip"

        nastavi.setOnClickListener {
            val intent = Intent(this, Utakmice::class.java)
            startActivity(intent)
        }
        login_dugme.setOnClickListener {
            val intent = Intent(this, LoginAndRegister::class.java)
            startActivity(intent)
        }
        lopta.setOnClickListener {
            val intent = Intent(this, ip_activity::class.java)
            startActivity(intent)
        }
    }
}
