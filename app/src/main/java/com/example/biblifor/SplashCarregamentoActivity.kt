package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashCarregamentoActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val navegarParaLogin = Runnable {
        startActivity(Intent(this@SplashCarregamentoActivity, LoginUsuarioActivity::class.java))
        finish() // não permite voltar para o splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_carregamento)

        // Agenda a navegação após ~6 segundos
        handler.postDelayed(navegarParaLogin, 4000L)
    }

    override fun onDestroy() {
        // Evita vazamento removendo callbacks se a activity for destruída antes do tempo
        handler.removeCallbacks(navegarParaLogin)
        super.onDestroy()
    }
}