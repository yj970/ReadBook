package cn.yj.readbook.read.interfaces;

import cn.yj.readbook.read.view.ObservableScrollView;

/**
 * Created by yangjie on 2017/10/23.
 */

public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}