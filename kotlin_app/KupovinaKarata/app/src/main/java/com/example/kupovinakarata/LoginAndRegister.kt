package com.example.kupovinakarata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.kupovinakarata.Retrofit.RetrofitClient
import com.example.kupovinakarata.Retrofit.nodejs
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.rengwuxian.materialedittext.MaterialEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_and_register.*

class LoginAndRegister : AppCompatActivity() {

    lateinit var myAPI: nodejs
    private var compositeDisposable = CompositeDisposable()
    private var pom=1
    private var check = 0
    override fun onStop(){
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_and_register)

        val retrofit = RetrofitClient.getInstance(this)
        myAPI = retrofit.create(nodejs::class.java)

        login_button.setOnClickListener {
                loginUser(email_polje.text.toString(), edt_password.text.toString())
            }

        txt_create_acc.setOnClickListener {
            val itemView = LayoutInflater.from(this@LoginAndRegister)
                .inflate(R.layout.register_layout,null)

            MaterialStyledDialog.Builder(this@LoginAndRegister)
                .setIcon(R.drawable.ic_acc)
                .setTitle("Register")
                .setDescription("Molimo vas da popunite sva polja")
                .setCustomView(itemView)
                .setNegativeText("Cancel")
                .onNegative{dialog,_->  dialog.dismiss()}
                .setPositiveText("Register")
                .onPositive(
                    MaterialDialog.SingleButtonCallback{_ , _->
                    val edt_email = itemView.findViewById<View>(R.id.email_polje) as MaterialEditText
                    val edt_username = itemView.findViewById<View>(R.id.edt_username) as MaterialEditText
                    val edt_password = itemView.findViewById<View>(R.id.edt_password) as MaterialEditText

                    if(TextUtils.isEmpty(edt_email.text.toString())){
                        Toast.makeText(this@LoginAndRegister,"Niste uneli email adresu!",Toast.LENGTH_SHORT).show()
                        return@SingleButtonCallback
                    }
                    if(TextUtils.isEmpty(edt_username.text.toString())){
                        Toast.makeText(this@LoginAndRegister,"Niste uneli vaš username!",Toast.LENGTH_SHORT).show()
                        return@SingleButtonCallback
                    }
                    if(TextUtils.isEmpty(edt_password.text.toString())) {
                        Toast.makeText(this@LoginAndRegister, "Niste uneli vašu šifru!", Toast.LENGTH_SHORT).show()
                        return@SingleButtonCallback
                    }

                    registerUser(edt_email.text.toString(), edt_username.text.toString(),edt_password.text.toString())
                }).show()
        }

    }

    private fun registerUser(email: String, username: String, password: String) {

        compositeDisposable.addAll(myAPI.registerUser(email,username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                Toast.makeText(this@LoginAndRegister,""+result,Toast.LENGTH_SHORT).show()
            })
    }

    private fun loginUser(email: String, password: String) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this@LoginAndRegister,"Niste uneli email adresu!",Toast.LENGTH_SHORT).show()
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this@LoginAndRegister,"Niste uneli vašu šifru!",Toast.LENGTH_SHORT).show()
        }
        var token:String = "Uspesno ste se ulogovali."
        compositeDisposable.add(myAPI.loginUser(email,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                if (result.contains(token)) {
                    pom = 0
                } else {
                    pom = 1
                }
                Toast.makeText(this@LoginAndRegister,""+result,Toast.LENGTH_SHORT).show()
                if(pom == 0){
                    val intent = Intent(this, Utakmice::class.java)
                    intent.putExtra("email",email)
                    startActivity(intent)
                }
            })
    }

}
