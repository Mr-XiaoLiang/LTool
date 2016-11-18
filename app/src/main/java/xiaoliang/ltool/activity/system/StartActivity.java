package xiaoliang.ltool.activity.system;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.MainActivity;
import xiaoliang.ltool.activity.meizhi.MeizhiActivity;
import xiaoliang.ltool.activity.note.NoteAddActivity;
import xiaoliang.ltool.activity.qr.QRCreateActivity;
import xiaoliang.ltool.activity.qr.QRReadActivity;
import xiaoliang.ltool.activity.webcode.WebCodeActivity;
import xiaoliang.ltool.util.HttpUtil;

public class StartActivity extends AppCompatActivity {

    private boolean init = false;
    private boolean show = false;
    private int shortName[] = {R.string.shortcuts_note_short,R.string.shortcuts_qc_short,R.string.shortcuts_qr_short,R.string.shortcuts_web_short,R.string.shortcuts_mei_short};
    private int longName[] = {R.string.shortcuts_note_long,R.string.shortcuts_qc_long,R.string.shortcuts_qr_long,R.string.shortcuts_web_long,R.string.shortcuts_mei_long};
    private Class[] className = {NoteAddActivity.class,QRCreateActivity.class,QRReadActivity.class,WebCodeActivity.class,MeizhiActivity.class};
    private String[] id = {"note_add","qr_create","qr_read","web","meizhi"};
    private int[] icon = {R.drawable.ic_shortcuts_note,R.drawable.ic_shortcuts_qc,R.drawable.ic_shortcuts_qr,R.drawable.ic_shortcuts_web,R.drawable.ic_shortcuts_mei};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        HttpUtil.getThread(new Start());
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            init();
        }else{
            init = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void init(){
//        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
//        List<ShortcutInfo> infos = new ArrayList<>();
//        int max = shortName.length;
//        if(shortcutManager.getMaxShortcutCountPerActivity()<max)
//            max = shortcutManager.getMaxShortcutCountPerActivity();
//        max = 4;
//        for (int i = 0; i < max; i++) {
//            Intent intent = new Intent(this, className[i]);
//            intent.setAction(Intent.ACTION_VIEW);
//            ShortcutInfo info = new ShortcutInfo.Builder(this, id[i])
//                    .setShortLabel(getString(shortName[i]))
//                    .setLongLabel(getString(longName[i]))
//                    .setIcon(Icon.createWithResource(this, icon[i]))
//                    .setIntent(intent)
//                    .build();
//            infos.add(info);
////            manager.addDynamicShortcuts(Arrays.asList(info));
//        }
//        shortcutManager.setDynamicShortcuts(infos);
        init = true;
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
                    show = true;
                    if(init){
                        startActivity(new Intent(StartActivity.this,MainActivity.class));
                        finish();
                    }
                    break;
                case 201:
                    init = true;
                    if(show){
                        startActivity(new Intent(StartActivity.this,MainActivity.class));
                        finish();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
