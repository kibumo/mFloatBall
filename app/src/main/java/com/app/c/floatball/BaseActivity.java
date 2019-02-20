package com.app.c.floatball;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

//在AS中创建项目的时候，自动继承的是AppCompatActivity
public class BaseActivity extends AppCompatActivity{

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        int sdkInt=Build.VERSION.SDK_INT;

////          权限判断
//        if (sdkInt>= 23) {
//            if(!Settings.canDrawOverlays(getApplicationContext())) {
//                //启动Activity让用户授权
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                startActivity(intent);
//                return;
//            } else {
//                //执行6.0以上绘制代码
//            }
//        } else {
//            //执行6.0以下绘制代码
//        }

        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
//        layoutParams.type=sdkInt>=Build.VERSION_CODES.O?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        if (sdkInt >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }

//        判断sdk版本是否大于4.4
        if(sdkInt>=Build.VERSION_CODES.KITKAT){
//            透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(layoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
            getWindow().addFlags(layoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}