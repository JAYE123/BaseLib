package core.base.rxvolley;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import core.base.R;


public class LoadingDialogView extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '·';

    private TextView tv_title;
    private TextView tv_point;
    private ImageView loadImage;
    private Animation mAnim;
    private boolean cancelable = true;

    private Handler handler = new Handler() {
        private int num = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num++;
                for (int i = 0; i < num; i++) {
                    builder.append(SUFFIX);
                }
                tv_point.setText(builder.toString());
                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            }
        }

        ;
    };

    public LoadingDialogView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.loding_dialog_layout, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setDimAmount(0);//设置不要
        setContentView(contentView);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelable) {
                    dismiss();
                }
            }
        });
        tv_title = (TextView) findViewById(R.id.tv);
        loadImage = (ImageView) findViewById(R.id.loading_image);
        tv_point = (TextView) findViewById(R.id.tv_point);
        initAnim();
    }


    private void initAnim() {
        mAnim = AnimationUtils.loadAnimation(loadImage.getContext(), R.anim.load_anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        mAnim.setInterpolator(lin);
        loadImage.setAnimation(mAnim);
    }

    @Override
    public void show() {
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
        super.show();
    }

    @Override
    public void dismiss() {
        mAnim.cancel();
        super.dismiss();
    }


    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }
}
