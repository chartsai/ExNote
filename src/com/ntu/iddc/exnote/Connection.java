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

public class Connection {
	//在gae上創日記本
	public static boolean createDiary(String diaryId){
		
		//TODO load data from database by diaryId 
		
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/create-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
		nvps.add(new BasicNameValuePair("diaryName", ""));
		nvps.add(new BasicNameValuePair("diaryIcon", ""));
		nvps.add(new BasicNameValuePair("ownerId", ""));
		nvps.add(new BasicNameValuePair("ownerName", ""));
		nvps.add(new BasicNameValuePair("updateBy", ""));
		nvps.add(new BasicNameValuePair("permission", ""));
		
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
	
	//邀請朋友加入
		public static boolean addCoWorker(String diaryId, String coworkerId, String permission){
			HttpClient hc = new DefaultHttpClient();
	    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/add-cowoker");
	    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
	    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
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
	public static boolean synchronizeDiaryContent(String diaryId){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/synchronize-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();

    	JSONArray jsonarray = new JSONArray();
    	//TODO for loop to put new content <--
    	//for(xxxxx){
	    	try {
	    		JSONObject jsonobject = new JSONObject();
				jsonobject.put("author_id", "");
				jsonobject.put("author_name", "");
		    	jsonobject.put("datetime", "");
		    	jsonobject.put("title", "");
		    	jsonobject.put("article", "");
		    	jsonarray.put(jsonobject.toString());
			} catch (JSONException e) {}
    	//}
    	// -->

    	nvps.add(new BasicNameValuePair("diaryId", diaryId));
		nvps.add(new BasicNameValuePair("update_time", ""));
		nvps.add(new BasicNameValuePair("uploadData", jsonarray.toString()));
		
		boolean result = false;		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());
			
			JSONObject jo = new JSONObject(responseString);
			String update_time = jo.getString("update_datetime");
			//TODO write time to db
			JSONArray ja = jo.getJSONArray("new_content");
			//TODO write content to db
			for(int i=0; i<ja.length(); i++){
				JSONObject j = new JSONObject(ja.getString(i));
				
				//TODO write diary content show below to db 
				String di = j.getString("diaryId");
				String ai = j.getString("authorId");
				String at = j.getString("dateTime");
				String tt = j.getString("title");
				String ac = j.getString("article");
				
				/* oringnal data name
				"diaryId"  : contents_query.diary_id,
                "authorId" : contents_query.author_id,
                "dateTime" : create_datetime_int_string(contents_query.datetime),
                "title"    : contents_query.title,
                "article"  : contents_query.article
				*/
			}
			
			result = true;
			
		} catch (Exception e) {} 
		return result;
	}
	
	//新增被邀請的日記本(未完)
	public static String getNewDiary(){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/get-new-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", ""));
		nvps.add(new BasicNameValuePair("userId", ""));
		nvps.add(new BasicNameValuePair("ownerId", ""));
		nvps.add(new BasicNameValuePair("permission", ""));
		
		boolean result = false;		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());
			if(responseString.equals("succeed")){
				result = true;
			}
		} catch (Exception e) {} 
		
		return null;
	}
}
