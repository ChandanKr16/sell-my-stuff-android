package com.chandankumar.sellmystuff.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;


public class SlantedLabel extends View {

        /**
         *Paintbrush
         */
        private Paint paint;
        /**
         * Path
         */
        private Path path;
        /**
         *View width
         */
        private float width;
        /**
         *View height
         */
        private float height;
        /**
         *Label background width
         */
        private float labelWidth;
        /**
         *Label fold area width
         */
        private float pointWidth;
        /**
         *Label fold area height
         */
        private float pointHeight;
        /**
         *Label background color
         */
        private int labelColor;
        /**
         *Label fold area background color
         */
        private int pointColor;
        /**
         *X coordinate of center point
         */
        private float centerX;
        /**
         *Y coordinate of center point
         */
        private float centerY;
        /**
         *Label text content
         */
        private String text;
        /**
         *Label text color
         */
        private int textColor;
        public SlantedLabel(Context context) {
            super(context);
        }
        public SlantedLabel(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }
        private void init() {
            labelWidth = 120;
            pointWidth = 10;
            pointHeight = 0;
            paint = new Paint();
            path = new Path();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(10);
            setBackgroundColor(Color.TRANSPARENT);
            labelColor = Color.parseColor("#EA6724");
            pointColor = Color.parseColor("#C43200");
            this.text = "";
            textColor = Color.WHITE;
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            width = w;
            height = h;
            centerX = w/2;
            centerY = h/2;
        }
        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            //The folded area (small triangle area) above the background of the label area
            path.reset();
            path.moveTo(width-pointWidth,0);
            path.lineTo(width,pointHeight);
            path.lineTo(width-pointWidth-26,pointHeight);
            path.close();
            paint.setColor(pointColor);
            canvas.drawPath(path,paint);
            //Draw the fold area under the label background area
            path.reset();
            path.moveTo(0,height-pointWidth);
            path.lineTo(pointHeight,height);
            path.lineTo(pointHeight,height-pointWidth-26);
            path.close();
            paint.setColor(pointColor);
            canvas.drawPath(path,paint);
            //Draw label background area
            path.reset();
            paint.setColor(labelColor);
            paint.setStyle(Paint.Style.FILL);
            path.moveTo(width-labelWidth-pointWidth,0);
            path.lineTo(width-pointWidth,0);
            path.lineTo(0,height-pointWidth);
            path.lineTo(0,height-labelWidth-pointWidth);
            canvas.drawPath(path,paint);
            //Select 45 degrees counter clockwise
            canvas.rotate(-45,centerX,centerY);
            //Abscissa of text center point
            float textX = width / 2;
            //Ordinate of text center point
            float textY = (height-pointWidth-(labelWidth / 2f)) / 2f;
            paint.setColor(textColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(38);
            //Center text
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text,textX,textY,paint);
        }

        public void setText(String text){
            this.text = text;
        }

        public String getText(){
            return this.text;
        }
}
