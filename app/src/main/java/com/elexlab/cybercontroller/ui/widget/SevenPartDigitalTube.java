package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SevenPartDigitalTube extends View {
    private int value = 8;
    private Context mContext;

    public SevenPartDigitalTube(Context context) {
        super(context);
        mContext = context;
    }

    public SevenPartDigitalTube(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SevenPartDigitalTube(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }



    public void setValue(int value){
        this.value = value;
        invalidate();
    }

    public int getValue(){
        return this.value;
    }




    Paint p;

    private int rectangleWidth = 20;
    private int rectangleLength = 80;
    @Override
    protected void onDraw(Canvas canvas) {

        rectangleWidth = getWidth()/6;
        rectangleLength = getWidth()*4/6;


        Log.d("width","width:"+getWidth());
        Log.d("height","height:"+getHeight());
        p = new Paint();
        //p.setColor(0xff00a5fc);
        //p.setColor(0xff00daff);
        p.setColor(0xffffffff);
        p.setStyle(Paint.Style.FILL);//设置填满


        switch (value){
            case 0:{
                drawTopLine(canvas);
                drawLeftTopLine(canvas);
                drawRightTopLine(canvas);
                drawLeftButtomLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 1:{
                drawRightButtomLine(canvas);
                drawRightTopLine(canvas);
            }
            break;
            case 2:{
                drawTopLine(canvas);
                drawRightTopLine(canvas);
                drawMiddleLine(canvas);
                drawLeftButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 3:{
                drawTopLine(canvas);
                drawRightTopLine(canvas);
                drawMiddleLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 4:{
                drawLeftTopLine(canvas);
                drawRightTopLine(canvas);
                drawMiddleLine(canvas);
                drawRightButtomLine(canvas);
            }
            break;
            case 5:{
                drawTopLine(canvas);
                drawLeftTopLine(canvas);
                drawMiddleLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 6:{
                drawTopLine(canvas);
                drawLeftTopLine(canvas);
                drawMiddleLine(canvas);
                drawLeftButtomLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 7:{
                drawTopLine(canvas);
                drawRightTopLine(canvas);
                drawRightButtomLine(canvas);
            }
            break;
            case 8:{
                drawTopLine(canvas);
                drawLeftTopLine(canvas);
                drawRightTopLine(canvas);
                drawMiddleLine(canvas);
                drawLeftButtomLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            case 9:{
                drawTopLine(canvas);
                drawLeftTopLine(canvas);
                drawRightTopLine(canvas);
                drawMiddleLine(canvas);
                drawRightButtomLine(canvas);
                drawButtomLine(canvas);
            }
            break;
            default:break;

        }

    }


    private void drawTopLine(Canvas canvas){

//        Paint p = new Paint();
//        p.setColor(Color.BLUE);
//        p.setStyle(Paint.Style.FILL);//设置填满

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(0, 0);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleWidth, 0);
        pathLeftAngle.lineTo(rectangleWidth, rectangleWidth);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);


        canvas.drawRect(rectangleWidth, 0, rectangleWidth+rectangleLength, rectangleWidth, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(rectangleWidth+rectangleLength, 0);// 此点为多边形的起点
        pathRightAngle.lineTo(2*rectangleWidth+rectangleLength, 0);
        pathRightAngle.lineTo(rectangleWidth+rectangleLength, rectangleWidth);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);
    }



    public void drawLeftTopLine(Canvas canvas){
//        Paint p = new Paint();
//        p.setColor(Color.BLUE);
//        p.setStyle(Paint.Style.FILL);//设置填满

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(0, 5);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleWidth,rectangleWidth+5);
        pathLeftAngle.lineTo(0, rectangleWidth+5);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);


        canvas.drawRect(0, rectangleWidth+5, rectangleWidth, rectangleWidth+rectangleLength+5, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(0, rectangleWidth+rectangleLength+5);// 此点为多边形的起点
        pathRightAngle.lineTo(rectangleWidth, rectangleWidth+rectangleLength+5);
        pathRightAngle.lineTo(rectangleWidth/2, rectangleWidth+rectangleLength+rectangleWidth/2+5);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);

    }

    public void drawRightTopLine(Canvas canvas){

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(2*rectangleWidth+rectangleLength, 5);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleWidth+rectangleLength,rectangleWidth+5);
        pathLeftAngle.lineTo(2*rectangleWidth+rectangleLength, rectangleWidth+5);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);

        canvas.drawRect(rectangleWidth+rectangleLength, rectangleWidth+5, 2*rectangleWidth+rectangleLength, rectangleWidth+rectangleLength+5, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(rectangleWidth+rectangleLength, rectangleWidth+rectangleLength+5);// 此点为多边形的起点
        pathRightAngle.lineTo(2 * rectangleWidth + rectangleLength, rectangleWidth + rectangleLength + 5);
        pathRightAngle.lineTo(rectangleWidth+rectangleLength+rectangleWidth/2, rectangleWidth+rectangleLength+rectangleWidth/2+5);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);

    }

    public void drawMiddleLine(Canvas canvas){

        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(rectangleWidth + 3, rectangleWidth + rectangleLength + 5 + 3);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleWidth/2+3,rectangleWidth+rectangleLength+rectangleWidth/2+5+3);
        pathLeftAngle.lineTo(rectangleWidth + 3, 2 * rectangleWidth + rectangleLength + 5 + 3);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);

        canvas.drawRect(rectangleWidth+3, rectangleWidth+rectangleLength+5+3, rectangleWidth+rectangleLength-3, 2*rectangleWidth+rectangleLength+5+3, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(rectangleWidth+rectangleLength-3, rectangleWidth + rectangleLength + 5 + 3);// 此点为多边形的起点
        pathRightAngle.lineTo(rectangleWidth+rectangleLength+rectangleWidth/2-3, rectangleWidth+rectangleLength+rectangleWidth/2+5+3);
        pathRightAngle.lineTo(rectangleWidth+rectangleLength-3, 2*rectangleWidth+rectangleLength+5+3);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);

    }


    private void drawLeftButtomLine(Canvas canvas){
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(rectangleWidth/2,5+5+rectangleWidth+rectangleWidth/2+rectangleLength);// 此点为多边形的起点
        pathLeftAngle.lineTo(0,5+5+rectangleWidth*2+rectangleLength);
        pathLeftAngle.lineTo(rectangleWidth, 5+5+rectangleWidth*2+rectangleLength);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);

        canvas.drawRect(0, 5+5+rectangleWidth*2+rectangleLength,rectangleWidth, rectangleLength+5+5+rectangleWidth*2+rectangleLength, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(0, rectangleLength+(5+5+rectangleWidth*2+rectangleLength));// 此点为多边形的起点
        pathRightAngle.lineTo(rectangleWidth, rectangleLength+(5+5+rectangleWidth*2+rectangleLength));
        pathRightAngle.lineTo(0, rectangleLength+(5+5+rectangleWidth*2+rectangleLength)+rectangleWidth);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);
    }

    private void drawRightButtomLine(Canvas canvas){
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(rectangleWidth/2+rectangleLength+rectangleWidth,5+5+rectangleWidth+rectangleWidth/2+rectangleLength);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleLength+rectangleWidth,5+5+rectangleWidth*2+rectangleLength);
        pathLeftAngle.lineTo(rectangleLength+2*rectangleWidth, 5+5+rectangleWidth*2+rectangleLength);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);

        canvas.drawRect(rectangleLength+rectangleWidth, 5+5+rectangleWidth*2+rectangleLength,rectangleLength+2*rectangleWidth, rectangleLength+(5+5+rectangleWidth*2+rectangleLength), p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(rectangleLength+rectangleWidth, rectangleLength+(5+5+rectangleWidth*2+rectangleLength));// 此点为多边形的起点
        pathRightAngle.lineTo(rectangleLength+2*rectangleWidth, rectangleLength+(5+5+rectangleWidth*2+rectangleLength));
        pathRightAngle.lineTo(rectangleLength+2*rectangleWidth, rectangleLength+(5+5+rectangleWidth*2+rectangleLength)+rectangleWidth);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);
    }

    private void drawButtomLine(Canvas canvas){

//        Paint p = new Paint();
//        p.setColor(Color.BLUE);
//        p.setStyle(Paint.Style.FILL);//设置填满

        int buttomY = 5+rectangleWidth*2+rectangleLength*2+rectangleWidth+5+3;//233
        // 绘制这个三角形,你可以绘制任意多边形
        Path pathLeftAngle = new Path();
        pathLeftAngle.moveTo(3, buttomY);// 此点为多边形的起点
        pathLeftAngle.lineTo(rectangleWidth+3, buttomY);
        pathLeftAngle.lineTo(rectangleWidth+3, buttomY-rectangleWidth);
        pathLeftAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathLeftAngle, p);


        canvas.drawRect(rectangleWidth+3,  buttomY-rectangleWidth, rectangleLength+rectangleWidth-3, buttomY, p);// 长方形

        // 绘制这个三角形,你可以绘制任意多边形
        Path pathRightAngle = new Path();
        pathRightAngle.moveTo(rectangleLength+rectangleWidth-3,buttomY-rectangleWidth);// 此点为多边形的起点
        pathRightAngle.lineTo(rectangleLength+rectangleWidth-3, buttomY);
        pathRightAngle.lineTo(rectangleLength+rectangleWidth*2-3, buttomY);
        pathRightAngle.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathRightAngle, p);
    }
}
