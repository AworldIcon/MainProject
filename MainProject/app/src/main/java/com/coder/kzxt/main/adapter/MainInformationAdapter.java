package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.information.activity.InformationListActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.views.MyListView;
import java.util.List;

/**
 * 最新资讯
 */
public class MainInformationAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;

    public MainInformationAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
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
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        moduleType.setText(configBean.getModule_name());
        InformationAdapter personAdapter = new InformationAdapter(mContext, data);
        myListView.setAdapter(personAdapter);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, InformationListActivity.class).putExtra("model_key",configBean.getModule_key()).putExtra("title",configBean.getModule_name()));
//                EToast.makeText(mContext,"更多资讯", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }


}
