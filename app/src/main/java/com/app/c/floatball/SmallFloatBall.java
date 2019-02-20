package com.app.c.floatball;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class SmallFloatBall extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private int mstatusBarHeight;
    private WindowManager mwindowManager;
    private WindowManager.LayoutParams mParams;
    public static WindowManager.LayoutParams smallParams;

    public SmallFloatBall(Context context){
        super(context);
        mwindowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater.from(context).inflate(R.layout.small_float_ball,this);
        View view =findViewById(R.id.small_ball_layout);
        viewWidth=view.getLayoutParams().width;
        viewHeight=view.getLayoutParams().height;
        TextView percentView=findViewById(R.id.percent);
        percentView.setText(getUsedPercentValue());

        DisplayMetrics dm = new DisplayMetrics();
        mwindowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
//        获取状态栏的高度
        mstatusBarHeight = getStatusBarHeight();
    }

//    分别记录手按在屏幕上相对屏幕的位置(dx,dy)，以及相对控件本身的位置(mx,my)
    float dx,dy,mx,my;
    float moveX,moveY;
//    用于up和move不冲突
    boolean isMove;
//    屏幕的宽度和高度
    int width,height;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                dx=event.getRawX();
                dy=event.getRawY();

                mx=event.getX();
                my=event.getY();
                isMove = false;
                return false;

            case MotionEvent.ACTION_MOVE:
//                计算手移动的距离
                int x=Math.abs((int)(event.getRawX()-dx));
                int y=Math.abs((int)(event.getRawY()-dy));
//                如果x和y距离都小于10，说明用户并没打算移动，只是手触摸时产生的move
                if (x < 5 || y < 5) {
                    isMove = false;
                    return false;
                } else {
                    isMove = true;
                }
//                计算控件移动的距离
                x = (int) (event.getRawX() - mx);
                y = (int) (event.getRawY() - my);
                mParams.x = x;
                mParams.y = y - mstatusBarHeight;
                //刷新
                mwindowManager.updateViewLayout(this, mParams);
                return true;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    smallParams = mParams;
                    mwindowManager.updateViewLayout(this, mParams);
                }else {
                    smallParams = mParams;
                    openBigBall();
                }
                return isMove;//false为down，true为move
            default:
                break;
        }
        return false;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
        smallParams = mParams;
    }

    private void openBigBall() {
        FloatBallManager.createBigBall(getContext());
        FloatBallManager.removeSmallBall(getContext());
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        if (statusBarHeight == 0) {
            try {
                //获取字节码文件对象
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                //实例化获得类对象
                Object o = c.newInstance();
                //得到相应成员变量
                Field field = c.getField("status_bar_height");
                //获得成员变量的值
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public String getUsedPercentValue(){
        return " ";
    }

}
