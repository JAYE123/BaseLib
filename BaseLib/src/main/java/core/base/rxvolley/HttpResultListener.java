package core.base.rxvolley;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public interface HttpResultListener {
    /**
     * 联网回调方法
     * @param data 结果数据，若为null，说明联网失败
     * @param errorNo 错误编号
     * @param flag   请求标记数组
     * @param errorMsg 错误信息
     */
    void onHttpResult(String data, int errorNo, String flag[], String errorMsg);
}
