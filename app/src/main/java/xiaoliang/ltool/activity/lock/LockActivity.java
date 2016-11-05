package xiaoliang.ltool.activity.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import xiaoliang.ltool.R;

/**
 * Created by Liuj on 2016/10/13.
 * 锁屏用的页面
 */

public class LockActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_lock);
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        policyManager.lockNow();
        finish();
    }
}
