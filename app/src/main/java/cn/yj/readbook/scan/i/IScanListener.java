package cn.yj.readbook.scan.i;

import java.util.ArrayList;

import cn.yj.readbook.base.bean.Book;

/**
 * Created by yangjie on 2017/11/3.
 */

public interface IScanListener {
    void onScanComplete(ArrayList<Book> books);
}
