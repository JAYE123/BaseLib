package core.base.rxvolley;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.kymjs.rxvolley.client.HttpCallback;

import java.lang.ref.WeakReference;
import java.util.Map;

import core.base.application.ABApplication;
import core.base.log.L;
import core.base.log.T;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public class HttpCallBackDialog extends HttpCallback {

    private WeakReference<Dialog> dialog;
    private WeakReference<HttpResultListener> listenerWeakReference;
    private String flag[];

    public HttpCallBackDialog(Dialog dialog, HttpResultListener listener, String flag[]) {
        this.dialog = new WeakReference<>(dialog);
        this.listenerWeakReference = new WeakReference<>(listener);
        this.flag = flag;
    }

    @Override
    public void onPreStart() {
        super.onPreStart();
        if (dialog.get() != null) {//如果dialog还存在，则弹出
            dialog.get().show();
        }
    }

    @Override
    public void onPreHttp() {
        super.onPreHttp();
    }

    @Override
    public void onSuccessInAsync(byte[] t) {
        super.onSuccessInAsync(t);
    }

    @Override
    public void onSuccess(String t) {
        super.onSuccess(t);
        L.json("network-success", t);
        if (listenerWeakReference.get() != null) {//成功后返回1
            listenerWeakReference.get().onHttpResult(t, 1, flag, null);
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, byte[] t) {
        super.onSuccess(headers, t);
    }

    @Override
    public void onFailure(int errorNo, String strMsg, String completionInfo) {
        super.onFailure(errorNo, strMsg, completionInfo);
        L.e("network-error", completionInfo + "#strMsg#" + strMsg + "#errorNo#" + errorNo);
        if (listenerWeakReference.get() != null) {
            listenerWeakReference.get().onHttpResult(null, errorNo, flag, strMsg);
        }
        T.s(ABApplication.getInstance(), VolleyErrorMsg.getMessage(errorNo, strMsg));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (dialog.get() != null) {//如果dialog还存在，则关闭
            dialog.get().dismiss();
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, Bitmap bitmap) {
        super.onSuccess(headers, bitmap);
    }
}
