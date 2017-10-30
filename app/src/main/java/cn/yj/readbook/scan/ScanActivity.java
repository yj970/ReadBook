package cn.yj.readbook.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yj.readbook.Constant;
import cn.yj.readbook.R;
import cn.yj.readbook.base.BaseActivity;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.main.MainActivity;
import cn.yj.readbook.scan.adapter.ScanAdapter;
import cn.yj.readbook.utils.jFile.JFileUtil;
import cn.yj.readbook.utils.jFile.bean.FileInfo;
import cn.yj.readbook.view.recyclerview.RBRecyclerView;

/**
 * Created by yangjie on 2017/10/23.
 */

public class ScanActivity extends BaseActivity {
    private RBRecyclerView mRcyBook;
    private ScanAdapter mAdapter;
    private Button mBtnAdd;

    private enum Type {TYPE1, TYPE2}

    private Type type = Type.TYPE1;
    private EditText edtSearch;

    public static void startScanActivity(Activity activity, int flags) {
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.setFlags(flags);
        activity.startActivity(intent);
    }

    public static void startScanActivity(Activity activity) {
        Intent intent = new Intent(activity, ScanActivity.class);
        activity.startActivity(intent);
    }

    public static void startScanActivityForResult(Activity activity, int codeScan) {
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra(Constant.EXTRA_SCAN_TYPE, Type.TYPE2);
        activity.startActivityForResult(intent, codeScan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        findView();
        bindData();
        setListener();
    }


    private void setListener() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> selectedBooks = mAdapter.getSelectedBooks();
                // model 1
                if (type == Type.TYPE1) {
                    MainActivity.startMainActivity(ScanActivity.this, selectedBooks);
                    finish();
                } else if (type == Type.TYPE2) {
                    // model 2
                    Intent intent = new Intent();
                    intent.putExtra(Constant.EXTRA_ADD_TO_BOOKSHELF, (Serializable) selectedBooks);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        // 搜索
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWord = s.toString();
                mAdapter.setKeyWord(keyWord);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void bindData() {
        ((TextView) findViewById(R.id.tv_title)).setText("本地书库");
        findViewById(R.id.ll_status_bar).setBackgroundColor(getResources().getColor(R.color.bar_color));

        if (getIntent().hasExtra(Constant.EXTRA_SCAN_TYPE)) {
            type = (Type) getIntent().getSerializableExtra(Constant.EXTRA_SCAN_TYPE);
        }

        MPermissions.requestPermissions(this, Constant.PERMISSION_CODE_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(Constant.PERMISSION_CODE_STORAGE)
    public void requestSdcardSuccess()
    {
        doScan();
    }

    @PermissionDenied(Constant.PERMISSION_CODE_STORAGE)
    public void requestSdcardFailed()
    {
        Toast.makeText(this, "已拒绝授予SD卡的读取权限", Toast.LENGTH_SHORT).show();
    }


    void doScan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileUtil.Type[] types = new JFileUtil.Type[]{JFileUtil.Type.TXT};
                ArrayList<FileInfo> list = JFileUtil.scanFile(ScanActivity.this, types);
                // 过滤分类
                ArrayList<FileInfo>[] arrays = JFileUtil.filter(list, types);
                //
                final ArrayList<Book> books = new ArrayList<Book>();
                for (FileInfo info : arrays[0]) {
                    Book book = new Book();
                    book.name = info.name;
                    book.path = info.path;
                    books.add(book);
                }

                mRcyBook.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ScanAdapter(books);
                        mRcyBook.setAdapter(mAdapter);
                    }
                });

            }
        }).start();
    }

    private void findView() {
        mRcyBook = (RBRecyclerView) findViewById(R.id.rcy);
        mRcyBook.setLayoutManager(new LinearLayoutManager(this));
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        edtSearch = (EditText) findViewById(R.id.edt_search);
    }


}
