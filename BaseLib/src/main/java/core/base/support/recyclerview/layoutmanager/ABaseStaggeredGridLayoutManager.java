package core.base.support.recyclerview.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.Arrays;

import core.base.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;


public class ABaseStaggeredGridLayoutManager extends StaggeredGridLayoutManager implements RecyclerViewScrollManager.OnScrollManagerLocation {
    private static final String TAG = ABaseStaggeredGridLayoutManager.class.getSimpleName();

    private RecyclerViewScrollManager recyclerViewScrollManager;

    public void setOnRecyclerViewScrollListener(RecyclerView recyclerView, OnRecyclerViewScrollLocationListener onRecyclerViewScrollLocationListener) {
        ensureRecyclerViewScrollManager();
        recyclerViewScrollManager.setOnRecyclerViewScrollLocationListener(onRecyclerViewScrollLocationListener);
        recyclerViewScrollManager.setOnScrollManagerLocation(this);
        recyclerViewScrollManager.registerScrollListener(recyclerView);
    }

    public ABaseStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public boolean isScrolling() {
        if (null != recyclerViewScrollManager) {
            return recyclerViewScrollManager.isScrolling();
        }
        return false;
    }

    public RecyclerViewScrollManager getRecyclerViewScrollManager() {
        ensureRecyclerViewScrollManager();
        return recyclerViewScrollManager;
    }
    private void ensureRecyclerViewScrollManager(){
        if (null == recyclerViewScrollManager) {
            recyclerViewScrollManager = new RecyclerViewScrollManager();
        }
    }


    @Override
    public boolean isTop(RecyclerView recyclerView) {
        int[] into = findFirstVisibleItemPositions(null);
        return !isEmpty(into) && 0 == into[0];
    }

    @Override
    public boolean isBottom(RecyclerView recyclerView) {
        int into[] = findLastCompletelyVisibleItemPositions(null);
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        Arrays.sort(into);
        return !isEmpty(into) && lastPosition == into[into.length - 1];
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }
}
