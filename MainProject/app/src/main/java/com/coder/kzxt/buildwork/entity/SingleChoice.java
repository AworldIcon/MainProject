package com.coder.kzxt.buildwork.entity;

/**
 * Created by pc on 2017/2/15.
 */

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2016/10/17.
 */
public class SingleChoice implements Serializable{

    private String id;//某一单选试题id
    private String score;//某一单选试题分数
    private List<String>choices;//某一单选试题下的选项集合
    private List<String>answer;//某一单选试题下的答案集合
    private String stem_content;//某一试题的题干
    private  String type;//标记
    private float totalScore;
    private String number;
    private String questionType;
    private String diffculty;
    private String initContent;//填空题没有处理情况下的题干，用于查看详情传值
    private String imgURL;//题干Url
    private String answerUrl;//问答答案图片
//一下这几个属性在批阅页面用到，三个接口属性合在一起
    private int test_result_id;
    private int question_id;
    private int status;
    private float result_score;
    private String teacher_say;
    private List<String>answer_stu;//某一单选试题下x学生的答案集合

    public List<String> getAnswer_stu() {
        return answer_stu;
    }

    public void setAnswer_stu(List<String> answer_stu) {
        this.answer_stu = answer_stu;
    }

    public float getResult_score() {
        return result_score;
    }

    public void setResult_score(float result_score) {
        this.result_score = result_score;
    }

    public String getTeacher_say() {
        return teacher_say;
    }

    public void setTeacher_say(Object teacher_say) {
        this.teacher_say = teacher_say!=null?teacher_say.toString():"";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTest_result_id() {
        return test_result_id;
    }

    public void setTest_result_id(int test_result_id) {
        this.test_result_id = test_result_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getInitContent() {
        return initContent;
    }

    public void setInitContent(String initContent) {
        this.initContent = initContent;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getDiffculty() {
        return diffculty;
    }

    public void setDiffculty(String diffculty) {
        this.diffculty = diffculty;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SingleChoice{" +
                "id='" + id + '\'' +
                ", score='" + score + '\'' +
                ", choices=" + choices +
                ", stem_content='" + stem_content + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getStem_content() {
        return stem_content;
    }

    public void setStem_content(String stem_content) {
        this.stem_content = stem_content;
    }
}

