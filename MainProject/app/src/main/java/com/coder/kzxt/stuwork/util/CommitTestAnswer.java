package com.coder.kzxt.stuwork.util;

/**
 * Created by pc on 2017/2/27.
 */

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.stuwork.entity.CommitAnswer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommitTestAnswer {

    private Context context;
    public CommitTestAnswer(Context context) {
        super();
        this.context = context;
    }

    /**
     * 提交单个问题答案
     *
     */
    public void commit_Answer(ArrayList<CommitAnswer> answers, final int arg1, String isCenter,Boolean isCourse) {
        JSONObject childData = new JSONObject();
        JSONArray groupArray = new JSONArray();
        JSONArray childArray = new JSONArray();
        final CommitAnswer answer = answers.get(arg1);
        try {
            // 提交一个题的答案

            for (int i = 0; i < answer.getResultList().size(); i++) {
                childArray.put(answer.getResultList().get(i));
            }
            childData.put("answer", childArray);

            childData.put("id", answer.getQuestionId());

            groupArray.put(childData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg="";
        if(answer.getQuestionType().equals("single_choice")){
            msg="单选题";
        }
        if(answer.getQuestionType().equals("choice")){
            msg="多选题";

        }
        if(answer.getQuestionType().equals("fill")){
            msg="填空题";

        }
        if(answer.getQuestionType().equals("essay")){
            msg="问答题";

        }
        if(answer.getQuestionType().equals("determine")){
            msg="判断题";
        }
        final String finalMsg = msg;
        new HttpPutBuilder(context).setClassObj(null).setUrl(isCourse?UrlsNew.TEST_DETAIL_BATCH:UrlsNew.TEST_DETAIL_BATCH_SERVICE).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                //以后确定是否添加此提示
               // ToastUtils.makeText(context, finalMsg +"第"+(arg1+1)+"题提交成功");
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                //ToastUtils.makeText(context, finalMsg +"第"+(arg1+1)+"题提交失败");
            }
        }).addBodyParam(groupArray.toString()).build();

    }

    /**
     * 提交全部问题答案
     *
     */
    public void commitAnswer(String  id, int arg1, List<String> answers) {
        JSONObject childData = new JSONObject();
        JSONArray groupArray = new JSONArray();
        JSONArray childArray = new JSONArray();
        try {
            // 提交一个题的答案


            for (int i = 0; i < answers.size(); i++) {
                childArray.put(answers.get(i));
            }
            childData.put("answer", childArray);

            childData.put("id", id);

            groupArray.put(childData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        new HttpPutBuilder(context).setClassObj(null).setUrl(UrlsNew.URLHeader+"/test/result/detail/batch").setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {

            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {

            }
        }).addBodyParam(groupArray.toString()).build();

    }
}
