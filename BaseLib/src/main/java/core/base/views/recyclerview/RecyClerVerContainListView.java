package core.base.views.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 刘红亮 on 2016/2/2.
 */
public class RecyClerVerContainListView extends RecyclerView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    public RecyClerVerContainListView(Context context) {
        super(context);
    }

    public RecyClerVerContainListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyClerVerContainListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
