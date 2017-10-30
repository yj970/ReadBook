package cn.yj.readbook.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yj.readbook.R;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.read.ReadActivity;
import cn.yj.readbook.view.recyclerview.RBRecyclerAdapter;

/**
 * Created by yangjie on 2017/10/23.
 */

public class MainAdapter extends RBRecyclerAdapter<MainAdapter.ViewHolder> {
    private List<Book> books;
    private Activity activity;

    public MainAdapter(Activity activity, List<Book> books) {
        this.activity = activity;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books == null ? 0 : books.size();
    }

    @Override
    public FEViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return FEViewHolder.newInstance(item);
    }

    @Override
    public void onBindHolder(View itemView, final int position) {
        final Book book = books.get(position);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.mTvName.setText(book.name.replace(".txt", ""));

        if (position % 2 == 0) {
            viewHolder.mIvBook.setImageResource(R.mipmap.bg_1);
        } else {
            viewHolder.mIvBook.setImageResource(R.mipmap.bg_2);
        }

        String progress = String.valueOf(book.progress * 100);
        if (progress.length() > 5) {
            progress = progress.substring(0, 5);
        }
        viewHolder.mTvProgress.setText("已读 " + progress + "%");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadActivity.startReadActivity(activity, book);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("提示")
                        .setMessage("移除这个书本？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                books.remove(books.get(position));
                                notifyDataSetChanged();
                            }
                        })
                        .show();
                return false;
            }
        });
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName;
        private ImageView mIvBook;
        private TextView mTvProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mIvBook = (ImageView) itemView.findViewById(R.id.iv);
            mTvProgress = (TextView) itemView.findViewById(R.id.tv_proress);
        }
    }
}
