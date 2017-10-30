package cn.yj.readbook.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yj.readbook.R;
import cn.yj.readbook.utils.CommonUtil;

/**
 * Created by yangjie on 2017/10/24.
 */

public class CommonDialog {
    private Dialog dialog;
    private IItemClickListener iItemClickListener;
    public CommonDialog(Activity activity, String[] keys, final Object[] values) {
        if (keys.length != values.length) {
            throw new RuntimeException("key和value的数量要一致");
        }

        dialog = new Dialog(activity, R.style.Theme_AppCompat_Dialog);
        LinearLayout view = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.layout_dialog, null);
        for (int i = 0; i < keys.length; i++) {
            LinearLayout linearLayout = new LinearLayout(activity);
            TextView textView = new TextView(activity);
            textView.setText(keys[i]);
            textView.setTextSize(CommonUtil.sp2px(activity, 5));
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            linearLayout.addView(textView, params);
            if (Build.VERSION.SDK_INT >= 21) {
                linearLayout.setBackground(activity.getDrawable(R.drawable.background_press));
            }
            final int finalI = i;
            final int finalI1 = i;
            final String finalI2 = keys[i];
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iItemClickListener != null) {
                        iItemClickListener.onItemClick(finalI, finalI2, values[finalI1]);
                        dismiss();
                    }
                }
            });
            view.addView(linearLayout, new LinearLayout.LayoutParams(CommonUtil.dp2px(activity, 200), CommonUtil.dp2px(activity, 70)));
        }
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface IItemClickListener {
        void onItemClick(int position, String key, Object value);
    }

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }
}
