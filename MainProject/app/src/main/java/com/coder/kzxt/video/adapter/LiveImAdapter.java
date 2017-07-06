package com.coder.kzxt.video.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.im.message.TextMessage;
import com.coder.kzxt.im.util.EmojiUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.tencent.TIMElemType;
import com.tencent.TIMFaceElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class LiveImAdapter extends BaseAdapter{
    private Context context;
    private SharedPreferencesUtil spu;
    private List<com.coder.kzxt.im.message.Message> mData;

    public LiveImAdapter(Context context, List<com.coder.kzxt.im.message.Message>  mData) {
        this.context = context;
        this.mData = mData;
        spu = new SharedPreferencesUtil(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_course_chat,null);
        }
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextMessage messages = (TextMessage) mData.get(position);

        TIMMessage message = messages.getMessage();
        StringBuffer stringBuffer = new StringBuffer();


        for (int j = 0; j < message.getElementCount(); j++) {
            if (message.getElement(j) == null) continue;
            if (message.getElement(j).getType() == TIMElemType.Text) {
                TIMTextElem elem = (TIMTextElem) message.getElement(j);
                stringBuffer.append(elem.getText());
            } else if (message.getElement(j).getType() == TIMElemType.Face) {
                TIMFaceElem elem = (TIMFaceElem) message.getElement(j);
                stringBuffer.append(EmojiUtil.getInstace().pcEmojis[elem.getIndex() - 1]);
            }
        }
        String name = "";
        if (message.getSenderGroupMemberProfile()!=null)
            name = message.getSenderGroupMemberProfile().getNameCard();//身份标识
        if (name.equals("")&&message.getSenderProfile()!=null)
            name = message.getSenderProfile().getNickName();
        if (name.equals(""))
            name = message.getSender();
        //自己发的消息显示自己发的id
        if(name.equals(spu.getUid())){
            name = spu.getNickName();
        }

        SpannableString spannableString = EmojiUtil.getInstace().getSpannableString(context, name+":"+stringBuffer.toString());
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.style_text0), 0, name.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(spannableString);

        return convertView;
    }


}
