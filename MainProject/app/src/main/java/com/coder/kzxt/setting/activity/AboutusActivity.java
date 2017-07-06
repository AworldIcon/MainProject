package com.coder.kzxt.setting.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.setting.beans.ShareBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ShareSdkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Utils;

/**
 * 关于我们

 */
public class AboutusActivity extends BaseActivity {

	private SharedPreferencesUtil spu;
	private ImageView leftImage;
	private RelativeLayout about_guanwang_layout;
	private RelativeLayout about_jiaru_layout;
	private TextView jiaru_link;
	private TextView app_name;
	private RelativeLayout about_we_layout_notwocode;
	private RelativeLayout about_we_layout_twocode;
	private TextView app_name_code;
	private TextView app_version_code;
	private ImageView code_img;
	private TextView downloadTextView;
	private Toolbar mToolbarView;
	private RelativeLayout myLayout;
	private String shareUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		spu = new SharedPreferencesUtil(this);
		initView();
		initListener();
		getShareTask();
	}

	private void initView() {
		mToolbarView = (Toolbar) findViewById(R.id.toolbar);
		mToolbarView.setTitle(R.string.about_we);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		app_name = (TextView) findViewById(R.id.app_name);
		String version2 = Utils.getVersion(AboutusActivity.this);
		app_name.setText(getResources().getString(R.string.app_name) + " v" + version2);
		about_guanwang_layout = (RelativeLayout) findViewById(R.id.about_guanwang_layout);
		about_jiaru_layout = (RelativeLayout) findViewById(R.id.about_jiaru_layout);
		jiaru_link = (TextView) findViewById(R.id.jiaru_link);
		myLayout = (RelativeLayout) findViewById(R.id.aaaa);
		// 无二维码
		about_we_layout_notwocode = (RelativeLayout) findViewById(R.id.about_we_layout_notwocode);
		// 有二维码
		about_we_layout_twocode = (RelativeLayout) findViewById(R.id.about_we_layout_twocode);
		app_name_code = (TextView) findViewById(R.id.app_name_code);
		app_name_code.setText(this.getResources().getString(R.string.app_name));
		app_version_code = (TextView) findViewById(R.id.app_version_code);
		app_version_code.setText("V" + version2);
		code_img = (ImageView) findViewById(R.id.code_img);

		downloadTextView = (TextView) findViewById(R.id.downloadTextView);
//		downloadTextView.setText(getResources().getString(R.string.scanning_twocode_before) + getResources().getString(R.string.app_name) + getResources().getString(R.string.scanning_twocode_after));

//		if (!TextUtils.isEmpty(pu.getAboutUsAddressUrl())) {
//			jiaru_link.setText(pu.getAboutUsAddressUrl());
//		}
	}

	private void initListener() {
		// 分享高校云
		about_guanwang_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(spu.getAboutUsShareUrl())) {
					ShareSdkUtil shareSdkUtil = new ShareSdkUtil(spu);
					shareSdkUtil.shareSDK(AboutusActivity.this, spu.getAboutUsShareUrl()
							, getResources().getString(R.string.share_before) + getResources().getString(R.string.app_name)
									+ getResources().getString(R.string.share_after));
				}
			}
		});

		about_jiaru_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(AboutusActivity.this, TextView_Link_Activity.class);
//				intent.putExtra("web_url", pu.getAboutUsAddressUrl());
//				intent.putExtra("title", "");
//				startActivity(intent);
			}
		});
	}


	private void getShareTask() {
		       new HttpGetBuilder(AboutusActivity.this)
				.setUrl(UrlsNew.GET_SHARE_URL)
				.setHttpResult(new HttpCallBack() {
					@Override
					public void setOnSuccessCallback(int requestCode, Object resultBean) {
                         ShareBean share = (ShareBean) resultBean;
						 shareUrl = share.getItem().getShareUrl();
						 spu.setAboutUsShareUrl(shareUrl);
					}

					@Override
					public void setOnErrorCallback(int requestCode, int code, String msg) {
						if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
							NetworkUtil.httpRestartLogin(AboutusActivity.this, myLayout);
						} else {
							NetworkUtil.httpNetErrTip(AboutusActivity.this, myLayout);
						}
					}
				})
				.setClassObj(ShareBean.class)
				.build();

	}

	private void downImage() {
//		ImageLoader.getInstance().displayImage(Constants.ABOUT_URL, code_img, new ImageLoadingListener() {
//
//			@Override
//			public void onLoadingStarted(String arg0, View arg1) {
//
//			}
//
//			@Override
//			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//				about_we_layout_notwocode.setVisibility(View.VISIBLE);
//				about_we_layout_twocode.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
//				if (bitmap != null) {
//					about_we_layout_notwocode.setVisibility(View.GONE);
//					about_we_layout_twocode.setVisibility(View.VISIBLE);
//				} else {
//					about_we_layout_notwocode.setVisibility(View.VISIBLE);
//					about_we_layout_twocode.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onLoadingCancelled(String arg0, View arg1) {
//				about_we_layout_notwocode.setVisibility(View.VISIBLE);
//				about_we_layout_twocode.setVisibility(View.GONE);
//			}
//		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
