package com.coder.kzxt.message.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.im.adapter.ChatAdapter;
import com.coder.kzxt.im.beans.GroupInfo;
import com.coder.kzxt.im.conversation.FriendProfile;
import com.coder.kzxt.im.conversation.FriendshipInfo;
import com.coder.kzxt.im.message.CustomMessage;
import com.coder.kzxt.im.message.ImageMessage;
import com.coder.kzxt.im.message.Message;
import com.coder.kzxt.im.message.MessageFactory;
import com.coder.kzxt.im.message.TextMessage;
import com.coder.kzxt.im.message.VoiceMessage;
import com.coder.kzxt.im.persenter.ChatPresenter;
import com.coder.kzxt.im.util.EmojiUtil;
import com.coder.kzxt.im.util.MediaUtil;
import com.coder.kzxt.im.util.RecorderUtil;
import com.coder.kzxt.im.view.ChatInput;
import com.coder.kzxt.im.view.ChatView;
import com.coder.kzxt.im.view.VoiceSendingView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.beans.ChatRoomResult;
import com.coder.kzxt.video.beans.CourseRoleResult;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMGroupSelfInfo;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMMessageStatus;
import com.tencent.TIMValueCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.coder.kzxt.utils.Constants.CHAT_SEND_MESSAGE_MAX_LENGTH;
import static com.coder.kzxt.utils.ToastUtils.makeText;


/**
 * Created by MaShiZhao on 2017/3/15
 */

public class ChatDetailActivity extends BaseActivity implements HttpCallBack, ChatView
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private ListView myPullRecyclerView;
    private ChatInput input;
    private VoiceSendingView voiceSendingView;

    private ChatAdapter adapter;
    private List<Message> mData;
    private ChatPresenter presenter;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private Uri fileUri;

    private String courseId;
    private String classId;
    private String identify;
    private TIMConversationType type;
    private Boolean isColse;

    private RecorderUtil recorder = new RecorderUtil();

    private List<CourseRoleResult.Item.ListBean> allClassData;

    private String qrcodeContent; //二维码内容
    private String className; //班级名称

    //个人跳转
    public static void gotoActivity(Context context, String identify, String courseId, TIMConversationType conversationType)
    {
        gotoActivity(context, identify, courseId, "", "",conversationType, false);
    }

    //群聊跳转
    public static void gotoActivity(Context context, String identify, String courseId, String classId, String className,TIMConversationType conversationType, Boolean isClose)
    {
        Intent intent = new Intent(context, ChatDetailActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("courseId", courseId);
        intent.putExtra("classId", classId);
        intent.putExtra("className",className);
        intent.putExtra("type", conversationType);
        intent.putExtra("close", isClose);
        context.startActivity(intent);
    }


    private void initVariable()
    {
        identify = getIntent().getStringExtra("identify");
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
        type = (TIMConversationType) getIntent().getExtras().getSerializable("type");
        isColse = getIntent().getBooleanExtra("close", false);
        className = getIntent().getStringExtra("className");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        //初始化 view
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
//        showLoadingView();
        createQrcodeContent();
    }


    private void initFindViewById()
    {
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (ListView) findViewById(R.id.myPullRecyclerView);
        input = (ChatInput) findViewById(R.id.chatInput);
        voiceSendingView = (VoiceSendingView) findViewById(R.id.voice_sending);

        myPullRecyclerView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        myPullRecyclerView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });

        // 判断是否隐藏
        if (isColse)
        {
            input.setVisibility(View.GONE);
        } else
        {
            input.setVisibility(View.VISIBLE);
        }
    }

    private void initData()
    {
        toolbar.setTitle(identify);
        setSupportActionBar(toolbar);
        input.setChatView(this);

        allClassData = new ArrayList<>();
        mData = new LinkedList<>();
        adapter = new ChatAdapter(this, R.layout.item_chat_detail, mData);
        myPullRecyclerView.setAdapter(adapter);

        presenter = new ChatPresenter(this, identify, type);
        presenter.start();

        switch (type)
        {
            case C2C:
                FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
                toolbar.setTitle(profile == null ? identify : profile.getName());

                break;
            case Group:
                toolbar.setTitle(GroupInfo.getInstance().getGroupName(identify));
                //请求网络 获取加入过的班级
                requestCourseRole();
                break;
        }

    }

    private void initClick()
    {

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //如果拉到顶端读取更多消息
                presenter.getMessage(mData.size() > 0 ? mData.get(0).getMessage() : null);

            }
        });
    }

    /**
     * 请求网络 获取加入过的班级
     */
    private void requestCourseRole()
    {
        new HttpGetBuilder(ChatDetailActivity.this)
                .setHttpResult(this)
                .setClassObj(CourseRoleResult.class)
                .setUrl(UrlsNew.GET_COURSE_ROLE)
                .addQueryParams("course_id", courseId)
                .build();
    }

    /**
     * 请求聊天室的信息状态
     */
    private void requestChatRoom()
    {
        new HttpGetBuilder(ChatDetailActivity.this)
                .setUrl(UrlsNew.CHAT_ROOM)
                .setPath(classId)
                .setClassObj(ChatRoomResult.class)
                .setHttpResult(ChatDetailActivity.this)
                .setRequestCode(1002)
                .build();
    }


    /**
     * 显示消息
     *
     * @param message
     */
    public void showMessage(TIMMessage message)
    {
        if (message == null)
        {
            adapter.notifyDataSetChanged();
        } else
        {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null)
            {
                if (mMessage instanceof CustomMessage)
                {
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType)
                    {
                        case TYPING:
//                            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
//                            title.setTitleText(getString(R.string.chat_typing));
//                            handler.removeCallbacks(resetTitle);
//                            handler.postDelayed(resetTitle,3000);
                            break;
                        default:
                            break;
                    }
                } else
                {
                    if (mData.size() == 0)
                    {
                        mMessage.setHasTime(null);
                    } else
                    {
                        mMessage.setHasTime(mData.get(mData.size() - 1).getMessage());
                    }
                    mData.add(mMessage);
                    adapter.notifyDataSetChanged();
                    myPullRecyclerView.setSelection(adapter.getCount() - 1);
                }

            }
        }

    }

    /**
     * 显示消息
     *
     * @param messages
     */
    public void showMessage(List<TIMMessage> messages)
    {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i)
        {
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted)
                continue;
            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1)
            {
                mMessage.setHasTime(messages.get(i + 1));
                mData.add(0, mMessage);
            } else
            {
                mMessage.setHasTime(null);
                mData.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        myPullRecyclerView.setSelection(newMsgNum);

        if (myPullSwipeRefresh.isRefreshing())
        {
            myPullSwipeRefresh.setRefreshing(false);
        }

    }

    /**
     * 清除所有消息，等待刷新
     */
    public void clearAllMessage()
    {
        mData.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    public void onSendMessageSuccess(TIMMessage message)
    {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */

    public void onSendMessageFail(int code, String desc, TIMMessage message)
    {
        long id = message.getMsgUniqueId();
        for (Message msg : mData)
        {
            if (msg.getMessage().getMsgUniqueId() == id)
            {
                switch (code)
                {
                    case 80001:
                        //发送内容包含敏感词
//                        msg.setDesc(getString(R.string.chat_content_bad));
                        adapter.notifyDataSetChanged();
                        break;
                    case 10017:
                        makeText(ChatDetailActivity.this, getResources().getString(R.string.shut_up));
                        break;
                    default:
                        ToastUtils.makeText(ChatDetailActivity.this, desc);
                        break;
                }
            }
        }


    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage()
    {
//        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
//        intent_album.setType("image/*");
//        startActivityForResult(intent_album, IMAGE_STORE);

        //获取颜色值
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(ChatDetailActivity.this, R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(1);
        AndroidImagePicker.getInstance().pickSingle(ChatDetailActivity.this, false, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
        {
            @Override
            public void onImagePickComplete(List<ImageItem> items)
            {
                if (items != null && items.size() > 0)
                {
                    L.d("onImagePickComplete:  " + items.size());
                    sendPicture(items.get(0).path);
                }
            }
        });

    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto()
    {

//        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent_photo.resolveActivity(getPackageManager()) != null)
//        {
//            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//            if (tempFile != null)
//            {
//                fileUri = Uri.fromFile(tempFile);
//            }
//            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }

        //获取颜色值
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(ChatDetailActivity.this, R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(1);
        AndroidImagePicker.getInstance().pickCamera(ChatDetailActivity.this, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
        {
            @Override
            public void onImagePickComplete(List<ImageItem> items)
            {

                if (items != null && items.size() > 0)
                {
                    L.d("onImagePickComplete:  " + items.size());
                    sendPicture(items.get(0).path);
                }
            }
        });

    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText()
    {
        String inputString = input.getText().toString().trim();

        if (inputString.length() == 0)
        {
            makeText(ChatDetailActivity.this, getResources().getString(R.string.canot_send_empty_message));
            return;
        }

        if(inputString.length() > CHAT_SEND_MESSAGE_MAX_LENGTH)
        {
            makeText(ChatDetailActivity.this, getResources().getString(R.string.message_limit_before));
            return;
        }

//        try
//        {
//            byte[] byte_num = inputString.getBytes("utf8");
//            if (byte_num.length > 1024)
//            {
//                makeText(ChatDetailActivity.this, getResources().getString(R.string.message_limit_before));
//                return;
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            return;
//        }
//        TIMMessage msg = new TIMMessage();
//        addTextFaceMsg(msg, str);
//        sendMsgContent(msg);
//        EmojiUtil.getMessage(inputString);
//
//        Message message = new TextMessage(input.getText());
//        presenter.sendMessage(message.getMessage());
        presenter.sendMessage(EmojiUtil.getMessage(inputString));
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile()
    {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        startActivityForResult(intent, FILE_CODE);
    }

    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice()
    {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();
    }

    /**
     * 显示录音状态
     */
    @Override
    public void showRecording()
    {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
    }


    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice()
    {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1)
        {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else
        {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName)
    {
//        Message message = new VideoMessage(fileName);
//        presenter.sendMessage(message.getMessage());
    }


    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice()
    {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
    }

    @Override
    public void showCancelSendVoice()
    {
        voiceSendingView.showCancel();
    }

    /**
     * 正在发送
     */
    @Override
    public void sending()
    {
        if (type == TIMConversationType.C2C)
        {
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */

    public void showDraft(TIMMessageDraft draft)
    {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_detail_menu, menu);
        MenuItem menu_all = menu.findItem(R.id.menu_all);
        MenuItem menu_member = menu.findItem(R.id.menu_member);
        if (allClassData.size() < 2)
        {
            menu_all.setVisible(false);
        }
        if (type != TIMConversationType.Group)
        {
            menu_member.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_all:
                showDialogOfClass();
                break;
//            case R.id.menu_function:
//                FunctionListActivity.gotoActivity(ChatDetailActivity.this);
//                break;
            case R.id.menu_member:
                //只有是该群的创建者才会跳转到管理界面 并非是老师
                TIMGroupManager.getInstance().getSelfInfo(identify, new TIMValueCallBack<TIMGroupSelfInfo>()
                {
                    @Override
                    public void onError(int i, String s)
                    {

                    }

                    @Override
                    public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo)
                    {
                        if (timGroupSelfInfo.getRole() == TIMGroupMemberRoleType.Admin || timGroupSelfInfo.getRole() == TIMGroupMemberRoleType.Owner)
                        {
                            ManagerTeacherActivity.gotoActivity(ChatDetailActivity.this, identify,qrcodeContent,className,classId);

                        } else
                        {
                            ManagerStudentActivity.gotoActivity(ChatDetailActivity.this, identify,qrcodeContent,className,classId);
                        }
                    }
                });
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //显示对话框
    private CustomListDialog classDialog;

    private void showDialogOfClass()
    {
        if (allClassData.size() < 2) return;

        if (classDialog == null)
        {
            classDialog = new CustomListDialog(ChatDetailActivity.this);
            for (CourseRoleResult.Item.ListBean chat : allClassData)
            {
                classDialog.addData(chat.getClass_name());
            }
            classDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    CourseRoleResult.Item.ListBean itemsBean = allClassData.get(position);
                    className = itemsBean.getClass_name();
                    toolbar.setTitle(className);
                    classId = itemsBean.getId();
                    classDialog.dismiss();

                    requestChatRoom();
                    createQrcodeContent();
                }
            });
            classDialog.show();

        }
        classDialog.show();
    }

    /**
     * 创建二维码内容
     */
    private void createQrcodeContent() {
        double timeStamp = System.currentTimeMillis() / 1000;
        long newTime = Math.round(timeStamp);
        qrcodeContent = String.valueOf(newTime) + "|" + courseId + "|" + classId + "|" + "scanQrcodeJoinClassAction";
    }


    private void sendPicture(String path)
    {
        File file = new File(path);
        if (file.exists() && file.length() > 0)
        {
            if (file.length() > 1024 * 1024 * 10)
            {
                makeText(ChatDetailActivity.this, getResources().getString(R.string.chat_file_too_large));
            } else
            {
                Message message = new ImageMessage(path, true);
                presenter.sendMessage(message.getMessage());
            }
        } else
        {
            makeText(ChatDetailActivity.this, getResources().getString(R.string.chat_file_not_exist));
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (input.getText().length() > 0)
        {
            TextMessage message = new TextMessage(input.getText());
            presenter.saveDraft(message.getMessage());
        } else
        {
            presenter.saveDraft(null);
        }
//        RefreshEvent.getInstance().onRefresh();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        presenter.stop();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1002)
        {
            ChatRoomResult chatRoomResult = (ChatRoomResult) resultBean;
            if (chatRoomResult.getItem().getStatus().equals("1"))
            {
                mData.clear();
                presenter.stop();
                presenter = new ChatPresenter(this, chatRoomResult.getItem().getGroup_id(), type);
                presenter.start();
            }
        } else
        {
            CourseRoleResult chat = (CourseRoleResult) resultBean;
            allClassData.addAll(chat.getItem().getList());
            for (CourseRoleResult.Item.ListBean listBean : allClassData)
            {
                if (listBean.getId().equals(classId))
                {
                    toolbar.setTitle(listBean.getClass_name());
                    break;
                }
            }
            invalidateOptionsMenu();

        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(ChatDetailActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(ChatDetailActivity.this, mainView);
        }
    }
}
