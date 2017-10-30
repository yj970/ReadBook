package cn.yj.readbook.welcome;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import cn.yj.readbook.Constant;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.main.MainActivity;
import cn.yj.readbook.scan.ScanActivity;
import cn.yj.readbook.utils.sharedPreference.SharedPreferenceUtil;

/**
 * Created by yangjie on 2017/10/23.
 */

public class WelcomeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化跳转
        Object oBooks = SharedPreferenceUtil.get(Constant.FILE_KEY, Constant.BOOKSHELF);
        if (oBooks == null) {
            ScanActivity.startScanActivity(this);
        } else {
            MainActivity.startMainActivity(this, (List<Book>) oBooks);
        }
    }
}
