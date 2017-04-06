package core.base.views.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by hongliang on 16-5-25.
 */
public class WebViewNoLeak extends WebView {

    public WebViewNoLeak(Context context) {
        super(context.getApplicationContext());
    }
    public WebViewNoLeak(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }
    public WebViewNoLeak(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
    }
}
