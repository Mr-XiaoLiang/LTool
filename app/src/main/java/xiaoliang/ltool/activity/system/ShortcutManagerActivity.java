package xiaoliang.ltool.activity.system;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.ShortcutBean;
import xiaoliang.ltool.listener.LItemTouchCallback;
import xiaoliang.ltool.listener.LItemTouchHelper;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.ToastUtil;

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
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_shortcut_manager_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.activity_shortcut_manager_recyclerview);
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
            bean.index = ind;
            shortcutBeens.add(bean);
        }
        ShortcutAdapter adapter = new ShortcutAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        LItemTouchHelper helper = LItemTouchHelper.newInstance(recyclerView,new LItemTouchCallback.DefaultOnItemTouchCallbackListener(shortcutBeens,adapter));
        adapter.setHelper(helper);
        adapter.notifyDataSetChanged();
    }

    private void setShortcutIndex(){
        String indexMenu = "";
        for(ShortcutBean bean : shortcutBeens){
            indexMenu += ",";
            indexMenu += bean.index;
        }
        indexMenu = indexMenu.substring(1);
        SharedPreferencesUtils.put(this,"shortcutIndex",indexMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shortcut_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_shortcut_manager_done:
                sendShortcut();
                setShortcutIndex();
                ToastUtil.T(this,"快捷菜单已更新");
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                    .setRank(i)
                    .build();
            infos.add(info);
        }
        shortcutManager.setDynamicShortcuts(infos);
//        shortcutManager.updateShortcuts(infos);
    }

    private class ShortcutAdapter extends RecyclerView.Adapter<ShortcutHolder>{

        private LayoutInflater inflater;
        private ItemTouchHelper helper;

        public ShortcutAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setHelper(ItemTouchHelper helper) {
            this.helper = helper;
        }

        @Override
        public ShortcutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShortcutHolder(inflater.inflate(R.layout.item_shortcut_manager,parent,false));
        }

        @Override
        public void onBindViewHolder(ShortcutHolder holder, int position) {
            holder.onBind(shortcutBeens.get(position),helper);
        }

        @Override
        public int getItemCount() {
            if(shortcutBeens==null)
                return 0;
            return shortcutBeens.size();
        }
    }

    private class ShortcutHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

        private ItemTouchHelper helper;
        private ImageView icon;
        private TextView shortName;
        private TextView longName;
        private View move;

        public ShortcutHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_shortcut_manager_img);
            shortName = (TextView) itemView.findViewById(R.id.item_shortcut_manager_name);
            longName = (TextView) itemView.findViewById(R.id.item_shortcut_manager_msg);
            move = itemView.findViewById(R.id.item_shortcut_manager_move);
            move.setOnTouchListener(this);
        }
        public void onBind(ShortcutBean bean,ItemTouchHelper helper){
            icon.setImageResource(bean.imgId);
            shortName.setText(bean.shortName);
            longName.setText(bean.longName);
            this.helper = helper;
        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(view == move)
                helper.startDrag(this);
            return false;
        }
    }
}
