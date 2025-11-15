package com.example.biblifor

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar

class SplashCarregamentoActivity : BaseActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressBar: ProgressBar

    private val duracaoSplash = 4000L // 4 segundos

    private val navegarParaLogin = Runnable {
        startActivity(Intent(this@SplashCarregamentoActivity, LoginUsuarioActivity::class.java))
        finish() // não permite voltar para o splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_carregamento)

        progressBar = findViewById(R.id.progressBarSplash)

        // Anima a barra de 0 a 100 em 4 segundos
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = duracaoSplash
        animator.addUpdateListener { anim ->
            val value = anim.animatedValue as Int
            progressBar.progress = value
        }
        animator.start()

        // Continua usando o mesmo delay para ir para o login
        handler.postDelayed(navegarParaLogin, duracaoSplash)
    }

    override fun onDestroy() {
        // Evita vazamento removendo callbacks se a activity for destruída antes do tempo
        handler.removeCallbacks(navegarParaLogin)
        super.onDestroy()
    }
}
