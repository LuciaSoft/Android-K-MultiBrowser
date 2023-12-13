package com.luciasoft.browser.multibrowser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.luciasoft.browser.R;

public class MyRecyclerView extends RecyclerView
{
    public MyRecyclerView(@NonNull Context context)
    {
        super(context);

        Initialize();
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        Initialize();
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        Initialize();
    }

    private final TextPaint textPaint = new TextPaint();

    private String text = null;

    MultiBrowserActivity act;

    private void Initialize()
    {
        textPaint.setColor(getResources().getColor(R.color.colorListItemText));
        textPaint.setTextSize(20 * getResources().getDisplayMetrics().density);
        textPaint.setAntiAlias(true);
    }

    void setText(MultiBrowserActivity act, String text)
    {
        this.act = act;
        this.text = text;
    }

    void clearText()
    {
        this.text = null;
    }

    @Override
    public void onDraw(Canvas c)
    {
        if (text != null)
        {
            drawCenter(c, textPaint, text);

            return;
        }

        super.onDraw(c);
    }

    private void drawCenter(Canvas canvas, TextPaint paint, String text)
    {
        if (text.isEmpty()) return;

        Typeface font = act.THM().getFontBdIt(act.getAssets());
        textPaint.setTypeface(font);

        String[] texts = text.split("\n");

        float textHeight = paint.getTextSize();
        float totalTextHeight = textHeight * texts.length + textHeight * 0.4f * (texts.length - 1);
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cWidth = r.width();
        int cHeight = r.height();

        paint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < texts.length; i++)
        {
            String t = texts[i];
            paint.getTextBounds(t, 0, t.length(), r);
            float x = (cWidth / 2f - r.width() / 2f - r.left);
            float y = (cHeight / 2f + r.height() / 2f - r.bottom) - (totalTextHeight / 2f) + i * textHeight * 1.4f;
            canvas.drawText(t, x, y, paint);
        }

    }
}
