package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.FeatureResult;

import java.util.List;

/**
 * 功能区 保护老师 学生 推荐的课程
 */
public class MainTopicAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;
    private FeatureAdapter personAdapter;

    public MainTopicAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        //this.pu = new PublicUtils(this.mContext);
        this.configBean = moduleType;
    }

    public boolean isAdded=false;
    public View getView() {
        View view = null;
        if (this.data == null || this.data.size() == 0) return view;
        isAdded=true;
        view = View.inflate(mContext, R.layout.item_main_mygridview_notitle, null);
        LinearLayout titleLy = (LinearLayout) view.findViewById(R.id.titleLy);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        View whiteLine = view.findViewById(R.id.whiteLine);
        if(TextUtils.isEmpty(configBean.getModule_name())){
            titleLy.setVisibility(View.GONE);
            whiteLine.setVisibility(View.VISIBLE);
        } else {
            whiteLine.setVisibility(View.GONE);
            titleLy.setVisibility(View.VISIBLE);
            moduleType.setText(configBean.getModule_name());
            loadMore.setVisibility(View.GONE);
        }

        personAdapter = new FeatureAdapter(mContext,configBean, data);
        personAdapter.setCount(5);
        myGridView.setAdapter(personAdapter);
        return view;
    }

    public void setUnReadChatNum(int unReadNoticeNum) {
        if (personAdapter != null) {
            personAdapter.setunReadChatNumber(unReadNoticeNum);
        }
    }

    public void setUnReadNoticeNum(String unReadNoticeNum) {
        if (personAdapter != null) {
            personAdapter.setUnReadNoticeNumber(Integer.valueOf(unReadNoticeNum));
        }
    }


}
