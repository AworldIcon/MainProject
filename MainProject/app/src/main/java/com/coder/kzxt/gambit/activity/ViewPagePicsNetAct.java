package com.coder.kzxt.gambit.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.gesture.view.PhotoViewAttacher;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.views.PicturesViewPage;
import com.coder.kzxt.views.RecyclingPagerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewPagePicsNetAct extends AppCompatActivity {
    private ImageView back_title_button;
    private ImageView rightImage;
    private PicturesViewPage viewpager;
    private ArrayList<String> sdurlList;
    private int index;
    private TextView current_position;
    private ViewPagePicsAdapter adapter;
    private View fenge_view;
    private Dialog dialog;
    private RelativeLayout main;
    private ImageView save_img;
    private String saveUrl;
    private Bitmap tmpBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_pics_net);

        sdurlList = new ArrayList<String>();
        main = (RelativeLayout) findViewById(R.id.activity_view_page_pics_net);
        back_title_button = (ImageView) findViewById(R.id.leftImage);
        rightImage = (ImageView) findViewById(R.id.rightImage);
        save_img = (ImageView) findViewById(R.id.save_img);
        rightImage.setBackgroundResource(R.drawable.pics_menu);
        fenge_view = findViewById(R.id.fenge_view);
        fenge_view.setVisibility(View.GONE);
        current_position = (TextView) findViewById(R.id.title);
        viewpager = (PicturesViewPage) findViewById(R.id.viewpager);
        sdurlList = getIntent().getStringArrayListExtra("imgurl");
        index = getIntent().getIntExtra("index", 0);
        adapter = new ViewPagePicsAdapter(ViewPagePicsNetAct.this, sdurlList);

        back_title_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

        viewpager.setAdapter(adapter.setInfiniteLoop(false));// 不能循环滚动

        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());

        current_position.setText("1" + "/" + sdurlList.size());
        viewpager.setOffscreenPageLimit(sdurlList.size());// 设置缓存个数
        viewpager.setCurrentItem(index);

        saveUrl = sdurlList.get(index);

        dialog = MyPublicDialog.createLoadingDialog(this);
        dialog.show();

        rightImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PopupWindows(ViewPagePicsNetAct.this, main);
            }
        });
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // 算出imageViews里的角标
            position = position % sdurlList.size();
            index = position;
            current_position.setText(position + 1 + "" + "/" + sdurlList.size());
            saveUrl = sdurlList.get(position);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            index = position;

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 自定义PopupWindows
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {
            super(mContext);

            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            bt1.setText("保存");
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            bt2.setVisibility(View.GONE);
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            View fenge_3 = view.findViewById(R.id.fenge_3);
            View fenge_2 = view.findViewById(R.id.fenge_2);
            fenge_3.setVisibility(View.GONE);
            fenge_2.setVisibility(View.GONE);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();

                    if (tmpBitmap == null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_save_fail), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Long startVoiceT = System.currentTimeMillis();
                    try {
                        Bimp.saveBitmapToFile(tmpBitmap, Constants.SAVE_PICTURE + startVoiceT + ".jpg", false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_saved_to) + Constants.SAVE_PICTURE + getResources().getString(R.string.picture_file), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.picture_save_fail), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    class ViewPagePicsAdapter extends RecyclingPagerAdapter {

        private Context context;
        private ArrayList<String> sdurlsList;

        private int size;
        private PhotoViewAttacher mAttacher;

        public ViewPagePicsAdapter(Context context, ArrayList<String> sdurlsList) {
            this.context = context;
            this.sdurlsList = sdurlsList;
            this.size = sdurlsList.size();

        }

        @Override
        public int getCount() {
            return sdurlsList.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.viewpage_imgs, null);
                holder.imageView = (ImageView) view.findViewById(R.id.my_img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            String sd_url = sdurlsList.get(position);
            mAttacher = new PhotoViewAttacher(holder.imageView);
            // 加载sd卡中的图片
            new ShowImageAsyncTask(sd_url, holder.imageView, mAttacher).execute();

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

        /**
         * @param isInfiniteLoop
         *            the isInfiniteLoop to set
         */
        public ViewPagePicsAdapter setInfiniteLoop(boolean isInfiniteLoop) {
            return this;
        }

        private int count = 0;

        private class ShowImageAsyncTask extends AsyncTask<String, Integer, Boolean> {

            private String sd_url;
            private ImageView imageView;
            private PhotoViewAttacher mAttacherAsy;

            public ShowImageAsyncTask(String sd_url, ImageView imageView, PhotoViewAttacher mAttacher) {
                super();
                this.sd_url = sd_url;
                this.imageView = imageView;
                this.mAttacherAsy = mAttacher;
            }

            @Override
            protected Boolean doInBackground(String... params) {
                boolean flag = false;
                publishProgress(1);
                try {
                    URL url = new URL(sd_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    tmpBitmap = BitmapFactory.decodeStream(input);

                    flag = true;
                } catch (Exception e) {
                    flag = false;
                    e.printStackTrace();
                }

                return flag;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values[0] == 1) {
                }

            }

            @Override
            protected void onPostExecute(Boolean result) {

                if (isFinishing()) {
                    return;
                }
                count++;

                if (count == sdurlsList.size() || count == 2) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                }

                if (result) {
                    if (tmpBitmap != null) {
                        imageView.setImageBitmap(tmpBitmap);
                    } else {
                        imageView.setImageResource(R.drawable.load_failure_big);
                    }

                    mAttacherAsy = new PhotoViewAttacher(imageView);

                } else {
                    // 否则加载默认裂图
                    imageView.setImageResource(R.drawable.load_failure_big);
                }

                super.onPostExecute(result);
            }
        }
    }

}
