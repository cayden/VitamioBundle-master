package io.vov.vitamio.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import io.vov.vitamio.utils.Log;

/**
 * Created by cuiran
 * Time  16/12/6 22:11
 * Email cuiran2001@163.com
 * Description
 */

public class VitamioApplication extends Application {
    private static final String TAG="VitamioApplication";
    private static final String MEDIA_PNAME="io.vov.vitamio.demo:remote";
    private static VitamioApplication instance=null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        instance=this;

    }


    public static VitamioApplication getInstance() {
        return instance;
    }


    public  void killPid(){

        int pid=getProcessPid(this,MEDIA_PNAME);
        Log.d(TAG,"killPid pid="+pid);

        android.os.Process.killProcess(pid);
        change();
    }
    private  void change(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private String getCurProcessName (Context context) {
        int     pid = android.os.Process.myPid ();
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses())  {
            if(appProcess.pid == pid){
                return appProcess.processName;
            }
        }
        return null ;
    }

    public  int getProcessPid(Context paramContext, String paramString){
        ActivityManager mActivityManager = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses())  {
            if(appProcess.processName == paramString){
                return appProcess.pid;
            }
        }
        return -1 ;
    }

}
