package cn.yj.readbook.find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yj.readbook.R;
import cn.yj.readbook.base.bean.Book;
import cn.yj.readbook.utils.CommonUtil;
import cn.yj.readbook.view.recyclerview.RBRecyclerAdapter;

public class FindAdapter extends RBRecyclerAdapter {

    private List<File> data;
    IClickListener clickListenerImpl;
    private ArrayList<Book> selectedBooks;
    private List<String> selectedBooksPath;
    private List<String> lastPathList;// 上一级目录
    private Context context;
    IPathChangeListener pathChangeImpl;

    public FindAdapter(Context context) {
        selectedBooks = new ArrayList<>();
        selectedBooksPath = new ArrayList<>();
        lastPathList = new ArrayList<>();
        this.context = context;
    }

    // 是否属于txt文件
    private boolean isTxt(File file) {
        int lastIndex = file.getAbsolutePath().lastIndexOf(".");
        String type = file.getAbsolutePath().substring(lastIndex + 1, file.getAbsolutePath().length());
        if (type.equals("txt")) {
            return true;
        }
        return false;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public FEViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find, parent, false);
        return FEViewHolder.newInstance(view);
    }

    @Override
    public void onBindHolder(View itemView, final int position) {
        ViewHolder holder = new ViewHolder(itemView);
        int lastIndex = data.get(position).getAbsolutePath().lastIndexOf("/");
        holder.textView.setText(data.get(position).getAbsolutePath().substring(lastIndex, data.get(position).getAbsolutePath().length()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = data.get(position);
                if (file.isDirectory()) {
                    // 保存此时的文件路径
                    lastPathList.add(file.getParent());
                    data = CommonUtil.getFiles(file.getAbsolutePath());
                    if (pathChangeImpl != null) {
                        pathChangeImpl.onPathChange(data);
                    }
                } else {
                    // 目前只支持txt文件
                    if (!isTxt(file)) {
                        Toast.makeText(context, "抱歉，目前只支持加载txt类型的文件!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!selectedBooksPath.contains(file.getAbsolutePath())) {
                        selectedBooksPath.add(file.getAbsolutePath());
                        Book book = new Book();
                        book.name = file.getName();
                        book.path = file.getAbsolutePath();
                        book.progress = 0;
                        selectedBooks.add(book);
                    } else {
                        selectedBooksPath.remove(file.getAbsolutePath());
                        for (Book book : selectedBooks) {
                            if (book.path.equals(file.getAbsolutePath())) {
                                selectedBooks.remove(book);
                                break;
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        File file = data.get(position);
        if (file.isDirectory()) {
            holder.cbSelect.setVisibility(View.GONE);
        } else {
            holder.cbSelect.setVisibility(View.VISIBLE);
        }
        if (selectedBooksPath.contains(file.getAbsolutePath())) {
            holder.cbSelect.setChecked(true);
        } else {
            holder.cbSelect.setChecked(false);
        }
    }

    public void setData(List<File> data) {
        this.data = data;
    }

    public List<Book> getSelectBooks() {
        return selectedBooks;
    }

    // 只显示含有关键字的数据
    public void filterKey(String key) {
        List<File> temp = new ArrayList<>();
        for (File file : data) {
            if (file.getName().contains(key)) {
                temp.add(file);
            }
        }
        setData(temp);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RBRecyclerAdapter.FEViewHolder {
        TextView textView;
        CheckBox cbSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cb_select);
            textView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface IClickListener {
        void onClick(File file);
    }

    public void setClickListenerImpl(IClickListener clickListenerImpl) {
        this.clickListenerImpl = clickListenerImpl;
    }

    public interface IPathChangeListener {
        void onPathChange(List<File> data);
    }

    public void setPathChange(IPathChangeListener pathChangeImpl) {
        this.pathChangeImpl = pathChangeImpl;
    }

    // 返回null，证明已经是根目录了，不能返回了。
    public String getLastPath() {
        if (lastPathList.size() > 0) {
            String lastPath = lastPathList.get(lastPathList.size() - 1);
            lastPathList.remove(lastPathList.size() - 1);
            return lastPath;
        }
        return null;
    }

}
