package com.coder.kzxt.classe.contacts;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/10.
 */
public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> users;
    private String memberId;
    private String userId;

    public UserAdapter(Context context,ArrayList<User> data,String uid) {
        this.mContext = context;
        this.users = filterData(data);
        this.userId = uid;
    }

    public ArrayList<User> filterData(ArrayList<User> data) {
        if(data != null){
            for (int i = 0;i < data.size();i++){
                User user = data.get(i);
                if(user.getLetter().equals("@")){
                   memberId = user.getClassMemberId();
                }
                if(!TextUtils.isEmpty(memberId)){
                    if(!user.getLetter().equals("@") && memberId.equals(user.getClassMemberId())){
                        data.remove(user);
                    }
                }
            }
        }
        return data;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contacts_list_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvItem = (RelativeLayout) convertView.findViewById(R.id.item);
            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_user_head);
            viewHolder.tvMe = (TextView) convertView.findViewById(R.id.me);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final User user = users.get(position);
        viewHolder.tvName.setText(user.getName());
        GlideUtils.loadCircleHeaderOfCommon(mContext, user.getIcon(), viewHolder.iv_head);
        viewHolder.tvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!users.get(position).getLetter().equals("@")) {
                    if (classMemberListener != null) {
                        classMemberListener.onClassMemberItem(user);
                    }
                }
                return true;
            }
        });

        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        if (position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvMe.setVisibility(View.GONE);
            viewHolder.tvTitle.setText(user.getLetter().toUpperCase());
        } else if(users.get(position).getLetter().equals("@")){
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvMe.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText("创建者");
        } else {
            viewHolder.tvMe.setVisibility(View.GONE);
            viewHolder.tvTitle.setVisibility(View.GONE);
        }

        if (userId.equals(user.getUserId())){
            viewHolder.tvMe.setVisibility(View.VISIBLE);
            viewHolder.tvMe.setText(mContext.getResources().getString(R.string.main_i));
        }else {
            viewHolder.tvMe.setVisibility(View.GONE);
         }

        if (users.get(position).getLetter().equals("@")) {
            viewHolder.tvItem.setEnabled(false);
        } else {
            viewHolder.tvItem.setEnabled(true);
        }
        return convertView;
    }

    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {
        String letter = users.get(position).getLetter();
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (cnAscii == users.get(i).getLetter().charAt(0)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     *
     * @param letter
     * @return
     */
    public int getFirstLetterPosition(String letter) {
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (letter.charAt(0) == users.get(i).getLetter().charAt(0)) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTitle;
        RelativeLayout tvItem;
        ImageView iv_head;
        TextView tvMe;
    }

    private OnClassMemberInfterface classMemberListener;

    public interface OnClassMemberInfterface{
        void onClassMemberItem(User user);
    }

    public void setOnClassMemberListener(OnClassMemberInfterface listener){
        this.classMemberListener = listener;
    }

}
