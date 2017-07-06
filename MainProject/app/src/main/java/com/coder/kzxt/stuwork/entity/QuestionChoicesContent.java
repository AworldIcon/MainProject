package com.coder.kzxt.stuwork.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 试题选项内容
 */
public class QuestionChoicesContent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;//选项标示
	private String isChoices;//是否被选中 0代表未选中 1代表选中
	private ArrayList<HashMap<String, String>> arrayList; //选项内容
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIsChoices() {
		return isChoices;
	}
	public void setIsChoices(String isChoices) {
		this.isChoices = isChoices;
	}
	public ArrayList<HashMap<String, String>> getArrayList() {
		return arrayList;
	}
	public void setArrayList(ArrayList<HashMap<String, String>> arrayList) {
		this.arrayList = arrayList;
	}
	
	
	
	
	
}
