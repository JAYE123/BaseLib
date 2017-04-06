package core.base.views.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by 刘红亮 on 2015/12/28.
 */
public class NoScrollExpand extends ExpandableListView {
    public NoScrollExpand(Context context) {
        super(context);
    }

    public NoScrollExpand(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollExpand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
