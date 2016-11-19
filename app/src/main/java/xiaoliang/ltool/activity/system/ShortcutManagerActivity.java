package xiaoliang.ltool.activity.system;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.ShortcutBean;
import xiaoliang.ltool.util.SharedPreferencesUtils;

/**
 * 快捷方式排序管理
 */
public class ShortcutManagerActivity extends AppCompatActivity {
    //锁屏，二维码识别，二维码生成，记事本添加，妹子图，网页解码,天气
    private int[] shortName = {R.string.shortcuts_lock_short,R.string.shortcuts_qr_short,R.string.shortcuts_qc_short,R.string.shortcuts_note_short,R.string.shortcuts_mei_short,R.string.shortcuts_web_short,R.string.shortcuts_weather_short};
    private int[] longName = {R.string.shortcuts_lock_long,R.string.shortcuts_qr_long,R.string.shortcuts_qc_long,R.string.shortcuts_note_long,R.string.shortcuts_mei_long,R.string.shortcuts_web_long,R.string.shortcuts_weather_long};
    private int[] icon = {R.drawable.ic_shortcuts_lock,R.drawable.ic_shortcuts_qr,R.drawable.ic_shortcuts_qc,R.drawable.ic_shortcuts_note,R.drawable.ic_shortcuts_mei,R.drawable.ic_shortcuts_web,R.drawable.ic_shortcuts_weather};
    private int[] action = {R.string.action_lock,R.string.action_qr_read,R.string.action_qr_create,R.string.action_note_add,R.string.action_meizhi,R.string.action_web_code,R.string.action_weather};
    private int[] id = {R.string.shortcuts_id_lock,R.string.shortcuts_id_qr,R.string.shortcuts_id_qc,R.string.shortcuts_id_note,R.string.shortcuts_id_mei,R.string.shortcuts_id_web,R.string.shortcuts_id_weather};

    private ArrayList<ShortcutBean> shortcutBeens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_shortcut_manager_toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init(){
        shortcutBeens = new ArrayList<>();
        String indexMenu = SharedPreferencesUtils.get(this,"shortcutIndex","0,1,2,3,4,5,6");
        String[] indexs = indexMenu.split(",");
        ShortcutBean bean;
        for(String index:indexs){
            bean = new ShortcutBean();
            int ind = Integer.parseInt(index);
            bean.putIntent(getString(action[ind]));
            bean.id = getString(id[ind]);
            bean.shortName = getString(shortName[ind]);
            bean.longName = getString(longName[ind]);
            bean.imgId = icon[ind];
            bean.failureName = getString(R.string.shortcuts_disabled_message);
            shortcutBeens.add(bean);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shortcut_manager, menu);
        return true;
    }
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void sendShortcut(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        List<ShortcutInfo> infos = new ArrayList<>();
        int max = shortcutBeens.size();
        if(shortcutManager.getMaxShortcutCountPerActivity()<max)
            max = shortcutManager.getMaxShortcutCountPerActivity();
        for (int i = 0; i < max; i++) {
            ShortcutInfo info = new ShortcutInfo.Builder(this, shortcutBeens.get(i).id)
                    .setShortLabel(shortcutBeens.get(i).shortName)
                    .setLongLabel(shortcutBeens.get(i).longName)
                    .setIcon(Icon.createWithResource(this, shortcutBeens.get(i).imgId))
                    .setIntent(shortcutBeens.get(i).intent)
                    .build();
            infos.add(info);
        }
        shortcutManager.setDynamicShortcuts(infos);
    }

}
