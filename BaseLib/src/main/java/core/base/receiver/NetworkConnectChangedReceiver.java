package core.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import core.base.log.T;

/**
 * 监听网络状态变化广播接收器
 * Created by Administrator on 2017/8/18
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {


    public static final String TAG1 = "xxx";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
////            Log.e(TAG1, "wifiState" + wifiState);
//            switch (wifiState) {
//                case WifiManager.WIFI_STATE_DISABLED:
////                    APP.getInstance().setEnablaWifi(false);
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//
//                    break;
//                case WifiManager.WIFI_STATE_ENABLING:
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
////                    APP.getInstance().setEnablaWifi(true);
//                    break;
//                case WifiManager.WIFI_STATE_UNKNOWN:
//                    break;
//                default:
//                    break;
//
//
//            }
//        }
//        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager
//        // .WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
//        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
//        // 当然刚打开wifi肯定还没有连接到有效的无线
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (null != parcelableExtra) {
//                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                NetworkInfo.State state = networkInfo.getState();
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
////                Log.e(TAG1, "isConnected=" + isConnected);
//                if (isConnected) {
////                    APP.getInstance().setWifi(true);
////                    Log.e(TAG1, "wifi已连接");
//                } else {
////                    APP.getInstance().setWifi(false);
////                    Log.e(TAG1, "wifi未连接");
//                }
//            }
//        }
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
//                        APP.getInstance().setWifi(true);
//                        Log.e(TAG1, "当前WiFi连接可用 ");
                        T.s(context, "您的网络处于WiFi网络状态");
//                        T.s(context, ABAppUtil.getWiFiHostIpAddress(context) + "");
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
//                        APP.getInstance().setMobile(true);
//                        Log.e(TAG1, "当前移动网络连接可用 ");
                        T.s(context, "您的网络处于移动网络状态");
                    }
                } else {
                    T.s(context, "当前网络未连接");
                }
//                Log.e(TAG1, "info.getTypeName()" + activeNetwork.getTypeName());
//                Log.e(TAG1, "getSubtypeName()" + activeNetwork.getSubtypeName());
//                Log.e(TAG1, "getState()" + activeNetwork.getState());
//                Log.e(TAG1, "getDetailedState()" + activeNetwork.getDetailedState().name());
//                Log.e(TAG1, "getDetailedState()" + activeNetwork.getExtraInfo());
//                Log.e(TAG1, "getType()" + activeNetwork.getType());
            } else {   // not connected to the internet
                T.s(context, "当前网络未连接");
//                APP.getInstance().setWifi(false);
//                APP.getInstance().setMobile(false);
//                APP.getInstance().setConnected(false);
            }
        }
    }
}
//调用网络变化广播器示例
//注册网络变化广播器
//    private void registerNetworkReceiver() {
//        IntentFilter filter = new IntentFilter();
//        connectChangedReceiver = new NetworkConnectChangedReceiver();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(connectChangedReceiver, filter);
//    }
