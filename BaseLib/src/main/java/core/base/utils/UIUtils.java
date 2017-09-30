package core.base.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * 一些ui 方面的工具类
 * Created by min on 2015/6/17.
 */
public class UIUtils {
    /**
     * 多个EditText都要填写，这个button才能够被点击
     * @param button 当前button
     * @param editTexts 当前要判断的所有EditText
     */
    public static void setButtonEnableByEditText(final Button button,final EditText...editTexts){
        button.setEnabled(false);
        for(int i=0;i<editTexts.length;i++){
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(isEveryEditTextNotNull(editTexts)){
                        button.setEnabled(true);
                    }else{
                        button.setEnabled(false);
                    }
                }
            });
        }
    }

    /**
     * 是否所有的Edittext不为空
     * @param editTexts 要判断的edittext
     * @return 是否所有的不为空
     */
    private static boolean isEveryEditTextNotNull(EditText...editTexts){
        boolean flag = true;
        for(EditText editText:editTexts){
            if(TextUtils.isEmpty(editText.getText().toString().trim())){
                flag = false;
                break;
            }
        }
        return flag;
    }
    /**
     * 抖动指定的view
     * @param context
     * @param view 目标
     */
    public static void shake(Context context,View view){
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(1000);
        animator.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),
                ObjectAnimator.ofFloat(view, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0)
        );
        animator.start();
        return;
    }
}
