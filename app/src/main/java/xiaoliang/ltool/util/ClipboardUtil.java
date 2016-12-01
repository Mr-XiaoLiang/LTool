package xiaoliang.ltool.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by liuj on 2016/12/1.
 * 剪切板工具
 */

public class ClipboardUtil {

    private ClipboardManager clipboard;
    private ContentResolver contentResolver;

    public ClipboardUtil(Context context) {
        clipboard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        contentResolver = context.getContentResolver();
    }

    public void putText(String s){
        clipboard.setPrimaryClip(ClipData.newPlainText("text", s));
    }

    public void putUri(Uri u){
        clipboard.setPrimaryClip(ClipData.newUri(contentResolver,"uri",u));
    }

    public void putIntent(Intent intent){
        clipboard.setPrimaryClip(ClipData.newIntent("intent",intent));
    }

    public Uri getUri(){
        ClipData.Item item;
        //无数据时直接返回
        if(!clipboard.hasPrimaryClip()){
            return null;
        }
        if (clipboard.getPrimaryClipDescription().hasMimeType(
                ClipDescription.MIMETYPE_TEXT_URILIST)) {
            ClipData cdUri = clipboard.getPrimaryClip();
            for(int i = 0;i<cdUri.getItemCount();i++){
                item = cdUri.getItemAt(i);
                Uri uri = item.getUri();
                if(uri!=null){
                    return uri;
                }
            }
        }
        return null;
    }

    public Intent getIntent(){
        ClipData.Item item;
        //无数据时直接返回
        if(!clipboard.hasPrimaryClip()){
            return null;
        }
        if (clipboard.getPrimaryClipDescription().hasMimeType(
                ClipDescription.MIMETYPE_TEXT_INTENT)) {
            ClipData cdIntent = clipboard.getPrimaryClip();
            for(int i = 0;i<cdIntent.getItemCount();i++){
                item = cdIntent.getItemAt(i);
                Intent intent = item.getIntent();
                if(intent != null){
                    return intent;
                }
            }
        }
        return null;
    }

    public String getString(){
        ClipData.Item item;
        //无数据时直接返回
        if(!clipboard.hasPrimaryClip()){
            return "";
        }
        if (clipboard.getPrimaryClipDescription().hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            for(int i = 0;i<cdText.getItemCount();i++){
                item = cdText.getItemAt(i);
                CharSequence text =  item.getText();
                if(text != null){
                    return text.toString();
                }
            }
        }
        return "";
    }

}
