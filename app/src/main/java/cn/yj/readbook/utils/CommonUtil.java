package cn.yj.readbook.utils;

import android.content.Context;
import android.view.WindowManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangjie on 2017/10/24.
 */

public class CommonUtil {
    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    // 获取路径下的全部文件
    public static List<File> getFiles(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        List<File> list = Arrays.asList(files);
        return list;
//        if (files == null){
//            Log.e("error","空目录");return null;}
//        List<String> s = new ArrayList<>();
//        for(int i =0;i<files.length;i++){
//            s.add(files[i].getAbsolutePath());
//        }
//        return s;
    }
}
