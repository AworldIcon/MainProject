package com.coder.kzxt.dialog.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomListDialog extends Dialog
{

    private MyRecyclerView listView;
    private ListView listView1;
    private List<BaseString> data;
    private CustomListAdapter adapter;


    /**
     * 调用 dialog的时候 弹出的list格式用此函数
     *
     * @param context
     */
    public CustomListDialog(final Context context)
    {
        this(context, Gravity.CENTER);
    }

    public CustomListDialog(final Context context, final int theme)
    {
        this(context, R.layout.dlg_custom_list, R.style.DialogBlack, theme);
    }

    public CustomListDialog(final Context context, final int layout, final int theme, int gravity)
    {
        super(context, theme);
        this.setContentView(layout);
        final Window dialogWindow = this.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(gravity);
        this.setCanceledOnTouchOutside(true);

        listView = (MyRecyclerView) this.findViewById(R.id.myRecyclerView);
        listView1 = (ListView) this.findViewById(R.id.list);
//        listView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        data = new ArrayList<BaseString>();
        adapter = new CustomListAdapter(context, data);
        listView.setAdapter(adapter);
    }

    public void addData(String... strings)
    {
        if (strings.length == 0)
        {
            return;
        }
        for (String string : strings)
        {
            data.add(new BaseString(string));
        }
        adapter.notifyDataSetChanged();
    }

    public void addData(BaseString bean)
    {
        data.add(bean);
        adapter.notifyDataSetChanged();
    }

    public List<BaseString> getData()
    {
        if (data == null)
        {
            return new ArrayList<>();
        }
        return data;
    }

    public CustomListAdapter getAdapter()
    {
        return adapter;
    }
    public ListView getListView() {
        return listView1;
    }
    public MyRecyclerView getRecyclerView() {
        return listView;
    }
}
