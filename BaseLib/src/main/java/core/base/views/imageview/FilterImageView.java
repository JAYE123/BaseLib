package core.base.views.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 作者：luwenming on 2015/9/7 09:34
 * 类说明：带滤镜效果的imageView
 */
public class FilterImageView extends ImageView implements GestureDetector.OnGestureListener{


    Context context;

    /**
     * 监听手势
     */
    private GestureDetector gestureDetector;


    public FilterImageView(Context context) {
        super(context);
        this.context= context;
        gestureDetector = new GestureDetector(context,this);
    }

    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        gestureDetector = new GestureDetector(context,this);
    }

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
        gestureDetector = new GestureDetector(context,this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在cancel里将滤镜取消，注意不要捕获cacncel事件,mGestureDetector里有对cancel的捕获操作
        //在滑动GridView时，AbsListView会拦截掉Move和UP事件，直接给子控件返回Cancel
        if(event.getActionMasked()== MotionEvent.ACTION_CANCEL
                ||event.getActionMasked()== MotionEvent.ACTION_UP){
            removeFilter();
        }
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     *   设置滤镜
     */
    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable=getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable==null) {
            drawable=getBackground();
        }
        if(drawable!=null){
            //设置滤镜
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);;
        }
    }
    /**
     *   清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable=getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable==null) {
            drawable=getBackground();
        }
        if(drawable!=null){
            //清除滤镜
            drawable.clearColorFilter();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        setFilter();
        //这里必须返回true，表示捕获本次touch事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
