package com.coder.kzxt.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.im.message.Message;
import com.coder.kzxt.message.activity.ChatPersonActivity;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message>
{

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects)
    {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftAvatar = (ImageView) view.findViewById(R.id.leftAvatar);
            viewHolder.rightAvatar = (ImageView) view.findViewById(R.id.rightAvatar);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            view.setTag(viewHolder);
        }
        if (position < getCount())
        {
            final Message data = getItem(position);
//            用户角色身份
//            data.getMessage().getSenderGroupMemberProfile().getNameCard()
            data.showMessage(viewHolder, getContext());

            if (data.isSelf())
            {
                GlideUtils.loadCircleHeaderOfCommon(getContext(),new  SharedPreferencesUtil(getContext()).getUserHead(), viewHolder.rightAvatar);
            } else
            {
                GlideUtils.loadCircleHeaderOfCommon(getContext(), data.getAvatar(), viewHolder.leftAvatar);
            }

            viewHolder.leftAvatar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ChatPersonActivity.gotoActivity(getContext(),
                            data.getMessage().getSenderProfile().getIdentifier());
                }
            });

        }

        return view;
    }


    public class ViewHolder
    {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public ImageView leftAvatar;
        public ImageView rightAvatar;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
    }
}
