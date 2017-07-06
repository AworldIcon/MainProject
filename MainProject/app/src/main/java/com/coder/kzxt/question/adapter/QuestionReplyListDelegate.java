package com.coder.kzxt.question.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.delegate.TextImageDelegate;
import com.coder.kzxt.question.activity.QuestionsDetailActivity;
import com.coder.kzxt.question.beans.QuestionsReplyListBean;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by pc on 2017/4/19.
 */

public class QuestionReplyListDelegate extends PullRefreshDelegate<QuestionsReplyListBean.ItemsBean>
{
    private QuestionsDetailActivity context;
    private String name, courseName, replyNum;

    public QuestionReplyListDelegate(QuestionsDetailActivity context, String nickname, String course_name, String reply_num)
    {
        super(R.layout.header_question_reply, R.layout.item_question_reply_list);
        this.context = context;
        this.name = nickname;
        this.courseName = course_name;
        this.replyNum = reply_num;
    }

    @Override
    public void initHeaderView(BaseViewHolder holder)
    {
        TextView nickname = holder.findViewById(R.id.nickname);
        TextView title = holder.findViewById(R.id.title);
//        TextView content = holder.findViewById(R.id.content);
       // content.setVisibility(View.GONE);
        TextView time_fore = holder.findViewById(R.id.time_fore);
        TextView reply_num = holder.findViewById(R.id.reply_num);
        TextView course_name = holder.findViewById(R.id.course_name);
        MyRecyclerView recyclerView = holder.findViewById(R.id.content_recycler_view);
        if (context.getQuestion() != null)
        {
            nickname.setText(name);
            title.setText(context.getQuestion().getItem().getTitle());
            recyclerView.setAdapter(new BaseRecyclerAdapter(context,context.getQuestion().getItem().getContent(),new TextImageDelegate(context)));
            //content.setText(delHTMLTag(context.getQuestion().getItem().getContent()));
            title.setText(context.getQuestion().getItem().getTitle());
            time_fore.setText(DateUtil.getDayBef(context.getQuestion().getItem().getCreate_time()));
            reply_num.setText(context.getQuestion().getItem().getPost_num() + "");
            course_name.setText(courseName);
        }
    }

    @Override
    public void initCustomView(BaseViewHolder holder, final List<QuestionsReplyListBean.ItemsBean> data, final int position)
    {
        final ImageView user_head_img = holder.findViewById(R.id.reply_header);
        ImageView best_answer_image = holder.findViewById(R.id.best_answer_image);
        TextView reply_name = holder.findViewById(R.id.reply_name);
        TextView time_fore = holder.findViewById(R.id.time_fore);
        TextView content = holder.findViewById(R.id.content);
        TextView reply_num = holder.findViewById(R.id.reply_num);
        ImageView content_image=holder.findViewById(R.id.content_image);
        reply_name.setText(data.get(position).getUser().getNickname());
        reply_num.setVisibility(View.VISIBLE);
        reply_num.setText(data.get(position).getCount()+"");
        if(data.get(position).getContent().size()==1){
            //如果首行是文字则展示首行文字，首行是图片则只展示这一个图片，并不再次展示后面的任何内容了
            if(data.get(position).getContent().get(0).getType()==1){
                content_image.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                content.setText(data.get(position).getContent().get(0).getContent());
            }else {
                content.setVisibility(View.GONE);
                content_image.setVisibility(View.VISIBLE);
                GlideUtils.loadCourseImg(context,data.get(position).getContent().get(0).getContent(),content_image);
            }
        }
        //如果首行是文字且后面的类型是图片，则展示跟随首行内容后面第二行的第一个图片
        if(data.get(position).getContent().size()>1&&data.get(position).getContent().get(0).getType()==1&&data.get(position).getContent().get(1).getType()==2){
            content.setVisibility(View.VISIBLE);
            content.setText(data.get(position).getContent().get(0).getContent());
            content_image.setVisibility(View.VISIBLE);
            GlideUtils.loadCourseImg(context,data.get(position).getContent().get(1).getContent(),content_image);
        }else if(data.get(position).getContent().size()>1&&data.get(position).getContent().get(0).getType()==1&&data.get(position).getContent().get(1).getType()==1){
            content.setVisibility(View.VISIBLE);
            content.setText(data.get(position).getContent().get(0).getContent());
            content_image.setVisibility(View.GONE);
        } else if(data.get(position).getContent().size()>1&&data.get(position).getContent().get(0).getType()==2){//首行是图片
            content.setVisibility(View.GONE);
            content_image.setVisibility(View.VISIBLE);
            GlideUtils.loadCourseImg(context,data.get(position).getContent().get(0).getContent(),content_image);
        }


        time_fore.setText(DateUtil.getDayBef(data.get(position).getCreate_time()));
        if (data.get(position).getIs_best_answer().equals("0"))
        {
            best_answer_image.setVisibility(View.GONE);
        } else
        {
            best_answer_image.setVisibility(View.VISIBLE);
        }
        GlideUtils.loadCircleHeaderOfCommon(context,data.get(position).getUser().getAvatar(),user_head_img);

//        Glide.with(context).load(data.get(position).getUser().getAvatar()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header).error(R.drawable.default_teacher_header).centerCrop().into(new BitmapImageViewTarget(user_head_img)
//        {
//
//            @Override
//            protected void setResource(Bitmap resource)
//            {
//                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                circularBitmapDrawable.setCornerRadius(25);
//                user_head_img.setImageDrawable(circularBitmapDrawable);
//            }
//
//        });

    }

    /**
     * java 去掉html标签
     * 使用正则表达式删除HTML标签。
     */
    public static String delHTMLTag(String htmlStr)
    {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * Java中去掉网页HTML标记的方法
     * Java里面去掉网页里的HTML标记的方法：
     * 去掉字符串里面的html代码。<br>
     * 要求数据要规范，比如大于小于号要配套,否则会被集体误杀。
     *
     * @param content 内容
     * @return 去掉后的内容
     */
    public static String stripHtml(String content)
    {
// <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
// <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
// 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
// 还原HTML
// content = HTMLDecoder.decode(content);
        return content;
    }
}
