package com.alan.app.mvvm.ui.views.capcha

import android.graphics.*
import android.graphics.Bitmap.Config
import java.io.CharArrayWriter
import java.util.*
import kotlin.math.abs

class TextCaptcha(width: Int, height: Int, wordLength: Int, opt: TextOptions) : Captcha() {

    private var _options: TextOptions = opt
    private var _wordLength: Int = wordLength
    private var mCh: Char = ' '

    enum class TextOptions {
        UPPERCASE_ONLY,
        LOWERCASE_ONLY,
        NUMBERS_ONLY,
        LETTERS_ONLY,
        NUMBERS_AND_LETTERS
    }

    init {
        setHeight(height)
        setWidth(width)
        setUsedColors(ArrayList())
        this.setImage(genImage())
    }

    override fun genImage(): Bitmap {
        this.answer = ""

        val width = getWidth() / this._wordLength
        val height = getHeight() / 2
        val gradient = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(), makeColor(), makeColor(), Shader.TileMode.CLAMP)
        val bgPaint = Paint()
        bgPaint.isDither = true
        bgPaint.shader = gradient
        val bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888)
        val drawBgCanvas = Canvas(bitmap)
        drawBgCanvas.drawRect(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), bgPaint)
        val textPaint = Paint()
        textPaint.isDither = true
        textPaint.textSize = (getWidth() / getHeight() * 20).toFloat()

        val r = Random(System.currentTimeMillis())
        val charsWriter = CharArrayWriter()
        for (i in 0 until this._wordLength) {
            val ch: Char = when (_options) {
                TextOptions.UPPERCASE_ONLY -> (r.nextInt(91 - 65) + 65).toChar()
                TextOptions.LOWERCASE_ONLY -> (r.nextInt(123 - 97) + 97).toChar()
                TextOptions.NUMBERS_ONLY -> (r.nextInt(58 - 49) + 49).toChar()
                TextOptions.LETTERS_ONLY -> getLetters(r)
                TextOptions.NUMBERS_AND_LETTERS -> getLettersNumbers(r)
            }
            charsWriter.append(ch)
            this.answer += ch
        }

        val data = charsWriter.toCharArray()
        for (i in data.indices) {
            this.x += (30 - 3 * this._wordLength + abs(r.nextInt()) % (65 - 1.2 * this._wordLength)).toInt() + 20
            this.y = 50 + abs(r.nextInt()) % 50
            val drawTextCanvas = Canvas(bitmap)
            textPaint.textSkewX = r.nextFloat() - r.nextFloat()
            textPaint.color = makeColor()
            drawTextCanvas.drawText(data, i, 1, this.x.toFloat(), this.y.toFloat(), textPaint)
            textPaint.textSkewX = 0f
        }
        return bitmap
    }

    private fun getLetters(r: Random): Char {
        val randomInt = r.nextInt(123 - 65) + 65
        if (randomInt in 91..96) getLetters(r)
        else mCh = randomInt.toChar()
        return mCh
    }

    private fun getLettersNumbers(r: Random): Char {
        when (val randomInt = r.nextInt(123 - 49) + 49) {
            in 91..96 -> getLettersNumbers(r)
            in 58..64 -> getLettersNumbers(r)
            else -> mCh = randomInt.toChar()
        }
        return mCh
    }

    override fun checkAnswer(ans: String): Boolean {
        return ans == answer
    }
}