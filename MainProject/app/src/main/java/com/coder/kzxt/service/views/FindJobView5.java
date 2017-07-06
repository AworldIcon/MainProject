package com.coder.kzxt.service.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPostFileBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.widget.LimitInputTextWatcher;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.login.beans.AvatarBean;
import com.coder.kzxt.poster.activity.Show_Image_Activity;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.beans.ServiceGraduateResult;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.RegularUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class FindJobView5 extends BaseView
{

    private LinearLayout mCommitLy;
    private EditText mUserNameEdit;
    private EditText mCardEdit;
    private ImageView mPhoto;
    private ImageView delete;
    private LinearLayout mResultLy;
    private ImageView mStatusImage;
    private TextView mNeedPage;
    private TextView mIntroduce;
    private ImageView mResultImage;
    private Button mButton;


    public FindJobView5(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        super(context, itemBean);
    }

    @Override
    protected View getMainView()
    {
        View view = View.inflate(mContext, R.layout.view_find_service_step5, null);
        mCommitLy = (LinearLayout) view.findViewById(R.id.commitLy);
        mUserNameEdit = (EditText) view.findViewById(R.id.userNameEdit);
        mCardEdit = (EditText) view.findViewById(R.id.cardEdit);
        mPhoto = (ImageView) view.findViewById(R.id.photo);
        delete = (ImageView) view.findViewById(R.id.delete);
        mResultLy = (LinearLayout) view.findViewById(R.id.resultLy);
        mStatusImage = (ImageView) view.findViewById(R.id.statusImage);
        mIntroduce = (TextView) view.findViewById(R.id.introduce);
        mNeedPage = (TextView) view.findViewById(R.id.needPage);
        mResultImage = (ImageView) view.findViewById(R.id.resultImage);
        mButton = (Button) view.findViewById(R.id.button);

        mUserNameEdit.addTextChangedListener(new LimitInputTextWatcher(mUserNameEdit));
        return view;
    }

    @Override
    protected String getTitleString()
    {
        return getStage() + this.mContext.getResources().getString(R.string.service_step_5);
    }

    private String getStage()
    {
//        "type": "服务类型 1就业服务 0认证服务",
        if (itemBean.getType() == 0)
        {
            return "3/3";
        } else
        {
            return "5/5";
        }
    }

    private String imgUrl;
    private String imgUrlCommit;
    private String needPage;

    @Override
    protected void requestData()
    {

        if (itemBean.getMyType() == 2)
        {
            //没有通过阶段考核则不可点击
            if (itemBean.getExamine() != 2)
            {
                setTitleUnEnable();
                return;
            } else if (itemBean.getGraduate() != 2)
            {
                // 没有完成申请结业则默认显示中间内容
                setMainVisible();
            }
        } else if (itemBean.getMyType() == 1)
        {
            //就业服务
            //没有通过综合面试则不可点击
            if (itemBean.getGlobal_interview() != 2)
            {
                setTitleUnEnable();
                return;
            } else if (itemBean.getGraduate() != 2)
            {
                // 没有完成申请结业则默认显示中间内容
                setMainVisible();
            }
        }

        // graduate": "结业状态：0未开启 1等待 2通过 3失败 ",

        if (itemBean.getGraduate() == 0)
        {
            mCommitLy.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.VISIBLE);
            mResultLy.setVisibility(View.GONE);

        } else if (itemBean.getGraduate() == 1)
        {
            mCommitLy.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mResultLy.setVisibility(View.VISIBLE);

            mStatusImage.setImageResource(R.drawable.service_status_wait);
            mIntroduce.setText("结业申请已提交，等待审核结果。");
        } else if (itemBean.getGraduate() == 2)
        {

            mCommitLy.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mResultLy.setVisibility(View.VISIBLE);
        } else if (itemBean.getGraduate() == 3)
        {
            mResultLy.setVisibility(View.VISIBLE);
            mCommitLy.setVisibility(View.GONE);
            mButton.setVisibility(View.VISIBLE);
            mStatusImage.setImageResource(R.drawable.service_status_unpassed);
            mIntroduce.setText("结业申请未通过");
            mButton.setText("申请结业");

            new HttpGetBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_GRADUATE_ITEM)
                    .setHttpResult(FindJobView5.this)
                    .setClassObj(ServiceGraduateResult.class)
                    .setPath(itemBean.getId() + "")
                    .setRequestCode(2)
                    .build();

        }

        initClick();

    }

    private void initClick()
    {
        mNeedPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog();
            }
        });

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                delete.setVisibility(View.GONE);
                imgUrl = "";
                mPhoto.setImageResource(R.drawable.add_work);
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (TextUtils.isEmpty(imgUrl))
                {
                    //获取颜色值
                    String themeColor = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.first_theme));
                    //设置最多选择几张图片
                    AndroidImagePicker.getInstance().setSelectLimit(1);
                    AndroidImagePicker.getInstance().pickMulti((BaseActivity) mContext, true, themeColor, new AndroidImagePicker.OnImagePickCompleteListener()
                    {
                        @Override
                        public void onImagePickComplete(List<ImageItem> items)
                        {
                            try
                            {
                                if (items != null && items.size() > 0)
                                {
                                    // 保存图片到sd卡
                                    String filename = System.currentTimeMillis() + "";
                                    imgUrl = Bimp.saveBitmap(Bimp.revitionImageSize(items.get(0).path), "" + filename);
                                    GlideUtils.loadHeaderOfCommon(mContext, imgUrl, mPhoto);
                                    delete.setVisibility(View.VISIBLE);
                                    commitPhoto();
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                } else
                {
                    Show_Image_Activity.gotoActivity(mContext, imgUrl);
                }

            }
        });

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (itemBean.getGraduate() == 3)
                {
                    mButton.setText("提交");
                    mCommitLy.setVisibility(View.VISIBLE);
                    mResultLy.setVisibility(View.GONE);
                    itemBean.setGraduate(1);
                } else
                {
                    commit();
                }

            }

        });
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1)
        {
            ToastUtils.makeText(mContext, "提交结业申请成功");

            mCommitLy.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mResultLy.setVisibility(View.VISIBLE);
            mStatusImage.setImageResource(R.drawable.service_status_wait);
            mIntroduce.setText("结业申请已提交，等待审核结果。");
            itemBean.setGraduate(1);

        } else if (requestCode == 1000)
        {
            AvatarBean avatarBean = (AvatarBean) resultBean;
            imgUrlCommit = avatarBean.getItems().get(0);
        } else if (requestCode == 2)
        {
            ServiceGraduateResult serviceGraduateResult = (ServiceGraduateResult) resultBean;

            if (serviceGraduateResult.getItem() != null)
            {

                ServiceGraduateResult.ItemBean itemBean = serviceGraduateResult.getItem();
                graduateId = itemBean.getId();
                imgUrl = itemBean.getImg_url();
                imgUrlCommit = itemBean.getImg_url();
                mUserNameEdit.setText(itemBean.getName());
                mCardEdit.setText(itemBean.getId_card());
                GlideUtils.loadHeaderOfCommon(mContext, imgUrl, mPhoto);
                delete.setVisibility(View.VISIBLE);

                needPage = itemBean.getNeed_paper() + "";
                if (itemBean.getNeed_paper() == 1)
                {
                    mNeedPage.setText("需要");
                } else
                {
                    mNeedPage.setText("不需要");
                }
            }
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        if (requestCode != 2)
            ToastUtils.makeText(mContext, msg);
    }

    private CustomListDialog customListDialog;

    private void showDialog()
    {

        if (customListDialog == null)
        {
            final String[] data = new String[]{"不需要", "需要"};
            customListDialog = new CustomListDialog(mContext);
            customListDialog.addData(data);
            customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    needPage = position + "";
                    mNeedPage.setText(data[position]);
                    customListDialog.cancel();
                }
            });
        }
        customListDialog.show();

    }


    private void commitPhoto()
    {
        HashMap<String, String> postImages = new HashMap<>();
        postImages.put("file0", imgUrl);

        new HttpPostFileBuilder(mContext)
                .setUrl(UrlsNew.POST_SYSTEM_FILE)
                .setFileNames(postImages)
                .setClassObj(AvatarBean.class)
                .setHttpResult(this)
                .addQueryParams("type", "graduate")
                .setRequestCode(1000)
                .build();
    }

    private String graduateId;

    private void commit()
    {

        if (!(mUserNameEdit.getText().toString().length() > 1 && mUserNameEdit.getText().toString().length() < 11))
        {
            ToastUtils.makeText(mContext, "请输入有效姓名");
            return;
        }
        if (!RegularUtils.isIDCard(mCardEdit.getText().toString()))
        {
            ToastUtils.makeText(mContext, "请输入有效身份证");
            return;
        }
        if (TextUtils.isEmpty(imgUrl))
        {
            ToastUtils.makeText(mContext, "请上传有效尺寸的照片");
            return;
        }
        if (TextUtils.isEmpty(needPage))
        {
            ToastUtils.makeText(mContext, "请选择是否需要纸质证书");
            return;
        }

        if (itemBean.getGraduate() == 0)
        {

            new HttpPostBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_GRADUATE)
                    .setHttpResult(FindJobView5.this)
                    .setClassObj(BaseBean.class)
                    .addBodyParam("item_id", itemBean.getId() + "")
                    .addBodyParam("id_card", mCardEdit.getText().toString())
                    .addBodyParam("name", mUserNameEdit.getText().toString())
                    .addBodyParam("img_url", imgUrlCommit)
                    .addBodyParam("need_paper", needPage)
                    .setRequestCode(1)
                    .build();
        } else
        {

            new HttpPutBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_GRADUATE)
                    .setHttpResult(FindJobView5.this)
                    .setClassObj(BaseBean.class)
                    .setPath(graduateId + "")
                    .addBodyParam("item_id", itemBean.getId() + "")
                    .addBodyParam("id_card", mCardEdit.getText().toString())
                    .addBodyParam("name", mUserNameEdit.getText().toString())
                    .addBodyParam("img_url", imgUrlCommit)
                    .addBodyParam("need_paper", needPage)
                    .setRequestCode(1)
                    .build();
        }
    }

}
