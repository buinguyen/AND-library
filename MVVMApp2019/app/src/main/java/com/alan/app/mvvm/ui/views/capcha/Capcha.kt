package com.alan.app.mvvm.ui.views.capcha

import android.graphics.Bitmap
import android.graphics.Color
import java.util.*

abstract class Captcha {
    private var _image: Bitmap? = null
    private var _width: Int = 0
    private var _height: Int = 0
    protected var answer = ""
    protected var x = 0
    protected var y = 0

    protected abstract fun genImage(): Bitmap

    protected abstract fun checkAnswer(ans: String): Boolean

    fun getHeight(): Int = this._height

    fun setHeight(height: Int) {
        if (height in 1..9999) {
            this._height = height
        } else {
            this._height = 100
        }
    }

    fun getWidth(): Int = this._width

    fun setWidth(width: Int) {
        if (width in 1..9999) {
            this._width = width
        } else {
            this._width = 300
        }
    }

    fun setImage(image: Bitmap) {
        this._image = image
    }

    fun getImage(): Bitmap? = this._image

    companion object {
        private var usedColors: MutableList<Int>? = null

        fun setUsedColors(usedColors: List<Int>) {
            Captcha.usedColors = usedColors.toMutableList()
        }

        fun makeColor(): Int {
            val r = Random()
            var number: Int
            do {
                number = r.nextInt(9)
            } while (usedColors?.contains(number) == true)

            usedColors?.add(number)

            return when (number) {
                0 -> Color.BLACK
                1 -> Color.BLUE
                2 -> Color.CYAN
                3 -> Color.DKGRAY
                4 -> Color.GRAY
                5 -> Color.GREEN
                6 -> Color.MAGENTA
                7 -> Color.RED
                8 -> Color.YELLOW
                9 -> Color.WHITE
                else -> Color.WHITE
            }
        }
    }
}