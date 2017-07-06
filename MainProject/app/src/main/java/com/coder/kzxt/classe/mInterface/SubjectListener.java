package com.coder.kzxt.classe.mInterface;

/**
 * Created by wangtingshun on 2017/6/19.
 * 被观察者
 */

public interface SubjectListener {

    /**
     * 添加一个观察者
     * @param listener
     */
    void add(ObserverListener listener);


    /**
     * 刷新数据
     * @param id
     * @param content
     */
    void notifyObserver(String id,String content);

    /**
     * 移除一个观察者
     * @param listener
     */
    void remove(ObserverListener listener);
}
