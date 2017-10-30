package cn.yj.readbook.base;

import cn.yj.readbook.APP;
import cn.yj.readbook.Constant;
import cn.yj.readbook.R;
import cn.yj.readbook.utils.CommonUtil;
import cn.yj.readbook.utils.sharedPreference.SharedPreferenceUtil;

/**
 * Created by yangjie on 2017/10/24.
 */

public class SettingManager {
    private SettingManager() {}
    private static final SettingManager single = new SettingManager();

    public static SettingManager getInstance() {
        return single;
    }

    // 字体大小
    public final int TEXT_SIZE_SMALL = 15;
    public final int TEXT_SIZE_NORMAL = 20;
    public final int TEXT_SIZE_LAGER = 26;
    public final int TEXT_SIZE_SUPER_LAGER = 35;

    // 行间距
    public final int ROW_SPACING_SMALL = 8;
    public final int ROW_SPACING_NORMAL = 10;
    public final int ROW_SPACING_LAGER = 12;
    public final int ROW_SPACING_SUPER_LAGER = 14;

    public final Integer[] rowSpacingValues = {ROW_SPACING_SMALL, ROW_SPACING_NORMAL, ROW_SPACING_LAGER, ROW_SPACING_SUPER_LAGER};
    public final String[] keys = {"偏小", "中等", "偏大", "超大"};
    public final Integer[] textSizeValues = {TEXT_SIZE_SMALL, TEXT_SIZE_NORMAL, TEXT_SIZE_LAGER, TEXT_SIZE_SUPER_LAGER};

    public final String[] readModelKeys = {"白天", "黑夜"};
    public final Integer[] readModelValues = {0, 1};
    public final Integer[] textColors = {R.color.black, R.color.white_smoke};
    public final Integer[] backgroundColors = {R.color.white_smoke, R.color.black};
    public int readModel = 0;// 阅读模式  0：白天 1：黑夜


    public final String[] typefaceKeys = {"默认", "微软雅黑", "方正兰亭黑", "苹方"};
    public final String[] typefacePathValues = {null, "fonts/微软雅黑.ttf", "fonts/方正兰亭黑.ttf", "fonts/苹方.ttf"};
    public int typeface = 0;

    public int textSize = TEXT_SIZE_NORMAL; // 字体大小
    public int rowSpacing = ROW_SPACING_NORMAL; // 行间距
    public String textSizeString = keys[1]; // 字体大小(String)
    public String readModelString = readModelKeys[0];
    public int textColor = textColors[0];
    public int backgroundColor = backgroundColors[0];


    public int getTextSize() {
        return textSize;
    }

    public String getTextSizeString() {
        return textSizeString;
    }

    public int getRowSpacing() {
        return CommonUtil.dp2px(APP.getApp(), rowSpacing);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        for (int i = 0; i < textSizeValues.length; i++) {
            if (textSize == textSizeValues[i]) {
                textSizeString = keys[i];
                break;
            }
        }
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
    }

    public void save() {
        SharedPreferenceUtil.save(Constant.FILE_KEY, Constant.TEXT_SIZE_KEY, textSize);
        SharedPreferenceUtil.save(Constant.FILE_KEY, Constant.TEXT_READ_MODEL_KEY, readModel);
        SharedPreferenceUtil.save(Constant.FILE_KEY, Constant.TEXT_TYPEFACE_KEY, typeface);


    }

    public void load() {
        Object o = SharedPreferenceUtil.get(Constant.FILE_KEY, Constant.TEXT_SIZE_KEY);
        if (o != null) {
           setTextSize((int)o);
        }
        Object j = SharedPreferenceUtil.get(Constant.FILE_KEY, Constant.TEXT_READ_MODEL_KEY);
        if (j != null) {
            setReadModel((int)j);
        }
        Object k = SharedPreferenceUtil.get(Constant.FILE_KEY, Constant.TEXT_TYPEFACE_KEY);
        if (k != null) {
            setTypeface((int)k);
        }

    }

    public void setReadModel(int readModel) {
        this.readModel = readModel;
        textColor = textColors[readModel];
        backgroundColor = backgroundColors[readModel];

        for (int i = 0; i < readModelValues.length; i++) {
            if  (readModel == readModelValues[i]) {
                readModelString = readModelKeys[i];
                break;
            }
        }
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public String getTypefacePath() {
        return typefacePathValues[typeface];
    }
    public String getTypefaceKey() {
        return typefaceKeys[typeface];
    }
}
