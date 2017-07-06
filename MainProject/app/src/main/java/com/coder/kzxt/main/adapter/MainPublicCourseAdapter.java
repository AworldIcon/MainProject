package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.app.utils.L;
import com.coder.kzxt.course.activity.LocalCourseActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.views.MyListView;
import com.coder.kzxt.activity.R;

import java.util.List;

/**
 * 最新推荐课程
 */

public class MainPublicCourseAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;

    //所有的类型；
    private static final String
            PUBLIC_ZHENG_FOUR_1 = "PUBLIC_ZHENG_FOUR_1", PUBLIC_ZHENG_FOUR_2 = "PUBLIC_ZHENG_FOUR_2",
            PUBLIC_SHU_TWO_1 = "PUBLIC_SHU_TWO_1", PUBLIC_SHU_TWO_2 = "PUBLIC_SHU_TWO_2",
            PUBLIC_SHANGTUO_TWO_1 = "PUBLIC_SHANGTUO_TWO_1", PUBLIC_SHANGTUO_TWO_2 = "PUBLIC_SHANGTUO_TWO_2",
            PUBLIC_HENGXIANG_TWO_1 = "PUBLIC_HENGXIANG_TWO_1", PUBLIC_HENGXIANG_TWO_2 = "PUBLIC_HENGXIANG_TWO_2",
            PUBLIC_SHUONE_HENGTWO_1 = "PUBLIC_SHUONE_HENGTWO_1", PUBLIC_SHUONE_HENGTWO_2 = "PUBLIC_SHUONE_HENGTWO_2",
            PUBLIC_SHUONE_HENGTWO_XIAHENGONE_1 = "PUBLIC_SHUONE_HENGTWO_XIAHENGONE_1", PUBLIC_SHUONE_HENGTWO_XIAHENGONE_2 = "PUBLIC_SHUONE_HENGTWO_XIAHENGONE_2",
            PUBLIC_SHUONE_HENGTWO_XIASHUTWO_1 = "PUBLIC_SHUONE_HENGTWO_XIASHUTWO_1", PUBLIC_SHUONE_HENGTWO_XIASHUTWO_2 = "PUBLIC_SHUONE_HENGTWO_XIASHUTWO_2",
            PUBLIC_ZHENGFOUR_XIAHENGONE_1 = "PUBLIC_ZHENGFOUR_XIAHENGONE_1", PUBLIC_ZHENGFOUR_XIAHENGONE_2 = "PUBLIC_ZHENGFOUR_XIAHENGONE_2";
    /**
     * 需要显示老师名称和学习人数的
     */
    public static String showTeacherAndNumber =
            PUBLIC_ZHENG_FOUR_1 + "," + PUBLIC_SHU_TWO_1 + "," + PUBLIC_SHANGTUO_TWO_1 + "," +
                    PUBLIC_HENGXIANG_TWO_1 + "," + PUBLIC_SHUONE_HENGTWO_1 + "," + PUBLIC_SHUONE_HENGTWO_XIAHENGONE_1 + "," +
                    PUBLIC_SHUONE_HENGTWO_XIASHUTWO_1 + "," + PUBLIC_ZHENGFOUR_XIAHENGONE_1;


    public static String types =
            PUBLIC_ZHENG_FOUR_1 + "," + PUBLIC_ZHENG_FOUR_2 + "," + PUBLIC_SHU_TWO_1 + "," + PUBLIC_SHU_TWO_2 + "," +
                    PUBLIC_SHANGTUO_TWO_1 + "," + PUBLIC_SHANGTUO_TWO_2 + "," + PUBLIC_HENGXIANG_TWO_1 + "," +
                    PUBLIC_HENGXIANG_TWO_2 + "," + PUBLIC_SHUONE_HENGTWO_1 + "," + PUBLIC_SHUONE_HENGTWO_2 + "," +
                    PUBLIC_SHUONE_HENGTWO_XIAHENGONE_1 + "," + PUBLIC_SHUONE_HENGTWO_XIAHENGONE_2 + "," +
                    PUBLIC_SHUONE_HENGTWO_XIASHUTWO_1 + "," + PUBLIC_SHUONE_HENGTWO_XIASHUTWO_2 + "," +
                    PUBLIC_ZHENGFOUR_XIAHENGONE_1 + "," + PUBLIC_ZHENGFOUR_XIAHENGONE_2;


    public MainPublicCourseAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
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
        switch (configBean.getModule_style()) {
            case PUBLIC_ZHENG_FOUR_1:
                view = disPlayGridView(view);
                break;
            case PUBLIC_ZHENG_FOUR_2:
                view = disPlayGridView(view);
                break;
            case PUBLIC_SHU_TWO_1:
                view = disPlayListView(view, 2);
                break;
            case PUBLIC_SHU_TWO_2:
                view = disPlayListView(view, 2);
                break;
            case PUBLIC_SHANGTUO_TWO_1:
                view = setListViewGridView(view, 2);
                break;
            case PUBLIC_SHANGTUO_TWO_2:
                view = setListViewGridView(view, 2);
                break;
            case PUBLIC_HENGXIANG_TWO_1:
                view = disPlayListView(view, 1);
                break;
            case PUBLIC_HENGXIANG_TWO_2:
                view = disPlayListView(view, 1);
                break;
            //不规则
            case PUBLIC_SHUONE_HENGTWO_1:
                view = setListViewThreeItem(view);
                break;
            case PUBLIC_SHUONE_HENGTWO_2:
                view = setListViewThreeItem(view);
                break;
            case PUBLIC_SHUONE_HENGTWO_XIAHENGONE_1:
                view = setListViewListView(view);
                break;
            case PUBLIC_SHUONE_HENGTWO_XIAHENGONE_2:
                view = setListViewListView(view);
                break;
            case PUBLIC_SHUONE_HENGTWO_XIASHUTWO_1:
                view = setGridViewThreeItem(view);
                break;
            case PUBLIC_SHUONE_HENGTWO_XIASHUTWO_2:
                view = setGridViewThreeItem(view);
                break;
            case PUBLIC_ZHENGFOUR_XIAHENGONE_1:
                view = setListViewGridView(view, 4);
                break;
            case PUBLIC_ZHENGFOUR_XIAHENGONE_2:
                view = setListViewGridView(view, 4);
                break;
            default:
                view = disPlayListView(view, 1);
                break;
        }

        if (view == null) {
            L.d("mainLiveCourseAdapter view is null");
            return view;
        }

        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        moduleType.setText(configBean.getModule_name());
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, LocalCourseActivity.class).putExtra("model_key",configBean.getModule_key()).putExtra("title",configBean.getModule_name()));
            }
        });
        return view;
    }

    /**
     * type 类型 区分加载不同的adapter
     * 1 垂直
     * 2 水平
     *
     * @param view
     * @param type
     * @return
     */
    private View disPlayListView(View view, int type) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        if (type == 1) {
            myListView.setAdapter(new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data));
        } else {
            myListView.setAdapter(new PublicCourseHorizontalAdapter(mContext, this.data));
        }
        return view;
    }

    private View disPlayGridView(View view) {
        view = View.inflate(mContext, R.layout.item_main_mygridview, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data));
        return view;
    }

    /**
     * 下面是listview item是3个的布局
     */
    public View setListViewThreeItem(View view) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        myListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data));

        return view;
    }

    /**
     * listView header是2或者4个的布局
     */
    public View setListViewGridView(View view, int count) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        View headView = View.inflate(mContext, R.layout.view_mygridview, null);
        MyGridView myGridView = (MyGridView) headView.findViewById(R.id.myGridView);
        myListView.addHeaderView(headView);
        L.d("size = " + data.size() + " ; count = " + count);
        int i = Math.min(count, this.data.size());
        myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data.subList(0, i)));
        SchoolCourseVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > count) {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data.subList(count, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, null);
        }
        myListView.setAdapter(courseLiveVerticalBigAdapter);
        return view;
    }

    /**
     * listView header是3个不规则的布局 把它当做list看
     */
    public View setListViewListView(View view) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        View headView = View.inflate(mContext, R.layout.view_mylistview, null);
        MyListView headerListView = (MyListView) headView.findViewById(R.id.myListView);
        myListView.addHeaderView(headView);

        int count = Math.min(this.data.size(), 3);
        headerListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        SchoolCourseVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > 3) {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data.subList(3, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, null);
        }
        myListView.setAdapter(courseLiveVerticalBigAdapter);
        return view;
    }

    /**
     * 下面是gridview header是3个的布局 把它当做list看
     */

    public View setGridViewThreeItem(View view) {
        view = View.inflate(mContext, R.layout.item_main_gridview_header, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        int count = Math.min(this.data.size(), 3);
        myListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        if (this.data.size() > 3) {
            myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data.subList(3, this.data.size())));
        }
        return view;
    }

}
