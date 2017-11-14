package cn.yj.readbook.read;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.yj.readbook.Constant;
import cn.yj.readbook.R;
import cn.yj.readbook.base.BaseActivity;
import cn.yj.readbook.base.DataManager;
import cn.yj.readbook.base.SettingManager;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.read.interfaces.ScrollViewListener;
import cn.yj.readbook.read.view.ObservableScrollView;
import cn.yj.readbook.utils.CommonUtil;
import cn.yj.readbook.utils.TypefaceUtil;
import cn.yj.readbook.utils.fileUtil.FileUtil;
import cn.yj.readbook.view.dialog.CommonDialog;

/**
 * Created by yangjie on 2017/10/23.
 */

public class ReadActivity extends BaseActivity {
    private TextView mTvBook;
    private ObservableScrollView mObservableScrollView;
    private int scrollY;
    private int totalY;
    private Handler handler = new Handler();
    private DataManager dataManager;
    private String bookPath;
    private RelativeLayout mRlBottomBar;
    private RelativeLayout mRlTopBar;
    private RelativeLayout mRlSet;
    private LinearLayout mRlSettingBar;
    private SeekBar mSbProgress;
    private TextView mTvProgress;
    private TextView mTvBookName;
    private Boolean canChangeBar = true;
    private LinearLayout mLlTextSize;
    private TextView mTvTextSize;
    private LinearLayout mLlReadModel;
    private TextView mTvReadModel;
    private LinearLayout mLlTypeface;
    private TextView mTvTypeface;
    private SettingManager settingManager;
    private Book book;
    private RelativeLayout mRlToast;
    private TextView mTvToast;
    private float brightness;
    private final float maxBrightness = 255f;

    public static void startReadActivity(Activity activity, Book book) {
        Intent intent = new Intent(activity, ReadActivity.class);
        intent.putExtra(Constant.EXTRA_READ_BOOK, book);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        findView();
        bindData();
        setListener();

    }



    public void load() {
        // 重新载入历史记录（书的浏览位置）
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                totalY = mObservableScrollView.getChildAt(0).getBottom();
                double height = book.progress * totalY - mObservableScrollView.getMeasuredHeight();
                mObservableScrollView.scrollTo(0, (int) height);
                Toast.makeText(ReadActivity.this, "已载入已读记录", Toast.LENGTH_SHORT).show();
            }
        }, 50);
    }

    private void setListener() {
        mTvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRlSettingBar.getVisibility() == View.VISIBLE) {
                    hideSettingBar();
                    return;
                }

                if (mRlBottomBar.getVisibility() == View.GONE) {
                    showBar();
                } else {
                    hideBar();
                }
            }
        });

        mRlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBar();
                showSettingBar();
            }
        });

        mLlTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialog
                CommonDialog dialog = new CommonDialog(ReadActivity.this, settingManager.keys, settingManager.textSizeValues);
                dialog.setiItemClickListener(new CommonDialog.IItemClickListener() {
                    @Override
                    public void onItemClick(int position, String key, Object value) {
                        book.progress = getProgress();
                        settingManager.setTextSize((Integer) value);
                        settingManager.save();
                        resetFontView();
                        load();
                    }
                });
                dialog.show();
            }
        });

        mLlReadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(ReadActivity.this, settingManager.readModelKeys, settingManager.readModelValues);
                dialog.setiItemClickListener(new CommonDialog.IItemClickListener() {
                    @Override
                    public void onItemClick(int position, String key, Object value) {
                        settingManager.setReadModel((Integer) value);
                        settingManager.save();
                        resetFontView();
                    }
                });
                dialog.show();
            }
        });

        mLlTypeface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(ReadActivity.this, settingManager.typefaceKeys, settingManager.typefacePathValues);
                dialog.setiItemClickListener(new CommonDialog.IItemClickListener() {
                    @Override
                    public void onItemClick(int position, String key, Object value) {
                        settingManager.setTypeface(position);
                        settingManager.save();
                        resetFontView();
                    }
                });
                dialog.show();
            }
        });

    }

    /**
     * 重设字体
     */
    private void resetFontView() {
        int textSize = settingManager.getTextSize();
        int rowSpacing = settingManager.getRowSpacing();
        mTvBook.setTextSize(textSize);
        mTvBook.setLineSpacing(rowSpacing, 1);
        mTvTextSize.setText(settingManager.getTextSizeString());
        mTvReadModel.setText(settingManager.readModelString);
        mTvBook.setTextColor(getResources().getColor(settingManager.textColor));
        mTvBook.setBackgroundResource(settingManager.backgroundColor);
        findViewById(R.id.status_bar).setBackgroundColor(getResources().getColor(settingManager.backgroundColor));
        // 字体
        TypefaceUtil tfUtil = new TypefaceUtil(this, settingManager.getTypefacePath());
        tfUtil.setTypeface(mTvBook, false);
        mTvTypeface.setText(settingManager.getTypefaceKey());
    }

    private void hideBar() {
        mRlTopBar.setVisibility(View.GONE);
        mRlBottomBar.setVisibility(View.GONE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, 0, CommonUtil.dp2px(this, 80));
        bottomAnimation.setDuration(300);
        mRlBottomBar.startAnimation(bottomAnimation);
        TranslateAnimation topAnimation = new TranslateAnimation(0, 0, 0, -CommonUtil.dp2px(this, 80));
        topAnimation.setDuration(300);
        mRlTopBar.startAnimation(topAnimation);
    }

    private void showBar() {
        setSeekBarProgress();
        mRlTopBar.setVisibility(View.VISIBLE);
        mRlBottomBar.setVisibility(View.VISIBLE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, CommonUtil.dp2px(this, 80), 0);
        bottomAnimation.setDuration(300);
        mRlBottomBar.startAnimation(bottomAnimation);
        TranslateAnimation topAnimation = new TranslateAnimation(0, 0, -CommonUtil.dp2px(this, 80), 0);
        topAnimation.setDuration(300);
        mRlTopBar.startAnimation(topAnimation);
    }

    private void setSeekBarProgress() {
        mSbProgress.setOnSeekBarChangeListener(null);
        mSbProgress.setProgress((int) (getProgress() * 100));
        mSbProgress.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void showSettingBar() {
        mRlSettingBar.setVisibility(View.VISIBLE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, CommonUtil.dp2px(this, 160), 0);
        bottomAnimation.setDuration(300);
        mRlBottomBar.startAnimation(bottomAnimation);
    }

    private void hideSettingBar() {
        mRlSettingBar.setVisibility(View.GONE);
        TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, 0, CommonUtil.dp2px(this, 160));
        bottomAnimation.setDuration(300);
        mRlSettingBar.startAnimation(bottomAnimation);
    }

    private void bindData() {
        // 屏幕亮度
        try {
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "获取系统亮度失败!", Toast.LENGTH_SHORT).show();
        }
        dataManager = DataManager.getInstance();
        settingManager = SettingManager.getInstance();
        settingManager.load();
        mObservableScrollView.setScrollViewListener(scrollViewListener);
        resetFontView();

        if (getIntent().hasExtra(Constant.EXTRA_READ_BOOK)) {
            book = (Book) getIntent().getSerializableExtra(Constant.EXTRA_READ_BOOK);
            bookPath = book.path;
            String content = FileUtil.ReadTxtFile(book.path);
            mTvBook.setText(content);
            mTvBookName.setText(book.name.replace(".txt", ""));
            load();
        }


    }

    private void findView() {
        mTvBook = (TextView) findViewById(R.id.tv_book);
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.osv);
        mRlBottomBar = (RelativeLayout) findViewById(R.id.rl_bottom_bar);
        mRlTopBar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        mRlSettingBar = (LinearLayout) findViewById(R.id.rl_setting_bar);
        mSbProgress = (SeekBar) findViewById(R.id.sb_progress);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mTvBookName = (TextView) findViewById(R.id.tv_name);
        mRlSet = (RelativeLayout) findViewById(R.id.rl_set);
        mLlTextSize = (LinearLayout) findViewById(R.id.ll_text_size);
        mTvTextSize = (TextView) findViewById(R.id.tv_text_size);
        mLlReadModel = (LinearLayout) findViewById(R.id.ll_read_model);
        mTvReadModel = (TextView) findViewById(R.id.tv_read_model);
        mLlTypeface = (LinearLayout) findViewById(R.id.ll_typeface);
        mTvTypeface = (TextView) findViewById(R.id.tv_tpyeface);
        mRlToast = (RelativeLayout) findViewById(R.id.rl_toast);
        mTvToast = (TextView) findViewById(R.id.tv_toast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalY != 0) {
            saveBookProgress();
        }
    }

    private void saveBookProgress() {
        for (Book book : dataManager.bookshelf) {
            if (book.path.equals(bookPath)) {
                book.progress = getProgress();
                break;
            }
        }
        dataManager.save();
    }

    /**
     * 获得当前的浏览百分比
     *
     * @return
     */
    private double getProgress() {
        double progress = (double) scrollY * 10000 / (double) totalY * 0.0001;
        return progress;
    }

    // seekBar 滑动监听
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 移动书的浏览位置
            double height = progress * 0.01 * totalY - mObservableScrollView.getMeasuredHeight();
            mObservableScrollView.scrollTo(0, (int) height);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            canChangeBar = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            canChangeBar = true;
        }
    };

    ScrollViewListener scrollViewListener = new ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
            ReadActivity.this.scrollY = y + scrollView.getMeasuredHeight();
            mTvProgress.setText("进度: " + (int) (getProgress() * 100) + "%");

            // 滑动手势
            if (canChangeBar) {
                if (mRlSettingBar.getVisibility() == View.VISIBLE) {
                    hideSettingBar();
                }

                if (y - oldy > 0) {
                    if (mRlBottomBar.getVisibility() == View.VISIBLE) {
                        hideBar();
                    }
                }
            }
        }
    };


    /* 改变App当前Window亮度
    * @param i  true:增加亮度 flag：减小亮度
    * */

    public float changeAppBrightness(boolean flag) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (flag) {
            brightness += 10;
        } else {
            brightness -= 10;
        }
        if (brightness <= 0) {
            brightness = 0;
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
        } else if (brightness >= maxBrightness) {
            brightness = maxBrightness;
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / maxBrightness;
        }
        window.setAttributes(lp);
        return brightness;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRlBottomBar.getVisibility() == View.GONE) {
                showBar();
                return true;
            }
        }
        // 监听音量键
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            changeBrightness(true);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            changeBrightness(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeBrightness(boolean f) {
        float brightness = changeAppBrightness(f);
        mTvToast.setText("亮度 " + (int)(brightness*100/maxBrightness)+"%");
        mRlToast.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        mRlToast.startAnimation(alphaAnimation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRlToast.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(500);
                        mRlToast.startAnimation(alphaAnimation);
                        mRlToast.setVisibility(View.GONE);
                    }
                }, 500);
            }
        }).start();
    }
}
