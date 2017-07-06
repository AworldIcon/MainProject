package com.coder.kzxt.main.delegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.GoodCourseActivity;
import com.coder.kzxt.course.activity.LiveCourseActivity;
import com.coder.kzxt.information.activity.InformationListActivity;
import com.coder.kzxt.main.beans.DiscoveryBean;
import com.coder.kzxt.poster.activity.PosterActivity;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.service.activity.ServiceMainActivity;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;

import java.util.List;


public class DiscoveryDelegate extends BaseDelegate<DiscoveryBean.ItemBean.ModuleBean>
{
    private final String TOPIC = "TOPIC";//精品课程
    private final String CENTER_COURSE = "CENTER_COURSE";//公开课联盟
    private final String LIVE = "LIVE";//直播课
    private final String SERVICE = "SERVICE";//服务
    private final String POSTER = "POSTER";//海报
    private final String NEWS = "NEWS";//新闻
    private final String OTHER = "OTHER";//其他
    //下边3个晋阳水业独有
    private final String FREAMEWORK = "FREAMEWORK";//企业规章
    private final String NEWSAGEN = "NEWSAGEN";//机构新闻
    private final String INTROCT = "INTROCT";//公司介绍

    private Context context;
    private List<DiscoveryBean.ItemBean.ModuleBean> modules;
    private String style;

    public DiscoveryDelegate(Context context, String style, List<DiscoveryBean.ItemBean.ModuleBean> modules)
    {
        super(R.layout.item_discovery_list);
        this.context = context;
        this.style = style;
        this.modules = modules;
    }

    @Override
    public int getLayoutId(int viewType)
    {
        if (this.modules != null)
        {
            if (style.equals("1"))
            {
                return R.layout.item_discovery_list;
            } else if (style.equals("2"))
            {
                return R.layout.item_discovery_grid;
            }
        }
        return 0;
    }

    @Override
    public void initView(BaseViewHolder holder, List<DiscoveryBean.ItemBean.ModuleBean> modules, int position, int d)
    {
        holder.setIsRecyclable(false);
        RelativeLayout item_rl = holder.findViewById(R.id.item_rl);
        ImageView image = holder.findViewById(R.id.image);
        TextView text = holder.findViewById(R.id.text);
        View fenge_view = holder.findViewById(R.id.fenge_view);
        final DiscoveryBean.ItemBean.ModuleBean module = this.modules.get(position);

        String item = "";
        if (!(module == null))
        {
            item = module.getName();
            text.setText(module.getTitle());
            GlideUtils.loadDiscoverImage(context,module.getImage(),image);
            if (style.equals("1"))
            {
                if (position > 0 && (position + 1) % 3 == 0 && !(position == this.modules.size() - 1))
                {//列表  3条数据一组
                    fenge_view.setVisibility(View.VISIBLE);
                } else
                {
                    fenge_view.setVisibility(View.GONE);
                }
            } else if (style.equals("2"))
            {//九宫格  9条一组
                if (this.modules.size() > 0)
                {
                        if (position>3&&(position+1)%9==0)
                        {
                            fenge_view.setVisibility(View.VISIBLE);
                        } else
                        {
                            fenge_view.setVisibility(View.GONE);
                        }
                } else
                {
                    fenge_view.setVisibility(View.GONE);
                }
            }
        } else
        {
            item_rl.setVisibility(View.VISIBLE);
            ImageLoad.loadImage(context, "", 0, image);
        }
        final String finalItem = item;
        final Intent intent = new Intent();
        holder.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /**
                 * 晋阳水业“微站”页面所有条目必须登录
                 */
                if (context.getPackageName().equals("com.coder.jysypx.activity"))
                {
                    switch (finalItem)
                    {
                        case TOPIC:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_topic");
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, GoodCourseAct.class);
//                                context.startActivity(intent);
//                            }
                            break;
                        case CENTER_COURSE:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_publiccourse");
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, CommonLesson_Activity.class);
//                                context.startActivity(intent);
//                            }
                            break;
                        case LIVE:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_live");
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, LiveLesson_SeeMore_Activity.class);
//                                context.startActivity(intent);
//                            }
                            break;
                        case SERVICE:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_service");
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, ServiceListActivity.class);
//                                context.startActivity(intent);
//                            }
                            break;
                        case POSTER:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_poster");
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, PostersMainAct.class);
//                                context.startActivity(intent);
//                            }
                            break;
                        case NEWS:
//                            if (TextUtils.isEmpty(module.getUrl())) {
//                                PublicUtils.makeToast(context, context.getResources().getString(R.string.no_news_now));
//                                return;
//                            }
                        case OTHER:
                        case FREAMEWORK:
                        case NEWSAGEN:
                        case INTROCT:
//                            if (TextUtils.isEmpty(pu.getIsLogin())) {
//                                intent.setClass(context, LoginActivity.class);
//                                intent.putExtra("from", "discovery_news");
//                                intent.putExtra("web_url", module.getUrl());
//                                intent.putExtra("title", module.getTitle());
//                                context.startActivity(intent);
//                            } else {
//                                intent.setClass(context, TextView_Link_Activity.class);
//                                intent.putExtra("web_url", module.getUrl());
//                                intent.putExtra("title", module.getTitle());
//                                context.startActivity(intent);
//                            }
                            break;
                    }
                } else
                {
                    switch (finalItem)
                    {
                        case TOPIC:
                            intent.setClass(context, GoodCourseActivity.class);
                            context.startActivity(intent);
                            break;
                        case CENTER_COURSE:
//                            intent.setClass(context, CommonLesson_Activity.class);
//                            context.startActivity(intent);
                            break;
                        case LIVE:
                            intent.setClass(context, LiveCourseActivity.class);
                            context.startActivity(intent);
                            break;
                        case SERVICE:
//                            intent.setClass(context, ServiceListActivity.class);
//                            context.startActivity(intent);
                            ServiceMainActivity.gotoActivity(context,module.getTitle());
                            break;
                        case POSTER:
                            intent.setClass(context, PosterActivity.class);
                            context.startActivity(intent);
                            break;
                        case NEWS:
//                            if (TextUtils.isEmpty(module.getUrl())) {
//                                ToastUtils.makeText(context, context.getResources().getString(R.string.no_news_now));
//                                return;
//                            }
//                            intent.setClass(context, TextView_Link_Activity.class);
//                            intent.putExtra("web_url", module.getUrl());
//                            intent.putExtra("title", context.getResources().getString(R.string.shool_news));
//                            context.startActivity(intent);
                            context.startActivity(new Intent(context, InformationListActivity.class).putExtra("model_key","INFORMATION").putExtra("title",module.getTitle()));

                            break;
                        case OTHER:
                            intent.setClass(context, TextView_Link_Activity.class);
                            intent.putExtra("web_url", module.getUrl());
                            intent.putExtra("title", module.getTitle());
                            context.startActivity(intent);
                            break;
                    }
                }
            }
        });
    }
}
