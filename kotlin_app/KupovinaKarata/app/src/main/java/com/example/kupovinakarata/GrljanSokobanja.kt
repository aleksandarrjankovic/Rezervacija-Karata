package com.example.kupovinakarata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_grljan_sokobanja.*

class GrljanSokobanja : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grljan_sokobanja)

        val intentid = intent
        val pom = intentid.getStringExtra("id")
        val email = intent.getStringExtra("email")
        naruci_gs.setOnClickListener {
            val intent = Intent(this@GrljanSokobanja, KupovinaKarata::class.java)
            intent.putExtra("id", pom)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }

}

