package cn.yj.readbook.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import cn.yj.readbook.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yangjie on 2017/3/24.
 */

public abstract class RBRecyclerAdapter<T> extends RecyclerView.Adapter {
    private boolean isShowEmpty;
    private ISetTopView setTopViewlmpl;
    private Object topViewData;
    private String tipString;

    public enum EmptyType {
        ITEM,// 空数据的展示形式为一个item的高度
        ALL // 空数据的展示形式为整个Recycler的高度
    }

    ;
    private EmptyType type = EmptyType.ALL;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    // 设置展示tpye的形式
    public void setEmptyType(EmptyType type) {
        this.type = type;
    }

    // 设置空数据的提醒字符串
    public void setTipString(String tipString) {
        this.tipString = tipString;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (position == 0) {
            ((FEViewHolder) holder).showEmpty(isShowEmpty, type, tipString);
        }
        if (!isShowEmpty) {
            onBindHolder(((FEViewHolder) holder).getView(), position);
            holder.itemView.setTag(position);
        }

    }

    @Override
    public int getItemCount() {
        if (getCount() == 0) {
            isShowEmpty = true;
            return 1;
        } else {
            isShowEmpty = false;
            return getCount();
        }
    }


    public static class FEViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout parent;

        public static FEViewHolder newInstance(View view) {
            // 搞事情
            LinearLayout parent = new LinearLayout(view.getContext());
            parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(view);
            View empty = LayoutInflater.from(view.getContext()).inflate(R.layout.empty_data_view, null);
            empty.setVisibility(View.GONE);
            parent.addView(empty, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            return new FEViewHolder(parent);
        }

        public FEViewHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout) itemView;
        }

        public View getView() {
            return parent.getChildAt(0);
        }

        private void showEmpty(boolean isShowEmpty, EmptyType type, String tipString) {
            // tipString
            if (tipString != null) {
               TextView tvTip = (TextView) parent.getChildAt(1).findViewById(R.id.tv_tip);
                tvTip.setText(tipString);
            }

            if (isShowEmpty) {
                parent.getChildAt(0).setVisibility(View.GONE);
                parent.getChildAt(1).setVisibility(View.VISIBLE);
                if (type == EmptyType.ALL) {
                    parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                } else if (type == EmptyType.ITEM) {
                    parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                }
                parent.getChildAt(1).startAnimation(getAlphaAnimation(0, 1, 700));
            } else {
                parent.getChildAt(0).setVisibility(View.VISIBLE);
                parent.getChildAt(1).setVisibility(View.GONE);
                parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                parent.getChildAt(1).startAnimation(getAlphaAnimation(1, 0, 700));

            }
        }
    }

    private static AlphaAnimation getAlphaAnimation(int from, int to, int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(from, to);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }


    public abstract int getCount();

    public abstract FEViewHolder onCreateHolder(ViewGroup parent, int viewType);

    public abstract void onBindHolder(View itemView, int position);


// **********************************  topView *************************************//

    public void setTopViewData(Object firstViewData) {
       this.topViewData = firstViewData;
    }

    private void addTopViewData() {
        if (setTopViewlmpl != null) {
            if (topViewData != null && setTopViewlmpl.getDataSource() != null && !setTopViewlmpl.getUniqueValue(setTopViewlmpl.getDataSource().get(0)).equals(setTopViewlmpl.getUniqueValue(topViewData))){
                setTopViewlmpl.getDataSource().add(0, topViewData);
            }
        }
    }

    // 在
    public void clearTopViewData() {
        topViewData = null;
    }

    public boolean hasTopView() {
        return  topViewData != null;
    }

    public Object getTopViewData() {
        return topViewData;
    }


    protected void editModel(List<T> o) {
        filterTopViewData(o);
        addTopViewData();
    }


    public List<T> filterTopViewData(List<T> sourceData) {
        if (topViewData != null) {
            if (setTopViewlmpl != null) {
                Iterator<T> it = sourceData.iterator();
                while (it.hasNext()) {
                    T t = it.next();
                    if (setTopViewlmpl.getUniqueValue(topViewData).equals(setTopViewlmpl.getUniqueValue(t))) {
                        it.remove();
                    }
                }
            }
        }
        return sourceData;
    }

   public interface ISetTopView<T>{
       Object getUniqueValue(T o);
       List<T> getDataSource();
   }

    public void setTopViewListener(ISetTopView setFirstViewlmpl) {
        this.setTopViewlmpl = setFirstViewlmpl;
    }
}
