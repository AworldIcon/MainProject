package com.coder.kzxt.im.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.coder.kzxt.im.adapter.EmViewPagerAdapter;
import com.coder.kzxt.im.adapter.EmojiAdapter;
import com.coder.kzxt.im.util.EmojiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/1
 */

public class EmojiViewPage extends ViewPager
{

    private Context mContext;
    private EditText editText;
    public EmojiViewPage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setEditText(EditText editText)
    {
        this.editText = editText;
    }

    private void initView()
    {
        List<View> pageViews = new ArrayList<View>();
        final List<EmojiAdapter> emojiAdapters = new ArrayList<EmojiAdapter>();

        EmojiUtil.getInstace().initData();
        List<List<String>> emojis = EmojiUtil.getInstace().mEmojiPageList;

        final int current = 0;

        for (int i = 0; i < emojis.size(); i++)
        {
            GridView view = new GridView(getContext());
            EmojiAdapter adapter = new EmojiAdapter(getContext(), emojis.get(i));
            // Log.d(TAG,"InitViewPager:" + emojis.get(i).size());
            view.setAdapter(adapter);
            emojiAdapters.add(adapter);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {

                    String strEmoji = (String) emojiAdapters.get(getCurrentItem()).getItem(position);
                    if (strEmoji.equals(EmojiUtil.EMOJI_DELETE_NAME))
                    {
                        if (!TextUtils.isEmpty(editText.getText()))
                        {
                            int selection = editText.getSelectionStart();
                            String strInputText = editText.getText().toString();
                            if (selection > 0)
                            {
                                String strText = strInputText.substring(selection - 1);
                                if ("]".equals(strText))
                                {
                                    int start = strInputText.lastIndexOf("[");
                                    int end = selection;
                                    editText.getText().delete(start, end);
                                    return;
                                }
                                editText.getText().delete(selection - 1, selection);
                            }
                        }
                    } else
                    {
                        SpannableString spannableString = EmojiUtil.getInstace().addEmoji(getContext(), strEmoji);
                        if (spannableString == null)
                        {
                            return;
                        }
                        editText.append(spannableString);
                    }
                }
            });
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setNumColumns(7);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }
         setAdapter(new EmViewPagerAdapter(pageViews));
     }

}
