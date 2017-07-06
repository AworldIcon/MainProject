package com.coder.kzxt.classe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 班级公告
 * Created by wangtingshun on 2017/3/7.
 */

public class ClassAnnounceActivity extends BaseActivity {

    private EditText et_text;
    private String type;
    private String comecontent;
    private InputMethodManager imm;
    private Toolbar mToolbarView;
    private TextView tv_char_num; //字符个数
    private RelativeLayout rl_page;
    private  int length;
    private RelativeLayout delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        type = getIntent().getStringExtra("type");
        comecontent = getIntent().getStringExtra("content");
        initView();
        delayTime();
        initListener();
    }


    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_char_num = (TextView) findViewById(R.id.tv_char_num);
        rl_page = (RelativeLayout) findViewById(R.id.rl_page);
        delete = (RelativeLayout) findViewById(R.id.delete);
        et_text.setText(comecontent);
        length = comecontent.length();

        tv_char_num.setText(String.valueOf(length));
        rl_page.setVisibility(View.VISIBLE);
        mToolbarView.setTitle(getResources().getString(R.string.class_announce));
        et_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        et_text.setHeight((int) getResources().getDimension(R.dimen.woying_150_dip));
        et_text.setGravity(Gravity.TOP);
        if (et_text.getText().length() == 0) {
            et_text.setHint(comecontent);
        } else {
            et_text.setSelection(et_text.getText().length());
        }
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListener(){
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int index = s.length();
                if (TextUtils.isEmpty(s.toString().trim()) || index == 0) {
                    delete.setVisibility(View.INVISIBLE);
                } else {
                    et_text.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                length = s.toString().length();
                tv_char_num.setText(String.valueOf(length));
                if (length >= 200) {
                    tv_char_num.setTextColor(getResources().getColor(R.color.font_red));
                    ToastUtils.makeText(ClassAnnounceActivity.this, "仅限200以内个字符", Toast.LENGTH_SHORT).show();
                } else {
                    tv_char_num.setTextColor(getResources().getColor(R.color.font_gray));
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_text.setText("");
            }
        });


        mToolbarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.right_item:
                        save();
                        break;
                }
                return true;
            }
        });

    }

    private void save() {
        String content = et_text.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            setResult(3, new Intent().putExtra("content", content));
            finish();
//            ToastUtils.makeText(ClassAnnounceActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        } else {
//            ToastUtils.makeText(ClassAnnounceActivity.this, getResources().getString(R.string.save_content_empty), Toast.LENGTH_SHORT).show();
        }
    }

    private void delayTime() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) et_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_text, 0);
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
