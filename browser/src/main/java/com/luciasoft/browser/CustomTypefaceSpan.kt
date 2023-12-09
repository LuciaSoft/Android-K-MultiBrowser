package com.luciasoft.browser

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.TextPaint
import android.text.style.TypefaceSpan
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.P)
class CustomTypefaceSpan(family: String, private val type: Typeface) : TypefaceSpan(type)
{
    override fun updateDrawState(ds: TextPaint)
    {
        applyCustomTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint)
    {
        applyCustomTypeface(paint)
    }

    private fun applyCustomTypeface(paint: Paint)
    {
        val old = paint.typeface;
        val oldStyle = old.style

        val fake = oldStyle and type.style.inv();

        if ((fake and Typeface.BOLD) != 0)
        {
            paint.isFakeBoldText = true;
        }

        if ((fake and Typeface.ITALIC) != 0)
        {
            paint.textSkewX = -0.25f;
        }

        paint.typeface = type;
    }
}