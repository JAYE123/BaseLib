package core.base.rxvolley;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.kymjs.rxvolley.client.HttpCallback;

import java.lang.ref.WeakReference;
import java.util.Map;

import core.base.log.L;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public class HttpCallBackDialog extends HttpCallback{
    private WeakReference<Dialog> wdialog;
    private WeakReference<HttpResultListener> listenerWeakReference;
    private String flag[];
    public HttpCallBackDialog(Dialog dialog,HttpResultListener listener,String flag[]) {
        this.wdialog=new WeakReference<Dialog>(dialog);
        this.listenerWeakReference=new WeakReference<HttpResultListener>(listener);
        this.flag=flag;
    }
    @Override
    public void onPreStart() {
        super.onPreStart();
        if(wdialog.get()!=null){//如果dialog还存在，则弹出
            wdialog.get().show();
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
        L.json("netnet-success", t);
        if(listenerWeakReference.get()!=null){
            listenerWeakReference.get().onHttpResult(t,-1,flag,null);
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, byte[] t) {
        super.onSuccess(headers, t);
    }

    @Override
    public void onFailure(int errorNo, String strMsg,String completionInfo) {
        super.onFailure(errorNo, strMsg,completionInfo);
        L.e("netnet-error",completionInfo);
        if(listenerWeakReference.get()!=null){
            listenerWeakReference.get().onHttpResult(null,errorNo,flag,strMsg);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if(wdialog.get()!=null){//如果dialog还存在，则关闭
            wdialog.get().dismiss();
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, Bitmap bitmap) {
        super.onSuccess(headers, bitmap);
    }
}
