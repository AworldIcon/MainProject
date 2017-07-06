package com.coder.kzxt.dialog.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.adapter.HolderBaseAdapter;
import com.coder.kzxt.main.beans.CourseResult;
import com.coder.kzxt.utils.EToast;

public class TeacherCourseDialog extends Dialog {

	private ListView listView;
	private TeacherCourseAdapter adapter;


	/**
	 * 调用 dialog的时候 弹出的list格式用此函数
	 * @param context
	 */

	public TeacherCourseDialog(final Context context, CourseResult.DataBean.ListBean data) {
		super(context, R.style.DialogBlack);
		this.setContentView( R.layout.dlg_custom_list1);
		final Window dialogWindow = this.getWindow();
		dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogWindow.setGravity(Gravity.CENTER);
		this.setCanceledOnTouchOutside(true);

		listView = (ListView) this.findViewById(R.id.list);
		adapter = new TeacherCourseAdapter(context, data);
		listView.setAdapter(adapter);
	}


	public class TeacherCourseAdapter extends HolderBaseAdapter<CourseResult.DataBean.ListBean.classListBean> {

		private Context mContext;
		private CourseResult.DataBean.ListBean listBean;

		public TeacherCourseAdapter(Context mContext, CourseResult.DataBean.ListBean rangData) {
			super();
			this.mContext = mContext;
			this.data = rangData.getClassList();
			this.listBean = rangData;
		}


		@Override
		public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
			ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_teacher_course_class);
			TextView className = (TextView) mViewHolder.findViewById(R.id.className);
			TextView sign = (TextView) mViewHolder.findViewById(R.id.sign);
			ImageView classImage = (ImageView) mViewHolder.findViewById(R.id.classImage);
			LinearLayout ll_item = (LinearLayout) mViewHolder.findViewById(R.id.range_bgColor);

			final CourseResult.DataBean.ListBean.classListBean classBean = data.get(position);
			className.setText(classBean.getClassName());
			//PublicUtils.SetClassImage(classImage, Integer.parseInt(classBean.getClassId()));
			sign.setVisibility(View.GONE);

			ll_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转授课班详情
					EToast.makeText(mContext,"跳转授课班详情"+position, Toast.LENGTH_SHORT).show();

//					Intent intent = new Intent(mContext,LectureClassActivity.class);
//					intent.putExtra("className",classBean.getClassName());
//					intent.putExtra("classId",classBean.getClassId());
//					intent.putExtra("flag", Constants.ONLINE);
//					intent.putExtra("courseId",listBean.getCourseId());
//					intent.putExtra("courseName", listBean.getCourseTitle());
//					intent.putExtra("pic", classBean.getImage());
//					intent.putExtra(Constants.IS_CENTER, listBean.getPublicCourse());
//					mContext.startActivity(intent);
					dismiss();
				}
			});

//			sign.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					CourseClassBean bean = new CourseClassBean();
//					bean.setCourseId(Integer.parseInt(listBean.getCourseId()));
//					bean.setPublicCourse(listBean.getPublicCourse());
//					bean.setId(Integer.parseInt(classBean.getClassId()));
//					bean.setSignInNumber(classBean.getSignInNumber());
//					bean.setClassName(classBean.getClassName());
//					bean.setTeachers(classBean.getTeachers());
//					Intent intent = new Intent(mContext, SignManageActivityNew.class);
//					intent.putExtra("bean", bean);
//					mContext.startActivity(intent);
//					TeacherCourseDialog.this.dismiss();
//				}
//			});

			return mViewHolder;
		}

	}

}
