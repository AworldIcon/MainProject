package com.coder.kzxt.question.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.question.beans.QuestionDetailBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.MyGridView;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/14
 */
public class QuestionDetailDelegate extends PullRefreshDelegate<QuestionDetailBean.DataBean.AnswerListBean>
{
    private Context mContext;
    private QuestionDetailBean.DataBean.QuestionBean questionListBean;

    public QuestionDetailDelegate(Context context, QuestionDetailBean.DataBean.QuestionBean questionListBean)
    {
        super(R.layout.question_detail_header, R.layout.item_question_detail);
        this.mContext = context;
        this.questionListBean = questionListBean;
    }

    @Override
    public void initHeaderView(BaseViewHolder headerView)
    {

        LinearLayout head_view = (LinearLayout) headerView.findViewById(R.id.head_view);
        TextView question_status_tx = (TextView) headerView.findViewById(R.id.question_status_tx);
        TextView question_title_tx = (TextView) headerView.findViewById(R.id.question_title_tx);
        TextView question_content_tx = (TextView) headerView.findViewById(R.id.question_content_tx);
        ImageView question_single_img_iv = (ImageView) headerView.findViewById(R.id.question_single_img_iv);
        MyGridView imgs_gv = (MyGridView) headerView.findViewById(R.id.imgs_gv);
        TextView course_name_tx = (TextView) headerView.findViewById(R.id.course_name_tx);
        TextView question_reply_num_tx = (TextView) headerView.findViewById(R.id.question_reply_num_tx);
        RelativeLayout voice_ly = (RelativeLayout) headerView.findViewById(R.id.voice_ly);
        RelativeLayout video_question_bj_layout = (RelativeLayout) headerView.findViewById(R.id.video_question_bj_layout);
        ImageView video_question_img = (ImageView) headerView.findViewById(R.id.video_question_img);
        TextView video_time_text = (TextView) headerView.findViewById(R.id.video_time_text);
        ProgressBar voice_progress_header = (ProgressBar) headerView.findViewById(R.id.voice_progress_header);


        if (questionListBean.getIsFine().equals("0"))
        {

            if (questionListBean.getQuestionStatus().equals("0"))
            {
                GradientDrawable myGrad = (GradientDrawable) question_status_tx.getBackground();
                myGrad.setStroke(1, ContextCompat.getColor(mContext, R.color.font_yellow_question));// 设置边框宽度与颜色
                myGrad.setColor(ContextCompat.getColor(mContext, R.color.font_yellow_question));// 设置内部颜色
                question_status_tx.setTextColor(ContextCompat.getColor(mContext, R.color.white));// 设置字体颜色
                question_status_tx.setText(mContext.getResources().getString(R.string.to_be_solved));

            } else
            {
                GradientDrawable myGrad = (GradientDrawable) question_status_tx.getBackground();
                myGrad.setStroke(1, ContextCompat.getColor(mContext, R.color.font_green));// 设置边框宽度与颜色
                myGrad.setColor(ContextCompat.getColor(mContext, R.color.font_green));// 设置内部颜色
                question_status_tx.setTextColor(ContextCompat.getColor(mContext, R.color.white));// 设置字体颜色
                question_status_tx.setText(mContext.getResources().getString(R.string.resolved));
            }
        } else
        {
            GradientDrawable myGrad = (GradientDrawable) question_status_tx.getBackground();
            myGrad.setStroke(1, ContextCompat.getColor(mContext, R.color.font_red));// 设置边框宽度与颜色
            myGrad.setColor(ContextCompat.getColor(mContext, R.color.font_red));// 设置内部颜色
            question_status_tx.setTextColor(ContextCompat.getColor(mContext, R.color.white));// 设置字体颜色
            question_status_tx.setText(mContext.getResources().getString(R.string.the_essence_of));
        }
        question_title_tx.setText(questionListBean.getTitle().trim());
        question_content_tx.setText(questionListBean.getContent().trim());
        if (questionListBean.getImgs().size() != 0)
        {
            if (questionListBean.getImgs().size() == 1)
            {
                question_single_img_iv.setVisibility(View.VISIBLE);
                imgs_gv.setVisibility(View.GONE);
                GlideUtils.loadPorstersImg(mContext, questionListBean.getImgs().get(0), question_single_img_iv);
            } else
            {
                question_single_img_iv.setVisibility(View.GONE);
                imgs_gv.setVisibility(View.VISIBLE);
//                QuestionImgsAdapter adapter = new QuestionImgsAdapter(mContext, questionListBean.getImgs(), imageLoader);
//                imgs_gv.setAdapter(adapter);
//                imgs_gv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                    {
//                        Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
//                        intent.putStringArrayListExtra("imgurl", questionListBean.getImgs());
//                        intent.putExtra("index", position);
//                        startActivity(intent);
//                    }
//                });
            }

        } else
        {
            question_single_img_iv.setVisibility(View.GONE);
            imgs_gv.setVisibility(View.GONE);
        }
        course_name_tx.setText(questionListBean.getCourseName());
        question_reply_num_tx.setText(questionListBean.getPostNum());
        if (!TextUtils.isEmpty(questionListBean.getAudioUrl()))
        {
            voice_ly.setVisibility(View.VISIBLE);
            video_time_text.setText(questionListBean.getAudioDuration() + "\"");
            ViewGroup.LayoutParams layoutParams = video_question_bj_layout.getLayoutParams();
            if (questionListBean.getAudioDuration() <= 10)
            {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_80_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (questionListBean.getAudioDuration() > 10 && questionListBean.getAudioDuration() <= 30)
            {

                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_120_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (questionListBean.getAudioDuration() > 30 && questionListBean.getAudioDuration() <= 50)
            {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_140_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            } else if (questionListBean.getAudioDuration() > 50 && questionListBean.getAudioDuration() <= 60)
            {
                layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.woying_150_dip);
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.woying_30_dip);
            }
        } else
        {
            voice_ly.setVisibility(View.GONE);
        }
        question_single_img_iv.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(mContext, ViewPagePicsNetAct.class);
//                intent.putStringArrayListExtra("imgurl", questionListBean.getImgs());
//                intent.putExtra("index", 0);
//                startActivity(intent);
            }
        });


        video_question_bj_layout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
//                voice_progress_header.setVisibility(View.VISIBLE);
//                playMusic(video_question_img, questionListBean.getAudioUrl(), voice_progress_header);
            }
        });


    }

    @Override
    public void initCustomView(BaseViewHolder convertView, final List<QuestionDetailBean.DataBean.AnswerListBean> data, int position)
    {
        RelativeLayout zong_ly = (RelativeLayout) convertView.findViewById(R.id.zong_ly);
        ImageView user_head_img = (ImageView) convertView.findViewById(R.id.user_head_img);
        TextView user_name_tx = (TextView) convertView.findViewById(R.id.user_name_tx);
        TextView question_content_tx = (TextView) convertView.findViewById(R.id.question_content_tx);

        LinearLayout imgs_ly = (LinearLayout) convertView.findViewById(R.id.imgs_ly);
        ImageView question_single_img_iv = (ImageView) convertView.findViewById(R.id.question_single_img_iv);
        ImageView question_one_img_iv = (ImageView) convertView.findViewById(R.id.question_one_img_iv);
        ImageView question_two_img_iv = (ImageView) convertView.findViewById(R.id.question_two_img_iv);
        ImageView question_tr_img_iv = (ImageView) convertView.findViewById(R.id.question_tr_img_iv);

        TextView question_time_tx = (TextView) convertView.findViewById(R.id.question_time_tx);
        TextView question_reply_num_tx = (TextView) convertView.findViewById(R.id.question_reply_num_tx);

        TextView adopt_tx = (TextView) convertView.findViewById(R.id.adopt_tx);
        LinearLayout best_answer_ly = (LinearLayout) convertView.findViewById(R.id.best_answer_ly);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingbar);
        ImageView best_iv = (ImageView) convertView.findViewById(R.id.best_iv);


        final QuestionDetailBean.DataBean.AnswerListBean answerInfo = data.get(position);

        // TODO: 2017/3/15  这个地方有问题 需要判断是否是本人的

        if (answerInfo.getQuestionStatus().equals("0") && questionListBean.getMyId().equals(answerInfo.getUid()))
        {

            GradientDrawable myGrad = (GradientDrawable) adopt_tx.getBackground();
            myGrad.setStroke(1, ContextCompat.getColor(mContext, R.color.font_yellow_question));// 设置边框宽度与颜色
            myGrad.setColor(ContextCompat.getColor(mContext, R.color.transparent));// 设置内部颜色
            adopt_tx.setTextColor(ContextCompat.getColor(mContext, R.color.font_yellow_question));// 设置字体颜色
            adopt_tx.setVisibility(View.VISIBLE);

        } else
        {
            adopt_tx.setVisibility(View.GONE);
        }

        String isBest = answerInfo.getIsBest();
        if (isBest.equals("1"))
        {
            best_answer_ly.setVisibility(View.VISIBLE);
            if (answerInfo.getRole().equals("student"))
            {
                best_iv.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.GONE);
            } else
            {
                best_iv.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating((float) Integer.parseInt(answerInfo.getScore()));
            }
        } else
        {
            best_answer_ly.setVisibility(View.GONE);
        }
        GlideUtils.loadCircleHeaderOfCommon(mContext, answerInfo.getUserface(), user_head_img);
        user_name_tx.setText(answerInfo.getUsername());
        question_content_tx.setText(answerInfo.getContent());
        if (answerInfo.getImgs().size() != 0)
        {
            if (answerInfo.getImgs().size() == 1)
            {
                imgs_ly.setVisibility(View.GONE);
                question_single_img_iv.setVisibility(View.VISIBLE);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(0), question_single_img_iv);
            } else if (answerInfo.getImgs().size() == 2)
            {
                imgs_ly.setVisibility(View.VISIBLE);
                question_single_img_iv.setVisibility(View.GONE);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.VISIBLE);
                question_tr_img_iv.setVisibility(View.INVISIBLE);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(0), question_one_img_iv);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(1), question_two_img_iv);
            } else if (answerInfo.getImgs().size() == 3)
            {
                imgs_ly.setVisibility(View.VISIBLE);
                question_single_img_iv.setVisibility(View.GONE);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.VISIBLE);
                question_tr_img_iv.setVisibility(View.VISIBLE);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(0), question_one_img_iv);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(1), question_two_img_iv);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(2), question_tr_img_iv);
            } else
            {
                imgs_ly.setVisibility(View.VISIBLE);
                question_single_img_iv.setVisibility(View.GONE);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.VISIBLE);
                question_tr_img_iv.setVisibility(View.VISIBLE);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(0), question_one_img_iv);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(1), question_two_img_iv);
                GlideUtils.loadPorstersImg(mContext, answerInfo.getImgs().get(2), question_tr_img_iv);
            }
        } else
        {
            imgs_ly.setVisibility(View.GONE);
            question_single_img_iv.setVisibility(View.GONE);
        }
        question_time_tx.setText(DateUtil.getDistanceTime(answerInfo.getCtime()));
        question_reply_num_tx.setText(answerInfo.getPostNum());

        //采纳事件
        adopt_tx.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (answerInfo.getRole().equals("student"))
                {
                    final CustomNewDialog customDialog = new CustomNewDialog(mContext, R.layout.adop_student_dialog);
                    Button cance_bt = (Button) customDialog.findViewById(R.id.cance_bt);
                    Button confirm_bt = (Button) customDialog.findViewById(R.id.confirm_bt);
                    cance_bt.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            customDialog.dismiss();
                        }
                    });

                    confirm_bt.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            customDialog.dismiss();
                            //调用采纳接口
                            new HttpPostOld(mContext, mContext, new InterfaceHttpResult()
                            {
                                @Override
                                public void getCallback(int requestCode, int code, String msg, Object baseBean)
                                {
                                    answerInfo.setIsBest("1");
                                    questionListBean.setQuestionStatus("1");
                                    notifyDataSetChanged();
                                }
                            }, BaseBean.class, Urls.ADOPTIONQUESTION_STUDENT, questionListBean.getCourseId(), questionListBean.getQuestionId(), questionListBean.getPublicCourse(), answerInfo.getAnswerId())
                                    .excute();
                        }
                    });
                    customDialog.show();
                } else
                {

                    final CustomNewDialog customDialog = new CustomNewDialog(mContext, R.layout.adop_teacher_dialog);
                    Button cance_bt = (Button) customDialog.findViewById(R.id.cance_bt);
                    Button confirm_bt = (Button) customDialog.findViewById(R.id.confirm_bt);
                    final RatingBar ratingBar = (RatingBar) customDialog.findViewById(R.id.ratingbar);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                    {

                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
                        {
                        }
                    });

                    cance_bt.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            customDialog.dismiss();
                        }
                    });
                    confirm_bt.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            if (ratingBar.getProgress() != 0)
                            {
                                //调用采纳接口
                                //调用采纳接口
                                new HttpPostOld(mContext, mContext, new InterfaceHttpResult()
                                {
                                    @Override
                                    public void getCallback(int requestCode, int code, String msg, Object baseBean)
                                    {
                                        if (code == 0)
                                        {
                                            answerInfo.setIsBest("1");
                                            questionListBean.setQuestionStatus("1");
                                            answerInfo.setScore(ratingBar.getProgress() + "");
                                            notifyDataSetChanged();
                                        }
                                    }
                                }, BaseBean.class, Urls.ADOPTIONQUESTION_TEACHER, questionListBean.getCourseId(), questionListBean.getQuestionId(), questionListBean.getPublicCourse(), answerInfo.getAnswerId(), ratingBar.getProgress() + "")
                                        .excute();
                                customDialog.dismiss();
                            } else
                            {
                                ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.ensure_after_score));
                            }
                        }
                    });
                    customDialog.show();
                }
            }
        });

        zong_ly.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
//                //判断是否是抢答题
//                Intent intent = new Intent(mContext, QuestionAskDetaiActivity.class);
//                intent.putExtra("courseId", courseId);
//                intent.putExtra("questionId", questionId);
//                intent.putExtra("isCenter", isCenter);
//                intent.putExtra("answerId", answerInfo.getAnswerId());
//                intent.putExtra("username", answerInfo.getUsername());
//                if (entity.getIsGrab().equals("0"))
//                {
//                    //不是抢答题
//                    startActivityForResult(intent, 1);
//                } else
//                {
//                    //判断是否被抢答过
//                    if (entity.getCanGrab().equals("0"))
//                    {
//                        startActivityForResult(intent, 1);
//                    } else
//                    {
//                        new GrabQuestionActionAsk().executeOnExecutor(Constants.exec);
//                    }
//                }
            }
        });
    }


}
