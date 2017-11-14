package cn.yj.readbook.scan.p;

/**
 * Created by yangjie on 2017/11/3.
 */

public interface ScanPresenter {
    void scan();
    void save();
    boolean getFilterEnglishTitleChecked();
    boolean getFilterNumberTitleChecked();
    void setFilterEnglishTitleChecked(boolean flag);
    void setFilterNumberTitleChecked(boolean flag);

}
