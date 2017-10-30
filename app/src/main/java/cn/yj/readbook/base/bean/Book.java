package cn.yj.readbook.base.bean;

import java.io.Serializable;

/**
 * Created by yangjie on 2017/10/23.
 */

public class Book implements Serializable, Cloneable{
    public String name;
    public String path;
    public double progress;

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
