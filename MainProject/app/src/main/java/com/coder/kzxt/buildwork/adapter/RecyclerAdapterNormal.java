//package com.coder.kzxt.buildwork.adapter;
//
//import android.content.Context;
//import android.os.Build;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.buildwork.entity.WorkManage;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by pc on 2017/2/15.
// */
//
//public class RecyclerAdapterNormal extends RecyclerView.Adapter<RecyclerAdapterNormal.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
//
//    private static final String TAG = RecyclerAdapterNormal.class.getSimpleName();
//    private Context context;
//    private List<WorkManage.DataBean> data;
//    private LayoutInflater inflater;
//    private RecyclerView mRecyclerView;
//    private OnItemClickListener listener;
//    private OnItemLongClickListener longClickListener;
//    private int type;
//    public RecyclerAdapterNormal(Context context, List<WorkManage.DataBean> data,int type) {
//        this.data = data;
//        this.context=context;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.type=type;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }
//    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
//        this.longClickListener = longClickListener;
//    }
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = inflater.inflate(R.layout.work_manage_item, null);
//        itemView.setOnLongClickListener(this);
//        itemView.setOnClickListener(this);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
//        long lcc = Long.valueOf(data.get(position).getCreatedTime());
//        int i = Integer.parseInt(data.get(position).getCreatedTime());
//        String times = sdr.format(new Date(i * 1000L));
//        final String release=data.get(position).getRelease();
//        holder.textTile.setText(data.get(position).getTitle());
//        holder.work_num.setText(data.get(position).getItemCount());
//        holder.score.setText(data.get(position).getScore());
//        if(release.equals("0")){
//            holder.publish.setText("未开始");
//        }else {
//            holder.publish.setText("已发布");
//        }
//        if(type==1){
//            holder.create_time.setText("有效期至: "+times+"—"+times);
//            holder.classNames.setVisibility(View.VISIBLE);
//            holder.button.setVisibility(View.GONE);
//        }else {
//            holder.create_time.setText("创建时间: "+times);
//            holder.publish.setVisibility(View.GONE);
//            holder.classNames.setVisibility(View.INVISIBLE);
//            holder.button.setVisibility(View.VISIBLE);
//        }
//
//    }
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        mRecyclerView = recyclerView;
//    }
//
//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        mRecyclerView = null;
//    }
//
//    @Override
//    public int getItemCount() {
//        return data != null ? data.size() : 0;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mRecyclerView != null) {
//            // 通过传递点击的View计算出点击的位置
//            int position = mRecyclerView.getChildAdapterPosition(v);
//            if (listener != null) {
//                listener.onItemClick(position);
//            }
//        }
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        if (mRecyclerView != null) {
//            // 通过传递点击的View计算出点击的位置
//            int position = mRecyclerView.getChildAdapterPosition(v);
//            if (longClickListener != null) {
//                longClickListener.onItemLongClick(position);
//            }
//        }
//        return true;
//    }
//
//    public  class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textTile;
//        TextView create_time;
//        TextView work_num;
//        TextView score;
//        TextView publish;
//        TextView classNames;
//        TextView button;
//        public ViewHolder(View view) {
//            super(view);
//            textTile= (TextView) view.findViewById(R.id.work_title);
//            create_time= (TextView) view.findViewById(R.id.create_time);
//            work_num= (TextView) view.findViewById(R.id.work_num);
//            score= (TextView) view.findViewById(R.id.score);
//            publish= (TextView) view.findViewById(R.id.publish);
//            classNames= (TextView) view.findViewById(R.id.class_names);
//            button= (TextView) view.findViewById(R.id.button);
//        }
//    }
//    /**
//     * 接口回调的步骤
//     * ① 定义接口，接口中定义具体的方法
//     * ② 声明接口，在我们数据产生的地方声明并持有接口的引用，调用接口中的方法
//     * ③ 在我们需要处理数据的地方，实现接口，并将接口传递给数据产生的地方
//     */
//
//    public interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//    public interface OnItemLongClickListener{
//        void onItemLongClick(int position);
//    }
//
//}
