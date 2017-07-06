package com.coder.kzxt.service.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.download.DownloadManager;
import com.coder.kzxt.download.DownloadService;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceVideoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class ServiceVideoDelegate extends PullRefreshDelegate<ServiceVideoResult.ItemsBean>
{
    private BaseActivity mContext;
    private DownloadManager downloadManager;
    private ServiceBean serviceBean;
    private ArrayList<String> videoIds;
    private int memberedId;

    // 查询数据库中所有下载状态
    ArrayList<String> query_All_DownStatus;
    List<String> downLoading;

    public ServiceVideoDelegate(BaseActivity context, ServiceBean serviceBean, int memberedId)
    {
        super(R.layout.item_service_video);
        this.mContext = context;
        this.serviceBean = serviceBean;
        this.memberedId = memberedId;
        downloadManager = DownloadService.getDownloadManager(mContext);
        // 获得数据库中所有的视频id
        videoIds = new ArrayList<>();
        query_All_DownStatus = new ArrayList<>();
        downLoading = new ArrayList<>();
    }

    public void setDataDao(ArrayList<String> videoIds, ArrayList<String> query_All_DownStatus, List<String> downLoading)
    {
        this.videoIds = videoIds;
        this.query_All_DownStatus = query_All_DownStatus;
        this.downLoading = downLoading;
        notifyDataSetChanged();
        L.d("setDataDao: videoIds " + videoIds.toString() + "; query_All_DownStatus " + query_All_DownStatus.toString() + "; downLoading " + downLoading.toString());
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<ServiceVideoResult.ItemsBean> data, int position)
    {
        ImageView videoImage = holder.findViewById(R.id.videoImage);
        TextView videoName = holder.findViewById(R.id.videoName);
        TextView videoLength = holder.findViewById(R.id.videoLength);
        ProgressBar progressBar = holder.findViewById(R.id.progressBar);

        final ServiceVideoResult.ItemsBean itemsBean = data.get(position);
//        GlideUtils.loadZipImg(mContext, itemsBean.getPicture(), videoImage);
        videoName.setText(itemsBean.getTitle());
        videoLength.setText(itemsBean.getFormat_lenght());

        if (videoIds.contains(itemsBean.getId()))
        {
            if (downLoading.contains(itemsBean.getId()))
            {
                videoImage.setImageResource(R.drawable.service_video_downed);
                progressBar.setVisibility(View.GONE);
                videoImage.setVisibility(View.VISIBLE);
            } else
            {
                videoImage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        } else
        {
            videoImage.setImageResource(R.drawable.service_video_down);
            progressBar.setVisibility(View.GONE);
            videoImage.setVisibility(View.VISIBLE);
        }


        videoImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if (TextUtils.isEmpty(new SharedPreferencesUtil(mContext).getIsLogin()))
                {
                    mContext.startActivityForResult(new Intent(mContext, LoginActivity.class), 1000);
                } else if (memberedId == -1)
                {
                    ToastUtils.makeText(mContext, mContext.getResources().getString(R.string.plase_join_service));
                } else
                {
                    String downUrl = itemsBean.getMedia_uri();
                    if (!TextUtils.isEmpty(downUrl) && downUrl.length() > 0)
                    {
                        if (!videoIds.contains(itemsBean.getId()))
                        {
                            // 判断视频是否有正在下载的视频
                            if (query_All_DownStatus.contains("1"))
                            {
                                // 如果有重复的课时就不添加
                                String query_item_filename = DataBaseDao.getInstance(mContext).query_item_filename(itemsBean.getId());
                                if (TextUtils.isEmpty(query_item_filename))
                                {
                                    DataBaseDao.getInstance(mContext).initDownloadData(itemsBean.getService_id(), serviceBean.getTitle(),
                                            itemsBean.getId(), itemsBean.getTitle(), serviceBean.getPicture(), downUrl,0);
                                }

                            } else
                            {
                                String query_item_filename = DataBaseDao.getInstance(mContext).query_item_filename(itemsBean.getId());

                                if (TextUtils.isEmpty(query_item_filename))
                                {
                                    DataBaseDao.getInstance(mContext).initDownloadData(itemsBean.getService_id(), serviceBean.getTitle(),
                                            itemsBean.getId(), itemsBean.getTitle(), serviceBean.getPicture(), downUrl,1);

                                    downloadManager = DownloadService.getDownloadManager(mContext);
                                    downloadManager.addServiceVideoDownload(downUrl, itemsBean.getService_id(), itemsBean.getId(), itemsBean.getId(), 0, itemsBean.getTitle(), 0, "1");
                                }
                            }

                            Intent intent_refresh = new Intent();
                            intent_refresh.setAction(Constants.MY_DOWNLOAD_REFRESH);
                            intent_refresh.putExtra("id", itemsBean.getId());
                            mContext.sendBroadcast(intent_refresh);

                        }
                        ToastUtils.makeText(mContext, "已加入下载队列");
                    }
                }

            }
        });
    }
}
