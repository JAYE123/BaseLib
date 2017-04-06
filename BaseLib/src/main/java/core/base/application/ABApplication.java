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
    //状态栏颜色
    int statusColor;
    //底部导航栏颜色
    int navColor;
    //是否是黑色模式（小米和魅族有效）
    boolean darkmode;
    boolean requireStatusColor;
    private static ABApplication instance;


    public static ABApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
       // initCrashHandler(); // 初始化程序崩溃捕捉处理
        initPrefs(); // 初始化SharedPreference
        initOkHttp();
        //友盟统计日志加密
//        AnalyticsConfig.enableEncrypt(false);
        MobclickAgent.enableEncrypt(false);
    }

    private void initOkHttp() {
        NetRequest.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER,
                new OkHttpStack(new OkHttpClient())));
    }

    public void saveMd5Pwd(String md5Pwd){
        ABPrefsUtil.getPrefsUtil("encrypt_prefs").putString("md5Pwd",md5Pwd).commit();
    }
    public String getMd5Pwd(){
        return ABPrefsUtil.getPrefsUtil("encrypt_prefs").getString("md5Pwd","");
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
    /**
     * 配置状态栏颜色
     * @return
     */
    public int getStatusColor() {
        return statusColor;
    }

    public int getNavColor() {
        return navColor;
    }

    public boolean isDarkmode() {
        return darkmode;
    }

    /**
     *
     * @param statusColor 状态栏颜色
     * @param navColor  底部栏颜色，暂时未使用
     * @param darkmode  是否要启用白色黑色模式（纯白色适用，小米和魅族适用）
     */
    public void setStatusColor(int statusColor,int navColor,boolean darkmode) {
        this.statusColor = statusColor;
        this.navColor = navColor;
        this.darkmode = darkmode;
        requireStatusColor=true;
    }
}
