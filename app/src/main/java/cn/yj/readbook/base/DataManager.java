package cn.yj.readbook.base;

import java.util.ArrayList;
import java.util.List;

import cn.yj.readbook.Constant;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.utils.sharedPreference.SharedPreferenceUtil;

/**
 * Created by yangjie on 2017/10/23.
 */

public class DataManager {
    private DataManager() {}
    public List<Book> bookshelf = new ArrayList<>();
    private static final DataManager single = new DataManager();

    public static DataManager getInstance() {
        return single;
    }

    public void save () {
        SharedPreferenceUtil.save(Constant.FILE_KEY, Constant.BOOKSHELF, bookshelf);

    }


}
