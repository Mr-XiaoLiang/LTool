package xiaoliang.ltool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import xiaoliang.ltool.R;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.SharedPreferencesUtils;

public class SettingActivity extends AppCompatActivity implements SwitchCompat.OnCheckedChangeListener {

    private SwitchCompat autoLocationSwitch,onlyWifiSwitch,dayBgSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_setting_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        autoLocationSwitch = (SwitchCompat) findViewById(R.id.activity_setting_autolocation);
        onlyWifiSwitch = (SwitchCompat) findViewById(R.id.activity_setting_onlywifi);
        dayBgSwitch = (SwitchCompat) findViewById(R.id.activity_setting_daybg);
        autoLocationSwitch.setOnCheckedChangeListener(this);
        onlyWifiSwitch.setOnCheckedChangeListener(this);
        dayBgSwitch.setOnCheckedChangeListener(this);

        autoLocationSwitch.setChecked(SharedPreferencesUtils.getAutoLocation(this));
        onlyWifiSwitch.setChecked(SharedPreferencesUtils.isOnlyWifi(this));
        dayBgSwitch.setChecked(SharedPreferencesUtils.isLoadWebImg(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.activity_setting_autolocation:
                SharedPreferencesUtils.setAutoLocation(this,b);
                break;
            case R.id.activity_setting_onlywifi:
                SharedPreferencesUtils.setIsOnlyWifi(this,b);
                if(b){
                    dayBgSwitch.setChecked(true);
                }
                break;
            case R.id.activity_setting_daybg:
                SharedPreferencesUtils.setIsLoadWebImg(this,b);
                if(!b){
                    onlyWifiSwitch.setChecked(b);
                }
                break;
        }
    }
}
