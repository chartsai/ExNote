package com.ntu.iddc.exnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

public class InvolveFriendActivity extends ListActivity{

	private ListView  listview;
	private ArrayAdapter<String> mAdapter;
	private List<String> friendList;
	private List<String> idList;
	private AsyncFacebookRunner mRunner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.involved_friend_layout);
		
		initial();
	}

	private void initial(){
		
		friendList = new ArrayList<String>();
		idList	   = new ArrayList<String>();
		
		mRunner = new AsyncFacebookRunner(LoginActivity.mFacebook);
		
		mRunner.request("friends", new RequestListener() {
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				Toast.makeText(getApplicationContext(), "FaceBook Error", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
				Toast.makeText(getApplicationContext(), "FaceBook Error", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				Toast.makeText(getApplicationContext(), "FaceBook Error", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Toast.makeText(getApplicationContext(), "FaceBook Error", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(String response, Object state) {
				try {
					JSONObject jo = new JSONObject(response);
					JSONArray ja = jo.getJSONArray("data");
					for(int i=0; i<ja.length(); i++){
						JSONObject jo2 = ja.getJSONObject(i);
						friendList.add( jo2.getString("name") );
						idList    .add( jo2.getString("id")   );
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "FaceBook Error", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		listview = getListView();
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, friendList);
		
		setListAdapter(mAdapter);
	}
}
