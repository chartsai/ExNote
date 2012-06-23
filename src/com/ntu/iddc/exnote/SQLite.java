package com.ntu.iddc.exnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SQLite extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "exDiary.db";	//資料庫名稱
	private static final int DATABASE_VERSION = 1;	//資料庫版本
 
	private SQLiteDatabase db;
 
	public SQLite(Context context) {	//建構子
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE =
		    "create table DiaryContent ("
		        + "_ID INTEGER PRIMARY KEY,"
		        + "DiaryId TEXT,"
		        + "AuthorId TEXT,"
		        + "AuthorName TEXT,"
		        + "Date TEXT,"
		        + "Time TEXT,"
		        + "Title TEXT,"
		        + "Article TEXT"
		    + ");";
		//建立config資料表，詳情請參考SQL語法
		db.execSQL(DATABASE_CREATE_TABLE);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		db.execSQL("DROP TABLE IF EXISTS DiaryContent");	//刪除舊有的資料表
		onCreate(db);
	}
	
	public Cursor getAll() {
	    return db.rawQuery("SELECT * FROM DiaryContent", null);
	}
	
	public Cursor getDiaryByDate(String date,String diaryId){
	 Cursor cursor=db.rawQuery( 
		     "SELECT * FROM DiaryContent WHERE date=? AND diaryId=?", new String[]{date,diaryId});
	return cursor;
	}
//新增一筆記錄，成功回傳rowID，失敗回傳-1
	public void InsertNewDiary(String diaryId, String authorId, String authorName, String date, String time, String title, String article) {

	db.execSQL("INSERT INTO DiaryContent(DiaryId,AuthorId,authorName,Date,Time,Title,Article) values(?,?,?,?,?,?,?)",
			new Object[]{diaryId,authorId,authorName,date,time,title,article});
		//		ContentValues args = new ContentValues();
//		args.put("DiaryId", diaryId);
//		args.put("AthorId", authorId);
//		args.put("Date", date);
//		args.put("Time", time);
//		args.put("Tile", title);
//		args.put("Article", article);
//		return db.insert("DiaryContent", null, args);
    }
	
}