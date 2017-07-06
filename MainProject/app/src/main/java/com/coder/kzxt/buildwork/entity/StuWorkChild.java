package com.coder.kzxt.buildwork.entity;

/**
 * Created by pc on 2016/11/4.
 */
public class StuWorkChild {
    private String score;//某一提的分数
    private String content;//某一提的题干
    private String answer;//某一提的答案
    private String answerUrl;//某一提的答案图片
    private String result;//某一提的学生答案
    private String choices;//某一提的选项（单选/多选 有）
    private String status;//某一提答案是否正确==》right/false
    private String id;
    private String testId;

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StuWorkChild{" +
                "score='" + score + '\'' +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", result='" + result + '\'' +
                ", choices='" + choices + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
