package cn.yj.readbook.find;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import cn.yj.readbook.R;
import cn.yj.readbook.base.BaseActivity;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.event.MainActivityResumeEvent;
import cn.yj.readbook.main.MainActivity;
import cn.yj.readbook.utils.CommonUtil;

public class FindFileActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private FindAdapter adapter;
    private List<File> files;
    private TextView tvTitle;
    private RelativeLayout rlBack;
    private Button btnAdd;
    private EditText et_search;
    private RelativeLayout rlSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        EventBus.getDefault().register(this);
        // init
        rlSetting = (RelativeLayout) findViewById(R.id.rl_set);
        rlSetting.setVisibility(View.GONE);
        et_search = (EditText) findViewById(R.id.et_search);
        btnAdd = (Button) findViewById(R.id.btn_add);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("本机查找");
        rlBack = (RelativeLayout) findViewById(R.id.rl_left);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FindAdapter(this);
        recyclerView.setAdapter(adapter);

        // 查询根目录
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        files = CommonUtil.getFiles(rootPath);
        adapter.setData(files);
        adapter.notifyDataSetChanged();

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastPath  = adapter.getLastPath();
                if (lastPath != null) {
                    adapter.setData(CommonUtil.getFiles(lastPath));
                    adapter.notifyDataSetChanged();
                } else {
                    finish();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Book> selectedBooks = adapter.getSelectBooks();
                MainActivity.startMainActivity(FindFileActivity.this, selectedBooks);
                finish();
            }
        });

        adapter.setPathChange(new FindAdapter.IPathChangeListener() {
            @Override
            public void onPathChange(List<File> data) {
                files = data;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString();
                if (TextUtils.isEmpty(key)) {
                    adapter.setData(files);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.filterKey(key);
                }
            }
        });
    }

    @Subscribe
    public void accept(MainActivityResumeEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
