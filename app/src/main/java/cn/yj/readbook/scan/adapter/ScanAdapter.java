package cn.yj.readbook.scan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ArrayList<Book> realBooks;

    public ScanAdapter(ArrayList<Book> books) {
        this.books = books;
        realBooks = (ArrayList<Book>) books.clone();
        selectedBooks = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return realBooks == null ? 0 : realBooks.size();
    }

    @Override
    public FEViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan, parent, false);
        return FEViewHolder.newInstance(item);
    }

    @Override
    public void onBindHolder(View itemView, int position) {
        final Book book = realBooks.get(position);
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

    public void filter(String keyWord, boolean isFilterEnglishTitle, boolean isFilterNumberTitle ) {
        this.keyWord = keyWord;
        realBooks.clear();
        // 只保留keyword
        for (Book book : books) {
            if (book.name.contains(keyWord)) {
                realBooks.add(book);
            }
        }
        // 是否过滤英文标题
        if (isFilterEnglishTitle) {
            String regex = ".*[a-zA-Z]+.*";
            filterBook(regex);
        }
        //是否过滤数字标题
        if (isFilterNumberTitle) {
            String regex = ".*\\d+.*";
            filterBook(regex);
        }
    }

    private void filterBook(String regex) {
        List<Book> filterBoos = new ArrayList<>();
        for (int i = 0; i < realBooks.size(); i++) {
            Book book = realBooks.get(i);
            String name = book.name.replace(".txt", "");
            Matcher m = Pattern.compile(regex).matcher(name);
            if (m.matches()) {
                filterBoos.add(book);
            }
        }
        for (Book book : filterBoos) {
            realBooks.remove(book);
        }
    }


}
