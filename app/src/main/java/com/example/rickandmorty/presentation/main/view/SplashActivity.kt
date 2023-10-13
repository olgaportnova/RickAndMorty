package com.example.rickandmorty.presentation.main.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.rickandmorty.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        setUpFadeInAnimation()

        Handler().postDelayed({
            val mainIntent = Intent(this, RootActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_TIMEOUT_MS)
    }

    private fun setUpFadeInAnimation() {
        val fadeInAnimation = AlphaAnimation(
            FADE_IN_FROM_ALPHA,
            FADE_IN_TO_ALPHA
        )
        fadeInAnimation.interpolator = DecelerateInterpolator()
        fadeInAnimation.duration = FADE_IN_DURATION_MS
        val splashImage = findViewById<ImageView>(R.id.splashImage)
        splashImage.startAnimation(fadeInAnimation)
    }

    companion object {
        private const val SPLASH_TIMEOUT_MS: Long = 3000
        private const val FADE_IN_DURATION_MS: Long = 3000
        private const val FADE_IN_FROM_ALPHA = 0.0f
        private const val FADE_IN_TO_ALPHA = 1.0f
    }
}
