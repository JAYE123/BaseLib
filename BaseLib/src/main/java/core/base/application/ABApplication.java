package core.base.application;

import android.app.Application;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.squareup.okhttp.OkHttpClient;
import com.umeng.analytics.MobclickAgent;

import core.base.exception.ABCrashHandler;
import core.base.log.SP;
import core.base.rxvolley.NetRequest;
import core.base.rxvolley.OkHttpStack;
import core.base.utils.ABPrefsUtil;

/**
 * 基础的application类
 */
public class ABApplication extends Application {

    private static ABApplication instance;


    public static ABApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initPrefs();
        initOkHttp();
        initCrashHandler();
        MobclickAgent.enableEncrypt(false);
    }

    private void initOkHttp() {
        NetRequest.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER, new OkHttpStack(new OkHttpClient())));
    }

    public void saveMd5Pwd(String md5Pwd) {
        ABPrefsUtil.getPrefsUtil("encrypt_prefs").putString("md5Pwd", md5Pwd).commit();
    }

    public String getMd5Pwd() {
        return ABPrefsUtil.getPrefsUtil("encrypt_prefs").getString("md5Pwd", "");
    }


    /**
     * 初始化程序崩溃捕捉处理
     */
    protected void initCrashHandler() {
        ABCrashHandler.init(getApplicationContext());
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SP.init(getApplicationContext());
        ABPrefsUtil.init(this, "encrypt_prefs", MODE_PRIVATE);
    }
}
