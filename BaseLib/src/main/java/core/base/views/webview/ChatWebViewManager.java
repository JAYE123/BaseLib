/*
 * Copyright (c) $2015 杨强辉<yangqianghui7788@gmail.com>. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package core.base.views.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author: 杨强辉
 * @类   说   明:	
 * @version 1.0
 * @创建时间：2015-2-1 上午11:18:28
 * @最近修改时间：2015-7-6 下午16:34:00
 */
public class ChatWebViewManager {
	private static ChatWebViewManager instance;
	private ProgressWebView mWebView;
	private WebSettings mWebSettings=null;

	public WebView getWebView() {
		return mWebView;
	}

    public void loadUrl(String url) {
        if (mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
	public void initWebview(Context context) {
		if (mWebView==null) {
			mWebView=new ProgressWebView(context);
			mWebSettings=mWebView.getSettings();
		}
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.setWebViewClient(new WebViewClient());
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
				if (v instanceof WebView) {
					WebView.HitTestResult result = ((WebView) v).getHitTestResult();
					if (result != null) {
						int type = result.getType();
						if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
							String ImageUrl = result.getExtra();
							if(!TextUtils.isEmpty(ImageUrl)){
//								new WebSaveImageTools().saveImage(CommwlItemWebActivity.this, ImageUrl);
							}
						}
					}
				}
			}
		});
	}
	
	public static ChatWebViewManager getInstance() {
		if (instance==null) {
			instance=new ChatWebViewManager();
		}
		return instance;
	}
	
	private ChatWebViewManager(){}

}
