package io.vov.vitamio.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by cuiran
 * Time  16/12/6 22:13
 * Email cuiran2001@163.com
 * Description
 */

public class MainActivity extends Activity {

    Handler handler=new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this,"正在显示..",Toast.LENGTH_SHORT).show();
        handler.sendEmptyMessageDelayed(1,5000);
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            change();
        }
    }

    private void change(){
        Intent intent=new Intent(MainActivity.this,MediaPlayerDemo_Video.class);
        startActivity(intent);
        finish();
    }
}
