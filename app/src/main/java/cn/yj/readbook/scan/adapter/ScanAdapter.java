package cn.yj.readbook.scan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yj.readbook.R;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.view.recyclerview.RBRecyclerAdapter;

/**
 * Created by yangjie on 2017/10/23.
 */

public class ScanAdapter extends RBRecyclerAdapter<ScanAdapter.ViewHolder> {
    private ArrayList<Book> books;
    private ArrayList<Book> selectedBooks;
    private String keyWord = "";
    private ArrayList<Book> filterBooks;

    public ScanAdapter(ArrayList<Book> books) {
        this.books = books;
        filterBooks = (ArrayList<Book>) books.clone();
        selectedBooks = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filterBooks == null ? 0 : filterBooks.size();
    }

    @Override
    public FEViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan, parent, false);
        return FEViewHolder.newInstance(item);
    }

    @Override
    public void onBindHolder(View itemView, int position) {
        final Book book = filterBooks.get(position);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.mTvName.setText(book.name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !viewHolder.mCbSelect.isChecked();
                viewHolder.mCbSelect.setChecked(checked);
                if (checked) {
                    selectedBooks.add(book);
                } else {
                    selectedBooks.remove(book);
                }
            }
        });

        if (selectedBooks.contains(book)) {
            viewHolder.mCbSelect.setChecked(true);
        } else {
            viewHolder.mCbSelect.setChecked(false);
        }
    }

    public ArrayList<Book> getSelectedBooks() {
        return selectedBooks;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private CheckBox mCbSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mCbSelect = (CheckBox) itemView.findViewById(R.id.cb_select);
        }
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        filterBooks.clear();
        for (Book book : books) {
            if (book.name.contains(keyWord)) {
                filterBooks.add(book);
            }
        }
    }
}
