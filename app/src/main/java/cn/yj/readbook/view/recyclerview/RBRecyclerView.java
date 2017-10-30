package cn.yj.readbook.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Created by yangjie on 2017/3/21.
 */

public class RBRecyclerView extends RecyclerView{
    private boolean hasAddScrollListener;
    private LoadMoreListener loadMoreListener;

    public RBRecyclerView(Context context) {
        super(context);
    }
    public RBRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RBRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        loadMoreListener = listener;
    }

    public void setCanLoadMore(boolean flag) {
        if (flag) {
            addLoadMoreEvent();
        } else {
            removeLoadMoreEvent();
        }
    }

    private void addLoadMoreEvent(){
        if (!hasAddScrollListener) {
            hasAddScrollListener = true;
            this.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) this.getLayoutManager()) {
                @Override
                public void onLoadMore(int currentPage) {
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore();
                    }
                }
            });
        } else {
            removeLoadMoreEvent();
            addLoadMoreEvent();
        }
    }

    private void removeLoadMoreEvent(){
        this.clearOnScrollListeners();
        hasAddScrollListener = false;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

}
