package com.coder.kzxt.buildwork.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.buildwork.adapter.WorkStuAdapter;
import com.coder.kzxt.buildwork.adapter.WorkStuDelegate;
import com.coder.kzxt.buildwork.activity.CheckStuWorkActivity;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.FinishWorkNum;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.app.http.UrlsNew;
import java.util.ArrayList;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by pc on 2016/10/19.
 */
public class FinishWorkFragment extends BaseFragment implements HttpCallBack {
    private View v;
    private MyPullRecyclerView recyclerView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private String isCenter;
    private String testId;
    private String title="";
    private int workType;
    private ArrayList<FinishWorkNum.ItemsBean>listData=new ArrayList<>();
    private EditText search_edittext;
    private ImageView search_image;
    private FinishWorkNum finishWorkNum;
    private WorkStuAdapter workStuAdapter;
    private WorkStuDelegate workStuDelegate;
    private ImageView no_info_img;
    private Button load_fail_button;
    private LinearLayout jiazai_layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.finish_work_fragment, container, false);
            Bundle bundle=getArguments();
            testId=bundle.getString("testId");
            title=bundle.getString("name");
            workType=bundle.getInt("workType");
            search_edittext= (EditText) v.findViewById(R.id.search_edittext);
            search_image= (ImageView) v.findViewById(R.id.search_image);
            recyclerView= (MyPullRecyclerView) v.findViewById(R.id.myRecyclerView);
            myPullSwipeRefresh= (MyPullSwipeRefresh) v.findViewById(R.id.myPullSwipeRefresh);
            no_info_layout = (LinearLayout)v. findViewById(R.id.no_info_layout);
            load_fail_button = (Button)v.findViewById(R.id.load_fail_button);
            load_fail_layout = (LinearLayout)v. findViewById(R.id.load_fail_layout);
            jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
            no_info_img= (ImageView) v.findViewById(R.id.no_info_img);
            no_info_img.setImageResource(R.drawable.empty_no_student);
            workStuDelegate=new WorkStuDelegate(getContext());
            workStuAdapter=new WorkStuAdapter(getContext(),listData,workStuDelegate);
            recyclerView.setAdapter(workStuAdapter);
            workStuAdapter.setSwipeRefresh(myPullSwipeRefresh);
            jiazai_layout.setVisibility(View.VISIBLE);
            getData();

            initEvents();
        }
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;

    }

    private void getData() {
        new HttpGetBuilder(getContext()).setHttpResult(this)
                .setUrl(UrlsNew.TEST_RESULT)
                .setClassObj(FinishWorkNum.class)
                .addQueryParams("page",workStuAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .addQueryParams("test_id",testId)
                .build();
    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        load_fail_layout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        finishWorkNum= (FinishWorkNum) resultBean;
        workStuAdapter.setTotalPage(Integer.valueOf(finishWorkNum.getPaginate().getPageNum().trim()));
        workStuAdapter.setPullData(finishWorkNum.getItems());
        Intent intent=new Intent();
        intent.setAction("update_work_finishnum");
        intent.putExtra("num",finishWorkNum.getPaginate().getTotalNum());
        getContext().sendBroadcast(intent);
        if(workStuAdapter.getData().size()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.VISIBLE);
    }
    private void search() {

        String searchContext = search_edittext.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext)) {
           //(this, "输入框为空，请输入");
        } else {
            // 调用搜索的API方法
        }
    }
    private void initEvents() {
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                jiazai_layout.setVisibility(View.VISIBLE);
                workStuAdapter.resetPageIndex();
                getData();
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_image.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(TextUtils.isEmpty(s)){
                                    search_image.setVisibility(View.VISIBLE);
                                }else {
                                    search_image.setVisibility(View.GONE);
                                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    search();
                }
                return false;
            }
        });
        workStuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String resultId=workStuAdapter.getData().get(position).getId();//学生交卷后生成的结果id
                String name=workStuAdapter.getData().get(position).getProfile().getNickname();
                String status_text=workStuAdapter.getData().get(position).getStatus();//完成状态
                Intent intent=new Intent(getContext(),CheckStuWorkActivity.class);
                intent.putExtra("nickname",name);
                intent.putExtra("resultId",resultId);
                intent.putExtra("name",title);
                intent.putExtra("status",status_text);//不会出现答题中的状态，只有完成以及待批
                intent.putExtra("workType",workType);
                intent.putExtra(Constants.IS_CENTER,isCenter);
                getActivity().startActivityForResult(intent,1);
            }
        });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                workStuAdapter.resetPageIndex();
                getData();
            }
        });
        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                workStuAdapter.addPageIndex();
                getData();
            }
        });
    }


    @Override
    protected void requestData() {

    }

    public void update(){
        getData();
    }
}
