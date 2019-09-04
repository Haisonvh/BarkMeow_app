package fit5046.test.touchme;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.widget.TextView;

public class PaintView extends View {
    private Path myArc;
    private Paint mPaintText;
    private String text;
    /*public PaintView() {
        super(context);
        //create Path object
        myArc = new Path();
        //create RectF Object
        RectF oval = new RectF(50,100,200,250);

        myArc.addArc(oval, 180, 0);
        //create paint object
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        //set style
        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);


    }*/

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myArc = new Path();
        //create RectF Object
        RectF oval = new RectF(50,100,200,250);

        myArc.addArc(oval, 180, 0);
        //create paint object
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        //set style
        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);

        int cout = attrs.getAttributeCount();
        String temp ="";
        for (int i=0;i<cout;i++){
            temp = attrs.getAttributeName(i);
        }

    }

    public void setText(String text){
        this.text = text;
    }

    public void setColor(int color){
        mPaintText.setColor(color);
    }

    public void setTextSize(float textSize){
        mPaintText.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw Text on Canvas
        canvas.drawTextOnPath(text, myArc, 0, 20, mPaintText);
        invalidate();
    }
}
