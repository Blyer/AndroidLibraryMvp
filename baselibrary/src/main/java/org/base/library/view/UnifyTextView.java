package org.base.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 两端对齐的textview，可以设置最后一行靠左，靠右，居中对齐
 */
public class UnifyTextView extends TextView {
    private float textHeight; // 单行文字高度
    private int width; // textview宽度
    private List<String> lines = new ArrayList<String>(); // 分割后的行
    private List<Integer> tailLines = new ArrayList<Integer>(); // 尾行
    private Align align = Align.ALIGN_LEFT; // 默认最后一行左对齐
    private boolean firstCalc = true;  // 初始化计算

    private long mLastClickTime = 0;

    // 尾行对齐方式
    public enum Align {
        ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT  // 居中，居左，居右,针对段落最后一行
    }

    public UnifyTextView(Context context) {
        super(context);
    }

    public UnifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //首先进行高度调整
        if (firstCalc) {
            width = getMeasuredWidth();
            String text = getText().toString();
            TextPaint paint = getPaint();
            lines.clear();
            tailLines.clear();

            // 文本含有换行符时，分割单独处理
            String[] items = text.split("\\n");
            for (String item : items) {
                calc(paint, item);
            }
            //计算实际高度,加上多出的行的高度(一般是减少)
            int realHeight = getHeight() + getLineHeight() * (lines.size() - getLineCount());
            this.setHeight(realHeight);

            firstCalc = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();

        Paint.FontMetrics fm = paint.getFontMetrics();
        // 计算行高
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }

        width = getMeasuredWidth();
        textHeight = fm.descent - fm.ascent;

        textHeight = textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd();

        float firstHeight = getTextSize();

        int gravity = getGravity();
        if ((gravity & 0x1000) == 0) { // 是否垂直居中
            firstHeight = firstHeight + (textHeight - firstHeight) / 2;
        }

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        width = width - paddingLeft - paddingRight;

        for (int i = 0; i < lines.size(); i++) {
            float drawY = i * textHeight + firstHeight;
            String line = lines.get(i);
            // 绘画起始x坐标
            float drawSpacingX = paddingLeft;
            float gap = (width - paint.measureText(line));
            float interval = gap / (line.length() - 1);

            // 绘制最后一行
            if (tailLines.contains(i)) {
                interval = 0;
                if (align == Align.ALIGN_CENTER) drawSpacingX += gap / 2;
                else if (align == Align.ALIGN_RIGHT) drawSpacingX += gap;
            }

            for (int j = 0; j < line.length(); j++) {
                float drawX = paint.measureText(line.substring(0, j)) + interval * j;
                canvas.drawText(line.substring(j, j + 1), drawX + drawSpacingX, drawY, paint);
            }

        }
    }

    /**
     * 设置尾行对齐方式
     */
    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }

    /**
     * 计算每行应显示的文本数
     *
     * @param text 要计算的文本
     */
    private void calc(Paint paint, String text) {
        if (text.length() == 0) {
            lines.add("\n");
            return;
        }
        StringBuffer sb = new StringBuffer("");
        int startPosition = 0; // 起始位置
        for (int i = 0; i < text.length(); i++) {
            if (paint.measureText(text.substring(startPosition, i + 1)) > width) {
                startPosition = i;
                lines.add(sb.toString());

                sb = new StringBuffer();
            }
            sb.append(text.charAt(i));
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }

        tailLines.add(lines.size() - 1);
    }

    @Override
    public boolean performClick() {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - mLastClickTime > 500) {
            mLastClickTime = currentClickTime;
            return super.performClick();
        } else {
            return true;
        }
    }
}