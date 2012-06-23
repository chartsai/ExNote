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

public class Connection {
	//在gae上創日記本
	public static boolean createDiary(){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/create-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", ""));
		nvps.add(new BasicNameValuePair("diaryName", ""));
		nvps.add(new BasicNameValuePair("diaryIcon", ""));
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
	public static boolean registDiary(){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/regiest-diary");
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
		return result;
	}
	
	//增加日記條目
	public static boolean addContentToDiary(){
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/add-content");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", ""));
		nvps.add(new BasicNameValuePair("author_id", ""));
		nvps.add(new BasicNameValuePair("title", ""));
		nvps.add(new BasicNameValuePair("article", ""));
		
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
	
	//下載新日記內容(用JSONArray回傳，Array內的內容是JSONObject)
	public static JSONArray loadDiary(String diaryId){
		
		//TODO 拿db 時間
		
		HttpClient hc = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://exchangdiary.appspot.com/load-diary");
    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("diaryId", ""));
		nvps.add(new BasicNameValuePair("lastedUpdate", ""));
		
		JSONArray result = null;		
    	try {
    		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());
			result = new JSONArray(responseString);
		} catch (Exception e) {} 
		return result;
	}
}
