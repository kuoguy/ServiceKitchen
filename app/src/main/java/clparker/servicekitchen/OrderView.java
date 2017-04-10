package clparker.servicekitchen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Clown on 16/01/2017.
 */

public class OrderView extends View {

    Paint mTextPaint, mSquarePaint;
    float mTextHeight;
    int mTextColor=0xff101010;
    //boolean mShowText;
    int mTextPos;
    Order mData;


    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OrderView, 0, 0);

        try {
            //mShowText = a.getBoolean(R.styleable.OrderView_showText, false);
            //mTextPos = a.getInteger(R.styleable.OrderView_labelPosition, 0);
        } finally {
            //a.recycle();
        }

        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        mSquarePaint = new Paint(0);
        mSquarePaint.setColor(0xff101010);

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(50, 50, 50, 50, mSquarePaint);
        canvas.drawText(mData.getId(), 25, 25, mTextPaint);
    }

}
