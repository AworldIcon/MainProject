package com.coder.kzxt.im.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.app.utils.LogWriter;
import com.coder.kzxt.activity.R;
import com.tencent.TIMFaceElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil
{
    public HashMap<String, Integer> mEmojis = new HashMap<String, Integer>();
    public List<List<String>> mEmojiPageList = new ArrayList<List<String>>();
    public static final String EMOJI_DELETE_NAME = "EMOJI_DELETE_NAME";
    private int pageSize = 20;


    public String[] pcEmojis = new String[]{
            "[微笑]",
            "[撇嘴]",
            "[色]",
            "[发呆]",
            "[得意]",
            "[流泪]",
            "[害羞]",
            "[闭嘴]",
            "[睡]",
            "[大哭]",
            "[尴尬]",
            "[发怒]",
            "[调皮]",
            "[呲牙]",
            "[惊讶]",
            "[难过]",
            "[酷]",
            "[冷汗]",
            "[抓狂]",
            "[吐]",
            "[偷笑]",
            "[可爱]",
            "[白眼]",
            "[傲慢]",
            "[饥饿]",
            "[困]",
            "[惊恐]",
            "[流汗]",
            "[憨笑]",
            "[大兵]",
            "[奋斗]",
            "[咒骂]",
            "[疑问]",
            "[嘘]",
            "[晕]",
            "[折磨]",
            "[衰]",
            "[骷髅]",
            "[敲打]",
            "[再见]",
            "[擦汗]",
            "[抠鼻]",
            "[鼓掌]",
            "[糗大了]",
            "[坏笑]",
            "[左哼哼]",
            "[右哼哼]",
            "[哈欠]",
            "[鄙视]",
            "[委屈]",
            "[快哭了]",
            "[阴险]",
            "[亲亲]",
            "[吓]",
            "[可怜]",
            "[菜刀]",
            "[西瓜]",
            "[啤酒]",
            "[篮球]",
            "[乒乓]",
            "[咖啡]",
            "[饭]",
            "[猪头]",
            "[玫瑰]",
            "[凋谢]",
            "[示爱]",
            "[爱心]"};

    private static EmojiUtil mEmojiUtil;


    private EmojiUtil()
    {

    }

    public static EmojiUtil getInstace()
    {
        if (mEmojiUtil == null)
        {
            mEmojiUtil = new EmojiUtil();
        }
        return mEmojiUtil;
    }

    public void initData()
    {
        mEmojis.clear();
        mEmojis.put("[微笑]", R.drawable.emoji_000);
        mEmojis.put("[撇嘴]", R.drawable.emoji_001);
        mEmojis.put("[色]", R.drawable.emoji_002);
        mEmojis.put("[发呆]", R.drawable.emoji_003);
        mEmojis.put("[得意]", R.drawable.emoji_004);
        mEmojis.put("[流泪]", R.drawable.emoji_005);
        mEmojis.put("[害羞]", R.drawable.emoji_006);
        mEmojis.put("[闭嘴]", R.drawable.emoji_007);
        mEmojis.put("[睡]", R.drawable.emoji_008);
        mEmojis.put("[大哭]", R.drawable.emoji_009);
        mEmojis.put("[尴尬]", R.drawable.emoji_010);
        mEmojis.put("[发怒]", R.drawable.emoji_011);
        mEmojis.put("[调皮]", R.drawable.emoji_012);
        mEmojis.put("[呲牙]", R.drawable.emoji_013);
        mEmojis.put("[惊讶]", R.drawable.emoji_014);
        mEmojis.put("[难过]", R.drawable.emoji_015);
        mEmojis.put("[酷]", R.drawable.emoji_016);
        mEmojis.put("[冷汗]", R.drawable.emoji_017);
        mEmojis.put("[抓狂]", R.drawable.emoji_018);
        mEmojis.put("[吐]", R.drawable.emoji_019);
        mEmojis.put("[偷笑]", R.drawable.emoji_020);
        mEmojis.put("[可爱]", R.drawable.emoji_021);
        mEmojis.put("[白眼]", R.drawable.emoji_022);
        mEmojis.put("[傲慢]", R.drawable.emoji_023);
        mEmojis.put("[饥饿]", R.drawable.emoji_024);
        mEmojis.put("[困]", R.drawable.emoji_025);
        mEmojis.put("[惊恐]", R.drawable.emoji_026);
        mEmojis.put("[流汗]", R.drawable.emoji_027);
        mEmojis.put("[憨笑]", R.drawable.emoji_028);
        mEmojis.put("[大兵]", R.drawable.emoji_029);
        mEmojis.put("[奋斗]", R.drawable.emoji_030);
        mEmojis.put("[咒骂]", R.drawable.emoji_031);
        mEmojis.put("[疑问]", R.drawable.emoji_032);
        mEmojis.put("[嘘]", R.drawable.emoji_033);
        mEmojis.put("[晕]", R.drawable.emoji_034);
        mEmojis.put("[折磨]", R.drawable.emoji_035);
        mEmojis.put("[衰]", R.drawable.emoji_036);
        mEmojis.put("[骷髅]", R.drawable.emoji_037);
        mEmojis.put("[敲打]", R.drawable.emoji_038);
        mEmojis.put("[再见]", R.drawable.emoji_039);
        mEmojis.put("[擦汗]", R.drawable.emoji_040);
        mEmojis.put("[抠鼻]", R.drawable.emoji_041);
        mEmojis.put("[鼓掌]", R.drawable.emoji_042);
        mEmojis.put("[糗大了]", R.drawable.emoji_043);
        mEmojis.put("[坏笑]", R.drawable.emoji_044);
        mEmojis.put("[左哼哼]", R.drawable.emoji_045);
        mEmojis.put("[右哼哼]", R.drawable.emoji_046);
        mEmojis.put("[哈欠]", R.drawable.emoji_047);
        mEmojis.put("[鄙视]", R.drawable.emoji_048);
        mEmojis.put("[委屈]", R.drawable.emoji_049);
        mEmojis.put("[快哭了]", R.drawable.emoji_050);
        mEmojis.put("[阴险]", R.drawable.emoji_051);
        mEmojis.put("[亲亲]", R.drawable.emoji_052);
        mEmojis.put("[吓]", R.drawable.emoji_053);
        mEmojis.put("[可怜]", R.drawable.emoji_054);
        mEmojis.put("[菜刀]", R.drawable.emoji_055);
        mEmojis.put("[西瓜]", R.drawable.emoji_056);
        mEmojis.put("[啤酒]", R.drawable.emoji_057);
        mEmojis.put("[篮球]", R.drawable.emoji_058);
        mEmojis.put("[乒乓]", R.drawable.emoji_059);
        mEmojis.put("[咖啡]", R.drawable.emoji_060);
        mEmojis.put("[饭]", R.drawable.emoji_061);
        mEmojis.put("[猪头]", R.drawable.emoji_062);
        mEmojis.put("[玫瑰]", R.drawable.emoji_063);
        mEmojis.put("[凋谢]", R.drawable.emoji_064);
        mEmojis.put("[示爱]", R.drawable.emoji_065);
        mEmojis.put("[爱心]", R.drawable.emoji_066);

        int pageCount = (int) Math.ceil(mEmojis.size() / 20 + 0.1);

        for (int iPage = 0; iPage < pageCount; iPage++)
        {
            int startIndex = iPage * pageSize;
            int endIndex = startIndex + pageSize;

            if (endIndex > mEmojis.size())
            {
                endIndex = mEmojis.size();
            }

            List<String> list = new ArrayList<String>();
            int mapNum = 0;
//            for (Map.Entry<String, Integer> entry : mEmojis.entrySet()) {
//                if (mapNum >= startIndex) {
//                    list.add(entry.getKey());
//                    if (mapNum == (endIndex - 1)) {
//                        break;
//                    }
//                }
//                mapNum++;
//            }
            for (String entry : pcEmojis)
            {
                if (mapNum >= startIndex)
                {
                    list.add(entry);
                    if (mapNum == (endIndex - 1))
                    {
                        break;
                    }
                }
                mapNum++;
            }
            if (list.size() < pageSize)
            {
                for (int i = list.size(); i < pageSize; i++)
                {
                    String str = new String();
                    list.add(str);
                }
            }
            if (list.size() == pageSize)
            {
                list.add(EMOJI_DELETE_NAME);
            }
            mEmojiPageList.add(list);
        }

    }

    public SpannableString addEmoji(Context context, String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return null;
        }

        int imageID = (Integer) mEmojis.get(text);
        Drawable drawable = context.getResources().getDrawable(imageID);
        drawable.setBounds(0, 0, 45, 45);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(imageSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void replaceImage(Context context, SpannableString spannableString, Pattern patten, int start)
            throws Exception
    {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find())
        {
            String key = matcher.group();
            if (matcher.start() < start)
            {
                continue;
            }
            Integer imageID = mEmojis.get(key);
            if (imageID == null || imageID == 0)
            {
                continue;
            }

            ImageSpan imageSpan = new ImageSpan(context, imageID);
            int end = matcher.start() + key.length();
            spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (end < spannableString.length())
            {
                replaceImage(context, spannableString, patten, end);
            }
            break;
        }
    }

    public SpannableString getSpannableString(Context context, String str)
    {
        SpannableString spannableString = new SpannableString(str);
        String express = "\\[[^\\]]+\\]";
        Pattern patten = Pattern.compile(express, Pattern.CASE_INSENSITIVE);
        try
        {
            replaceImage(context, spannableString, patten, 0);
        } catch (Exception e)
        {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    public static TIMMessage getMessage(String str)
    {

        TIMMessage msg = new TIMMessage();

        List<String> emojis = new ArrayList<>();
        Pattern p = Pattern.compile("[\\[][(\\u4e00-\\u9fa5)]{1,3}[\\]]");//正则。
        Matcher m;
        m = p.matcher(str);//获得匹配
        while (m.find())
        { //注意这里，是while不是if
            emojis.add(m.group());
            LogWriter.d("send emoji = " + m.group());
        }
        LogWriter.d("send text = " + str);


        if (emojis.size() == 0)
        {
            TIMTextElem elem = new TIMTextElem();
            elem.setText(str);
            msg.addElement(elem);
        } else
        {
            for (int i = 0; i < emojis.size(); i++)
            {
                LogWriter.d("end position " + str.indexOf(emojis.get(0)));
                String subString = str.substring(0, str.indexOf(emojis.get(i)));
                LogWriter.d("subString " + subString);

                if (!TextUtils.isEmpty(subString))
                {
                    TIMTextElem elem = new TIMTextElem();
                    elem.setText(subString);
                    msg.addElement(elem);
                }

                for (int j = 0; j < EmojiUtil.getInstace().pcEmojis.length; j++)
                {
                    if ((EmojiUtil.getInstace().pcEmojis[j]).equals(emojis.get(i)))
                    {
                        TIMFaceElem elem = new TIMFaceElem();
                        elem.setIndex(j + 1);
                        elem.setData(emojis.get(i).getBytes());
                        msg.addElement(elem);
                        LogWriter.d("face elem  " + j + emojis.get(i));
                    }
                }
                LogWriter.d("subString " + subString.length() + emojis.get(i).length());
                str = str.substring(subString.length() + emojis.get(i).length());
            }

            if (!TextUtils.isEmpty(str))
            {
                TIMTextElem elem = new TIMTextElem();
                elem.setText(str);
                msg.addElement(elem);
            }
        }
        return msg;
    }
}