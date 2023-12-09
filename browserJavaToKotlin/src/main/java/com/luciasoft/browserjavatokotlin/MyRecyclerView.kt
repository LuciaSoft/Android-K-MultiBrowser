package com.luciasoft.browserjavatokotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView : RecyclerView
{
    constructor(context: Context) : super(context)
    {
        Initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    {
        Initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    {
        Initialize()
    }

    private val textPaint = TextPaint()
    private var text: String? = null
    var act: MultiBrowserActivity? = null
    private fun Initialize()
    {
        textPaint.color = resources.getColor(R.color.colorListItemText)
        textPaint.textSize = 20 * resources.displayMetrics.density
        textPaint.isAntiAlias = true
    }

    fun setText(act: MultiBrowserActivity?, text: String?)
    {
        this.act = act
        this.text = text
    }

    fun clearText()
    {
        text = null
    }

    override fun onDraw(c: Canvas)
    {
        if (text != null)
        {
            drawCenter(c, textPaint, text!!)
            return
        }
        super.onDraw(c)
    }

    private fun drawCenter(canvas: Canvas, paint: TextPaint, text: String)
    {
        if (text.isEmpty()) return
        val font = act!!.THM.getFontBdIt(act!!.assets)
        textPaint.typeface = font
        val texts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val textHeight = paint.textSize
        val totalTextHeight = textHeight * texts.size + textHeight * 0.4f * (texts.size - 1)
        val r = Rect()
        canvas.getClipBounds(r)
        val cWidth = r.width()
        val cHeight = r.height()
        paint.textAlign = Paint.Align.LEFT
        for (i in texts.indices)
        {
            val t = texts[i]
            paint.getTextBounds(t, 0, t.length, r)
            val x = cWidth / 2f - r.width() / 2f - r.left
            val y =
                cHeight / 2f + r.height() / 2f - r.bottom - totalTextHeight / 2f + i * textHeight * 1.4f
            canvas.drawText(t, x, y, paint)
        }
    }
}