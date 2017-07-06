package com.coder.kzxt.download;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadServiceFragment extends BaseFragment
{

    private View v;
    private LinearLayout no_info_layout;
    private MyRecyclerView myRecyclerView;
    private ArrayList<HashMap<String, String>> arrayList;
    private Handler handler;
    private ArrayList<HashMap<String, String>> query_All_DownLoad;
    private DownloadCourseDelegate downloadCourseDelegate;
    private BaseRecyclerAdapter listAdapter;
    private MyReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        arrayList = new ArrayList<>();
        query_All_DownLoad = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (v == null)
        {
            v = inflater.inflate(R.layout.fragment_download_service, container, false);
            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            myRecyclerView = (MyRecyclerView) v.findViewById(R.id.myRecyclerView);
        }


        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null)
        {
            parent.removeView(v);
        }

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MY_DOWNLOAD_FAIL);
        filter.addAction(Constants.MY_DOWNLOAD_FINSH);
        filter.addAction(Constants.MY_DOWNLOAD_REFRESH);
        filter.addAction(Constants.MY_DOWNLOAD_DOWNING);
        // 注册广播
        getActivity().registerReceiver(receiver, filter);
        return v;
    }


    /**
     * 获取服务的下载列表
     */
    public void getServiceVideos()
    {
        query_All_DownLoad = DataBaseDao.getInstance(getActivity()).queryAllDownLoadService();
        if (query_All_DownLoad.size() == 0)
        {
            no_info_layout.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.GONE);
        } else
        {
            no_info_layout.setVisibility(View.GONE);
            myRecyclerView.setVisibility(View.VISIBLE);
            arrayList.clear();
            for (int i = 0; i < query_All_DownLoad.size(); i++)
            {
                long allsize = 0;
                ArrayList<String> centre_redList = new ArrayList<String>();
                HashMap<String, String> courseMap = new HashMap<String, String>();
                HashMap<String, String> hashMap = query_All_DownLoad.get(i);
                String service_id = hashMap.get("service_id");
                String service_name = hashMap.get("service_name");
                String service_picture = hashMap.get("service_picture");

                courseMap.put("treeid", service_id);
                courseMap.put("treename", service_name);
                courseMap.put("treepicture", service_picture);

                ArrayList<HashMap<String, String>> query_All_videoId = DataBaseDao.getInstance(getActivity()).queryServiceVideoSize(service_id);
                courseMap.put("service_number", String.valueOf(query_All_videoId.size()));// 下载个数

                for (int j = 0; j < query_All_videoId.size(); j++)
                {
                    allsize = allsize + Long.parseLong(query_All_videoId.get(j).get("downloadedSize"));
                    String downloadStatus = query_All_videoId.get(j).get("downloadStatus");
                    centre_redList.add(downloadStatus);
                }

                courseMap.put("allFileSize", FileUtil.FormetFileSize(allsize));// 所有文件大小

                if (centre_redList.contains("1"))
                {
                    courseMap.put("centre_red", "1");// 此课程是否正在下载
                } else
                {
                    courseMap.put("centre_red", "0");
                }
                arrayList.add(courseMap);
            }
            if(listAdapter != null)
            {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onVisible()
    {
        super.onVisible();
        if (listAdapter != null)
            getServiceVideos();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (listAdapter != null)
            getServiceVideos();
    }

    @Override
    protected void requestData()
    {

        downloadCourseDelegate = new DownloadCourseDelegate(getActivity(), arrayList);
        listAdapter = new BaseRecyclerAdapter(getActivity(), arrayList, downloadCourseDelegate);
        myRecyclerView.setAdapter(listAdapter);
        getServiceVideos();

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HashMap<String, String> hashMap = arrayList.get(position);
                final String service_id = hashMap.get("treeid");
                final String service_name = hashMap.get("treename");
                Intent intent = new Intent(getActivity(), DownloadServiceVideoActivity.class);
                intent.putExtra("service_id", service_id);
                intent.putExtra("service_name", service_name);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);

    }

    private class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            //下载完成
            if (action.equals(Constants.MY_DOWNLOAD_FINSH) || action.equals(Constants.MY_DOWNLOAD_REFRESH)|| action.equals(Constants.MY_DOWNLOAD_FAIL)|| action.equals(Constants.MY_DOWNLOAD_DOWNING)) {
                getServiceVideos();
            }
        }
    }


}
