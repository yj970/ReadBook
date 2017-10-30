package cn.yj.readbook.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.yj.readbook.R;

/**
 * Created by yangjie on 2017/10/24.
 */

public class SettingActivity extends Activity {

    public static void StartSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        findView();
    }

    private void findView() {
        ((TextView)findViewById(R.id.tv_title)).setText("设置");
    }
}
