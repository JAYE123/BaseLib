/*
 * Copyright (c) $2015 杨强辉<yangqianghui7788@gmail.com>. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package core.base.views.webview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import core.base.log.L;
import core.base.views.progressbar.NumberProgressBar;


/**
 * 带进度条的WebView Created by 杨强辉 on 2015/2/3.
 *
 * * 可以加入到xml。长按图片能返回图片的url地址
 * @最近修改时间：20150706 17:02
 */
public class ProgressWebView extends WebView {

	public static NumberProgressBar progressbar;
    public static boolean numberProgressBarEnable=true;
    private OnPictureLongClickListener onPictureLongClickListener;

    public OnPictureLongClickListener getOnPictureLongClickListener() {
        return onPictureLongClickListener;
    }

    public void setOnPictureLongClickListener(OnPictureLongClickListener onPictureLongClickListener) {
        this.onPictureLongClickListener = onPictureLongClickListener;
    }

    public ProgressWebView(Context context) {
		super(context);
	    initWebView(context);
	}

	public ProgressWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initWebView(context);
    }

    private void initWebView(Context context){
        if (isInEditMode()) { return; }
        if (numberProgressBarEnable) {
            progressbar = new NumberProgressBar(context);
            progressbar.setReachedBarColor(Color.GREEN);
            progressbar.setProgressTextColor(Color.GREEN);
            addView(progressbar);
        }
        setWebChromeClient(new WebChromeClient());
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setWebViewClient(new WebViewClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadsImagesAutomatically(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setBuiltInZoomControls(true);
        setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (v instanceof WebView) {
                    HitTestResult result = ((WebView) v).getHitTestResult();
                    if (result != null) {
                        int type = result.getType();
                        if (type == HitTestResult.IMAGE_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                            String pictureUrl = result.getExtra();
                            L.i(getClass().getName(), "获取到图片地址pictureUrl=：" + pictureUrl);
                            if (!TextUtils.isEmpty(pictureUrl)) {
                                if (onPictureLongClickListener != null) {
                                    onPictureLongClickListener.onPictureLongClick(pictureUrl);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

	public static class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
            if (numberProgressBarEnable&&progressbar!=null) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (numberProgressBarEnable&&progressbar!=null) {
            LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressbar.setLayoutParams(lp);
        }
		super.onScrollChanged(l, t, oldl, oldt);
	}


    public static interface OnPictureLongClickListener {
        void onPictureLongClick(String PictureUrl);
    }

}
