package com.coder.kzxt.stuwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 
 * 试题内容的adapter 
 * @author Administrator
 *
 */
public class TextImgAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, String>> arrayList;
	private Context context;
	private String showType;

	public TextImgAdapter(Context context, ArrayList<HashMap<String, String>> arrayList, String showType) {
		super();
		this.arrayList = arrayList;
		this.context = context;
		this.showType = showType;
	}

	@Override
	public int getCount() {
		return arrayList == null ? 0:arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
	    convertView = LayoutInflater.from(context).inflate(R.layout.question_content_item, null);
		LinearLayout content_ly = (LinearLayout) convertView.findViewById(R.id.content_ly);
		HashMap<String, String> hashMap = arrayList.get(position);
		String type_msg = hashMap.get("type_msg");
	
		if (type_msg.equals("text")) {
			
			String content = hashMap.get("content");
			addTextView(content_ly, content);
			
		} else if (type_msg.equals("img")) {
			
			String content = hashMap.get("content");
			addImagView(content_ly, content);
			
		}
		
		return convertView;
	}
	
	
	
	private void addTextView(LinearLayout myLayout, String texturl) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.question_content_text, null);
		TextView textView = (TextView) v.findViewById(R.id.title_tx);
		v.setLayoutParams(params);
		textView.setText(texturl.trim());
		myLayout.addView(v);

	}
	
	
	private void addImagView(LinearLayout myLayout, String texturl) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 解析html
		Document document = Jsoup.parse(texturl);
		Elements imgElements = document.getElementsByTag("img");
		final String imageUrl = imgElements.attr("src");

		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.question_content_img, null);
		ImageView add_imgview = (ImageView) v.findViewById(R.id.add_imgview);
		v.setLayoutParams(params);

		ImageLoad.loadImage(context,imageUrl,R.drawable.imageloader_imglist_default,R.drawable.imageloader_imglist_default,0, RoundedCornersTransformation.CornerType.ALL,add_imgview);


		add_imgview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(context, Show_Image_Activity.class);
//				intent.putExtra("imgurl", imageUrl);
//				context.startActivity(intent);

			}
		});

		myLayout.addView(v);
	}


}
