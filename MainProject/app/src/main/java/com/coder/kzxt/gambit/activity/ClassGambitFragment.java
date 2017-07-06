package com.coder.kzxt.gambit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.gambit.activity.adapter.ClassGambitAdapter;
import com.coder.kzxt.gambit.activity.adapter.ClassGambitDelegate;
import com.coder.kzxt.gambit.activity.bean.GambitEntity;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/3/15.
 */

public class ClassGambitFragment extends BaseFragment implements InterfaceHttpResult {
    private View v;
    private MyPullRecyclerView myPullRecyclerView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private HttpGetOld httpGet;
    private List<GambitEntity> arrayList=new ArrayList<>();
    private String canSubmitTopic;//当前用户是否拥有发表话题权限： 0否 1是
    private ClassGambitDelegate myGambitDelegate;
    private ClassGambitAdapter myGambitAdapter;
    private int index=1;
    /** 请求类型 */
    private String type;
    /** 班级id*/
    private String classId;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;

    @Override
    protected void requestData() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_class_gambit, container, false);
            Bundle bundle=getArguments();
            type = bundle != null ? bundle.getString("type") : "";
            classId = bundle != null ? bundle.getString("classId") : "";
        }
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        initview();
        httpRequest();
        return v;

    }


    private void initview() {
        search_jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) v.findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
        myPullSwipeRefresh= (MyPullSwipeRefresh) v.findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView= (MyPullRecyclerView) v.findViewById(R.id.myRecyclerView);
        myGambitDelegate=new ClassGambitDelegate(getContext());
        myGambitAdapter=new ClassGambitAdapter(getContext(),arrayList,myGambitDelegate);
        myPullRecyclerView.setAdapter(myGambitAdapter);
        myGambitAdapter.setSwipeRefresh(myPullSwipeRefresh);
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index=1;
                myGambitAdapter.resetPageIndex();
                httpRequest();
            }
        });
        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                myGambitAdapter.addPageIndex();
                if(index<myGambitAdapter.getTotalPage()){
                    index++;
                }
                httpRequest();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest();
            }
        });
    }

    private void httpRequest() {
        no_info_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        httpGet=new HttpGetOld(getContext(),getContext(),this,null, Urls.GET_CLASS_THREAD_APPRAISE_ACTION,classId,"20",String.valueOf(index),type);
        httpGet.excute();
    }
    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
        search_jiazai_layout.setVisibility(View.GONE);
        if(code==Constants.HTTP_CODE_SUCCESS){
            List<GambitEntity>newList=new ArrayList<>();
            try {
                JSONObject jsonO = new JSONObject(httpGet.getStringData());
                JSONArray array= new JSONArray(jsonO.getString("data"));
                if(array.length()>0){
                    for (int i = 0; i <array.length() ; i++) {
                        GambitEntity GambitEntity = new GambitEntity();
                        ArrayList<String> list = new ArrayList<String>();
                        JSONObject jsonObject = array.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String userName = jsonObject.getString("userName");
                        String userFace = jsonObject.getString("userFace");
                        String title = jsonObject.getString("title");
                        String isTop = jsonObject.getString("isTop");
                        String isLiked = jsonObject.getString("isLiked");
                        String likeNum = jsonObject.getString("likeNum");
                        String createdTime = jsonObject.getString("createdTime");
                        String audioUrl = jsonObject.getString("audioUrl");
                        String audioDuration = jsonObject.getString("audioDuration");

                        GambitEntity.setId(id);
                        GambitEntity.setUserName(userName);
                        GambitEntity.setIsLiked(isLiked);
                        GambitEntity.setLikeNum(likeNum);
                        GambitEntity.setUserFace(userFace);
                        GambitEntity.setTitle(title);
                        GambitEntity.setIsTop(isTop);
                        GambitEntity.setCreatedTime(createdTime);
                        GambitEntity.setAudioUrl(audioUrl);
                        GambitEntity.setAudioDuration(audioDuration);

                        if(jsonObject.has("img")){
                            String img = jsonObject.getString("img");
                            JSONArray imgArray= new JSONArray(img);
                            if(imgArray.length()>0){
                                for (int j = 0; j < imgArray.length(); j++) {
                                    String imgUrl = imgArray.getString(j);
                                    list.add(imgUrl);
                                }
                            }
                        }

                        GambitEntity.setImgs(list);
                        newList.add(GambitEntity);
                    }

                }
                if(newList.size()==0){
                    no_info_layout.setVisibility(View.VISIBLE);
                }else {
                    search_jiazai_layout.setVisibility(View.GONE);
                }
                myGambitAdapter.setTotalPage(jsonO.getInt("totalPages"));
                myGambitAdapter.setPullData(newList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void refreshView() {
        httpRequest();
    }

    }
