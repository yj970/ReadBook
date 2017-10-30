package cn.yj.readbook.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.yj.readbook.R;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.view.recyclerview.RBRecyclerAdapter;

/**
 * Created by yangjie on 2017/10/23.
 */

public class BaseAdapter extends RBRecyclerAdapter<BaseAdapter.ViewHolder> {
    private List<Book> books;

    public BaseAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getCount() {
        return books == null ? 0 : books.size();
    }

    @Override
    public FEViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan, parent, false);
        return FEViewHolder.newInstance(item);
    }

    @Override
    public void onBindHolder(View itemView, int position) {
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.mTvName.setText(books.get(position).name);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
