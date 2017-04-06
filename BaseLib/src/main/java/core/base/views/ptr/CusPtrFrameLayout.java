package core.base.views.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import core.base.utils.ABAppUtil;
import core.base.views.ptr.header.MaterialHeader;

/**
 * Created by Nowy on 2016/3/28.
 * 下拉刷新默认封装
 * RefreshListener 刷新监听，刷新前和刷新结束
 */
public class CusPtrFrameLayout extends PtrFrameLayout implements PtrHandler{
    private RefreshListener mRefreshListener;
    public CusPtrFrameLayout(Context context) {
        super(context);
        initRefreshDef();

    }

    public CusPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRefreshDef();
    }

    public CusPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRefreshDef();
    }


    public void initRefreshDef(){
        // header
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(core.base.R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, ABAppUtil.dip2px(getContext(), 15), 0, ABAppUtil.dip2px(getContext(), 10));
        header.setPtrFrameLayout(this);
        setHeaderView(header);
        disableWhenHorizontalMove(true);
        addPtrUIHandler(header);
        setPtrHandler(this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if(mRefreshListener!=null)
            mRefreshListener.onRefreshBegin(frame);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                endRefresh();
            }
        }, 2000);
    }


    public RefreshListener getOnRefreshListener() {
        return mRefreshListener;
    }

    public void setOnRefreshListener(RefreshListener refreshListener) {
        this.mRefreshListener = refreshListener;
    }

    public void endRefresh(){
        refreshComplete();
        if(mRefreshListener!=null)
            mRefreshListener.onRefreshComplete(this);
    }
    public interface RefreshListener{
        void onRefreshComplete(PtrFrameLayout layout);
        void onRefreshBegin(PtrFrameLayout layout);
    }
}
