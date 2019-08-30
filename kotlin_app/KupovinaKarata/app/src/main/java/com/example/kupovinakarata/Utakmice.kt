package com.example.kupovinakarata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView


class Utakmice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utakmice)

        val listView = findViewById<ListView>(R.id.list)
        val utakmice = arrayOf("FK Rudan Grljan- FK Ozren Sokobanja","FK Timok Zaječar - FK Kablovi Zaječar")
        val adp: ArrayAdapter<String> = ArrayAdapter(this@Utakmice,android.R.layout
            .simple_list_item_1, utakmice)

        val email = intent.getStringExtra("email")

        listView.adapter = adp
        listView.setOnItemClickListener { _, _, _, id ->
            //Toast.makeText(this@Utakmice,""+id, Toast.LENGTH_SHORT).show()
            if(id.toString()=="0"){
                val pom = id.toString()
                val intent = Intent(this@Utakmice, GrljanSokobanja::class.java)
                intent.putExtra("id", pom)
                intent.putExtra("email", email)
                startActivity(intent)
            }
            else if(id.toString()=="1"){
                val intent = Intent(this, TimokKablovi::class.java)
                intent.putExtra("email", email)
                startActivity(intent)}
        }
    }
}

