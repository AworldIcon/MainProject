package com.coder.kzxt.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.widget.LimitInputTextWatcher;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.beans.ServiceFormResult;
import com.coder.kzxt.utils.DateTimePickerDialog;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.RegularUtils;
import com.coder.kzxt.utils.ToastUtils;


/**
 * 就业登记表1
 */

public class FindJobActivity1 extends BaseActivity
{


    private View mainView;
    private Toolbar mToolbarView;
    private Button bottomButton;

    private EditText mName;
    private TextView mSex;
    private EditText mPhone;
    private EditText mEmail;
    private EditText mQq;
    private TextView mBirthday;
    private EditText mCardId;
    private Button mButton;

    ServiceFormResult.ItemBean serviceFormBean;

    private int Job_register;

    public static void gotoActivity(Context context, ServiceFormResult.ItemBean bean, int Job_register)
    {
        ((BaseActivity) context).startActivityForResult(new Intent(context, FindJobActivity1.class)
                .putExtra("bean", bean).putExtra("Job_register", Job_register), 3000);
    }

    private void initVariable()
    {
        serviceFormBean = (ServiceFormResult.ItemBean) getIntent().getSerializableExtra("bean");
        Job_register = getIntent().getIntExtra("Job_register", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_job1);
        //初始化 view findviewbyid
        initFindViewById();
        initData();
        //响应事件click
        initClick();

    }

    private void initFindViewById()
    {
        mainView = findViewById(R.id.mainView);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mName = (EditText) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);
        mPhone = (EditText) findViewById(R.id.phone);
        mEmail = (EditText) findViewById(R.id.email);
        mQq = (EditText) findViewById(R.id.qq);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mCardId = (EditText) findViewById(R.id.cardId);
        mButton = (Button) findViewById(R.id.button);
        bottomButton = (Button) findViewById(R.id.button);

        mToolbarView.setTitle(getResources().getString(R.string.find_job_form));
        setSupportActionBar(mToolbarView);
    }

    private void initData()
    {
        if (serviceFormBean.getId() != null)
        {
            mName.setText(serviceFormBean.getName());
            mSex.setText(serviceFormBean.getGender());
            mPhone.setText(serviceFormBean.getMobile());
            mEmail.setText(serviceFormBean.getEmail());
            mQq.setText(serviceFormBean.getQq());
            mBirthday.setText(DateUtil.getDateStr2(serviceFormBean.getDate_birth()));
            mCardId.setText(serviceFormBean.getId_card());
            gender = serviceFormBean.getGenderValue();
        }
        mName.addTextChangedListener(new LimitInputTextWatcher(mName));
    }

    private void initClick()
    {
        mSex.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showGenderDialog();
            }
        });

        mBirthday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDateTimeDialog();
            }
        });

        bottomButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!(mName.getText().toString().length() > 1 && mName.getText().toString().length() < 11))
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效姓名");
                    return;
                }
                if (TextUtils.isEmpty(gender))
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请选择性别");
                    return;
                }
                if (mPhone.getText().toString().length() != 11)
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效手机号");
                    return;
                }

                String s = mEmail.getText().toString();
                if (!RegularUtils.isEmail(s))
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效邮箱");
                    return;
                }

                int qqlength = mQq.getText().toString().length();
                if (qqlength < 6 || qqlength > 10)
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效QQ号");
                    return;
                }
                if (TextUtils.isEmpty(mBirthday.getText().toString()))
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效出生日期");
                    return;
                }

                if (!RegularUtils.isIDCard(mCardId.getText().toString()))
                {
                    ToastUtils.makeText(FindJobActivity1.this, "请输入有效身份证");
                    return;
                }

                serviceFormBean.setName(mName.getText().toString());
                serviceFormBean.setGender(mSex.getText().toString());
                serviceFormBean.setMobile(mPhone.getText().toString());
                serviceFormBean.setEmail(mEmail.getText().toString());
                serviceFormBean.setQq(mQq.getText().toString());
                serviceFormBean.setDate_birthStr(mBirthday.getText().toString());
                serviceFormBean.setId_card(mCardId.getText().toString());

                FindJobActivity2.gotoActivity(FindJobActivity1.this, serviceFormBean, Job_register);
            }
        });
    }


    private String gender;

    private CustomListDialog customListDialog;

    private void showGenderDialog()
    {

        if (customListDialog == null)
        {
            final String[] data = new String[]{"男", "女"};
            customListDialog = new CustomListDialog(FindJobActivity1.this);
            customListDialog.addData(data);
            customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    gender = position + "";
                    mSex.setText(data[position]);
                    customListDialog.cancel();
                }
            });
        }
        customListDialog.show();

    }


    private DateTimePickerDialog dateTimePickerDialog;

    private void showDateTimeDialog()
    {

        if (dateTimePickerDialog == null)
        {
            dateTimePickerDialog = new DateTimePickerDialog(FindJobActivity1.this);
            dateTimePickerDialog.setTimeGone();
            dateTimePickerDialog.updateDate(1990, 0, 1);
            dateTimePickerDialog.setDatePickerClickListener(new DateTimePickerDialog.DatePickerClickListener()
            {
                @Override
                public void onDateClick(int year, int month, int day)
                {
                    mBirthday.setText(year + "-" + month + "-" + day);
                }
            });
        }
        dateTimePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000)
        {
            setResult(3000);
            finish();
        }
    }

}
