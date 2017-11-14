package cn.yj.readbook.scan.v;

import android.app.Activity;

import java.util.ArrayList;

import cn.yj.readbook.base.bean.Book;

/**
 * Created by yangjie on 2017/11/3.
 */

public interface ScanView {
    Activity getActivity();
    void scan();
    void showData(ArrayList<Book> books);
    void add2BookShelf();
    void save();
}
