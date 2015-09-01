package com.games.jefferson.collectingnumbers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * The service provider which provides edit text with lines between each input line
 * Created by jeff on 8/7/2015.
 */
public class LinedEditText extends EditText {
    int drawnLines;
    InputFilter filter;
    boolean falseText;


    public LinedEditText(Context c){
        super(c);
        drawnLines = 24;
        falseText = false;
    }

    public LinedEditText(Context c, AttributeSet as){
        super(c, as);
        drawnLines = 24;
        falseText = false;
    }

    public void setDrawnLines (int drawnLines){
        this.drawnLines = drawnLines;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int lines = drawnLines;
        Paint linePainter = new Paint();
        linePainter.setStyle(Paint.Style.STROKE);
        linePainter.setColor(0xFFFFFFFF);
        int yLine = 0;
        if(getLineCount() >= 0){
            yLine = getLineBounds(0, null);
        }
        for(int i = 0; i<lines; i++){
            canvas.drawLine(getX(), yLine+(i*getLineHeight()), getX()+getWidth(),
                    yLine+(i*getLineHeight()), linePainter);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent e){
        //super.onKeyUp(keyCode, e);
        return true;
    }


    private int getCurrentCursorLine()
    {
//        int selectionStart = Selection.getSelectionStart(getText());
//        Layout layout = getLayout();
//
//        if (!(selectionStart == -1)) {
//            return layout.getLineForOffset(selectionStart);
//        }
//
//        return -1;
        return getLineCount();
    }
}
