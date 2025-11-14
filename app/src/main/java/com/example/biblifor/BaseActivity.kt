package com.example.biblifor

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    // ====== FECHAR TECLADO AO CLICAR FORA DO EDITTEXT ======
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val focused = currentFocus
            if (focused is EditText) {
                val r = Rect()
                focused.getGlobalVisibleRect(r)
                val x = ev.rawX.toInt()
                val y = ev.rawY.toInt()

                if (!r.contains(x, y)) {
                    focused.clearFocus()

                    val root = window.decorView
                    root.isFocusable = true
                    root.isFocusableInTouchMode = true
                    root.requestFocus()

                    hideIme(root)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideIme(anchor: View) {
        ViewCompat.getWindowInsetsController(anchor)
            ?.hide(WindowInsetsCompat.Type.ime())

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(anchor.windowToken, 0)
    }

    // ====== ANIMAÇÕES ESTILO "SISTEMA" ======

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        // Indo pra frente (nova tela)
        overridePendingTransition(
            R.anim.open_enter,
            R.anim.open_exit
        )
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
        // Indo pra frente (com options)
        overridePendingTransition(
            R.anim.open_enter,
            R.anim.open_exit
        )
    }

    override fun finish() {
        super.finish()
        // Voltando (back)
        overridePendingTransition(
            R.anim.close_enter,
            R.anim.close_exit
        )
    }
}
