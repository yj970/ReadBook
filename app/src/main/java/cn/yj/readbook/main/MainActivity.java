package cn.yj.readbook.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import cn.yj.readbook.Constant;
import cn.yj.readbook.R;
import cn.yj.readbook.base.BaseActivity;
import cn.yj.readbook.base.DataManager;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.scan.ScanActivity;
import cn.yj.readbook.view.recyclerview.RBRecyclerView;

public class MainActivity extends BaseActivity {
    private ImageView ivScan;
    private RBRecyclerView mRcvBook;
    private MainAdapter mainAdapter;
    private long exitTime = 0;
    private DataManager dataManager;

    public static void startMainActivity(Activity activity, List<Book> books, int flags) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(flags);
        intent.putExtra(Constant.EXTRA_BOOKSHELF, (Serializable) books);
        activity.startActivity(intent);
    }

    public static void startMainActivity(Activity activity, List<Book> books) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Constant.EXTRA_BOOKSHELF, (Serializable) books);
        activity.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        bindData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainAdapter != null) {
            mainAdapter.notifyDataSetChanged();
        }
    }

    private void setListener() {
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.startScanActivityForResult(MainActivity.this, Constant.CODE_SCAN);
            }
        });
    }

    private void bindData() {
        dataManager = DataManager.getInstance();
        dataManager.bookshelf.clear();

        if (getIntent().hasExtra(Constant.EXTRA_BOOKSHELF)) {
            List<Book> selectedBooks = (List<Book>) getIntent().getSerializableExtra(Constant.EXTRA_BOOKSHELF);
            dataManager.bookshelf.addAll(selectedBooks);
        }
        mainAdapter = new MainAdapter(MainActivity.this, dataManager.bookshelf);
        mRcvBook.setAdapter(mainAdapter);
    }

    private void findView() {
        mRcvBook = (RBRecyclerView) findViewById(R.id.rcv);
        mRcvBook.setLayoutManager(new GridLayoutManager(this, 3));
        ivScan = (ImageView) findViewById(R.id.iv_scan);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataManager.save();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CODE_SCAN:
                    List<Book> selectedBooks = (List<Book>) data.getSerializableExtra(Constant.EXTRA_ADD_TO_BOOKSHELF);
                    dataManager.bookshelf.addAll(selectedBooks);
                    mainAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
