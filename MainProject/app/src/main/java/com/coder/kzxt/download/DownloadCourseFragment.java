package com.coder.kzxt.download;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadCourseFragment extends BaseFragment {

    private View v;
    private LinearLayout no_info_layout;
    private MyRecyclerView myRecyclerView;
    private ArrayList<HashMap<String, String>> query_All_DownLoad;
    private ArrayList<HashMap<String, String>> arrayList;
    private Handler handler=null;
    private DownloadCourseDelegate downloadCourseDelegate;
    private BaseRecyclerAdapter listAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler();
        query_All_DownLoad = new ArrayList<>();
        arrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v==null){
            v = inflater.inflate(R.layout.fragment_download_course, container, false);
            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            myRecyclerView = (MyRecyclerView) v.findViewById(R.id.myRecyclerView);
        }


        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;

    }

    //查询数据库后，更新UI界面
    Runnable runnableUi=new Runnable() {
        @Override
        public void run() {
            getCourseVideos();
        }
    };

    @Override
    public void onResume() {
        /**
         * 查询数据库耗时操作    在fragment中需要开子线程，不然报错，下一个fragment有预加载，则可有可无
         */
        new Thread(){
            @Override
            public void run() {
                handler.post(runnableUi);
            }
        }.start();
        // 页面埋点
        StatService.onResume(this);
        super.onResume();
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onPause() {
        super.onPause();
        // 页面埋点
        StatService.onPause(this);
    }

    /**
     *  获取课程的下载列表
     */
    public void getCourseVideos() {
        // 查询所有下载的课程
        query_All_DownLoad = DataBaseDao.getInstance(getActivity()).queryAllDownLoadCourse();
        if (query_All_DownLoad.size() == 0) {
            no_info_layout.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.GONE);
        } else {
            myRecyclerView.setVisibility(View.VISIBLE);
            no_info_layout.setVisibility(View.GONE);
            arrayList.clear();
            for (int i = 0; i < query_All_DownLoad.size(); i++) {
                long allsize = 0;
                ArrayList<String> centre_redList = new ArrayList<String>();
                HashMap<String, String> courseMap = new HashMap<String, String>();
                HashMap<String, String> hashMap = query_All_DownLoad.get(i);
                String treeid = hashMap.get("treeid");
                String treename = hashMap.get("treename");
                String treepicture = hashMap.get("treepicture");

                courseMap.put("treeid", treeid);// 课程id
                courseMap.put("treename", treename);// 课程名字
                courseMap.put("treepicture", treepicture);// 课程图片

                ArrayList<HashMap<String, String>> query_All_videoId = DataBaseDao.getInstance(getActivity()).query_All_videoId(treeid);
                courseMap.put("coursenumber", String.valueOf(query_All_videoId.size()));// 下载个数
                for (int j = 0; j < query_All_videoId.size(); j++) {
                    allsize += Long.parseLong(query_All_videoId.get(j).get("downloadedSize"));
                    String downloadStatus = query_All_videoId.get(j).get("downloadStatus");
                    centre_redList.add(downloadStatus);
                }

                String allFileSize = FileUtil.FormetFileSize(allsize);
                courseMap.put("allFileSize", allFileSize);// 所有文件大小
                if (centre_redList.contains("1")) {
                    courseMap.put("centre_red", "1");// 此课程是否正在下载
                } else {
                    courseMap.put("centre_red", "0");
                }

                arrayList.add(courseMap);
            }
            downloadCourseDelegate = new DownloadCourseDelegate(getActivity(),arrayList);
            listAdapter = new BaseRecyclerAdapter(getActivity(),arrayList,downloadCourseDelegate);
            myRecyclerView.setAdapter(listAdapter);

           listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String,String> hashMap = arrayList.get(position);
                final String treeid = hashMap.get("treeid");
                final String treename = hashMap.get("treename");
                final String treepicture = hashMap.get("treepicture");
                Intent intent = new Intent(getActivity(), Download_ExpandableList_Activity.class);
                intent.putExtra("treeid", treeid);
                intent.putExtra("treename", treename);
                intent.putExtra("treepicture", treepicture);
                startActivityForResult(intent, 1);
            }
        });

        }

    }

}
