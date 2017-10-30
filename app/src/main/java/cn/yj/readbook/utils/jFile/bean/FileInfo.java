package cn.yj.readbook.utils.jFile.bean;

/**
 * Created by yangjie on 2017/10/23.
 */


public class FileInfo {
    public String name;
    public String path;
    public String size;
    public boolean isSelect;

    public FileInfo(String name, String path, String size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
