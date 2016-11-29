package xiaoliang.ltool.activity.lock;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by liuj on 2016/11/29.
 * 锁屏用的广播
 */

public class LockBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        policyManager.lockNow();
        Log.d("LockBroadcastReceiver","LockBroadcastReceiver");
    }
}
