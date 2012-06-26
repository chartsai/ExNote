package com.ntu.iddc.exnote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class InvolveFriendActivity extends ListActivity{

	private ListView lv_listview;
	private Button bt_addFriends;
	
	private String diaryId;
	private String diaryName;
	
	private ArrayAdapter<String> mAdapter;
	private List<String> friendList;
	private List<String> idList;
	private List<String> selectedFriendId;
	private List<String> selectedFriendName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("朋友名單");
		setContentView(R.layout.involved_friend_layout);
		
		Intent intent = getIntent();
		diaryId = intent.getStringExtra("diaryId");
		diaryName = intent.getStringExtra("diaryName");
		
		initial();
		setListeners();
	}

	private void initial(){
		
		lv_listview   = getListView();
		bt_addFriends = (Button) findViewById (R.id.add_friends_Button);
		
		friendList = new ArrayList<String>();
		idList	   = new ArrayList<String>();
		selectedFriendId = new ArrayList<String>();
		selectedFriendName = new ArrayList<String>();
		
		Log.d("TAG", DiaryListActivity.friendJSONString);
		try {
			JSONObject jo = new JSONObject(DiaryListActivity.friendJSONString);
			JSONArray ja = jo.getJSONArray("data");
			for(int i=0; i<ja.length(); i++){
				JSONObject jo2 = ja.getJSONObject(i);
				friendList.add( jo2.getString("name") );
				idList    .add( jo2.getString("id")   );
			}
		} catch (JSONException e) {	
			Log.d("TAG", "friendJSONString Error");
		}
		
		lv_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, friendList);
		
		setListAdapter(mAdapter);
	}
	
	
	
	private void setListeners(){
		
		lv_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if( ! selectedFriendId.remove(idList.get(arg2)) ){
					selectedFriendId.add(idList.get(arg2));
				}
				if( ! selectedFriendName.remove(friendList.get(arg2)) ){
					selectedFriendName.add(friendList.get(arg2));
				}
			}
		});
		
		bt_addFriends.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SQLite dbHelper = new SQLite(InvolveFriendActivity.this);
				for(int i=0;i<selectedFriendId.size();i++){
					Connection.addCoWorker(diaryId, diaryName, selectedFriendId.get(i), 
										   selectedFriendName.get(i), DiaryListActivity.userId, DiaryListActivity.userName, "");
					dbHelper.insertNewCoWorker(diaryId, selectedFriendId.get(i), selectedFriendName.get(i), DiaryListActivity.userId);
				}
				dbHelper.close();
				finish();
			}
		});
	}
}
