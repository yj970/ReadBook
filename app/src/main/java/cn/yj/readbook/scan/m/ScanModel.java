package cn.yj.readbook.scan.m;

import android.app.Activity;

import cn.yj.readbook.scan.i.IScanListener;

/**
 * Created by yangjie on 2017/11/3.
 */

public interface ScanModel {
    void scan(Activity activity, IScanListener listener);
    void save();
    boolean getFilterEnglishTitleChecked();
    boolean getFilterNumberTitleChecked();
    void setFilterEnglishTitleChecked(boolean flag);
    void setFilterNumberTitleChecked(boolean flag);
}
