package com.coder.kzxt.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.views.ScrollHeaderListView;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/3/18.
 */

public class DownloadVideoAdapter extends BaseExpandableListAdapter implements com.coder.kzxt.views.ScrollHeaderListView.QQHeaderAdapter{

    private Context context;
    private List<CatalogueBean.ItemsBean> downloadList;
    private ScrollHeaderListView listView;
    private HashMap<String, Boolean> isSelectedMap;

    public DownloadVideoAdapter(Context context, List<CatalogueBean.ItemsBean> downloadList, ScrollHeaderListView listView) {
        this.context = context;
        this.downloadList = downloadList;
        this.listView = listView;
        isSelectedMap = new HashMap<String, Boolean>();
    }


    @Override
    public int getGroupCount() {
        return downloadList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return downloadList.get(groupPosition).getVideo().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return downloadList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return downloadList.get(groupPosition).getVideo().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public HashMap<String, Boolean> getIsSelected() {
        return isSelectedMap;
    }

    public void setIsSelected(HashMap<String, Boolean> isSelectedMap) {
        this.isSelectedMap = isSelectedMap;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewExpandableHolder holder;
        if (convertView == null) {
            holder = new ViewExpandableHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.videoview_downlaod_group_item, null);
            holder.chapter_name = (TextView) convertView.findViewById(R.id.chapter_name);
            holder.top_fenge = convertView.findViewById(R.id.top_fenge);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewExpandableHolder) convertView.getTag();
        }

        if (groupPosition == 0) {
            holder.top_fenge.setVisibility(View.GONE);
        } else {
            holder.top_fenge.setVisibility(View.VISIBLE);
        }

       CatalogueBean.ItemsBean itemsBean= downloadList.get(groupPosition);
        String foldername = itemsBean.getTitle();
        holder.chapter_name.setText(foldername);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewExpandableHolder holder;
        if (convertView == null) {
            holder = new ViewExpandableHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.videoview_download_child_item, null);
            holder.video_name = (TextView) convertView.findViewById(R.id.video_name);
            holder.video_check = (CheckBox) convertView.findViewById(R.id.video_check);
            holder.download_child_item_zong_ly = (RelativeLayout) convertView.findViewById(R.id.download_child_item_zong_ly);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewExpandableHolder) convertView.getTag();
        }

        CatalogueBean.ItemsBean.VideoBean videoBean =downloadList.get(groupPosition).getVideo().get(childPosition);
        final String video_id  = videoBean.getId();
        final String name = videoBean.getTitle();
        final String type = videoBean.getType();

        holder.video_name.setText(name);

        if(type!=null){
            if (type.equals("1")||type.equals("6")) {
                // 根据getIsSelected().get(video_id)来设置checkbox的选中状况
                holder.video_check.setChecked(getIsSelected().get(video_id));
                holder.video_check.setVisibility(View.VISIBLE);
            } else {
                holder.video_check.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getQQHeaderState(int groupPosition, int childPosition) {
        if (groupPosition >= 0) {
            final int childCount = getChildrenCount(groupPosition);
            if (childPosition == childCount - 1) {
                return PINNED_HEADER_PUSHED_UP;
            } else if (childPosition == -1 && !listView.isGroupExpanded(groupPosition)) {
                return PINNED_HEADER_GONE;
            } else {
                return PINNED_HEADER_VISIBLE;
            }
        } else {
            return PINNED_HEADER_GONE;
        }
    }

    @Override
    public void configureQQHeader(View header, int groupPosition, int childPosition, int alpha) {
        TextView chapter_name = (TextView) header.findViewById(R.id.chapter_name);
        chapter_name.setText(downloadList.get(groupPosition).getTitle());
    }

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {

    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        return 0;
    }

    public class ViewExpandableHolder {
        View top_fenge;
        TextView chapter_name;
        TextView video_name;
        CheckBox video_check;
        RelativeLayout download_child_item_zong_ly;

    }

}
