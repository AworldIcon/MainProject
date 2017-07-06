package com.coder.kzxt.stuwork.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;


public class TextUitl {
	
	/**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
	public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
	
	
    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
    

    /**
     * @param answers
     * 根据角标选择单选题的解析答案
     */
    public static String singleChoiceAnswersText(String answers){
    	String[] s = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
    	if(!TextUtils.isEmpty(answers)){
    		return s[Integer.parseInt(answers.trim())];
    	}else {
    		return "未选择 ";
		}
    }
    
    /**
     * @param list
     * 根据角标选择多选题的解析答案
     */
    public static String choiceAnswersText(ArrayList<String> list){
    	String choiceStr = "";
    	
    	for(int i = 0;i<list.size();i++){
    		String string = list.get(i);
    		if(!TextUtils.isEmpty(string)){
    			choiceStr += singleChoiceAnswersText(string)+",";
    		 }else {
    			choiceStr += "未选择 "+",";
			 }
    	
    	}
		return choiceStr.substring(0, choiceStr.length()-1);
    	
    }
    
    /**
     * 返回填空题的解析答案
     */
    
    @NonNull
	public static String fillAnswersText(ArrayList<String> list){
    	String choiceStr = "";
    	for(int i = 0;i<list.size();i++){
    		String string = list.get(i);
    		choiceStr += string+" | ";
    	}
		return choiceStr.substring(0, choiceStr.length()-3);
    }
 
    
    /**
     * 返回判断题的解析答案
     */
    public static String detemineAnswersText(String answers){
    	String choiceStr = "";
    	if(answers.equals("1")){
    		choiceStr = "正确";
    	}else {
    		choiceStr = "错误";
		}
    	
		return choiceStr;
    	
    }
    /**
     * 返回观看多少万人
     */
    public static String getPeopleNumberWan(String people){
    	int peopleNumber = Integer.valueOf(people);
    	if(peopleNumber>9999){
    		return peopleNumber/1000+"."+peopleNumber/100+"W";
    	}
    	return peopleNumber+"";
    }

}
	
