package com.coder.kzxt.download;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.utils.FileUtil;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SdcardUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/22.
 */

public class DownloadServiceListAdapter extends BaseAdapter {

    private Context context;
    private DownloadServiceVideoActivity activity;
    private ArrayList<HashMap<String,String>> list;
    private HashMap<String, Boolean> isSelectedMap;
    private SharedPreferencesUtil spu;
    private String service_id;

    public DownloadServiceListAdapter(Context context, ArrayList<HashMap<String, String>> list,String service_id) {
        this.context = context;
        this.list = list;
        isSelectedMap = new HashMap<String, Boolean>();
        activity = (DownloadServiceVideoActivity) context;
        spu = new SharedPreferencesUtil(context);
        this.service_id = service_id;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public HashMap<String, Boolean> getIsSelected() {
        return isSelectedMap;
    }

    public void setIsSelected(HashMap<String, Boolean> isSelectedMap) {
        this.isSelectedMap = isSelectedMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.download_expandable_list_child_item, null);
            holder.video_name = (TextView) convertView.findViewById(R.id.video_name);
            holder.video_size_or_percentage = (TextView) convertView.findViewById(R.id.video_size_or_percentage);
            holder.video_button_play = (ImageView) convertView.findViewById(R.id.video_button_play);
            holder.video_button_down = (ImageView) convertView.findViewById(R.id.video_button_down);
            holder.video_button_wait = (ImageView) convertView.findViewById(R.id.video_button_wait);
            holder.video_button_stop = (ImageView) convertView.findViewById(R.id.video_button_stop);
            holder.video_progressbar = (ProgressBar) convertView.findViewById(R.id.video_progressbar);
            holder.video_check = (CheckBox) convertView.findViewById(R.id.video_check);
            holder.download_child_item_zong_layout = (RelativeLayout) convertView.findViewById(R.id.download_child_item_zong_layout);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = list.get(position);
        final String video_id = hashMap.get("id");
        final String tid = hashMap.get("tid");
        final String service_id = hashMap.get("service_id");
        final String name = hashMap.get("name");
        final String url = hashMap.get("url_content");
        String downloadStatus = hashMap.get("downloadStatus");
        final String downloadedSize = hashMap.get("downloadedSize");
        final String isfinish = hashMap.get("isfinish");
        final String mapKey = hashMap.get("mapKey");
        String downloadpos = hashMap.get("downloadpos");
        final String treepicture = hashMap.get("treepicture");
        String downloadPercent = hashMap.get("downloadPercent");
        String downloadSpeed = hashMap.get("downloadSpeed");
        String currentposition = hashMap.get("currentposition");
        String duration = hashMap.get("duration");
        String showcheckbox = hashMap.get("showcheckbox");
        String showtext = hashMap.get("showtext");

        holder.video_name.setText(name);
        holder.video_progressbar.setProgress((int) Float.parseFloat(downloadpos));

        // 根据getIsSelected().get(video_id)来设置checkbox的选中状况
        holder.video_check.setChecked(getIsSelected().get(video_id));

        if (showcheckbox.equals("0")) {
            holder.video_check.setVisibility(View.GONE);
        } else {
            holder.video_check.setVisibility(View.VISIBLE);
        }

        if (showtext.equals("0")) {
            holder.video_size_or_percentage.setVisibility(View.VISIBLE);
        } else {
            holder.video_size_or_percentage.setVisibility(View.GONE);
        }
        //下载完成
        if (isfinish.equals("1")) {

            long fliesize = Long.parseLong(downloadedSize);
            String formetFileSize = FileUtil.FormetFileSize(fliesize);
            holder.video_size_or_percentage.setText(formetFileSize);
            holder.video_size_or_percentage.setTextColor(context.getResources().getColor(R.color.font_gray));
            holder.video_name.setTag(3);
            holder.video_progressbar.setVisibility(View.GONE);
            holder.video_button_play.setVisibility(View.VISIBLE);
            holder.video_button_down.setVisibility(View.GONE);
            holder.video_button_wait.setVisibility(View.GONE);
            holder.video_button_stop.setVisibility(View.GONE);

            holder.video_button_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.playDownlaodVideo(hashMap);
                }
            });

        }else {
            holder.video_size_or_percentage.setText(downloadPercent + "%");
            holder.video_size_or_percentage.setTextColor(context.getResources().getColor(R.color.style_lan));
            holder.video_progressbar.setVisibility(View.VISIBLE);
            holder.video_button_play.setVisibility(View.GONE);

            if (downloadStatus.equals("1")) {
                holder.video_name.setTag(1);
                holder.video_button_down.setVisibility(View.GONE);
                holder.video_button_wait.setVisibility(View.GONE);
                holder.video_button_stop.setVisibility(View.VISIBLE);

                holder.video_size_or_percentage.setTextColor(context.getResources().getColor(R.color.style_lan));
                Drawable progressDrawable = context.getResources().getDrawable(R.drawable.download_progress_style);
                holder.video_progressbar.setProgressDrawable(progressDrawable);
            } else if (downloadStatus.equals("0")) {
                holder.video_name.setTag(0);
                holder.video_button_down.setVisibility(View.GONE);
                holder.video_button_wait.setVisibility(View.VISIBLE);
                holder.video_button_stop.setVisibility(View.GONE);
            } else if (downloadStatus.equals("2")) {
                holder.video_name.setTag(2);
                holder.video_button_down.setVisibility(View.VISIBLE);
                holder.video_button_wait.setVisibility(View.GONE);
                holder.video_button_stop.setVisibility(View.GONE);
            }

            holder.video_button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.stop_Dwonload();
                    DataBaseDao.getInstance(context).updata_DownStatus(video_id,service_id, 2);
                    for (int j = 0; j < list.size(); j++) {
                        HashMap<String, String> hashMap = list.get(j);
                        String update_id = hashMap.get("id");
                        if (update_id.equals(video_id)) {
                            hashMap.put("downloadStatus", "2");
                        }
                    }
                    activity.self_Download_Next_Video();
                    activity.updateAllOperationStatus();
                    notifyDataSetChanged();
                }
            });

            holder.video_button_wait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(context).query_All_DownStatus();
                    if (query_All_DownStatus.contains("1")) {
                        //点击不需要处理
                    } else {

                        if (NetworkUtil.isNetworkAvailable(context)) {
                            String downloadFlag = spu.getDownloadFlag();
                            if (downloadFlag.equals("1")) {

                                if (spu.getSelectAddress() == 0) {
                                    if (!SdcardUtils.isExistSdcard() || SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {
                                        Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } else if (spu.getSelectAddress() == 1) {

                                    if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                                        Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                }
                                activity.clickDownloadVideo(video_id, url, service_id, name);

                            } else {
                                if (NetworkUtil.isWifiNetwork(context)) {

                                    if (spu.getSelectAddress() == 0) {
                                        if (!SdcardUtils.isExistSdcard() || SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {
                                            Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                    } else if (spu.getSelectAddress() == 1) {

                                        if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                                            Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                    activity.clickDownloadVideo(video_id, url, service_id, name);
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.not_support_flow), Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.net_inAvailable), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            holder.video_button_down.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        String downloadFlag = spu.getDownloadFlag();

                        if (downloadFlag.equals("1")) {

                            if (spu.getSelectAddress() == 0) {
                                if (!SdcardUtils.isExistSdcard() || SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {
                                    Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                    return;
                                }

                            } else if (spu.getSelectAddress() == 1) {

                                if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                                    Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                    return;
                                }

                            }

                            ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(context).query_All_DownStatus();
                            //如果有下载的变成等待状态,否则开始下载
                            if (query_All_DownStatus.contains("1")) {
                                DataBaseDao.getInstance(context).updata_DownStatus(video_id,service_id, 0);
                                for (int j = 0; j < list.size(); j++) {
                                    HashMap<String, String> hashMap = list.get(j);
                                    String update_id = hashMap.get("id");
                                    if (update_id.equals(video_id)) {
                                        hashMap.put("downloadStatus", "0");
                                    }

                                }
                                activity.updateAllOperationStatus();
                            } else {
                                activity.clickDownloadVideo(video_id, url, service_id, name);
                            }

                        } else {
                            if (NetworkUtil.isWifiNetwork(context)) {

                                if (spu.getSelectAddress() == 0) {
                                    if (!SdcardUtils.isExistSdcard() || SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {
                                        Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } else if (spu.getSelectAddress() == 1) {
                                    if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                                        Toast.makeText(context, context.getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                                ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(context).query_All_DownStatus();
                                //如果有下载的变成等待状态,否则开始下载
                                if (query_All_DownStatus.contains("1")) {
                                    DataBaseDao.getInstance(context).updata_DownStatus(video_id,service_id, 0);
                                    for (int j = 0; j < list.size(); j++) {
                                        HashMap<String, String> hashMap = list.get(j);
                                        String update_id = hashMap.get("id");
                                        if (update_id.equals(video_id)) {
                                            hashMap.put("downloadStatus", "0");
                                        }
                                    }
                                    activity.updateAllOperationStatus();
                                } else {
                                    activity.clickDownloadVideo(video_id, url, service_id, name);
                                }
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.not_support_flow), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.net_inAvailable), Toast.LENGTH_LONG).show();
                    }
                    notifyDataSetChanged();
                }
            });

        }

        return convertView;
    }

    public class ViewHolder {
        View top_fenge;
        TextView chapter_name;
        TextView video_name;
        TextView video_size_or_percentage;
        ImageView video_button_play;
        ImageView video_button_down;
        ImageView video_button_wait;
        ImageView video_button_stop;
        ProgressBar video_progressbar;
        CheckBox video_check;
        RelativeLayout download_child_item_zong_layout;
    }
}
