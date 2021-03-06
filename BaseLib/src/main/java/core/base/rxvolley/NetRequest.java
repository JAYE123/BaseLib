package core.base.rxvolley;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.FileRequest;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.client.RequestConfig;
import com.kymjs.rxvolley.http.DefaultRetryPolicy;
import com.kymjs.rxvolley.http.RequestQueue;

import core.base.application.ABApplication;
import core.base.log.L;
import core.base.manager.AppManager;
import core.base.security.des3.Base64;
import core.base.utils.ABAppUtil;

public class NetRequest {
    private String flag[];
    private HttpParams params;
    private Dialog dialog;
    private Object mTag;
    private int contentType;
    public final static int JSON = RxVolley.ContentType.JSON;
    public final static int FORM = RxVolley.ContentType.FORM;
    private final String TAG = NetRequest.class.getSimpleName();
    private static boolean safeRequest = false;

    public static NetRequest request() {
        return new NetRequest();
    }

    /**
     * 获取一个请求队列(单例)
     */
    public synchronized static RequestQueue getRequestQueue() {
        return RxVolley.getRequestQueue();
    }

    /**
     * 设置请求队列,必须在调用Core#getRequestQueue()之前设置
     *
     * @return 是否设置成功
     */
    public synchronized static boolean setRequestQueue(RequestQueue queue) {
        return RxVolley.setRequestQueue(queue);
    }

    public NetRequest setContentType(int contentType) {
        this.contentType = contentType;
        return this;
    }

    public NetRequest setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    /**
     * 设置请求标示
     *
     * @param flag 单个或多个标示
     * @return
     */
    public NetRequest setFlag(String... flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 设置请求参数
     *
     * @param params 请求参数
     * @return
     */
    public NetRequest setParams(HttpParams params) {
        this.params = params;
        return this;
    }

    @Deprecated
    public NetRequest showDialog(Context context, String message, boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity != null) {
            this.dialog = new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
            this.dialog.setTitle(message);
        }
        return this;
    }

    @Deprecated
    public NetRequest showDialog(Context context, boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity != null) {
            this.dialog = new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
        }
        return this;
    }

    public NetRequest showDialog(String message, boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity != null) {
            this.dialog = new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
            this.dialog.setTitle(message);
        }
        return this;
    }

    public NetRequest showDialog(boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity != null) {
            this.dialog = new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
        }
        return this;
    }

    public NetRequest setDialog(Dialog dialog) {
        this.dialog = dialog;
        return this;
    }

    /**
     * 接口方式的get 请求
     *
     * @param url
     * @param httpResultListener 回调接口
     */
    public void get(String url, HttpResultListener httpResultListener) {
        if (mTag == null) this.mTag = httpResultListener;
        get(url, new HttpCallBackDialog(dialog, httpResultListener, flag));
    }

    /**
     * 接口方式的post 请求
     *
     * @param url
     * @param httpResultListener 回调接口
     */
    public void post(String url, HttpResultListener httpResultListener) {
        if (mTag == null) this.mTag = httpResultListener;
        post(url, new HttpCallBackDialog(dialog, httpResultListener, flag));
    }

    /**
     * 接口方式的post 请求 上传进度回调请求
     *
     * @param url
     * @param listener           上传进度回调接口
     * @param httpResultListener 回调接口
     */
    public void post(String url, ProgressListener listener, HttpResultListener httpResultListener) {
        if (mTag == null) this.mTag = httpResultListener;
        post(url, listener, new HttpCallBackDialog(dialog, httpResultListener, flag));
    }

    /**
     * 下载
     *
     * @param storeFilePath    本地存储绝对路径
     * @param url              要下载的文件的url
     * @param progressListener 下载进度回调
     * @param callback         回调
     */
    public void download(String storeFilePath, String url, ProgressListener progressListener, HttpCallback callback) {
//        addEncrypt(params);
        printUrl(url, params);
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        config.mRetryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setTag(url);
        request.setOnProgressListener(progressListener);
        new RxVolley.Builder().setRequest(request).doTask();
    }

    public void get(String url, HttpCallback callback) {
//        addEncrypt(params);
        printUrl(url, params);
        if (ABAppUtil.isOnline(ABApplication.getInstance())) {
            new RxVolley.Builder()
                    .url(url)
                    .params(params != null ? params : new HttpParams())
                    .setTag(mTag != null ? mTag : url)
                    .callback(callback)
                    .doTask();
        } else {
            callback.onFailure(502, "未连接网络");
        }

    }

    public void post(String url, HttpCallback callback) {
//        addEncrypt(params);
        printUrl(url, params);
        if (ABAppUtil.isOnline(ABApplication.getInstance())) {
            new RxVolley.Builder()
                    .contentType(contentType)
                    .url(url)
                    .retryPolicy(new DefaultRetryPolicy(1000 * 15, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                    .params(params != null ? params : new HttpParams())
                    .httpMethod(RxVolley.Method.POST)
                    .setTag(mTag != null ? mTag : url)
                    .callback(callback)
                    .doTask();
        } else {
            callback.onFailure(502, "未连接网络");
        }
    }

    public void post(String url, ProgressListener listener, HttpCallback callback) {
//        addEncrypt(params);
        printUrl(url, params);
        if (ABAppUtil.isOnline(ABApplication.getInstance())) {
            new RxVolley.Builder()
                    .contentType(contentType)
                    .url(url)
                    .retryPolicy(new DefaultRetryPolicy(1000 * 15, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                    .params(params != null ? params : new HttpParams())
                    .progressListener(listener)
                    .httpMethod(RxVolley.Method.POST)
                    .setTag(mTag != null ? mTag : url)
                    .callback(callback)
                    .doTask();
        } else {
            callback.onFailure(502, "未连接网络");
        }

    }

    public void printUrl(String url, HttpParams params) {
        if (params == null) {
            params = new HttpParams();
        }
        L.e("network-url", new StringBuffer(url).append(params.getUrlParams()).toString());
    }

    /**
     * 添加签名用于验证url
     *
     * @param params
     */
//    private void addEncrypt(HttpParams params) {
//        if (safeRequest) {
//            String appSign = getAppSign(params);
//            if (params == null) {
//                params = new HttpParams();
//            }
//            params.put("sign", appSign);//添加加密参数
//        }
//
//    }

//    private String getAppSign(HttpParams params) {
//        if (params == null) params = new HttpParams();
//        String data = params.getUrlParams().toString().trim().replace("?", "").trim();
//        L.e("###", data);
//        byte[] encodedData = RSAUtil.encryptByPublicKey(data.getBytes(), CommentUrl.publicKey);
//        String sign = Base64.encode(encodedData);
//        L.e("###", sign);
//        return sign;
//    }

}
