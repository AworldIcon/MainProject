package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.FinishNum;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by pc on 2017/3/2.
 */

public class UnFinishWorkAdapter extends RecyclerView.Adapter<UnFinishWorkAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context context;
    private LayoutInflater inflater;
    private RecyclerView mRecyclerView;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private FinishNum finishNum;
    public UnFinishWorkAdapter(Context context, FinishNum finishNum) {
        this.finishNum=finishNum;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
    @Override
    public UnFinishWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
            itemView = inflater.inflate(R.layout.unfinish_work, null);
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        return new UnFinishWorkAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UnFinishWorkAdapter.ViewHolder holder, int position) {
            holder.nickname.setText(finishNum.getData().getDoing().get(position).getNickname());

             ImageLoad.loadImage(context,finishNum.getData().getDoing().get(position).getSmallAvatar(),R.drawable.default_famous_teacher,R.drawable.default_famous_teacher,90, RoundedCornersTransformation.CornerType.ALL,holder.headImage);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public int getItemCount() {
        return finishNum==null?0:finishNum.getData().getDoing().size();
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView != null) {
            // 通过传递点击的View计算出点击的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            if (listener != null) {
                listener.onItemClick(position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mRecyclerView != null) {
            // 通过传递点击的View计算出点击的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position);
            }
        }
        return true;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;
        TextView nickname;
        public ViewHolder(View view) {
            super(view);
                headImage= (ImageView) view.findViewById(R.id.unhead_Img);
                nickname= (TextView) view.findViewById(R.id.unfinish_nickname);


        }
    }
    /**
     * 接口回调的步骤
     * ① 定义接口，接口中定义具体的方法
     * ② 声明接口，在我们数据产生的地方声明并持有接口的引用，调用接口中的方法
     * ③ 在我们需要处理数据的地方，实现接口，并将接口传递给数据产生的地方
     */

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

}
