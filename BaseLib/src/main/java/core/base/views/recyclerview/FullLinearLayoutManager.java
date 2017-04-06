package core.base.views.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:    ZhuWenWu
 * Version    V1.0
 * Date:      2015/2/26  14:15.
 * Description:
 * Modification  History:
 * Date             Author                Version            Description
 * -----------------------------------------------------------------------------------
 * 2015/2/26        ZhuWenWu            1.0                    1.0
 * Why & What is modified:
 */
public class FullLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = FullLinearLayoutManager.class.getSimpleName();

    public FullLinearLayoutManager(Context context) {
        super(context);
    }

    public FullLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {

        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

//        Log.i(TAG, "onMeasure called. \nwidthMode " + widthMode
//                + " \nheightMode " + heightSpec
//                + " \nwidthSize " + widthSize
//                + " \nheightSize " + heightSize
//                + " \ngetItemCount() " + getItemCount());

        int width = 0;
        int height = 0;
        for (int i = 0; i < getItemCount(); i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);

            if (getOrientation() == HORIZONTAL) {
                width = width + mMeasuredDimension[0];
                if (i == 0) {
                    height = mMeasuredDimension[1];
                }
            } else {
                height = height + mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
            }
        }
        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        try {
            View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsExceptionif (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = getChildMeasureSpec(heightSpec,getPaddingTop() + getPaddingBottom(), p.height);
//                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
//                        getPaddingTop() + getPaddingBottom(), p.height);

                view.measure(childWidthSpec, childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    /**
     * 在measureChildren中最难的部分：找出传递给child的MeasureSpec。
     * 目的是结合父view的MeasureSpec与子view的LayoutParams信息去找到最好的结果
     * （也就是说子view的确切大小由两方面共同决定：1.父view的MeasureSpec 2.子view的LayoutParams属性）
     *
     * @param spec 父view的详细测量值(MeasureSpec)
     * @param padding view当前尺寸的的内边距和外边距(padding,margin)
     * @param childDimension child在当前尺寸下的布局参数宽高值(LayoutParam.width,height)
     */
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        //父view的模式和大小
        int specMode = View.MeasureSpec.getMode(spec);
        int specSize = View.MeasureSpec.getSize(spec);

        //通过父view计算出的子view = 父大小-边距（父要求的大小，但子view不一定用这个值）
        int size = Math.max(0, specSize - padding);

        //子view想要的实际大小和模式（需要计算）
        int resultSize = 0;
        int resultMode = 0;

        //通过1.父view的MeasureSpec 2.子view的LayoutParams属性这两点来确定子view的大小
        switch (specMode) {
            // 当父view的模式为EXACITY时，父view强加给子view确切的值
            case View.MeasureSpec.EXACTLY:
                // 当子view的LayoutParams>0也就是有确切的值
                if (childDimension >= 0) {
                    //子view大小为子自身所赋的值，模式大小为EXACTLY
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                    // 当子view的LayoutParams为MATCH_PARENT时(-1)
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    //子view大小为父view大小，模式为EXACTLY
                    resultSize = size;
                    resultMode = View.MeasureSpec.EXACTLY;
                    // 当子view的LayoutParams为WRAP_CONTENT时(-2)
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    //子view决定自己的大小，但最大不能超过父view，模式为AT_MOST
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                }
                break;

            // 当父view的模式为AT_MOST时，父view强加给子view一个最大的值。
            case View.MeasureSpec.AT_MOST:
                // 道理同上
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                }
                break;

            // 当父view的模式为UNSPECIFIED时，子view为想要的值
            case View.MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // 子view大小为子自身所赋的值
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                    return View.MeasureSpec.makeMeasureSpec(resultSize, View.MeasureSpec.EXACTLY);
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    // 因为父view为UNSPECIFIED，所以MATCH_PARENT的话子类大小为0
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                    return View.MeasureSpec.makeMeasureSpec(resultSize, View.MeasureSpec.UNSPECIFIED);
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // 因为父view为UNSPECIFIED，所以WRAP_CONTENT的话子类大小为0
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                    return View.MeasureSpec.makeMeasureSpec(resultSize, View.MeasureSpec.UNSPECIFIED);
                }
                break;
        }
        return View.MeasureSpec.makeMeasureSpec(resultSize, View.MeasureSpec.UNSPECIFIED);
    }
}