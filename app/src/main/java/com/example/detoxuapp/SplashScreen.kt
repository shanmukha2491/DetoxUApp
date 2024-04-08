package com.example.detoxuapp


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextSwitcher
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var textSwitcher: TextSwitcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        textSwitcher = findViewById(R.id.textSwitcherSplashScreen)

        textSwitcher.setInAnimation(this,android.R.anim.slide_in_left)
        textSwitcher.setOutAnimation(this,android.R.anim.slide_out_right)
        textSwitcher.setText("DU")

        Handler().postDelayed({
            textSwitcher.setText("Detox Your Life")
        },2000)

        Handler().postDelayed({
            val homeIntent = Intent(this@SplashScreen,MainActivity::class.java)
            startActivity(homeIntent)
            finish()
        },4000)


    }
}