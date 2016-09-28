package xiaoliang.ltool.qr.decoding;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

import xiaoliang.ltool.view.QRFinderView;

/**
 * Created by liuj on 2016/9/27.
 * 扫描视图的回调
 */
public class ViewFinderResultPointCallback implements ResultPointCallback {


    private final QRFinderView qrFinderView;

    public ViewFinderResultPointCallback(QRFinderView qrFinderView) {
        this.qrFinderView = qrFinderView;
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        qrFinderView.addPossibleResultPoint(point);
    }
}
