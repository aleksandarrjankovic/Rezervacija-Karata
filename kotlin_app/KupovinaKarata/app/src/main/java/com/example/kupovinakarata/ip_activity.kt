package com.example.kupovinakarata

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_ip_activity.*

class ip_activity : AppCompatActivity() {

    private var ip:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_activity)

        potvrda.setOnClickListener {

            val ipadresa = findViewById<MaterialEditText>(R.id.ip_adresa)
            val m: String = ipadresa.text.toString()
            if (m.trim().length > 0) {
                ip = "http://" + m + ":3000/" //parametar za retrofit
                Toast.makeText(applicationContext, "Ip adresa : " +m, Toast.LENGTH_SHORT)
                    .show()

                val sharedPreferences = getSharedPreferences("IP", Context.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putString("adress", ip)
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Molimo vas unestie IP adresu! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

