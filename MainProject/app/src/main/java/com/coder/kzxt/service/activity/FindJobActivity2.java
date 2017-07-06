package com.coder.kzxt.service.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.beans.ServiceFormResult;
import com.coder.kzxt.utils.DateTimePickerDialog;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.SelectAddressDialog;
import com.coder.kzxt.utils.ToastUtils;

import static com.coder.kzxt.activity.R.id.experience;


/**
 * 就业登记表2
 */

public class FindJobActivity2 extends BaseActivity implements HttpCallBack
{


    private View mainView;
    private Toolbar mToolbarView;

    private EditText mSchool;
    private EditText mCategory;
    private TextView mAcademic;
    private TextView mFinishDay;
    private EditText mExperience;
    private TextView mCity;
    private TextView fontNumber;
    private Button mButton;

    private ServiceFormResult.ItemBean serviceBean;
    private int job_register;
    public static void gotoActivity(BaseActivity context, ServiceFormResult.ItemBean serviceBean, int job_register)
    {
        context.startActivityForResult(new Intent(context, FindJobActivity2.class)
                .putExtra("serviceBean", serviceBean)
                .putExtra("job_register", job_register)
                , 1000
        );
    }

    private void initVariable()
    {
        serviceBean = (ServiceFormResult.ItemBean) getIntent().getSerializableExtra("serviceBean");
        job_register = getIntent().getIntExtra("job_register",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_job2);
        //初始化 view findViewById
        initFindViewById();
        initData();
        //响应事件click
        initClick();

    }


    private void initFindViewById()
    {
        mainView = findViewById(R.id.mainView);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mSchool = (EditText) findViewById(R.id.school);
        mCategory = (EditText) findViewById(R.id.category);
        mAcademic = (TextView) findViewById(R.id.academic);
        mFinishDay = (TextView) findViewById(R.id.finishDay);
        mExperience = (EditText) findViewById(experience);
        mCity = (TextView) findViewById(R.id.city);
        fontNumber = (TextView) findViewById(R.id.fontNumber);
        mButton = (Button) findViewById(R.id.button);

        mToolbarView.setTitle(getResources().getString(R.string.find_job_form));
        setSupportActionBar(mToolbarView);
    }


    private void initData()
    {

        if (serviceBean.getId() != null)
        {
            mSchool.setText(serviceBean.getSchool());
            mCategory.setText(serviceBean.getMajor());
            mAcademic.setText(serviceBean.getEducation());
            mFinishDay.setText(DateUtil.getDateStr2(serviceBean.getGraduation_time()));
            mExperience.setText(serviceBean.getExperience());
            mCity.setText(serviceBean.getCity());
            fontNumber.setText(serviceBean.getExperience().length()+"/500");
        }

    }


    private void initClick()
    {
        mAcademic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAcademicDialog();
            }
        });

        mFinishDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDateTimeDialog();
            }
        });

        mCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCityDialog();
            }
        });

        mExperience.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s != null)
                {
                    fontNumber.setText(s.toString().length()+"/500" );
                }
            }
        });
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int schoolLength = mSchool.getText().toString().length();
                if (schoolLength < 4 || schoolLength > 20)
                {
                    ToastUtils.makeText(FindJobActivity2.this, "请输入有效学校名称");
                    return;
                }

                int mCategoryLength = mCategory.getText().toString().length();
                if (mCategoryLength < 2 || mCategoryLength > 10)
                {
                    ToastUtils.makeText(FindJobActivity2.this, "请输入有效专业名称");
                    return;
                }

                if (TextUtils.isEmpty(mAcademic.getText().toString()))
                {
                    ToastUtils.makeText(FindJobActivity2.this, "请选择学历");
                    return;
                }

                if (TextUtils.isEmpty(mFinishDay.getText().toString()))
                {
                    ToastUtils.makeText(FindJobActivity2.this, "请输入毕业年份");
                    return;
                }


                if (job_register == 0){

                    new HttpPostBuilder(FindJobActivity2.this)
                            .setUrl(UrlsNew.SERVICE_JOB_REGISTER)
                            .setHttpResult(FindJobActivity2.this)
                            .setClassObj(BaseBean.class)
                            .addBodyParam("id", serviceBean.getItem_id())
                            .addBodyParam("name", serviceBean.getName())
                            .addBodyParam("gender", serviceBean.getGenderString())
                            .addBodyParam("id_card", serviceBean.getId_card())
                            .addBodyParam("qq", serviceBean.getQq())
                            .addBodyParam("email", serviceBean.getEmail())
                            .addBodyParam("date_birth", serviceBean.getDate_birthStr()+"")
                            .addBodyParam("graduation_time", mFinishDay.getText().toString())
                            .addBodyParam("mobile", serviceBean.getMobile())
                            .addBodyParam("education", mAcademic.getText().toString())
                            .addBodyParam("major", mCategory.getText().toString())
                            .addBodyParam("school", mSchool.getText().toString())
                            .addBodyParam("city", mCity.getText().toString())
                            .addBodyParam("experience", mExperience.getText().toString())
                            .build();
                }else {

                    new HttpPutBuilder(FindJobActivity2.this)
                            .setUrl(UrlsNew.SERVICE_JOB_REGISTER)
                            .setHttpResult(FindJobActivity2.this)
                            .setClassObj(BaseBean.class)
                            .setPath(serviceBean.getId())
                            .addBodyParam("id", serviceBean.getItem_id())
                            .addBodyParam("name", serviceBean.getName())
                            .addBodyParam("gender", serviceBean.getGenderString())
                            .addBodyParam("id_card", serviceBean.getId_card())
                            .addBodyParam("qq", serviceBean.getQq())
                            .addBodyParam("email", serviceBean.getEmail())
                            .addBodyParam("date_birth", serviceBean.getDate_birthStr()+"")
                            .addBodyParam("graduation_time", mFinishDay.getText().toString())
                            .addBodyParam("mobile", serviceBean.getMobile())
                            .addBodyParam("education", mAcademic.getText().toString())
                            .addBodyParam("major", mCategory.getText().toString())
                            .addBodyParam("school", mSchool.getText().toString())
                            .addBodyParam("city", mCity.getText().toString())
                            .addBodyParam("experience", mExperience.getText().toString())
                            .build();
                }

            }
        });
    }


    private CustomListDialog customListDialog;

    private void showAcademicDialog()
    {

        if (customListDialog == null)
        {
            final String[] data = new String[]{"中专以下", "大专", "本科", "硕士", "博士"};
            customListDialog = new CustomListDialog(FindJobActivity2.this);
            customListDialog.addData(data);
            customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    mAcademic.setText(data[position]);
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
            dateTimePickerDialog = new DateTimePickerDialog(FindJobActivity2.this);
            dateTimePickerDialog.setTimeGone();
            dateTimePickerDialog.updateDate(2010, 6, 1);
            dateTimePickerDialog.setDatePickerClickListener(new DateTimePickerDialog.DatePickerClickListener()
            {
                @Override
                public void onDateClick(int year, int month, int day)
                {
                    mFinishDay.setText(year + "-" + month + "-" + day);
                }
            });
        }
        dateTimePickerDialog.show();
    }


    private SelectAddressDialog selectAddressDialog;

    private void showCityDialog()
    {
        if (selectAddressDialog == null)
        {
            selectAddressDialog = new SelectAddressDialog(FindJobActivity2.this);

            selectAddressDialog.setSelectAddressClickListener(new SelectAddressDialog.SelectAddressClickListener()
            {
                @Override
                public void onClick(String province, String city, String district)
                {
                    mCity.setText(province + "/" + city + "/" + district);
                }
            });
        }
        selectAddressDialog.show();
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
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        ToastUtils.makeText(FindJobActivity2.this, "提交成功！");
        setResult(1000);
        finish();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        ToastUtils.makeText(FindJobActivity2.this, msg);
    }
}
