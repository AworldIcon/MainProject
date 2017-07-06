package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class HolderBaseAdapter<T> extends BaseAdapter {

    protected List<T> data;

    public void setData(List<T> data) {
        this.data = data;
    }

    //重新赋值
    public void resetData(List<T> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(T bean) {
        if (this.data != null) {
            this.data.add(bean);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (this.data == null) {
            return;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    private int count = -1;

    public void setCount(int i) {
        count = i;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        int getCount = data.size();
        return count == -1 ? getCount : Math.min(getCount, count);
    }

    @Override
    public Object getItem(int position) {

        return data == null ? new Object() : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getViewHolder(convertView, parent, position).getConvertView();
    }

    public abstract ViewHolder getViewHolder(View convertView, ViewGroup parent, int position);


    public static class ViewHolder {
        private final SparseArray<View> views;
        private View convertView;

        private ViewHolder(Context mContext, View convertView) {
            this.views = new SparseArray<View>();
            this.convertView = convertView;
            convertView.setTag(this);
        }

        public static ViewHolder get(Context mContext, View convertView, ViewGroup parent, int resId) {
            if (convertView == null) {
                LayoutInflater factory = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = factory.inflate(resId, parent, false);
                return new ViewHolder(mContext, convertView);
            }
            return (ViewHolder) convertView.getTag();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T findViewById(int resourceId) {
            View view = views.get(resourceId);
            if (view == null) {
                view = convertView.findViewById(resourceId);
                views.put(resourceId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return convertView;
        }

        public void setConvertView(View convertView) {
            this.convertView = convertView;
        }

    }

    // public void updateView(AbsListView absView, int itemIndex) {
    // int firstVisiblePosition = absView.getFirstVisiblePosition();
    // int lastVisiblePosition = absView.getLastVisiblePosition();
    // if (itemIndex >= firstVisiblePosition && itemIndex <=
    // lastVisiblePosition) {
    // View view = absView.getChildAt(itemIndex - firstVisiblePosition);
    // int type = getItemViewType(itemIndex);
    // if (view.getTag() instanceof ViewHolder) {
    // ViewHolder mViewHolder = (ViewHolder) view.getTag();
    // updateView(itemIndex, type, mViewHolder);
    // }
    // }
    // }
    //
    // public void updateView(int index, int type, ViewHolder mViewHolder) {
    // };
}