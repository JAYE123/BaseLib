package core.base.views.recyclerview.listener;

/**
 * 普通的滚动监听，可以在LayoutManager中获取到RecyclerViewScrollManager后add多个
 */
public interface OnRecyclerViewScrollListener {
    public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState);

    public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy);
}
