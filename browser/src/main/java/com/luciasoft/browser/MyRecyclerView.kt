package com.luciasoft.browser

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.luciasoft.browser.MultiBrowserActivity

class MyRecyclerView : RecyclerView
{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val textPaint = TextPaint()

    var text: String? = null

    val act: MultiBrowserActivity = context as MultiBrowserActivity

    init
    {
        textPaint.setColor(resources.getColor(R.color.colorListItemText))
        textPaint.setTextSize(20 * resources.displayMetrics.density)
        textPaint.setAntiAlias(true)
    }

    fun clearText()
    {
        text = null
    }

    override fun onDraw(c: Canvas)
    {
        if (text != null)
        {
            drawCenter(c, textPaint, text!!);

            return;
        }

        super.onDraw(c);
    }

    private fun drawCenter(canvas: Canvas, paint: TextPaint, text: String)
    {
        if (text.isEmpty()) return;

        val font = act.THM.getFontBdIt(act.assets);
        textPaint.typeface = font;

        val texts = text.split("\n");

        val textHeight = paint.textSize;
        val totalTextHeight = textHeight * texts.size + textHeight * 0.4f * (texts.size - 1);
        val r = Rect();
        canvas.getClipBounds(r);
        val cWidth = r.width();
        val cHeight = r.height();

        paint.setTextAlign(Paint.Align.LEFT);

        for (i in 0 until texts.size)
        {
            val t = texts[i];
            paint.getTextBounds(t, 0, t.length, r);
            val x = (cWidth / 2f - r.width() / 2f - r.left);
            val y = (cHeight / 2f + r.height() / 2f - r.bottom) - (totalTextHeight / 2f) + i * textHeight * 1.4f;
            canvas.drawText(t, x, y, paint);
        }

    }
}

