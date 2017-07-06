package com.coder.kzxt.im.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;

import java.util.ArrayList;
import java.util.List;

import static com.coder.kzxt.activity.R.id.btn_voice;

/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener
{

    private static final String TAG = "ChatInput";

    private ImageButton btnAdd;
    private Button btnSend;
    private ImageButton btnVoice;
    private ImageButton btnKeyboard;
    private ImageButton btnEmotion;
    private EditText editText;
    private boolean isSendVisible, isEmoticonReady;
    private InputMode inputMode = InputMode.NONE;
    private ChatView chatView;
    private LinearLayout morePanel, textPanel;
    private TextView voicePanel;
    private EmojiViewPage viewPagerEmoji;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private float downY = 0;


    public ChatInput(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_chat_input, this);
        initView();
    }

    private void initView()
    {
        textPanel = (LinearLayout) findViewById(R.id.text_panel);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = (Button) findViewById(R.id.btn_send);

        GradientDrawable myGrad = (GradientDrawable) btnSend.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        btnSend.setOnClickListener(this);
        btnVoice = (ImageButton) findViewById(btn_voice);
        btnVoice.setOnClickListener(this);
        btnEmotion = (ImageButton) findViewById(R.id.btnEmoticon);
        btnEmotion.setOnClickListener(this);
        morePanel = (LinearLayout) findViewById(R.id.morePanel);
        TextView chat_more_picture = (TextView) findViewById(R.id.chat_more_picture);
        chat_more_picture.setOnClickListener(this);
        TextView chat_more_camera = (TextView) findViewById(R.id.chat_more_camera);
        chat_more_camera.setOnClickListener(this);
        TextView chat_more_test = (TextView) findViewById(R.id.chat_more_test);
        chat_more_test.setOnClickListener(this);
        TextView chat_more_work = (TextView) findViewById(R.id.chat_more_work);
        chat_more_work.setOnClickListener(this);
        TextView chat_more_sign = (TextView) findViewById(R.id.chat_more_sign);
        chat_more_sign.setOnClickListener(this);
        setSendBtn();
        btnKeyboard = (ImageButton) findViewById(R.id.btn_keyboard);
        btnKeyboard.setOnClickListener(this);
        voicePanel = (TextView) findViewById(R.id.voice_panel);

        voicePanel.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {

                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        updateVoiceView(0);
                        break;

                    case MotionEvent.ACTION_UP:
                        float y_up = downY - event.getY();
                        if (y_up > 150)
                        {
                            updateVoiceView(2);
                        } else
                        {
                            updateVoiceView(1);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float y_move = downY - event.getY();
                        if (y_move > 150)
                        {
                            updateVoiceView(3);
                        } else
                        {
                            updateVoiceView(4);
                        }
                        break;
                }
                return true;
            }
        });
        editText = (EditText) findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    updateView(InputMode.TEXT);
                }
            }
        });
        isSendVisible = editText.getText().length() != 0;
        viewPagerEmoji = (EmojiViewPage) findViewById(R.id.viewPagerEmoji);
        viewPagerEmoji.setEditText(editText);

    }

    /**
     * 隐藏语音功能
     */
    public void hideVoice()
    {
        btnVoice.setVisibility(GONE);
    }

    private void updateView(InputMode mode)
    {
        if (mode == inputMode) return;
        leavingCurrentState();
        switch (inputMode = mode)
        {
            case MORE:
                morePanel.setVisibility(VISIBLE);
                break;
            case TEXT:
                if (editText.requestFocus())
                {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            case VOICE:
                voicePanel.setVisibility(VISIBLE);
                textPanel.setVisibility(GONE);
                btnVoice.setVisibility(GONE);
                btnKeyboard.setVisibility(VISIBLE);
                break;
            case EMOTICON:
//                if (!isEmoticonReady)
//                {
//                    prepareEmoticon();
//                }
                viewPagerEmoji.setVisibility(VISIBLE);
                break;
        }
    }

    private void leavingCurrentState()
    {
        switch (inputMode)
        {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                break;
            case MORE:
                morePanel.setVisibility(GONE);
                break;
            case VOICE:
                voicePanel.setVisibility(GONE);
                textPanel.setVisibility(VISIBLE);
                btnVoice.setVisibility(VISIBLE);
                btnKeyboard.setVisibility(GONE);
                break;
            case EMOTICON:
                viewPagerEmoji.setVisibility(GONE);
        }
    }

    /**
     * 0 是按下按钮 1松开手指发送 2松开手指时 取消发送 3 按住屏幕 显示取消发送的状态 4 按住屏幕 显示录音状态
     *
     * @param status
     */

    private void updateVoiceView(int status)
    {
        switch (status)
        {
            case 0:

                voicePanel.setText(getResources().getString(R.string.chat_release_send));
                voicePanel.setBackgroundResource(R.drawable.chat_btn_voice_pressed);
                chatView.startSendVoice();
                break;
            case 1:

                voicePanel.setText(getResources().getString(R.string.chat_press_talk));
                voicePanel.setBackgroundResource(R.drawable.chat_voice_talk);
                chatView.endSendVoice();
                break;
            case 2:

                voicePanel.setText(getResources().getString(R.string.chat_press_talk));
                voicePanel.setBackgroundResource(R.drawable.chat_voice_talk);
                chatView.cancelSendVoice();
                break;
            case 3:

                voicePanel.setText(getResources().getString(R.string.chat_release_cancel_send));
                voicePanel.setBackgroundResource(R.drawable.chat_voice_talk);
                chatView.showCancelSendVoice();
                break;
            case 4:

                voicePanel.setText(getResources().getString(R.string.chat_release_send));
                voicePanel.setBackgroundResource(R.drawable.chat_btn_voice_pressed);
                chatView.showRecording();
                break;
            default:
                break;
        }
    }


    /**
     * 关联聊天界面逻辑
     */
    public void setChatView(ChatView chatView)
    {
        this.chatView = chatView;
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
        if (isSendVisible)
        {
            chatView.sending();
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s)
    {

    }

    private void setSendBtn()
    {
        if (isSendVisible)
        {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else
        {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        Activity activity = (Activity) getContext();
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_send:
                chatView.sendText();
                break;
            case R.id.btn_add:
                updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE);
                break;
            case btn_voice:
                if (activity != null && requestAudio(activity))
                {
                    updateView(InputMode.VOICE);
                }
                break;

            case R.id.btn_keyboard:
                updateView(InputMode.TEXT);
                break;
//            case R.id.btn_video:
//                if (getContext() instanceof FragmentActivity)
//                {
//                    FragmentActivity fragmentActivity = (FragmentActivity) getContext();
//                    if (requestVideo(fragmentActivity))
//                    {
////                    VideoInputDialog.show(fragmentActivity.getSupportFragmentManager());
//                    }
//                }
//                break;
//
//            case R.id.btn_file:
//                chatView.sendFile();
//                break;z
            case R.id.btnEmoticon:
                updateView(inputMode == InputMode.EMOTICON ? InputMode.TEXT : InputMode.EMOTICON);
                break;
            case R.id.chat_more_camera:
                if (activity != null && requestCamera(activity))
                {
                    chatView.sendPhoto();
                }
                break;
            case R.id.chat_more_picture:
                if (activity != null && requestStorage(activity))
                {
                    chatView.sendImage();
                }
                break;
            case R.id.chat_more_test:
                break;
            case R.id.chat_more_work:
                break;
            case R.id.chat_more_sign:
                break;
            default:
                break;
        }
    }


    /**
     * 获取输入框文字
     */
    public Editable getText()
    {
        return editText.getText();
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text)
    {
        editText.setText(text);
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode)
    {
        updateView(mode);
    }


    public enum InputMode
    {
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        VIDEO,
        NONE,
    }

    private boolean requestVideo(Activity activity)
    {
        if (afterM())
        {
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0)
            {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestCamera(Activity activity)
    {
        if (afterM())
        {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestAudio(Activity activity)
    {
        if (afterM())
        {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestStorage(Activity activity)
    {
        if (afterM())
        {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
