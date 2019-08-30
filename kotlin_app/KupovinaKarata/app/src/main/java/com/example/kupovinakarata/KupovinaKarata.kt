package com.example.kupovinakarata

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.kupovinakarata.Retrofit.RetrofitClient
import com.example.kupovinakarata.Retrofit.nodejs
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.rengwuxian.materialedittext.MaterialEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_kupovina_karata.*


class KupovinaKarata : AppCompatActivity() {

    private var price=0
    private var cD = CompositeDisposable()
    lateinit var myAPI: nodejs
    private var tribina=""

    override fun onStop() {
        cD.clear()
        super.onStop()
    }

    @SuppressLint("ResourceType", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kupovina_karata)

        val retrofit = RetrofitClient.getInstance(this)
        myAPI = retrofit.create(nodejs::class.java)

        val intentid = intent
        val id = intentid.getStringExtra("id")
        val id_p = findViewById<TextView>(R.id.id_p)
        val email =  intentid.getStringExtra("email")
        //val email_p = findViewById<TextView>(R.id.email_p)
        //email_p.text = email
        //Log.v("EMAIL : " , email)
        if (id.equals("0")) {
            id_p.text = "FK Rudar Grljan - FK Ozren Sokobanja"
        } else if (id.equals("1")) {
            id_p.text = "FK Timok Zaječar - FK Kablovi Zaječar"
        }


        severBox.setOnClickListener {isChecked->
            //Toast.makeText(this, "Izabrali ste sever",Toast.LENGTH_LONG).show()
            price = 300
            istokBox.isChecked = false
            zapadBox.isChecked = false
            jugBox.isChecked = false
            tribina = "Sever"
        }
        istokBox.setOnClickListener {isChecked->
            //Toast.makeText(this, "Izabrali ste istok",Toast.LENGTH_LONG).show()
            price= 200
            severBox.isChecked = false
            zapadBox.isChecked = false
            jugBox.isChecked = false
            tribina = "Istok"

        }
        zapadBox.setOnClickListener {isChecked->
            //Toast.makeText(this, "Izabrali ste zapad",Toast.LENGTH_LONG).show()
            price = 150
            severBox.isChecked = false
            istokBox.isChecked = false
            jugBox.isChecked = false
            tribina  = "Zapad"
        }
        jugBox.setOnClickListener {isChecked->
            //Toast.makeText(this, "Izabrali ste jug",Toast.LENGTH_LONG).show()
            price = 300
            severBox.isChecked = false
            zapadBox.isChecked = false
            istokBox.isChecked = false
            tribina = "Jug"
        }

        kupi_dugme.setOnClickListener {
            val itemView = LayoutInflater.from(this@KupovinaKarata)
                .inflate(R.layout.rezervacija, null)

            MaterialStyledDialog.Builder(this@KupovinaKarata)
                .setIcon(R.drawable.ic_acc)
                .setHeaderColor(R.color.headerColor)
                .setTitle("Rezervacija")
                .setDescription("Molimo vas da popunite sva polja")
                .setCustomView(itemView)
                .setNegativeText("Cancel")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .setPositiveText("Rezerviši")
                .onPositive(
                    MaterialDialog.SingleButtonCallback { _, _ ->
                        val edt_email =
                            itemView.findViewById<View>(R.id.edt_email) as MaterialEditText
                        val edt_name =
                            itemView.findViewById<View>(R.id.edt_ime) as MaterialEditText
                        val br_mesta =
                            itemView.findViewById<View>(R.id.br_mesta) as MaterialEditText

                        if (TextUtils.isEmpty(edt_email.text.toString())) {
                            Toast.makeText(
                                this@KupovinaKarata,
                                "Niste uneli email adresu!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@SingleButtonCallback
                        }
                        if (TextUtils.isEmpty(edt_name.text.toString())) {
                            Toast.makeText(
                                this@KupovinaKarata,
                                "Niste uneli vaše ime!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@SingleButtonCallback
                        }
                        if (TextUtils.isEmpty(br_mesta.text.toString())) {
                            Toast.makeText(
                                this@KupovinaKarata,
                                "Niste uneli broj krata!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@SingleButtonCallback
                        }
                        var broj_mesta = (br_mesta.text.toString()).toInt()
                        if (broj_mesta > 4) {
                            Toast.makeText(
                                this@KupovinaKarata,
                                "Uneli ste vise od 4!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@SingleButtonCallback
                        } else {
                            rezervacijaKarata(
                                edt_email.text.toString(),
                                edt_name.text.toString(),
                                br_mesta.text.toString(),
                                id.toString()
                            )
                        }
                    }).show()
        }

        rezervacijaLogin.setOnClickListener {
            val itemView = LayoutInflater.from(this@KupovinaKarata)
                .inflate(R.layout.rezervacija_login, null)

            MaterialStyledDialog.Builder(this@KupovinaKarata)
                .setIcon(R.drawable.ic_acc)
                .setHeaderColor(R.color.headerColor)
                .setTitle("Rezervacija")
                .setDescription("Molimo vas da popunite sva polja")
                .setCustomView(itemView)
                .setNegativeText("Cancel")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .setPositiveText("Rezerviši")
                .onPositive(
                    MaterialDialog.SingleButtonCallback { _, _ ->
                        val br_mesta =
                            itemView.findViewById<View>(R.id.br_mesta_login) as MaterialEditText
                        var broj_mesta = (br_mesta.text.toString()).toInt()
                        if (broj_mesta > 4) {
                        Toast.makeText(
                            this@KupovinaKarata,
                            "Uneli ste vise od 4!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@SingleButtonCallback
                        }
                        else{
                            rezLogin(br_mesta.text.toString(),email, id, tribina)
                        }
                    }).show()
        }

    }

    private fun rezervacijaKarata(email: String, ime: String, br_mesta: String,id:String) {
        price = price*br_mesta.toInt()
        cD.add(myAPI.rezervacija(email, ime, br_mesta, price.toString(), tribina, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val intent = Intent(this, Utakmice::class.java)
                    startActivity(intent)
            })
    }
    private fun rezLogin(br_mesta:String, email:String, id: String, tribina:String){
        price = price*br_mesta.toInt()
        cD.add(myAPI.rezervacijaLogin(email, id,br_mesta, price.toString(), tribina)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val intent = Intent(this, Utakmice::class.java)
                startActivity(intent)
            })
    }
}






