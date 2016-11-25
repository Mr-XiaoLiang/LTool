package xiaoliang.ltool.listener;

import android.view.View;

/**
 * Created by liuj on 2016/11/14.
 * 记事本碎片页的回调监听器
 */

public interface OnNoteFragmentListener {
    void onNoteClick(int noteId);
    void OnScrollChangeListener(boolean isScroll);
}
