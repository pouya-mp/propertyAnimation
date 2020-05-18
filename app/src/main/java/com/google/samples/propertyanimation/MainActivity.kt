/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button
    lateinit var multishowerButton: Button
    lateinit var rainbowShowerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)
        multishowerButton = findViewById<Button>(R.id.multiShowerButton)
        rainbowShowerButton = findViewById<Button>(R.id.rainbowShowerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }

        multishowerButton.setOnClickListener {
            multipleStarsShower()
        }

        rainbowShowerButton.setOnClickListener {
            rainbowShower()
        }

    }

    private fun rotater() {

        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()

    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(translateButton)
        }
        animator.start()
    }

    private fun scaler() {

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        animator.apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(scaleButton)
        }
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(star.parent, "backgroundColor", Color.BLACK, Color.RED)
        animator.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(colorizeButton)
        }
        animator.start()
    }

    private fun shower(colorCode: String? = "#FFFF00", rainbowStar: Boolean? = false) {

        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)
        newStar.setColorFilter(Color.parseColor(colorCode))
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Math.random().toFloat() * containerW - starW / 2f

        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)

        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION, Math.random().toFloat() * 1080)
        rotator.interpolator = LinearInterpolator()

        val calevl = ArgbEvaluator()
        val color = ObjectAnimator.ofObject(
            newStar,
            "colorFilter",
            calevl,
            Color.parseColor("#FF0000"),
            Color.parseColor("#FF7F00"),
            Color.parseColor("#FFFF00"),
            Color.parseColor("#00FF00"),
            Color.parseColor("#0000FF"),
            Color.parseColor("#4B0082"),
            Color.parseColor("#9400D3")

        )


        val set = AnimatorSet()
        if (rainbowStar == true) {
            set.playTogether(mover, rotator, color)
        } else {
            set.playTogether(mover, rotator)
        }

        set.duration = (Math.random() * 1500 + 500).toLong()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })

        set.start()

    }

    private fun multipleStarsShower() {
        val colors = listOf(
            "#FF0000",
            "#FF7F00",
            "#FFFF00",
            "#00FF00",
            "#0000FF",
            "#4B0082",
            "#9400D3"
        )

        for (i in 0 until 7) {
            runOnUiThread {
                shower(colors.shuffled().first())
            }
        }
    }

    fun rainbowShower(){
        shower(rainbowStar = true)
    }
}

fun ObjectAnimator.disableViewDuringAnimation(view: View) {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator?) {
            view.isEnabled = true
        }
    })
}