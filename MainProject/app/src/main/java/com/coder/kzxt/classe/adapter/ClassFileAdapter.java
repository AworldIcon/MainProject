package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassFileBean;
import com.coder.kzxt.utils.DateUtil;
import java.util.ArrayList;

/**
 * 文件适配器
 * Created by wangtingshun on 2017/3/15.
 */

public class ClassFileAdapter extends RecyclerView.Adapter<ClassFileAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ClassFileBean.ClassFile> classFiles;

    public ClassFileAdapter(Context context,ArrayList<ClassFileBean.ClassFile> files) {
        this.mContext = context;
        this.classFiles = files;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_class_file_share, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassFileBean.ClassFile classFile = classFiles.get(position);
        holder.fileImage.setVisibility(View.VISIBLE);
        holder.fileName.setText(classFile.getTitle());
        holder.fileSize.setText(classFile.getSize());
        holder.fileType.setText(classFile.getUserName());
        holder.fileDate.setText(DateUtil.getDiffTime(Long.parseLong(classFile.getTime())));
        if(onItemClickListener != null){
            holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onItemLongClick(classFile);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classFiles.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        //文件头像
        private ImageView fileImage;
        //文件名
        private TextView fileName;
        //文件大小
        private TextView fileSize;
        //类型
        private TextView fileType;
        //日期
        private TextView fileDate;
        private RelativeLayout rl_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            fileImage = (ImageView) itemView.findViewById(R.id.iv_share_file);
            fileName = (TextView) itemView.findViewById(R.id.tv_share_file);
            fileSize = (TextView) itemView.findViewById(R.id.tv_size);
            fileType = (TextView) itemView.findViewById(R.id.tv_share_file_type);
            fileDate = (TextView) itemView.findViewById(R.id.tv_share_file_date);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.re_share_file);
        }
    }
    private OnItemClickInterface onItemClickListener;

    public interface OnItemClickInterface{
        void onItemLongClick(ClassFileBean.ClassFile classFile);
    }

    public void setOnItemClickListener(OnItemClickInterface listener){
        this.onItemClickListener = listener;
    }
}
