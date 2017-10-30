package cn.yj.readbook;

import android.app.Application;

/**
 * Created by yangjie on 2017/10/23.
 */

public class APP extends Application{
    private static APP app;

    public APP() {
        app = this;
    }

    public static APP getApp() {
        return app;
    }
}
