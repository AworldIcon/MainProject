package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;

/**
 * Created by wangtingshun on 2017/6/7.
 * 加入限制
 */
public class JoinLimitActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RelativeLayout checkLayout; //需要审核
    private ImageView checkArrow;
    private RelativeLayout allowLayout; //允许加入
    private ImageView allowArrow;
    private RelativeLayout bannedLayout; //禁止加入
    private ImageView bannedArrow;
    private int joinFlag = 0; //加入限制:0=>需要审核 1=>允许加入 2=>禁止加入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_limit_layout);
        joinFlag = getIntent().getIntExtra("joinFlag",0);
        initView();
        initListener();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(getResources().getString(R.string.add_limit));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkLayout = (RelativeLayout) findViewById(R.id.check_layout);
        checkArrow = (ImageView) findViewById(R.id.iv_check_arrow);
        allowLayout = (RelativeLayout) findViewById(R.id.allow_join_layout);
        allowArrow = (ImageView) findViewById(R.id.iv_allow_arrow);
        bannedLayout = (RelativeLayout) findViewById(R.id.banned_from_layout);
        bannedArrow = (ImageView) findViewById(R.id.iv_banned_arrow);
        currentState(joinFlag);
    }

    private void initListener() {
        //需要审核
        checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinFlag = 0;
                currentState(joinFlag);
                backData();
            }
        });
        //允许加入
        allowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinFlag = 1;
                currentState(joinFlag);
                backData();
            }
        });
        //禁止加入
        bannedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinFlag = 2;
                currentState(joinFlag);
                backData();
            }
        });
    }

    /**
     * 当前加入状态
     * @param flag
     */
    private void currentState(int flag) {
        if (flag == 0) {
            checkArrow.setVisibility(View.VISIBLE);
            allowArrow.setVisibility(View.INVISIBLE);
            bannedArrow.setVisibility(View.INVISIBLE);
            checkArrow.setBackgroundResource(R.drawable.iv_class_select);
        } else if (flag == 1) {
            checkArrow.setVisibility(View.INVISIBLE);
            allowArrow.setVisibility(View.VISIBLE);
            bannedArrow.setVisibility(View.INVISIBLE);
            allowArrow.setBackgroundResource(R.drawable.iv_class_select);
        } else if (flag == 2) {
            checkArrow.setVisibility(View.INVISIBLE);
            allowArrow.setVisibility(View.INVISIBLE);
            bannedArrow.setVisibility(View.VISIBLE);
            bannedArrow.setBackgroundResource(R.drawable.iv_class_select);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                backData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backData() {
        setResult(10086,new Intent().putExtra("joinFlag",joinFlag));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
