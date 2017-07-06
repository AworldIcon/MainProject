package com.coder.kzxt.question.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.delegate.TextImageDelegate;
import com.coder.kzxt.question.activity.QuestionReplyDetailsActivity;
import com.coder.kzxt.question.beans.QuesReplyDetailsListBean;
import com.coder.kzxt.question.beans.QuestionsReplyListBean;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by zw on 2017/4/25.
 */

public class QuestionReplyDetaisListDelegate extends PullRefreshDelegate<QuesReplyDetailsListBean.ItemsBean> {
        private QuestionReplyDetailsActivity context;
        private QuestionsReplyListBean.ItemsBean headerBean;
        public QuestionReplyDetaisListDelegate(QuestionReplyDetailsActivity context,QuestionsReplyListBean.ItemsBean headerBean){
                super(R.layout.item_question_reply_head, R.layout.item_question_reply_list);
                this.context = context;
                this.headerBean=headerBean;
        }

        @Override
        public void initHeaderView(BaseViewHolder holder) {
                MyRecyclerView recyclerView=holder.findViewById(R.id.content_recycler_view);

                View header_fenge=holder.findViewById(R.id.header_fenge);
                ImageView best_answer_image=holder.findViewById(R.id.best_answer_image);
                TextView time_fore=holder.findViewById(R.id.time_fore);
//                TextView content=holder.findViewById(R.id.content);
//                content.setText(headerBean.getContent());
                recyclerView.setAdapter(new BaseRecyclerAdapter(context,headerBean.getContent(),new TextImageDelegate(context)));

                time_fore.setText(DateUtil.getDayBef(headerBean.getCreate_time()));
                if(headerBean.getIs_best_answer().equals("0")){
                        best_answer_image.setVisibility(View.GONE);
                }else {
                        best_answer_image.setVisibility(View.VISIBLE);
                }

                header_fenge.setVisibility(View.VISIBLE);
        }

        @Override
        public void initCustomView(BaseViewHolder holder, List<QuesReplyDetailsListBean.ItemsBean> data, int position) {
                final ImageView user_head_img=holder.findViewById(R.id.reply_header);
                ImageView best_answer_image=holder.findViewById(R.id.best_answer_image);
                TextView reply_name=holder.findViewById(R.id.reply_name);
                TextView user_name=holder.findViewById(R.id.user_name);
                TextView reply_tex=holder.findViewById(R.id.reply_tex);
                user_name.setVisibility(View.VISIBLE);
                reply_tex.setVisibility(View.VISIBLE);
                TextView time_fore=holder.findViewById(R.id.time_fore);
                TextView content=holder.findViewById(R.id.content);
                content.setVisibility(View.VISIBLE);
                content.setMaxLines(100);//因为item模板复用的，且这里的item显示全部，所以设置个100
                reply_name.setText(data.get(position).getReplyid().getNickname());
                user_name.setText(data.get(position).getUser().getNickname());
//                if(headerBean.getUser_id().equals(data.get(position).getReplyid().getId()+"")){
//                        reply_name.setVisibility(View.GONE);
//                        reply_tex.setVisibility(View.GONE);
//                }else {
//                        reply_name.setVisibility(View.VISIBLE);
//                        reply_tex.setVisibility(View.VISIBLE);
//                }
                if(data.get(position).getContent().size()>0){
                        content.setText(data.get(position).getContent().get(0).getContent());
                }
                time_fore.setText(DateUtil.getDayBef(data.get(position).getCreate_time()));

                best_answer_image.setVisibility(View.GONE);
                GlideUtils.loadCircleHeaderOfCommon(context,data.get(position).getUser().getAvatar(),user_head_img);

//                Glide.with(context).load(data.get(position).getUser().getAvatar()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header).error(R.drawable.default_teacher_header).centerCrop().into(new BitmapImageViewTarget(user_head_img) {
//
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                                circularBitmapDrawable.setCornerRadius(25);
//                                user_head_img.setImageDrawable(circularBitmapDrawable);
//                        }
//
//                });
        }
}
