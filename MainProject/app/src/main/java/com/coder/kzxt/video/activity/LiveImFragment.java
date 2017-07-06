package com.coder.kzxt.video.activity;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.im.adapter.ChatAdapter;
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
import com.coder.kzxt.message.beans.ChatGroupBean;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMMessageStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.coder.kzxt.utils.Constants.CHAT_SEND_MESSAGE_MAX_LENGTH;
import static com.coder.kzxt.utils.ToastUtils.makeText;


public class LiveImFragment extends BaseFragment implements HttpCallBack, ChatView
{


    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private ListView myPullRecyclerView;
    private ChatInput input;
    private VoiceSendingView voiceSendingView;

    private VideoLiveActivity mActivity;
    private Handler handler;

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

    private RecorderUtil recorder = new RecorderUtil();

    private List<ChatGroupBean.ItemsBean> allClassData;

    private LiveBean liveBean;

    public static Fragment newInstance(LiveBean liveBean)
    {
        LiveImFragment f = new LiveImFragment();
        Bundle b = new Bundle();
        b.putSerializable("liveBean", liveBean);
        f.setArguments(b);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        liveBean = (LiveBean) args.getSerializable("liveBean");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_live_im, container, false);

        //初始化 view findviewbyid
        initFindViewById(v);
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        return v;
    }

    @Override
    public void setArguments(Bundle args)
    {
        LiveBean liveBean = (LiveBean) args.getSerializable("liveBean");
        identify = liveBean.getChatroom_group_id();
        type = TIMConversationType.Group;
        super.setArguments(args);
    }

    private void initFindViewById(View view)
    {
        mainView = (RelativeLayout) view.findViewById(R.id.mainView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (ListView) view.findViewById(R.id.myPullRecyclerView);
        input = (ChatInput) view.findViewById(R.id.chatInput);
        voiceSendingView = (VoiceSendingView) view.findViewById(R.id.voice_sending);
        //隐藏聊天的发语音功能
        input.hideVoice();

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
    }

    private void initData()
    {
        input.setChatView(this);

        allClassData = new ArrayList<>();
        mData = new LinkedList<>();
        adapter = new ChatAdapter(getActivity(), R.layout.item_chat_detail, mData);
        myPullRecyclerView.setAdapter(adapter);

        presenter = new ChatPresenter(LiveImFragment.this, identify, type);
        presenter.start();


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
                    sendImData();
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
        //发送im聊天信息数据
        sendImData();
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
                        makeText(getActivity(), getResources().getString(R.string.shut_up));
                        break;
                    default:
                        makeText(getActivity(), desc);
                        break;
                }
            }
        }

    }

    private void sendImData()
    {
        ArrayList list = new ArrayList();
        List<Message> textmData = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++)
        {
            if (mData.get(i) instanceof TextMessage)
            {
                textmData.add(mData.get(i));
            }
        }

        list.add(textmData);
        android.os.Message selmsg = new android.os.Message();
        selmsg.what = Constants.LIVE_RES_MSG;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("imData", list);
        selmsg.setData(bundle);
        handler.sendMessage(selmsg);
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
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(1);
        AndroidImagePicker.getInstance().pickSingle(getActivity(), false, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
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
        String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.first_theme));
        //设置最多选择几张图片
        AndroidImagePicker.getInstance().setSelectLimit(1);
        AndroidImagePicker.getInstance().pickCamera(getActivity(), themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
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
        sendTextMsg(null);
    }


    public void sendTextMsg(EditText mETMsgInput)
    {
        String inputString = "";
        if (mETMsgInput == null)
        {
            inputString = input.getText().toString().trim();
        } else
        {
            inputString = mETMsgInput.getText().toString().trim();
        }

        if (inputString.length() == 0)
        {
            makeText(getActivity(), getResources().getString(R.string.canot_send_empty_message));
            return;
        }

        if(inputString.length() > CHAT_SEND_MESSAGE_MAX_LENGTH)
        {
            makeText(getActivity(), getResources().getString(R.string.message_limit_before));
            return;
        }

        presenter.sendMessage(EmojiUtil.getMessage(inputString));
        if (mETMsgInput == null)
        {
            input.setText("");
        } else
        {
            mETMsgInput.setText("");
        }
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
            makeText(getActivity(), getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
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
        input.getText().append(TextMessage.getString(draft.getElems(), getActivity()));
    }


    private void sendPicture(String path)
    {
        File file = new File(path);
        if (file.exists() && file.length() > 0)
        {
            if (file.length() > 1024 * 1024 * 10)
            {
                makeText(getActivity(), getResources().getString(R.string.chat_file_too_large));
            } else
            {
                Message message = new ImageMessage(path, true);
                presenter.sendMessage(message.getMessage());
            }
        } else
        {
            makeText(getActivity(), getResources().getString(R.string.chat_file_not_exist));
        }
    }


    @Override
    protected void requestData()
    {

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mActivity = (VideoLiveActivity) activity;
        handler = mActivity.getmHandler();
    }

    @Override
    public void onPause()
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
    public void onDestroy()
    {
        super.onDestroy();
        presenter.stop();

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        ChatGroupBean chat = (ChatGroupBean) resultBean;
        allClassData.addAll(chat.getItems());
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
    }
}
