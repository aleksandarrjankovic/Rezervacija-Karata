package com.example.kupovinakarata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_grljan_sokobanja.*

class TimokKablovi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timok_kablovi)

        val id = "1"
        val email = intent.getStringExtra("email")
        naruci_gs.setOnClickListener {
            val intent = Intent(this, KupovinaKarata::class.java)
            intent.putExtra("id",id)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}
