package com.app.c.floatball;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class FloatBallService extends Service {
//    用于在线程中创建或移除悬浮窗
    private Handler handler = new Handler();
//    定时器，定时进行检测当前应该创建还是移除悬浮窗
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        if(timer==null){
            timer=new Timer();
//            开启定时器，每隔0.5秒刷新一次
            timer.scheduleAtFixedRate(new RefreshTask(),0,500);
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
//        Service被终止的同时也停止定时器继续运行
        super.onDestroy();
        timer.cancel();
        timer=null;
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (isHome() && !FloatBallManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatBallManager.createSmallBall(getApplicationContext());
                    }
                });
            }
            // 当前界面不是桌面，且没有悬浮窗，开启小悬浮窗。
            else if (!isHome() && !FloatBallManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatBallManager.createSmallBall(getApplicationContext());

                    }
                });
            }
            // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
            else if (isHome() && FloatBallManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        FloatBallManager.updateUsedPercent();
                    }
                });
            }
        }

    }

    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

}
