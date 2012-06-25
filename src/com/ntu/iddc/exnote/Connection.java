package com.ntu.iddc.exnote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

public class Connection {
	//在gae上創日記本
	public static boolean createDiary(String diaryId, String diaryName, String icon, String updateTime){
		
		//TODO load data from database by diaryId 
		
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/create-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
		nvps.add(new BasicNameValuePair("diaryName", diaryName));
		nvps.add(new BasicNameValuePair("diaryIcon", icon));
		nvps.add(new BasicNameValuePair("ownerId", DiaryListActivity.userId));
		nvps.add(new BasicNameValuePair("ownerName", DiaryListActivity.userName));
		nvps.add(new BasicNameValuePair("updateBy", updateTime));
		nvps.add(new BasicNameValuePair("permission", ""));
		
		boolean result = false;
		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			Log.d("TAG", responseString);
			
			if(responseString.equals("succeed")){
				result = true;
			}
			
		} catch (Exception e) { Log.d("TAG", "some exception occur");} 
		return result;
	}
	
	//邀請朋友加入
	public static boolean addCoWorker(String diaryId, String diaryName, String coworkerId, String permission){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/add-cowoker");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
    	nvps.add(new BasicNameValuePair("diaryName", diaryName));
		nvps.add(new BasicNameValuePair("userId", coworkerId));
		nvps.add(new BasicNameValuePair("ownerId", ""));
		nvps.add(new BasicNameValuePair("permission", permission));
		
		boolean result = false;		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());
			if(responseString.equals("succeed")){
				result = true;
			}
		} catch (Exception e) {} 
		return result;
	}
		
	//同步日記內容
	public static boolean synchronizeDiaryContent(SQLite dbHelper, String diaryId){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/synchronize-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();

    	String lastUpdateTime = dbHelper.getLastUpdateTimeByDiaryId(diaryId);
    	
    	Log.d("TAG", "WHOWHOWHO" + lastUpdateTime);
    	
    	Cursor cursor = dbHelper.getDiaryContentByLastUpdateTimeAndDiaryId(lastUpdateTime, diaryId);
    	JSONArray jsonarray = new JSONArray();
    	//TODO for loop to put
    	
    	if( cursor.moveToFirst() ){
    		do{
		    	try {
		    		JSONObject jsonobject = new JSONObject();
		    		Log.e("EEEEEEE", cursor.getString(1));
					jsonobject.put("authorId", cursor.getString(2));
					jsonobject.put("authorName", cursor.getString(3));
					jsonobject.put("datetimeString", cursor.getString(4));
			    	jsonobject.put("datetime", cursor.getString(5) );
			    	jsonobject.put("title", cursor.getString(6));
			    	jsonobject.put("article", cursor.getString(7));
			    	jsonarray.put(jsonobject.toString());
				} catch (JSONException e) {}
    		} while( cursor.moveToNext() );
    	}
    	
    	cursor.close();
    	
    	//}
    	// -->

    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
		nvps.add(new BasicNameValuePair("updateTime", lastUpdateTime));
		nvps.add(new BasicNameValuePair("uploadData", jsonarray.toString()));
		
		Log.d("TAG", jsonarray.toString());
		
		boolean result = false;
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			
			//以上傳資料
			//以下收資料
			
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			Log.d("TAGGGGGG", "response String is:" + responseString);
			
			JSONObject jo = new JSONObject(responseString);
			String update_time = jo.getString("update_datetime");
			//TODO write time to db
			Log.d("TAG", "update_time = " + update_time);
			dbHelper.setUpdateTimeByDiaryIdAndTime(diaryId, update_time);
			
			JSONArray ja = jo.getJSONArray("new_content");
			//TODO write content to db
			for(int i=0; i<ja.length(); i++){
				JSONObject j = new JSONObject(ja.getString(i));
				
				//TODO write diary content show below to db 
				String di = j.getString("diary_id");
				String ai = j.getString("author_id");
				String an = j.getString("author_name");
				String ds = j.getString("datetime_string");
				String at = j.getString("dateTime");
				String tt = j.getString("title");
				String ac = j.getString("article");
				
				dbHelper.insertNewDiary(di, ai, an, ds, at, tt, ac);
				
				/* oringnal data name
				"diaryId"  : contents_query.diary_id,
                "authorId" : contents_query.author_id,
                "authorName" : contents_query.author_name,
                "datetimeString" : contents_query.author_name
                "datetime" : create_datetime_int_string(contents_query.datetime),
                "title"    : contents_query.title,
                "article"  : contents_query.article
				*/
			}
			
			result = true;
			
		} catch (Exception e) {}
    	
		return result;
	}
	
	//確認有無新的日記本，有就載下來(未完)
	public static boolean checkNewDiary(SQLite dbHelper){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/check-new-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("userId", DiaryListActivity.userId));
		//nvps.add(new BasicNameValuePair("ownerId", ""));
		//nvps.add(new BasicNameValuePair("permission", ""));
		
		Cursor cursor = dbHelper.getAllDiaryList();

		JSONArray diariesArray = new JSONArray();
		if( cursor.moveToFirst() ){
			do {
				diariesArray.put(cursor.getString(1));
			} while( cursor.moveToNext() );
		}

		nvps.add(new BasicNameValuePair("userDiaries", diariesArray.toString()));
		
		boolean result = false;
		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			JSONArray diaries_array = new JSONArray(responseString);
			for(int i=0; i<diaries_array.length(); i++ ){
				result = true;
				JSONObject json = diaries_array.getJSONObject(i);
				String diaryId = json.getString("diary_id");
				String diaryName = json.getString("diary_name");
				String ownerId = json.getString("owner_id");
				String ownerName = json.getString("owner_name");
				String updateTime = json.getString("update_time");
				String permisson = json.getString("permission");
				
				//TODO getAllDiaryContent(by diaryId)
			}
		} catch (Exception e) {} 
		
		return result;
	}
	
	public static boolean getNewDiary(SQLite dbHelper, String diaryId){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/get-new-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		
    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
		
		boolean result = false;
		
		try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			//Store All Diary In DataBase
			
			result = true;
		} catch (Exception e) {} 
		
		return result;
	}
}
