package com.coder.kzxt.utils;

import android.content.Context;

/**
 * 倒计时工具类
 */
public class Counter extends com.coder.kzxt.utils.CountDownTimer
{

    private static final int SECONDS = 60;    //秒数
    private static final int MINUTES = 60 * 60;    //小时
    private long first = 0, twice = 0, third = 0;
    private long mtmp = 0, mtmp2 = 0;
    private Context context;
    private CounterTick counterTick;

    /**
     * @param context
     * @param millisInFuture    //计时时间
     * @param countDownInterval //计时间隔
     */
    public Counter(Context context, long millisInFuture, long countDownInterval, CounterTick counterFinish)
    {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.counterTick = counterFinish;
    }

    @Override
    public void onFinish()
    {
        cancel();
//        textView.setText("00:00:00");
//		//发送广播
//		Intent intent = new Intent();
//		intent.setAction(Constants.MY_TIME_TO_COMPLETE);
//		context.sendBroadcast(intent);
        counterTick.finish();
    }


    @Override
    public void onTick(long millisUntilFinished)
    {
        //获取当前时间总秒数
        first = millisUntilFinished / 1000;

        if (millisUntilFinished <= 0)
        {
//            textView.setText(context.getResources().getString(R.string.live_will_begin));
            counterTick.tickTime(0, 0, 0, -1);
        } else if (first < SECONDS)
        {  //小于一分钟 只显示秒
//            textView.setText("00:00:" + (first < 10 ? "0" + first : first));
            counterTick.tickTime(0, 0, 0, first);
        } else if (first < MINUTES)
        {   //大于或等于一分钟，但小于一小时，显示分钟
            twice = first % 60; //将秒转为分钟取余，余数为秒
            mtmp = first / 60; //将秒数转为分钟

//            if (twice == 0)
//            {
//                textView.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":00");    //只显示分钟
//            } else
//            {
//                textView.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (twice < 10 ? "0" + twice : twice));    //显示分钟和秒
//            }

            if (twice == 0)
            {
                counterTick.tickTime(0, 0, mtmp, 0);
            } else
            {
                counterTick.tickTime(0, 0, mtmp, twice);
            }

        } else
        {
            twice = first % 3600;    //twice为余数 如果为0则小时为整数
            mtmp = first / 3600;
            if (twice == 0)
            {
                //只剩下小时
                counterTick.tickTime(0, first / 3600, 0, 0);
//                textView.setText("0" + first / 3600 + ":00:00");
            } else
            {
                if (twice < SECONDS)
                {
                    counterTick.tickTime(0, mtmp, 0, twice);
//                    //twice小于60 为秒
//                    textView.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":00:" + (twice < 10 ? "0" + twice : twice));    //显示小时和秒
                } else
                {
                    third = twice % 60;    //third为0则剩下分钟 否则还有秒
                    mtmp2 = twice / 60;
                    if (third == 0)
                    {
                        counterTick.tickTime(0, mtmp, mtmp2, 0);

//                        textView.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":00");
                    } else
                    {
                        counterTick.tickTime(0, mtmp, mtmp2, third);
//                        textView.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":" + third); //还有秒
                    }
                }
            }
        }

    }

    public String resetString(long mtmp)
    {
        return mtmp < 10 ? "0" + mtmp : mtmp + "";
    }

    public interface CounterTick
    {
        public void finish();

        public void tickTime(long day, long hour, long minute, long second);
    }

}
