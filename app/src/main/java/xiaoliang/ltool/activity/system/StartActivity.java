package xiaoliang.ltool.activity.system;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.MainActivity;
import xiaoliang.ltool.util.HttpUtil;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        HttpUtil.getThread(new Start());
    }
    private class Start implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                handler.sendEmptyMessage(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    startActivity(new Intent(StartActivity.this,MainActivity.class));
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
