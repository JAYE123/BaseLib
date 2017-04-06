package core.base.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import core.base.log.L;
import core.base.manager.AppManager;
import core.base.rxvolley.NetRequest;
import core.base.utils.ABAppUtil;

/**
 * @version 1.0
 * @#作者:XQ
 * @#创建时间：15-9-18 上午11:46
 * @#类 说 明:基础的Activity.
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getName();
    protected Context mContext;
    protected Activity mActivity;

    protected void le(String msg) {
        L.e(TAG, msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext =this.mActivity = this;
//        if(ABApplication.getInstance().requireStatusColor){
//            ABAppUtil.setStatusImmerse(this, ABApplication.getInstance().getStatusColor(), ABApplication.getInstance().isDarkmode());
//        }
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    /**
     * 设置状态栏，默认为Appliation
     */
    protected void setStatusBar() {
        if(ABApplication.getInstance().requireStatusColor){
//            StatusBarUtil.setColorNoTranslucent(this, ABApplication.getInstance().getStatusColor(),ABApplication.getInstance().isDarkmode());
            ABAppUtil.setStatusImmerse(this,ABApplication.getInstance().getStatusColor(),ABApplication.getInstance().getStatusColor(),ABApplication.getInstance().isDarkmode());
        }
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().removeActivity(this);
        NetRequest.getRequestQueue().cancelAll(this);//activity销毁时取消请求
        super.onDestroy();
    }

    /**
     * 加入了友盟时长统计
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 加入了友盟时长统计
     */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 跳转activity
     * @param clazz 目标activity
     * @param bundle 携带信息
     */
    public  static void go2Act(Context context,Class<? extends Activity> clazz,Bundle bundle){
        Intent intent = new Intent(context,clazz);
        if(bundle!=null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 回调跳转
     * @param activity
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public static void startForResult(Activity activity,Class<? extends Activity> clazz,int requestCode,Bundle bundle){
        Intent intent = new Intent(activity,clazz);
        if(bundle!=null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity,Class<? extends Activity> clazz,int requestCode){
        startForResult(activity,clazz,requestCode,null);
    }

    /**
     * 跳转activity
     * @param clazz
     */
    public static void go2Act(Context context,Class<? extends Activity> clazz){
        go2Act(context,clazz,null);
    }
}
