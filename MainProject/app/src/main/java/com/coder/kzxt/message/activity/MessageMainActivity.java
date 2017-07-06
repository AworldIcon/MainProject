package com.coder.kzxt.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.im.beans.UserInfo;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.im.conversation.Conversation;
import com.coder.kzxt.im.conversation.NomalConversation;
import com.coder.kzxt.im.message.CustomMessage;
import com.coder.kzxt.im.message.MessageFactory;
import com.coder.kzxt.im.persenter.ConversationPresenter;
import com.coder.kzxt.im.persenter.FriendshipManagerPresenter;
import com.coder.kzxt.im.persenter.GroupManagerPresenter;
import com.coder.kzxt.im.util.PushUtil;
import com.coder.kzxt.message.adapter.MessageMainDelegate;
import com.coder.kzxt.message.beans.ChatGroupBean;
import com.coder.kzxt.poster.activity.SearchPosterActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by MaShiZhao on 2017/3/15
 * 消息列表 第一层 包含通知和推送消息
 */

public class MessageMainActivity extends BaseActivity implements HttpCallBack
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private PullRefreshAdapter<NomalConversation> pullRefreshAdapter;
    private MessageMainDelegate messageMainDelegate;

    private List<NomalConversation> mData;
    private ConversationPresenter conversationPresenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;

//    private FriendshipConversation friendshipConversation;
//    private GroupManageConversation groupManageConversation;

    private HashMap<String, ChatGroupBean.ItemsBean> joinedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_swiperefresh);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        showLoadingView();

        new HttpGetBuilder(MessageMainActivity.this)
                .setUrl(UrlsNew.CHAT_ROOM)
                .setHttpResult(MessageMainActivity.this)
                .setClassObj(ChatGroupBean.class)
                .build();
    }

    private void initVariable()
    {
    }

    private void initFindViewById()
    {
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        myPullSwipeRefresh.setBackgroundResource(R.color.bg_white);
    }

    private void initData()
    {

        toolbar.setTitle(getResources().getString(R.string.message));
        setSupportActionBar(toolbar);

        mData = new LinkedList<>();
        messageMainDelegate = new MessageMainDelegate(this);
        pullRefreshAdapter = new PullRefreshAdapter<NomalConversation>(this, mData, messageMainDelegate);
        pullRefreshAdapter.setFooterCount(0);
        pullRefreshAdapter.setHeaderCount(1);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        groupManagerPresenter = new GroupManagerPresenter(this);
        conversationPresenter = new ConversationPresenter(this);

        joinedGroup = new HashMap<>();

    }

    private void initClick()
    {

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                conversationPresenter.getConversation();
            }
        });

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                String id = mData.get(position).getIdentify();

                ChatGroupBean.ItemsBean itemsBean = joinedGroup.get(id);
                String courseId = itemsBean == null ? "" : itemsBean.getCourse_class().getCourse().getId();
                String classId = itemsBean == null ? "" : itemsBean.getCourse_class_id();
                String className = itemsBean == null ? "" : itemsBean.getCourse_class().getClass_name();
                ChatDetailActivity.gotoActivity(MessageMainActivity.this,
                        id,
                        courseId,
                        classId,
                        className,
                        mData.get(position).getConversationType(),
                        mData.get(position).getClosed());
            }
        });

        pullRefreshAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.setOnItemLongClickListener()
        {
            @Override
            public void onItemLongClick(View view, int position)
            {
                showListDialog(position);

            }
        });

    }


    private CustomListDialog customListDialog;

    private void showListDialog(final int ps)
    {
        if (customListDialog == null)
        {
            customListDialog = new CustomListDialog(MessageMainActivity.this);
            customListDialog.addData(getResources().getString(R.string.delete));
            customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {

                    TIMManager.getInstance().deleteConversation(
                            mData.get(ps).getConversationType(),
                            mData.get(ps).getIdentify());
                    mData.remove(ps);
                    pullRefreshAdapter.notifyDataSetChanged();
                    customListDialog.dismiss();

                }
            });
        }
        customListDialog.show();

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1001)
        {
            conversationPresenter.getConversation();

        } else
        {
            ChatGroupBean groupBean = (ChatGroupBean) resultBean;
            if (groupBean.getItems() == null || groupBean.getItems().size() == 0)
            {
                hideLoadingView();
            } else
            {
                for (ChatGroupBean.ItemsBean chatGroupBean : groupBean.getItems())
                {
                    joinedGroup.put(chatGroupBean.getGroup_id(), chatGroupBean);
                }
                conversationPresenter.getConversation();
            }
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(MessageMainActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(MessageMainActivity.this, mainView);
        }
    }

    /**
     * 初始化界面或刷新界面 只接收c2c group
     *
     * @param data
     */
    public void initView(List<TIMConversation> data)
    {
        this.mData.clear();
//         groupList 好像暂时无用
//        groupList = new ArrayList<>();
        for (TIMConversation item : data)
        {
            switch (item.getType())
            {
                case C2C:
                    this.mData.add(new NomalConversation(item));
//                    groupList.add(item.getPeer());
                    break;
                case Group:
                    if (joinedGroup.containsKey(item.getPeer()))
                    {
                        this.mData.add(initGroupConversation(item));
                    }
                    break;
            }
        }
//        friendshipManagerPresenter.getFriendshipLastMessage();
//        groupManagerPresenter.getGroupManageLastMessage();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    public void updateMessage(TIMMessage message)
    {
        if (message == null)
        {
            pullRefreshAdapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System)
        {
//            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;

        TIMConversation item = message.getConversation();
        NomalConversation conversation = null;
        //私聊
        if (item.getType() == TIMConversationType.C2C)
        {
            conversation = new NomalConversation(message.getConversation());

        } else if (item.getType() == TIMConversationType.Group)
        {
            //群聊
            if (joinedGroup.containsKey(item.getPeer()))
            {
                conversation = initGroupConversation(item);
            }
        }

        if (conversation != null)
        {
            Iterator<NomalConversation> iterator = mData.iterator();
            while (iterator.hasNext())
            {
                Conversation c = iterator.next();
                if (conversation.equals(c))
                {
                    conversation = (NomalConversation) c;
                    iterator.remove();
                    break;
                }
            }
            if (message.getElement(0).getType() != TIMElemType.GroupTips )
            {
                conversation.setLastMessage(MessageFactory.getMessage(message));
             }
            mData.add(conversation);
            Collections.sort(mData);
            refresh();
        }
    }


//    /**
//     * 更新好友关系链消息
//     */
//    public void updateFriendshipMessage()
//    {
//        friendshipManagerPresenter.getFriendshipLastMessage();
//    }

    /**
     * 更新群信息
     *
     * @param info
     */
    public void updateGroupInfo(TIMGroupCacheInfo info)
    {
        for (Conversation conversation : mData)
        {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId()))
            {
                pullRefreshAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    public void refresh()
    {
        Collections.sort(mData);
        pullRefreshAdapter.notifyDataSetChanged();
//        setMsgUnread(getTotalUnreadNum() == 0);
        myPullSwipeRefresh.setRefreshing(false);
        hideLoadingView();
    }


    private long getTotalUnreadNum()
    {
        long num = 0;
        for (Conversation conversation : mData)
        {
            num += conversation.getUnreadNum();
        }
        return num;
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    public void removeConversation(String identify)
    {
        Iterator<NomalConversation> iterator = mData.iterator();
        while (iterator.hasNext())
        {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify))
            {
                iterator.remove();
                pullRefreshAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

//    /**
//     * 获取群管理最后一条系统消息的回调
//     *
//     * @param message     最后一条消息
//     * @param unreadCount 未读数
//     */
//    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount)
//    {
//        if (groupManageConversation == null)
//        {
//            groupManageConversation = new GroupManageConversation(message);
//            mData.add(groupManageConversation);
//        } else
//        {
//            groupManageConversation.setLastMessage(message);
//        }
//        groupManageConversation.setUnreadCount(unreadCount);
//        Collections.sort(mData);
//        refresh();
//    }
//
//    /**
//     * 获取群管理系统消息的回调
//     *
//     * @param message 分页的消息列表
//     */
//    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message)
//    {
//
//    }
//
//    /**
//     * 获取好友关系链管理系统最后一条消息的回调
//     *
//     * @param message     最后一条消息
//     * @param unreadCount 未读数
//     */
//    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount)
//    {
//        if (friendshipConversation == null)
//        {
//            friendshipConversation = new FriendshipConversation(message);
//            mData.add(friendshipConversation);
//        } else
//        {
//            friendshipConversation.setLastMessage(message);
//        }
//        friendshipConversation.setUnreadCount(unreadCount);
//        Collections.sort(mData);
//        refresh();
//    }
//
//    /**
//     * 获取好友关系链管理最后一条系统消息的回调
//     *
//     * @param message 消息列表
//     */
//    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message)
//    {
//        friendshipManagerPresenter.getFriendshipLastMessage();
//    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (UserInfo.getInstance().getId() == null)
        {
            new ImLoginBusiness()
                    .setRequestCode(1001)
                    .setHttpCallBack(this)
                    .login();
        } else
        {
            refresh();
            PushUtil.getInstance().reset();
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
                startActivity(new Intent(MessageMainActivity.this, SearchPosterActivity.class));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化 groupConversation 设置班级信息
     *
     * @param item
     * @return
     */
    private NomalConversation initGroupConversation(TIMConversation item)
    {
        NomalConversation nomalConversation = new NomalConversation(item);
        if (joinedGroup.containsKey(item.getPeer()))
        {
            ChatGroupBean.ItemsBean itemsBean = joinedGroup.get(item.getPeer());
            nomalConversation.setClosed(itemsBean.getStatus());
            nomalConversation.setClassName(itemsBean.getCourse_class().getClass_name());
        } else
        {
            nomalConversation.setClosed(true);
            nomalConversation.setClassName("直播聊天室");
        }
        return nomalConversation;
    }


}
