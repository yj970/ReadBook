package cn.yj.readbook.scan.v;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yj.readbook.Constant;
import cn.yj.readbook.R;
import cn.yj.readbook.base.BaseActivity;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.event.MainActivityResumeEvent;
import cn.yj.readbook.find.FindFileActivity;
import cn.yj.readbook.main.MainActivity;
import cn.yj.readbook.scan.adapter.ScanAdapter;
import cn.yj.readbook.scan.p.ScanPresenterImpl;
import cn.yj.readbook.utils.CommonUtil;
import cn.yj.readbook.view.recyclerview.RBRecyclerView;

/**
 * Created by yangjie on 2017/10/23.
 */

public class ScanActivity extends BaseActivity implements ScanView{
    private RBRecyclerView mRcyBook;
    private ScanAdapter mAdapter;
    private Button mBtnAdd;
    private RelativeLayout mRlSetting;
    private RelativeLayout mRlAdd;
    private ImageView ivLeft;
    private LinearLayout mLlBottomSettingBar;
    private SwitchButton mSbFilterEnglishTitle;
    private SwitchButton mSbFilterNumberTitle;

    private ScanPresenterImpl scanPresenter;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void scan() {
        scanPresenter.scan();
    }

    @Override
    public void showData(ArrayList<Book> books) {
        mAdapter = new ScanAdapter(books);
        adapterFilterData();
        mRcyBook.setAdapter(mAdapter);
    }


    @Override
    public void add2BookShelf() {
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

    @Override
    public void save() {
        scanPresenter.save();
    }

    private enum Type {TYPE1, TYPE2}
    private Type type = Type.TYPE1;
    private EditText edtSearch;

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
        EventBus.getDefault().register(this);

        findView();
        bindData();
        setListener();
    }


    private void setListener() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               add2BookShelf();
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
                adapterFilterData();
                mAdapter.notifyDataSetChanged();
            }
        });

        mRlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLlBottomSettingBar.getVisibility() == View.VISIBLE) {
                    hideBar();
                } else {
                    showBar();
                }
            }
        });

        mRcyBook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mLlBottomSettingBar.getVisibility() == View.VISIBLE) {
                    hideBar();
                    return true;
                }
                return false;
            }
        });

        mSbFilterEnglishTitle.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                scanPresenter.setFilterEnglishTitleChecked(isChecked);
                adapterFilterData();
                mAdapter.notifyDataSetChanged();
            }
        });

        mSbFilterNumberTitle.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                scanPresenter.setFilterNumberTitleChecked(isChecked);
                adapterFilterData();
                mAdapter.notifyDataSetChanged();
            }
        });

        mRlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScanActivity.this, FindFileActivity.class));
            }
        });

    }

    private void hideBar() {
        mLlBottomSettingBar.setVisibility(View.GONE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, 0, CommonUtil.dp2px(this, 160));
        bottomAnimation.setDuration(300);
        mLlBottomSettingBar.startAnimation(bottomAnimation);
    }

    private void showBar() {
        mLlBottomSettingBar.setVisibility(View.VISIBLE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, CommonUtil.dp2px(this, 160), 0);
        bottomAnimation.setDuration(300);
        mLlBottomSettingBar.startAnimation(bottomAnimation);
    }

    private void bindData() {
        scanPresenter = new ScanPresenterImpl(this);
        ((TextView) findViewById(R.id.tv_title)).setText("本地书库");
        mSbFilterEnglishTitle.setChecked(scanPresenter.getFilterEnglishTitleChecked());
        mSbFilterNumberTitle.setChecked(scanPresenter.getFilterNumberTitleChecked());
        ivLeft.setImageResource(R.mipmap.add);

        if (getIntent().hasExtra(Constant.EXTRA_SCAN_TYPE)) {
            type = (Type) getIntent().getSerializableExtra(Constant.EXTRA_SCAN_TYPE);
        }

        MPermissions.requestPermissions(this, Constant.PERMISSION_CODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(Constant.PERMISSION_CODE_STORAGE)
    public void requestSdcardSuccess() {
        scan();
    }

    @PermissionDenied(Constant.PERMISSION_CODE_STORAGE)
    public void requestSdcardFailed() {
        Toast.makeText(this, "已拒绝授予SD卡的读取权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void accept(MainActivityResumeEvent event){
        finish();
    }

    private void adapterFilterData() {
        if (mAdapter != null) {
            mAdapter.filter(edtSearch.getText().toString(), scanPresenter.getFilterEnglishTitleChecked(), scanPresenter.getFilterNumberTitleChecked());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanPresenter.save();
    }

    private void findView() {
        mRcyBook = (RBRecyclerView) findViewById(R.id.rcy);
        mRcyBook.setLayoutManager(new LinearLayoutManager(this));
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        mRlSetting = (RelativeLayout) findViewById(R.id.rl_set);
        mLlBottomSettingBar = (LinearLayout) findViewById(R.id.ll_setting_bar);
        mSbFilterEnglishTitle = (SwitchButton) findViewById(R.id.sb_filter_english_title);
        mSbFilterNumberTitle = (SwitchButton) findViewById(R.id.sb_filter_number_title);
        mRlAdd = (RelativeLayout) findViewById(R.id.rl_left);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLlBottomSettingBar.getVisibility() == View.VISIBLE) {
                hideBar();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
