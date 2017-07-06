package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.course.activity.LocalCourseActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.EToast;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.FamousTeacherResult;
import java.util.List;

/**
 * 名师推荐
 */
public class MainFamousTeacherAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;

    public MainFamousTeacherAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        //this.pu = new PublicUtils(this.mContext);
        this.configBean = moduleType;
    }

    public View getView() {

        View view = null;
        if (this.data == null || this.data.size() == 0) return view;

        view = View.inflate(mContext, R.layout.item_main_mygridview2, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        final TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        moduleType.setText(configBean.getModule_name());
        myGridView.setNumColumns(5);
        TeacherAdapter teacherAdapter=new TeacherAdapter(mContext,configBean, data);
        teacherAdapter.setCount(5);
        myGridView.setAdapter(teacherAdapter);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mContext.startActivity(new Intent(mContext, FamousTeacherListActivity.class).putExtra("title", configBean.getModuleName()));
//                SnackbarUtils.Custom(loadMore,"更多名师",1000).messageCenter().show();
                mContext.startActivity(new Intent(mContext, LocalCourseActivity.class).putExtra("model_key",configBean.getModule_key()).putExtra("title",configBean.getModule_name()).putExtra("style",configBean.getModule_style()));

            }
        });
        return view;
    }


}
