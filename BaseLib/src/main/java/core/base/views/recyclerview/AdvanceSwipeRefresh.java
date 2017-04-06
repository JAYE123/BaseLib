package core.base.views.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import core.base.R;
import core.base.log.L;
import core.base.log.T;
import core.base.support.recyclerview.layoutmanager.ABaseGridLayoutManager;
import core.base.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import core.base.support.recyclerview.layoutmanager.ABaseStaggeredGridLayoutManager;
import core.base.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import core.base.utils.ABAppUtil;
import core.base.views.progressbar.CircleProgressBar;
import core.base.views.ptr.PtrDefaultHandler;
import core.base.views.ptr.PtrFrameLayout;
import core.base.views.ptr.PtrHandler;
import core.base.views.ptr.header.MaterialHeader;
import core.base.views.recyclerview.listener.AdvanceSwipeRefreshListener;


/**
 * Created by 刘红亮 on 2015/11/13 14:16.
 */

public class AdvanceSwipeRefresh extends LinearLayout implements OnRecyclerViewScrollLocationListener,PtrHandler {
    private final static String TAG=AdvanceSwipeRefresh.class.getSimpleName();
    private String mStrTips ="没有更多数据";

    private static final int CIRCLE_DIAMETER = 40;
    public final static int TYPE_LINEAR=1;
    public final static int TYPE_GRID=2;
    public final static int TYPE_STAGGER=3;

    public final static int REFRESH_STATUS=4; //刷新状态
    public final static int LOADMORE_STATUS=5; //加载更多状态
    public final static int NORMAL_STATUS=6;//正常状态，可上拉，可下拉
    private  int current_status=NORMAL_STATUS; //纪录当前状态
    private Context context;
    private PtrFrameLayout refreshLayout;
    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private FrameLayout footviewLayout;
    private FrameLayout emptyLayout;
    private FrameLayout errorLayout;
    private AdvanceSwipeRefreshListener listener;
    private AdvRefreshListener advRefreshListener;

    private LayoutInflater mInflater;
    private boolean isRefreshing;//是否是在刷新中
    private boolean canLoadMore =true;
    public AdvanceSwipeRefresh(Context context) {
        this(context, null);
    }

    public AdvanceSwipeRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvanceSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        mInflater = LayoutInflater.from(getContext());
        setupUI();
    }

    public int getCurrent_status() {
        return current_status;
    }

    public RecyclerView getRecyclerview() {
        return recyclerview;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerview.getLayoutManager();
    }

    public PtrFrameLayout getRefreshLayout() {
        return refreshLayout;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    private void setupUI() {
        //把布局加载成自己
        View layout=mInflater.inflate(R.layout.advance_swipefresh, this, false);
        addView(layout);
        refreshLayout = (PtrFrameLayout) layout.findViewById(R.id.adv_refresh);
        recyclerview = (RecyclerView) layout.findViewById(R.id.adv_recyclerview);
        //初始化底部加载图
        footviewLayout = (FrameLayout) layout.findViewById(R.id.adv_footview);
//        initRefresh(refreshLayout);
//        initLoadMore(footviewLayout);
        emptyLayout = (FrameLayout) layout.findViewById(R.id.adv_fl_empty);
        //初始化加载失败布局里面的
        errorLayout = (FrameLayout) layout.findViewById(R.id.adv_fl_error);
        View view = errorLayout.findViewById(R.id.state_layout);
        //点击重新加载,加载成功后还要重置回原来的状态
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重试；
                if (current_status == NORMAL_STATUS) {//不是在刷新,不是在加载更多
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
                    animation.setInterpolator(new LinearInterpolator());
                    view.findViewById(R.id.loading_img).startAnimation(animation);
                    ((TextView) view.findViewById(R.id.loading_text)).setText("正在加载中···");
                    //延迟刷新，防止动画瞬间被重置
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onRefreshBegin(refreshLayout);//调用刷新
                        }
                    }, 500);
                }
            }
        });
        refreshLayout.setPtrHandler(this);
        beforeFirstOnRefresh();
        L.e(TAG, "布局初始化完成！");
    }

    public void setStrTips(String mStrTips) {
        this.mStrTips = mStrTips;
    }

    private void initLoadMore(FrameLayout footviewLayout) {
        CircleProgressBar circleProgressBar = (CircleProgressBar) footviewLayout.findViewById(R.id.adv_circle_progress);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        circleProgressBar.setColorSchemeColors(colors);
        circleProgressBar.setBackgroundColor(Color.WHITE);
//        circleProgressBar.setShowArrow(true);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams footLayoutParams = circleProgressBar.getLayoutParams();
        footLayoutParams.height=footLayoutParams.width=(int) (CIRCLE_DIAMETER * metrics.density);
        circleProgressBar.setLayoutParams(footLayoutParams);
    }

    private void initRefresh(PtrFrameLayout refreshLayout){
        // header
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, ABAppUtil.dip2px(getContext(), 15), 0, ABAppUtil.dip2px(getContext(), 10));
        header.setPtrFrameLayout(refreshLayout);
        refreshLayout.setHeaderView(header);
        refreshLayout.disableWhenHorizontalMove(true);
        refreshLayout.addPtrUIHandler(header);
    }
    /**
     * 隐藏所有盖住的控件
     */
    public void hideAllView(){
        emptyLayout.setVisibility(GONE);
        errorLayout.setVisibility(GONE);
    }
    //无网络,加载出错
    public void showErrorView(){
        endLoadMore();
        endRefresh();
        if(recyclerview.getAdapter()==null||recyclerview.getAdapter().getItemCount()==0){//列表没有数据,错误布局没有显示
            hideAllView();
            findViewById(R.id.loading_img).clearAnimation();
            errorLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.loading_text)).setText("加载失败,点击重试");
        }
    }

    //无数据
    public void showEmptyView() {
        if(recyclerview.getAdapter()==null||recyclerview.getAdapter().getItemCount()==0){//列表没有数据
            hideAllView();
            emptyLayout.setVisibility(VISIBLE);
        }
    }
    public void setEmptyView(View emptyView){
        emptyLayout.removeAllViews();
        emptyLayout.addView(emptyView);
    }
    /**
     * 设置布局管理器
     * @param type
     * @param orientation
     * @param spanCount
     */
    public void setLayoutManager(int type, int orientation,Integer spanCount){
        initRefresh(refreshLayout);
        initLoadMore(footviewLayout);
        switch (type){
            case TYPE_LINEAR:
                ABaseLinearLayoutManager linearLayoutManager = new ABaseLinearLayoutManager(context,orientation,false);
                recyclerview.setLayoutManager(linearLayoutManager);

                linearLayoutManager.setOnRecyclerViewScrollLocationListener(recyclerview, this);
                layoutManager=linearLayoutManager;
                break;
            case TYPE_GRID:

                ABaseGridLayoutManager gridLayoutManager = new ABaseGridLayoutManager(context,spanCount);
                recyclerview.setLayoutManager(gridLayoutManager);
                gridLayoutManager.setOnRecyclerViewScrollListener(recyclerview, this);
                layoutManager=gridLayoutManager;
                break;
            case TYPE_STAGGER:
                ABaseStaggeredGridLayoutManager staggeredGridLayoutManager = new ABaseStaggeredGridLayoutManager(spanCount,orientation);
                recyclerview.setLayoutManager(staggeredGridLayoutManager);
                staggeredGridLayoutManager.setOnRecyclerViewScrollListener(recyclerview, this);
                layoutManager=staggeredGridLayoutManager;
                break;
        }
    }

    /**
     * 设置是否可以下拉
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        refreshLayout.setEnabled(enable);
    }
    /**
     * 设置adapter
     * @param adapter
     */
    public void setAdapter(final RecyclerView.Adapter adapter){
        recyclerview.setAdapter(adapter);
        if(adapter!=null) {
            adapter.registerAdapterDataObserver(adapterDataObserver);
        }
    }

    /**
     * 监听数据改变，改变状态
     */
    RecyclerView.AdapterDataObserver adapterDataObserver=new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            hideAllView();//把盖在上面的view全部清除
            current_status=NORMAL_STATUS;//正常状态
            if(recyclerview.getAdapter().getItemCount()==0){//没有任何子项，不是在刷新状态
                showEmptyView();
            }
        }

    };
    /**
     * 设置自定义的footView
     * @param footView
     */
    public void setFootview(View footView){
        footviewLayout.removeAllViews();
        footviewLayout.addView(footView);
    }

    @Override
    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
        //滑动到顶
        Log.e(TAG, "滑动到顶");
    }

    @Override
    public void onBottomWhenScrollIdle(final RecyclerView recyclerView) {
        Log.e(TAG, "滑动到底");
        if(canLoadMore &&(listener !=null||advRefreshListener!=null)&&current_status==NORMAL_STATUS){//设置了监听，且当前为正常状态，不允许在上一次加载还未完成时，再次触发加载
            showLoadMore();
        }
    }

    private long startLoadMoreTime;
    /**
     * 显示加载更多
     */
    public void showLoadMore(){

        if(listener!=null){
            startLoadMoreTime=System.currentTimeMillis();
            footviewLayout.setVisibility(VISIBLE);
            current_status=LOADMORE_STATUS;//上拉加载更多状态
            requestPageNo = pageNo + 1;
            L.e(TAG,"开始加载更多");
            listener.onLoadMore();
        }
        if(advRefreshListener!=null){
            startLoadMoreTime=System.currentTimeMillis();
            footviewLayout.setVisibility(VISIBLE);
            current_status=LOADMORE_STATUS;//上拉加载更多状态
            requestPageNo = pageNo + 1;
            L.e(TAG,"开始加载更多");
            advRefreshListener.getPageData(requestPageNo);
        }

    }

    /**
     * 结束加载更多
     */
    public void endLoadMore(){
        //最少500ms才结束
        long time = System.currentTimeMillis() - startLoadMoreTime;
        if(time>=500){
            time=0;
        }else{
            time=500- time;
        }
        footviewLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                footviewLayout.setVisibility(GONE);
                current_status = NORMAL_STATUS;//还原为正常状态
                L.e(TAG,"结束加载更多");
            }
        }, time);
    }
    private long startRefreshTime;
    /**
     * 显示刷新
     */
    public void showRefresh(){
        refreshLayout.autoRefresh();
    }
    public void noShowRefresh(){
        onRefreshBegin(refreshLayout);
    }
    /**
     * 设置官方swipeRefreshLayout可以自动刷新，使用前需初始化
     */
    public void beforeFirstOnRefresh(){
//        refreshLayout.setProgressViewOffset(false, 0, dip2px(context, 24));
    }
    /**
     * 结束下拉刷新
     */
    public void endRefresh(){
        //最少500ms才结束
        long time = System.currentTimeMillis() - startRefreshTime;
        if(time>=500){
            time=0;
        }else{
            time=500-time;
        }
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.refreshComplete();
                current_status = NORMAL_STATUS;//还原为正常状态
            }
        }, time);
    }

    public AdvanceSwipeRefreshListener getListener() {
        return listener;
    }

    /**
     * 设置下拉刷新，上拉加载监听器
     * @param listener
     */
    public void setListener(AdvanceSwipeRefreshListener listener) {
        this.listener = listener;
    }
    //设置数据直接加载监听
    public void setListener(AdvRefreshListener advRefreshListener){
        this.advRefreshListener=advRefreshListener;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getRequestPageNo() {
        return requestPageNo;
    }

    public void setRequestPageNo(int requestPageNo) {
        this.requestPageNo = requestPageNo;
    }
    //准备请求的页码
    private int requestPageNo=1;
    //当前页码
    private int pageNo=1;

    /**
     * 处理联网请求回来的数据
     * @param currentDatas  当前adapter所关联的数据集合
     * @param newDatas      将要更新或者添加的服务器数据集合
     */
    public void dealResult(List currentDatas,List newDatas) {
        endLoadMore();
        endRefresh();
        if (requestPageNo == 1) {//是刷新
            currentDatas.clear();
            if (newDatas == null || newDatas.size() == 0) {    //没有数据
                showEmptyView();
            } else {
                currentDatas.addAll(newDatas);
            }
            pageNo=requestPageNo;//刷新联网成功回来了，不管数据有没有，页面都是1
//            getRecyclerview().setAdapter(getRecyclerview().getAdapter());//刷新重新设置adapter，置顶
            getRecyclerview().getAdapter().notifyDataSetChanged();//需要更新数据才能被上面注册的数据改变监听器检测到，避免出现数据和刷新出错页面共存
        } else {//加载更多
            if (newDatas== null || newDatas.size() == 0) {    //没有数据
                T.s(getContext(), mStrTips);
            } else {
                currentDatas.addAll(newDatas);
                pageNo=requestPageNo;//加载更多，有数据，才更改页面
                getRecyclerview().getAdapter().notifyDataSetChanged();//更新数据
            }
        }
        L.e(TAG,"pageNo="+pageNo+"  requestPageNo="+requestPageNo);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
       // 默认实现，根据实际情况做改动
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        L.e(TAG,"开始下拉刷新");
        startRefreshTime=System.currentTimeMillis();
        if(listener !=null){
            requestPageNo = 1;
            listener.onRefresh();
            current_status=REFRESH_STATUS;//标记为正在刷新
        }
        if(advRefreshListener!=null){ //数据直接加载器监听
            requestPageNo=1;
            advRefreshListener.getPageData(requestPageNo);
            current_status=REFRESH_STATUS;//标记为正在刷新
        }
    }

    public interface AdvRefreshListener{
        public void getPageData(int requestPageNo);
    }



}
