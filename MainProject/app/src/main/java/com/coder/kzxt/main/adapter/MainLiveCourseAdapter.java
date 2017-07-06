package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.course.activity.LiveCourseActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.EToast;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.views.MyListView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.CourseResult;

import java.util.List;

/**
 * 直播课程
 */

public class MainLiveCourseAdapter {

    private Activity mContext;
//    private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;

    //所有的类型；
    private static final String
            LIVE_ZHENG_FOUR_1 = "LIVE_ZHENG_FOUR_1", LIVE_ZHENG_FOUR_2 = "LIVE_ZHENG_FOUR_2",
            LIVE_SHU_TWO_1 = "LIVE_SHU_TWO_1", LIVE_SHU_TWO_2 = "LIVE_SHU_TWO_2",
            LIVE_SHANGTUO_TWO_1 = "LIVE_SHANGTUO_TWO_1", LIVE_SHANGTUO_TWO_2 = "LIVE_SHANGTUO_TWO_2",
            LIVE_HENGXIANG_TWO_1 = "LIVE_HENGXIANG_TWO_1", LIVE_HENGXIANG_TWO_2 = "LIVE_HENGXIANG_TWO_2",
            LIVE_SHUONE_HENGTWO_1 = "LIVE_SHUONE_HENGTWO_1", LIVE_SHUONE_HENGTWO_2 = "LIVE_SHUONE_HENGTWO_2",
            LIVE_SHUONE_HENGTWO_XIAHENGONE_1 = "LIVE_SHUONE_HENGTWO_XIAHENGONE_1", LIVE_SHUONE_HENGTWO_XIAHENGONE_2 = "LIVE_SHUONE_HENGTWO_XIAHENGONE_2",
            LIVE_SHUONE_HENGTWO_XIASHUTWO_1 = "LIVE_SHUONE_HENGTWO_XIASHUTWO_1", LIVE_SHUONE_HENGTWO_XIASHUTWO_2 = "LIVE_SHUONE_HENGTWO_XIASHUTWO_2",
            LIVE_ZHENGFOUR_XIAHENGONE_1 = "LIVE_ZHENGFOUR_XIAHENGONE_1", LIVE_ZHENGFOUR_XIAHENGONE_2 = "LIVE_ZHENGFOUR_XIAHENGONE_2";
    /**
     * 需要显示老师名称和学习人数的
     */
    public static String showTeacherAndNumber = LIVE_ZHENG_FOUR_1 + "," + LIVE_SHU_TWO_1 + "," + LIVE_SHANGTUO_TWO_1 + "," +
            LIVE_HENGXIANG_TWO_1 + "," + LIVE_SHUONE_HENGTWO_1 + "," + LIVE_SHUONE_HENGTWO_XIAHENGONE_1 + "," +
            LIVE_SHUONE_HENGTWO_XIASHUTWO_1 + "," + LIVE_ZHENGFOUR_XIAHENGONE_1;


    public static String types = LIVE_ZHENG_FOUR_1 + "," + LIVE_ZHENG_FOUR_2 + "," + LIVE_SHU_TWO_1 + "," + LIVE_SHU_TWO_2 + "," + LIVE_SHANGTUO_TWO_1 + "," + LIVE_SHANGTUO_TWO_2 + "," +
            LIVE_HENGXIANG_TWO_1 + "," + LIVE_HENGXIANG_TWO_2 + "," + LIVE_SHUONE_HENGTWO_1 + "," + LIVE_SHUONE_HENGTWO_2 + "," +
            "" + LIVE_SHUONE_HENGTWO_XIAHENGONE_1 + "," + LIVE_SHUONE_HENGTWO_XIAHENGONE_2 + "," +
            LIVE_SHUONE_HENGTWO_XIASHUTWO_1 + "," + LIVE_SHUONE_HENGTWO_XIASHUTWO_2 + "," + LIVE_ZHENGFOUR_XIAHENGONE_1 + "," + LIVE_ZHENGFOUR_XIAHENGONE_2;


    public MainLiveCourseAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
       // this.pu = new PublicUtils(this.mContext);
        this.configBean = moduleType;
    }

    public View getView() {
        Log.d("zw--12",configBean.getModule_style()+"***");

        View view = null;
        if (this.data == null || this.data.size() == 0) return view;

        switch (configBean.getModule_style()) {
            case LIVE_ZHENG_FOUR_1:
                view = disPlayGridView(view);
                break;
            case LIVE_ZHENG_FOUR_2:
                view = disPlayGridView(view);
                break;
            case LIVE_SHU_TWO_1:
                view = disPlayListView(view, 2);
                break;
            case LIVE_SHU_TWO_2:
                view = disPlayListView(view, 2);
                break;
            case LIVE_SHANGTUO_TWO_1:
                view = setListViewGridView(view, 2);
                break;
            case LIVE_SHANGTUO_TWO_2:
                view = setListViewGridView(view, 2);
                break;
            case LIVE_HENGXIANG_TWO_1:
                view = disPlayListView(view, 1);
                break;
            case LIVE_HENGXIANG_TWO_2:
                view = disPlayListView(view, 1);
                break;
            //不规则
            case LIVE_SHUONE_HENGTWO_1:
                view = setListViewThreeItem(view);
                break;
            case LIVE_SHUONE_HENGTWO_2:
                view = setListViewThreeItem(view);
                break;
            case LIVE_SHUONE_HENGTWO_XIAHENGONE_1:
                view = setListViewListView(view);
                break;
            case LIVE_SHUONE_HENGTWO_XIAHENGONE_2:
                view = setListViewListView(view);
                break;
            case LIVE_SHUONE_HENGTWO_XIASHUTWO_1:
                view = setGridViewThreeItem(view);
                break;
            case LIVE_SHUONE_HENGTWO_XIASHUTWO_2:
                view = setGridViewThreeItem(view);
                break;
            case LIVE_ZHENGFOUR_XIAHENGONE_1:
                view = setListViewGridView(view, 4);
                break;
            case LIVE_ZHENGFOUR_XIAHENGONE_2:
                view = setListViewGridView(view, 4);
                break;
            default:
                view = disPlayListView(view, 1);
                break;
        }

        if (view == null) {
          //  L.d("mainLiveCourseAdapter view is null");
            return view;
        }

        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        moduleType.setText(configBean.getModule_name());
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, LiveCourseActivity.class));

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
            myListView.setAdapter(new CourseLiveVerticalBigAdapter(mContext, configBean, this.data));
        } else {
            myListView.setAdapter(new CourseLiveHorizontalAdapter(mContext, this.data));
        }
        return view;
    }

    private View disPlayGridView(View view) {
        view = View.inflate(mContext, R.layout.item_main_mygridview, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        myGridView.setAdapter(new CourseLiveVerticalAdapter(mContext, configBean, this.data));
        return view;
    }

    /**
     * 下面是listview item是3个的布局
     */
    public View setListViewThreeItem(View view) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        myListView.setAdapter(new CourseLiveVerticalThreeAdapter(mContext, configBean, this.data));

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
      //  L.d("size = " + data.size() + " ; count = " + count);
        int i = Math.min(count, this.data.size());
        myGridView.setAdapter(new CourseLiveVerticalAdapter(mContext, configBean, this.data.subList(0, i)));
        CourseLiveVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > count) {
            courseLiveVerticalBigAdapter = new CourseLiveVerticalBigAdapter(mContext, configBean, this.data.subList(count, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new CourseLiveVerticalBigAdapter(mContext, configBean, null);
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
        headerListView.setAdapter(new CourseLiveVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        CourseLiveVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > 3) {
            courseLiveVerticalBigAdapter = new CourseLiveVerticalBigAdapter(mContext, configBean, this.data.subList(3, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new CourseLiveVerticalBigAdapter(mContext, configBean, null);
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
        myListView.setAdapter(new CourseLiveVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        if (this.data.size() > 3) {
            myGridView.setAdapter(new CourseLiveVerticalAdapter(mContext, configBean, this.data.subList(3, this.data.size())));
        }
        return view;
    }

}
