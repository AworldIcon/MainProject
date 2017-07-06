package com.coder.kzxt.classe.manager;

import com.coder.kzxt.classe.mInterface.ObserverListener;
import com.coder.kzxt.classe.mInterface.SubjectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/19.
 * 管理类
 */

public class ObserverManager implements SubjectListener {

    private static ObserverManager observerManager;

    /**
     * 观察者接口集合
     */
    private List<ObserverListener> list = new ArrayList<>();

    private ObserverManager(){

    }


    public static ObserverManager getInstance(){
        if(null == observerManager){
            synchronized (ObserverManager.class){
                if(null == observerManager){
                    observerManager = new ObserverManager();
                }
            }
        }
        return observerManager;
    }


    /**
     * 加入监听队列
     * @param listener
     */
    @Override
    public void add(ObserverListener listener) {
        list.add(listener);
    }

    /**
     * 通知观察者刷新数据
     * @param content
     */
    @Override
    public void notifyObserver(String id,String content) {
        for (ObserverListener listener:  list) {
              listener.observerUpData(id,content);
        }
    }

    /**
     * 监听队列删除
     * @param listener
     */
    @Override
    public void remove(ObserverListener listener) {
         if(list.contains(listener)){
             list.remove(listener);
         }
    }
}
