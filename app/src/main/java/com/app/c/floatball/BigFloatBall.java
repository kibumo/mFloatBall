package com.app.c.floatball;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BigFloatBall extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public BigFloatBall(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.big_float_ball, this);
        View view = findViewById(R.id.big_ball_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView close = (TextView) findViewById(R.id.close);
        TextView back = (TextView) findViewById(R.id.back);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                FloatBallManager.removeBigBall(context);
                FloatBallManager.removeSmallBall(context);
                Intent intent = new Intent(getContext(), FloatBallService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                FloatBallManager.removeBigBall(context);
                FloatBallManager.createSmallBall(context);
            }
        });
    }
}
