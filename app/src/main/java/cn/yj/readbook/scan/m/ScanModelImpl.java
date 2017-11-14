package cn.yj.readbook.scan.m;

import android.app.Activity;
import android.os.Handler;

import java.util.ArrayList;

import cn.yj.readbook.base.SettingManager;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.scan.i.IScanListener;
import cn.yj.readbook.utils.jFile.JFileUtil;
import cn.yj.readbook.utils.jFile.bean.FileInfo;

/**
 * Created by yangjie on 2017/11/3.
 */

public class ScanModelImpl implements ScanModel{
    private Handler handler = new Handler(){};
    private SettingManager settingManager;

    public ScanModelImpl() {
        settingManager = SettingManager.getInstance();
        settingManager.load();
    }

    @Override
    public void scan(final Activity activity, final IScanListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileUtil.Type[] types = new JFileUtil.Type[]{JFileUtil.Type.TXT};
                ArrayList<FileInfo> list = JFileUtil.scanFile(activity, types);
                // 过滤分类
                ArrayList<FileInfo>[] arrays = JFileUtil.filter(list, types);
                // 由QQ下载的文件
                ArrayList<FileInfo> qqArray = JFileUtil.scanQQfile_recv();
                // 由微信下载的文件
                ArrayList<FileInfo> wxArray = JFileUtil.scanWXfile_recv();

                final ArrayList<Book> books = new ArrayList<Book>();
                for (FileInfo info : arrays[0]) {
                    Book book = new Book();
                    book.name = info.name;
                    book.path = info.path;
                    books.add(book);
                }
                add2List(qqArray, books);
                add2List(wxArray, books);

                // ui线程
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onScanComplete(books);
                    }
                });

            }
        }).start();
    }

    @Override
    public void save() {
        settingManager.save();
    }

    @Override
    public boolean getFilterEnglishTitleChecked() {
        return settingManager.isFilterEnglishTitle();
    }

    @Override
    public boolean getFilterNumberTitleChecked() {
        return settingManager.isFilterNumberTitle();
    }

    @Override
    public void setFilterEnglishTitleChecked(boolean flag) {
        settingManager.setFilterEnglishTitle(flag);
    }

    @Override
    public void setFilterNumberTitleChecked(boolean flag) {
        settingManager.setFilterNumberTitle(flag);
    }

    /**
     * 判断文件路径，如果是源集合没有的文件，就添加进去
     *
     * @param fileInfos 要添加的集合
     * @param sources   被添加的集合，源集合
     */
    private void add2List(ArrayList<FileInfo> fileInfos, ArrayList<Book> sources) {
        for (FileInfo fileInfo : fileInfos) {
            boolean flag = true;
            for (Book sourceBook : sources) {
                if (sourceBook.path.equals(fileInfo.path)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                Book book = new Book();
                book.name = fileInfo.name;
                book.path = fileInfo.path;
                sources.add(book);
            }
        }
    }

}
