package xiaoliang.ltool.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Liuj on 2016/10/13.
 * 锁屏用的页面
 */

public class LockActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        policyManager.lockNow();
        finish();
    }
}
