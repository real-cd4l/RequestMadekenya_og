package com.madekenyarequest.madekenya

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.madekenyarequest.madekenya.pages.auth.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar)
        setContentView(R.layout.activity_welcome)



        mbNext.setOnClickListener {
            val intent: Intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}