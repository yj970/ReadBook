package cn.yj.readbook.scan.p;

import java.util.ArrayList;

import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.scan.i.IScanListener;
import cn.yj.readbook.scan.m.ScanModel;
import cn.yj.readbook.scan.m.ScanModelImpl;
import cn.yj.readbook.scan.v.ScanView;

/**
 * Created by yangjie on 2017/11/3.
 */

public class ScanPresenterImpl implements ScanPresenter{
    private ScanView scanView;
    private ScanModel scanModel;

    public ScanPresenterImpl(ScanView scanView) {
        this.scanView = scanView;
        this.scanModel = new ScanModelImpl();
    }

    @Override
    public void scan() {
        scanModel.scan(scanView.getActivity(), new IScanListener() {
            @Override
            public void onScanComplete(ArrayList<Book> books) {
                scanView.showData(books);
            }
        });
    }

    @Override
    public void save() {
        scanModel.save();
    }

    @Override
    public boolean getFilterEnglishTitleChecked() {
        return scanModel.getFilterEnglishTitleChecked();
    }

    @Override
    public boolean getFilterNumberTitleChecked() {
        return scanModel.getFilterNumberTitleChecked();
    }

    @Override
    public void setFilterEnglishTitleChecked(boolean filterEnglishTitleChecked) {
        scanModel.setFilterEnglishTitleChecked(filterEnglishTitleChecked);
    }

    @Override
    public void setFilterNumberTitleChecked(boolean filterNumberTitleChecked) {
        scanModel.setFilterNumberTitleChecked(filterNumberTitleChecked);
    }
}
