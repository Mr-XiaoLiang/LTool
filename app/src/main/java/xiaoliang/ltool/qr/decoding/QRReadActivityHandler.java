/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xiaoliang.ltool.qr.decoding;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.QRReadActivity;
import xiaoliang.ltool.qr.camera.CameraManager;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 */
public final class QRReadActivityHandler extends Handler {

  private static final String TAG = QRReadActivityHandler.class.getSimpleName();

  private final QRReadActivity activity;
  private final DecodeThread decodeThread;
  private State state;

  private enum State {
    PREVIEW,
    SUCCESS,
    DONE
  }

  public QRReadActivityHandler(QRReadActivity activity, Vector<BarcodeFormat> decodeFormats,
                               String characterSet) {
    this.activity = activity;
    decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
        new ViewFinderResultPointCallback(activity.getQrFinderView()));
    decodeThread.start();
    start();
  }

  public void start(){
    Log.d("QRReadActivityHandler","onStart------------");
    state = State.SUCCESS;
    // Start ourselves capturing previews and decoding.
    CameraManager.get().startPreview();
    restartPreviewAndDecode();
  }

  public void stop(){
    Log.d("QRReadActivityHandler","onStop------------");
    state = State.DONE;
    CameraManager.get().stopPreview();
    //移除所有队列中的消息
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  @Override
  public void handleMessage(Message message) {
    switch (message.what) {
      case R.id.auto_focus:
        Log.d(TAG, "decode failed");
        if (state == State.PREVIEW) {
          CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
        break;
      case R.id.restart_preview:
        Log.d(TAG, "Got restart preview message");
        restartPreviewAndDecode();
        break;
      case R.id.decode_succeeded:
        Log.d(TAG, "Got decode succeeded message");
        state = State.SUCCESS;
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null :
            (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
        activity.handleDecode((Result) message.obj, barcode);
        break;
      case R.id.decode_failed:
        Log.d(TAG, "decode failed");
        // We're decoding as fast as possible, so when one decode fails, start another.
        preview();
        break;
      case R.id.return_scan_result:
        Log.d(TAG, "Got return scan result message");
        activity.onScanEnd(Activity.RESULT_OK, (Intent) message.obj);
        break;
      case R.id.launch_product_query:
        Log.d(TAG, "Got product query message");
        String url = (String) message.obj;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        break;
      case R.id.encode_img_failed:
        activity.decodeImgFailed();
        break;
    }
  }

  public void preview(){
    state = State.PREVIEW;
    CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
  }

  public void quitSynchronously() {
    Log.d("QRReadActivityHandler","onQuit------------");
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      decodeThread.join();
    } catch (InterruptedException e) {
      // continue
    }
    // Be absolutely sure we don't send any queued up messages
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  private void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
      CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
      activity.drawViewfinder();
    }
  }

  public void decodeImage(Bitmap bitmap){
    stop();
    decodeThread.getHandler().obtainMessage(R.id.decode_photo, bitmap);
  }

}
