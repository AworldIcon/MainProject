package com.coder.kzxt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.utils.L;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 5;
    private static final String NAME = "kzxt.db";
    //	private static DBHelper mInstance;
    public static final String IMUSER = "ImUser"; // im用户信息
    public static final String NODISTURB = "noDisturb"; // 免打扰列表

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }


//	/**
//	 * @param context
//	 * @return
//	 * 单例创建数据库
//	 */
//	public synchronized static DBHelper getInstance(Context context) {
//		if(mInstance == null) {
//			mInstance = new DBHelper(context);
//		}
//		return mInstance;
//	}

    @Override
    public void onCreate(SQLiteDatabase db) {

        //下载表目前有两个字段没用到 currentLength 和 downloadSpeed
        String sql = "CREATE TABLE IF NOT EXISTS Download (_id integer primary key autoincrement," +
                "filename string, url string, picture string, urlTotal int,downloadpos int, currentLength double," +
                "downloadSpeed int, downloadPercent int," +
                "downloadStatus int, isfinish int, downloadedSize string," +
                "foldername string, localurl string, mapKey string)";

        db.execSQL(sql);
        db.execSQL("ALTER TABLE Download ADD COLUMN [treeid] INT(11) DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [video_id] INT(11) DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [continue_playing_time] INT DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [recordingtime] LONG DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [treename] VARCHAR(100)");
        db.execSQL("ALTER TABLE Download ADD COLUMN [currentposition] LONG DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [reporteddata] VARCHAR(100)");
        db.execSQL("ALTER TABLE Download ADD COLUMN [duration] LONG DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [randid] INT(11) DEFAULT 0");
        db.execSQL("ALTER TABLE Download ADD COLUMN [sort] INT(11) DEFAULT 0");
        // 版本2
        //添加服务的两个参数
        db.execSQL("ALTER TABLE Download ADD COLUMN [service_id] VARCHAR(100)");
        db.execSQL("ALTER TABLE Download ADD COLUMN [service_name] VARCHAR(100)");
        // 版本3
        // im 用户储存表 免打扰表
        updateVersion3(db);
        //版本4
        //添加课时类型参数
        updateVersion4(db);
        //版本5
        updateVersion5(db);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.v("tangcy", "数据库版本：" + "oldVersion" + oldVersion + "newVersion" + newVersion);
        if (oldVersion == 1 && newVersion == 2) {
            //添加服务的两个参数
            db.execSQL("ALTER TABLE Download ADD COLUMN [service_id] VARCHAR(100)");
            db.execSQL("ALTER TABLE Download ADD COLUMN [service_name] VARCHAR(100)");
        } else if (oldVersion == 1 && newVersion == 3) {
            db.execSQL("ALTER TABLE Download ADD COLUMN [service_id] VARCHAR(100)");
            db.execSQL("ALTER TABLE Download ADD COLUMN [service_name] VARCHAR(100)");
            updateVersion3(db);
        } else if (oldVersion == 2 && newVersion == 3) {
            updateVersion3(db);

        } else if (oldVersion == 1 && newVersion == 4) {


        } else if (oldVersion == 2 && newVersion == 4) {
            updateVersion3(db);
            updateVersion4(db);

        } else if (oldVersion == 3 && newVersion == 4) {
            updateVersion4(db);

        } else if (oldVersion == 1 && newVersion == 5) {


        } else if (oldVersion == 2 && newVersion == 5) {


        } else if (oldVersion == 3 && newVersion == 5) {
            updateVersion4(db);
            updateVersion5(db);
        } else if (oldVersion == 4 && newVersion == 5) {
            updateVersion5(db);

        } else if (oldVersion == 1 && newVersion == 6) {


        } else if (oldVersion == 2 && newVersion == 6) {


        } else if (oldVersion == 3 && newVersion == 6) {


        } else if (oldVersion == 4 && newVersion == 6) {

        } else if (oldVersion == 5 && newVersion == 6) {


        } else if (oldVersion == 1 && newVersion == 7) {


        } else if (oldVersion == 2 && newVersion == 7) {


        } else if (oldVersion == 3 && newVersion == 7) {


        } else if (oldVersion == 4 && newVersion == 7) {


        } else if (oldVersion == 5 && newVersion == 7) {


        } else if (oldVersion == 6 && newVersion == 7) {

        }

    }


    /**
     * 第三版 更新内容
     * @param db
     */
    private  void updateVersion3(SQLiteDatabase db){
        // im 用户储存表
//        String userSql = "CREATE TABLE IF NOT EXISTS " + IMUSER + "(" + UserInfoBean.IDENTIFIER +" string,"+ UserInfoBean.NICKNAME+" string,"+ UserInfoBean.ISRECENTCONTACT+" string,"+ UserInfoBean.FACEURL+" string"+")";
//        db.execSQL(userSql);
//        // im 免打扰表
//        String noDisturbSql = "CREATE TABLE IF NOT EXISTS " + NODISTURB + "(" + UserInfoBean.IDENTIFIER + " string"+")";
//        db.execSQL(noDisturbSql);
    }

    /**
     * 第四版 更新内容
     * @param db
     */
    private  void updateVersion4(SQLiteDatabase db){
        //课时类型
        db.execSQL("ALTER TABLE Download ADD COLUMN [video_type] VARCHAR(100)");
    }

    /**
     * 第五版 更新内容
     * @param db
     */
    private  void updateVersion5(SQLiteDatabase db){
        //课时类型
        db.execSQL("ALTER TABLE Download ADD COLUMN [is_drag] DEFAULT 0");
    }

}
