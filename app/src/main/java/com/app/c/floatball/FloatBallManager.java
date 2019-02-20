package com.app.c.floatball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class FloatBallManager {

    private static SmallFloatBall smallBall;
    private static BigFloatBall bigBall;
    private static LayoutParams smallBallParams;
    private static LayoutParams bigBallParams;

    private static WindowManager mBallManager;

    public static void createSmallBall(Context context){
        WindowManager windowManager = getWindowManager(context);
        DisplayMetrics dm=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        if(smallBall==null){
            smallBall=new SmallFloatBall(context);
            if(smallBallParams==null){
                smallBallParams=new WindowManager.LayoutParams();
                smallBallParams.type=WindowManager.LayoutParams.TYPE_PHONE;
                smallBallParams.format=PixelFormat.RGBA_8888;
                smallBallParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallBallParams.gravity=Gravity.LEFT|Gravity.TOP;

                smallBallParams.width = SmallFloatBall.viewWidth;
                smallBallParams.height = SmallFloatBall.viewHeight;
                smallBallParams.x = dm.widthPixels;
                smallBallParams.y = dm.heightPixels / 2 - smallBallParams.height / 2;
            }
            smallBall.setParams(smallBallParams);
            windowManager.addView(smallBall,smallBallParams);
        }
    }

    public static void removeSmallBall(Context context) {
        if (smallBall != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallBall);
            smallBall = null;
        }
    }

    public static void createBigBall(Context context) {
        WindowManager windowManager = getWindowManager(context);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        if (bigBall == null) {
            //这里必须先初始化大悬浮窗
            bigBall = new BigFloatBall(context);
        }
        //参数是变动的，所以每一次开启都必须更新
        bigBallParams = new LayoutParams();
        bigBallParams.type = LayoutParams.TYPE_PHONE;
        bigBallParams.format = PixelFormat.RGBA_8888;
        bigBallParams.gravity = Gravity.LEFT | Gravity.TOP;
        bigBallParams.width = BigFloatBall.viewWidth;
        bigBallParams.height = BigFloatBall.viewHeight;
        //如何不加这个，则会出现它一直霸占焦点，其他点击事件失效，切记
        bigBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //这里是根据小悬浮窗的位置来确定大悬浮窗的位置，当然我们这里强制聚焦，所以屏蔽掉
        bigBallParams.x = SmallFloatBall.smallParams.x + SmallFloatBall.viewWidth -
                bigBallParams.width;
        bigBallParams.y = SmallFloatBall.smallParams.y + SmallFloatBall.viewHeight / 2
                - bigBallParams.height / 2;
        windowManager.addView(bigBall, bigBallParams);
    }


    public static void removeBigBall(Context context) {
        if (bigBall != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigBall);
            bigBall = null;
        }
    }

    public static boolean isWindowShowing() {
        return smallBall != null || bigBall != null;
    }

    private static WindowManager getWindowManager(Context context) {
        if (mBallManager == null) {
            mBallManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mBallManager;
    }
}
