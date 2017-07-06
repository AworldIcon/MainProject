package com.coder.kzxt.buildwork.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class BuildNewWorkActivity extends BaseActivity
{
    private TextView title, total_num1, total_num2, total_num3, total_num4, total_num5;
    private String courseId;
    private RelativeLayout selfLayout, free_choice_work, single_choice, choices, fill, determine, essay, dissmiss;
    private List<String> selfList = new ArrayList<>();
    private SharedPreferencesUtil pu;
    private String isCenter = "";
    private String testId = "";
    private String name = "";
    private int workType;
    private int single_choiceNum, choiceNum, determineNum, fillNum, essayNum;
    private LinearLayout jiazai_layout, choice_bank;
    private ImageView leftImage;
    private View view;
    private PopupWindow pop;
    private int i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_new_work);
        MyApplication.workList.clear();
        MyApplication.workList.add(this);
        courseId = getIntent().getStringExtra("courseId");
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        testId = getIntent().getStringExtra("testId");
        name = getIntent().getStringExtra("name");
        workType = getIntent().getIntExtra("workType", 2);
        initView();
        initEvents();
        getQuestionNum();
    }

    private void initView()
    {
        pu = new SharedPreferencesUtil(this);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        title = (TextView) findViewById(R.id.title);
        if (workType == 1) {
            title.setText(name + "-考试");
        } else
        {
            title.setText(name + "-作业");
        }
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());
        title.setWidth(px);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setMarqueeRepeatLimit(100000);
        title.setFocusable(true);
        title.setSingleLine(true);
        title.setHorizontallyScrolling(true);
        title.setFocusableInTouchMode(true);
        choice_bank = (LinearLayout) findViewById(R.id.resource_work);
        selfLayout = (RelativeLayout) findViewById(R.id.self_build_work);
        free_choice_work = (RelativeLayout) findViewById(R.id.free_choice_work);
        view = LayoutInflater.from(BuildNewWorkActivity.this).inflate(R.layout.self_build_work_item, null);
        single_choice = (RelativeLayout) view.findViewById(R.id.single_choice);
        choices = (RelativeLayout) view.findViewById(R.id.choices);
        fill = (RelativeLayout) view.findViewById(R.id.fill);
        determine = (RelativeLayout) view.findViewById(R.id.determine);
        essay = (RelativeLayout) view.findViewById(R.id.essay);
        dissmiss = (RelativeLayout) view.findViewById(R.id.dissmiss);
    }

    private void initEvents()
    {
        leftImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        selfLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop = new PopupWindow(BuildNewWorkActivity.this);
                pop.setContentView(view);
                pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setBackgroundDrawable(ContextCompat.getDrawable(BuildNewWorkActivity.this, R.color.trans_half));
                pop.setFocusable(true);
                view.setAnimation(AnimationUtils.loadAnimation(BuildNewWorkActivity.this, R.anim.push_bottom_in_2));
                pop.showAtLocation(findViewById(R.id.popu_window), Gravity.BOTTOM, 0, 0);

            }
        });
        free_choice_work.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (single_choiceNum + choiceNum + determineNum + fillNum + essayNum == 0)
                {
                    Toast.makeText(BuildNewWorkActivity.this, "题库无题，请选择手动建题", Toast.LENGTH_SHORT).show();
                    return;
                }
//                final AlertDialog alertDialog=new AlertDialog.Builder(BuildNewWorkActivity.this).create();
//                alertDialog.setCanceledOnTouchOutside(false);
//                CustomDialog
                final CustomDialog customNewDialog = new CustomDialog(BuildNewWorkActivity.this);
                customNewDialog.setCanceledOnTouchOutside(false);
                final Window dialogWindow = customNewDialog.getWindow();
//                final Window dialogWindow = alertDialog.getWindow();
                dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogWindow.setGravity(Gravity.CENTER);
                View free_view = LayoutInflater.from(BuildNewWorkActivity.this).inflate(R.layout.free_choice_dialog, null);
                total_num1 = (TextView) free_view.findViewById(R.id.total_num1);
                total_num2 = (TextView) free_view.findViewById(R.id.total_num2);
                total_num3 = (TextView) free_view.findViewById(R.id.total_num3);
                total_num4 = (TextView) free_view.findViewById(R.id.total_num4);
                total_num5 = (TextView) free_view.findViewById(R.id.total_num5);
                TextView down_num1 = (TextView) free_view.findViewById(R.id.down_num1);
                TextView up_num1 = (TextView) free_view.findViewById(R.id.up_num1);
                final TextView num1 = (TextView) free_view.findViewById(R.id.num1);
                TextView down_num2 = (TextView) free_view.findViewById(R.id.down_num2);
                TextView up_num2 = (TextView) free_view.findViewById(R.id.up_num2);
                final TextView num2 = (TextView) free_view.findViewById(R.id.num2);
                TextView down_num3 = (TextView) free_view.findViewById(R.id.down_num3);
                TextView up_num3 = (TextView) free_view.findViewById(R.id.up_num3);
                final TextView num3 = (TextView) free_view.findViewById(R.id.num3);
                TextView down_num4 = (TextView) free_view.findViewById(R.id.down_num4);
                TextView up_num4 = (TextView) free_view.findViewById(R.id.up_num4);
                final TextView num4 = (TextView) free_view.findViewById(R.id.num4);
                TextView down_num5 = (TextView) free_view.findViewById(R.id.down_num5);
                TextView up_num5 = (TextView) free_view.findViewById(R.id.up_num5);
                final TextView num5 = (TextView) free_view.findViewById(R.id.num5);
                total_num1.setText(single_choiceNum + "");
                total_num2.setText(choiceNum + "");
                total_num3.setText(determineNum + "");
                total_num4.setText(fillNum + "");
                total_num5.setText(essayNum + "");
                if (single_choiceNum > 0)
                {
                    num1.setText(String.valueOf(1));
                }
                if (choiceNum > 0)
                {
                    num2.setText(String.valueOf(1));
                }
                if (determineNum > 0)
                {
                    num3.setText(String.valueOf(1));
                }
                if (fillNum > 0)
                {
                    num4.setText(String.valueOf(1));
                }
                if (essayNum > 0)
                {
                    num5.setText(String.valueOf(1));
                }
                down_num1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num1.getText().toString()) > 0)
                        {
                            i1--;
                            num1.setText(String.valueOf(Integer.valueOf(num1.getText().toString()) - 1));
                        }
                    }
                });
                up_num1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num1.getText().toString()) < single_choiceNum)
                        {
                            i1++;
                            num1.setText(String.valueOf(Integer.valueOf(num1.getText().toString()) + 1));
                        }
                    }
                });
                down_num2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num2.getText().toString()) > 0)
                        {
                            i2--;
                            num2.setText(String.valueOf(Integer.valueOf(num2.getText().toString()) - 1));
                        }
                    }
                });
                up_num2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num2.getText().toString()) < choiceNum)
                        {
                            i2++;
                            num2.setText(String.valueOf(Integer.valueOf(num2.getText().toString()) + 1));
                        }
                    }
                });
                down_num3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num3.getText().toString()) > 0)
                        {
                            i3--;
                            num3.setText(String.valueOf(Integer.valueOf(num3.getText().toString()) - 1));
                        }
                    }
                });
                up_num3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num3.getText().toString()) < determineNum)
                        {
                            i3++;
                            num3.setText(String.valueOf(Integer.valueOf(num3.getText().toString()) + 1));
                        }
                    }
                });
                down_num4.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num4.getText().toString()) > 0)
                        {
                            i4--;
                            num4.setText(String.valueOf(Integer.valueOf(num4.getText().toString()) - 1));
                        }
                    }
                });
                up_num4.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num4.getText().toString()) < fillNum)
                        {
                            i4++;
                            num4.setText(String.valueOf(Integer.valueOf(num4.getText().toString()) + 1));
                        }
                    }
                });
                down_num5.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num5.getText().toString()) > 0)
                        {
                            i5--;
                            num5.setText(String.valueOf(Integer.valueOf(num5.getText().toString()) - 1));
                        }
                    }
                });
                up_num5.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Integer.valueOf(num5.getText().toString()) < essayNum)
                        {
                            i5++;
                            num5.setText(String.valueOf(Integer.valueOf(num5.getText().toString()) + 1));
                        }
                    }
                });
                RelativeLayout sure_btn = (RelativeLayout) free_view.findViewById(R.id.btn_sure);
                RelativeLayout nagtive_btn = (RelativeLayout) free_view.findViewById(R.id.btn_false);
                customNewDialog.setContentView(free_view);
                customNewDialog.setRightTextColor(ContextCompat.getColor(BuildNewWorkActivity.this, R.color.font_blue));
                customNewDialog.show();
//                alertDialog.setContentView(free_view);
//                alertDialog.show();
                sure_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (num1.getText().equals("0") && num2.getText().equals("0") && num3.getText().equals("0") && num4.getText().equals("0") && num5.getText().equals("0"))
                        {
                            Toast.makeText(BuildNewWorkActivity.this, "至少添加一道试题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        customNewDialog.dismiss();
                        jiazai_layout.setVisibility(View.VISIBLE);
                        submitQuestionNum(num1.getText().toString(), num2.getText().toString(), num3.getText().toString(), num4.getText().toString(), num5.getText().toString());
                    }
                });
                nagtive_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        customNewDialog.dismiss();
                        i1 = 0;
                        i2 = 0;
                        i3 = 0;
                        i4 = 0;
                        i5 = 0;
                    }
                });

            }
        });
        single_choice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
                Intent intent = new Intent(BuildNewWorkActivity.this, SingleChoiceActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("choice", "choice");
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("from", "new");
                intent.putExtra(Constants.IS_CENTER, isCenter);
                startActivity(intent);
            }
        });
        choices.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
                Intent intent = new Intent(BuildNewWorkActivity.this, SingleChoiceActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("choice", "choices");
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("from", "new");
                intent.putExtra(Constants.IS_CENTER, isCenter);
                startActivity(intent);
            }
        });
        fill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
                Intent intent = new Intent(BuildNewWorkActivity.this, QuestionFillActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra(Constants.IS_CENTER, isCenter);
                intent.putExtra("title", name);
                intent.putExtra("from", "new");
                intent.putExtra("workType", workType);
                startActivity(intent);
            }
        });
        determine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
                Intent intent = new Intent(BuildNewWorkActivity.this, QuestionDetermineActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("from", "new");
                intent.putExtra(Constants.IS_CENTER, isCenter);
                startActivity(intent);
            }
        });
        essay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
                Intent intent = new Intent(BuildNewWorkActivity.this, QuestionEssayActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("from", "new");
                intent.putExtra(Constants.IS_CENTER, isCenter);
                startActivity(intent);
            }
        });
        dissmiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pop.dismiss();
            }
        });
        choice_bank.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (single_choiceNum + choiceNum + determineNum + fillNum + essayNum == 0)
//                {
//                    Toast.makeText(BuildNewWorkActivity.this, "题库无题，请选择手动建题", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                //题库选题
                Intent intent = new Intent(BuildNewWorkActivity.this, QuestionBankActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("from", "new");
                startActivity(intent);
            }
        });
    }


    private void getQuestionNum()
    {
        new HttpGetBuilder(this).setClassObj(null).setUrl(UrlsNew.URLHeader+"/test_paper/question/random").setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                try {
                    JSONObject jsonObject=new JSONObject(resultBean.toString());
                    single_choiceNum=jsonObject.getJSONObject("item").optInt("type_single_choice",0);
                    choiceNum=jsonObject.getJSONObject("item").optInt("type_multiple_choice",0);
                    determineNum=jsonObject.getJSONObject("item").optInt("type_fill",0);
                    fillNum=jsonObject.getJSONObject("item").optInt("type_determine",0);
                    essayNum=jsonObject.getJSONObject("item").optInt("type_short_answer",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {

            }
        }).setPath(courseId)
                .build();
    }

    private void submitQuestionNum(final String s, final String s1, final String s2, final String s3, final String s4)
    {

        JSONObject jsonObject=new JSONObject();
        JSONObject type_single_choice=new JSONObject();
        JSONObject type_multiple_choice=new JSONObject();
        JSONObject type_fill=new JSONObject();
        JSONObject type_determine=new JSONObject();
        JSONObject type_short_answer=new JSONObject();
        try {
            type_single_choice.put("amount",s);
            type_single_choice.put("total_score",Integer.valueOf(s)*5);
            type_multiple_choice.put("amount",s1);
            type_multiple_choice.put("total_score",Integer.valueOf(s1)*5);
            type_fill.put("amount",s2);
            type_fill.put("total_score",Integer.valueOf(s2)*5);
            type_determine.put("amount",s3);
            type_determine.put("total_score",Integer.valueOf(s3)*5);
            type_short_answer.put("amount",s4);
            type_short_answer.put("total_score",Integer.valueOf(s4)*5);
            jsonObject.put("type_single_choice",type_single_choice);
            jsonObject.put("type_multiple_choice",type_multiple_choice);
            jsonObject.put("type_fill",type_fill);
            jsonObject.put("type_determine",type_determine);
            jsonObject.put("type_short_answer",type_short_answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPutBuilder(this).setUrl(UrlsNew.URLHeader+"/test_paper/question/random").setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                i1 = 0;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = 0;
                Intent intent1 = new Intent();
                intent1.setAction("com.coder.kzxt.activity.TeaWorkRecourseActivity");
                sendBroadcast(intent1);
                Intent intent = new Intent(BuildNewWorkActivity.this, PublishWorkActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("testId", testId);
                intent.putExtra("title", name);
                intent.putExtra("workType", workType);
                intent.putExtra("status", 2);
                startActivity(intent);
                finish();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                    ToastUtils.makeText(BuildNewWorkActivity.this,msg);
                jiazai_layout.setVisibility(View.GONE);
            }
        }).addBodyParam(jsonObject.toString()).setPath(testId).build();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Intent intent = new Intent();
        intent.setAction("com.coder.kzxt.activity.TeaWorkRecourseActivity");
        sendBroadcast(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
