package com.tencent.qcloud.suixinbo.presenters.viewinface;

import com.com.tencent.qcloud.suixinbo.presenters.UserServerHelper;
import com.tencent.qcloud.suixinbo.model.RoomInfoJson;
import java.util.ArrayList;


/**
 *  列表页面回调
 */
public interface LiveListView extends MvpView {


    void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist);
}
