package com.coder.kzxt.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.im.conversation.FriendshipInfo;
import com.coder.kzxt.message.beans.ChatUserCloudResult;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import com.tencent.TIMConversationType;

/**
 * 学生 个人消息界面
 */
public class ChatPersonActivity extends BaseActivity implements HttpCallBack
{

    private android.support.v7.widget.Toolbar toolbar;
    private ImageView head;
    private TextView user_name;
    private ImageView sex;
    private TextView phone;
    private TextView birthday;
    private TextView email;
    private TextView desc;
    private Button send_message;

    private String identifier;


    public static void gotoActivity(Context context, String identifier)
    {
        context.startActivity(new Intent(context, ChatPersonActivity.class).putExtra("identifier", identifier));
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_person_detail);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        head = (ImageView) findViewById(R.id.head);
        user_name = (TextView) findViewById(R.id.user_name);
        sex = (ImageView) findViewById(R.id.sex);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        birthday = (TextView) findViewById(R.id.birthday);
        desc = (TextView) findViewById(R.id.desc);
        send_message = (Button) findViewById(R.id.send_message);

        initVariable();
        initData();
        inintEvent();
    }

    private void initVariable()
    {
        identifier = getIntent().getStringExtra("identifier");
    }

    private void initData()
    {

        toolbar.setTitle(getResources().getString(R.string.personal_info));
        setSupportActionBar(toolbar);

        showLoadingView();
        FriendshipInfo.getInstance().addFriend(identifier);

        new HttpGetBuilder(this)
                .setHttpResult(ChatPersonActivity.this)
                .setUrl(UrlsNew.CHAT_USER_DETAIL)
                .setClassObj(ChatUserCloudResult.class)
                .setPath(identifier)
                .build();

    }


    private void inintEvent()
    {
        send_message.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChatDetailActivity.gotoActivity(ChatPersonActivity.this, identifier, "", TIMConversationType.C2C);
            }
        });
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();
        ChatUserCloudResult chatUserCloudResult = (ChatUserCloudResult) resultBean;
        GlideUtils.loadCircleHeaderOfCommon(this, chatUserCloudResult.getItem().getProfile().getAvatar(), head);
        user_name.setText(chatUserCloudResult.getItem().getProfile().getNickname());
        if (chatUserCloudResult.getItem().getProfile().getGender().equals("1"))
        {
            sex.setBackgroundResource(R.drawable.user_male);
        } else
        {
            sex.setBackgroundResource(R.drawable.user_female);
        }

        phone.setText(chatUserCloudResult.getItem().getMobile());
        email.setHint("邮箱未开通");
        email.setText(chatUserCloudResult.getItem().getEmail());
        birthday.setText(chatUserCloudResult.getItem().getProfile().getBirthday());
        desc.setText(chatUserCloudResult.getItem().getProfile().getDesc());
        if (new SharedPreferencesUtil(this).getUserRole().equals("1"))
        {
            phone.setText(chatUserCloudResult.getItem().getMobile());
            send_message.setVisibility(View.VISIBLE);
        } else
        {
            String phoneString = chatUserCloudResult.getItem().getMobile();
            if (phoneString.length() >= 11)
            {
                phone.setText(phoneString.substring(0, 3) + "****" + phoneString.substring(8, 11));
            }
            send_message.setVisibility(View.GONE);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

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
}