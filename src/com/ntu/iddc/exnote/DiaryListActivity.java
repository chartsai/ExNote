/**
 * Main menu.
 * show all diary list 
 */

package com.ntu.iddc.exnote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class DiaryListActivity extends Activity {
	public static final String APP_SP = "applicationSharedPreferences";
	public static final String USER_ID = "userFacebookId";
	public static final String USER_NAME = "userFacebookName";
	public static final int REQUEST_CODE_LOGIN = 11;

	public static SharedPreferences settings;
	public static String userId;
	public static String userName;

	private SQLite dbHelper;
	
	private ActionBar mActionBar;
	private DiaryView dv_diaryView1, dv_diaryView2, dv_diaryView3,
					  dv_diaryView4, dv_diaryView5, dv_diaryView6;
	
	private TextView tv_selectedDiaryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diary_list_layout);
		
		dbHelper = new SQLite(this);

		if (settings == null) {
			settings = getSharedPreferences(APP_SP, MODE_PRIVATE);
		}
		String check = settings.getString(USER_ID, "");// get User pre-used id.

		if (check != "" && check != null) { // get userId succeed.
			userId = check;
			userName = settings.getString(USER_NAME, "No Name");
		} else {
			Intent intent = new Intent(DiaryListActivity.this,
									   LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
		}

		setViews();
		setListeners();
		setDiaries();
	}

	private void setViews() {
		mActionBar = (ActionBar) findViewById(R.id.action_bar);
		mActionBar.setTitle(userName + getResources().getString(R.string.diary_list));
		
		dv_diaryView1 = (DiaryView) findViewById(R.id.diaryView1);
		dv_diaryView2 = (DiaryView) findViewById(R.id.diaryView2);
		dv_diaryView3 = (DiaryView) findViewById(R.id.diaryView3);
		dv_diaryView4 = (DiaryView) findViewById(R.id.diaryView4);
		dv_diaryView5 = (DiaryView) findViewById(R.id.diaryView5);
		dv_diaryView6 = (DiaryView) findViewById(R.id.diaryView6);
		
		tv_selectedDiaryName = (TextView) findViewById(R.id.diaryName_text);
	}

	private void setListeners() {
		// TODO set Listeners
	}

	private void setDiaries() {
		//TODO 判斷幾本日記&日記封面
		
		dv_diaryView6.setVisibility(View.GONE);
		dv_diaryView5.setVisibility(View.GONE);
		dv_diaryView4.setVisibility(View.GONE);
		dv_diaryView3.setVisibility(View.GONE);
		dv_diaryView2.setVisibility(View.GONE);
		dv_diaryView1.setVisibility(View.GONE);
		
		Cursor cursor = dbHelper.getAllDiaryList();
		
		Log.d("TAG", "cursor.getall diary list");
		
		if( cursor.moveToLast() ) {
			switch(cursor.getCount()){
			case 6:
				dv_diaryView6.setDiaryId(cursor.getString(1));
				dv_diaryView6.setDiaryName(cursor.getString(2));
				dv_diaryView6.setImageResource(R.drawable.ic_launcher);
				dv_diaryView6.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
				cursor.moveToPrevious();
			case 5:
				dv_diaryView5.setDiaryId(cursor.getString(1));
				dv_diaryView5.setDiaryName(cursor.getString(2));
				//TODO set image
				dv_diaryView5.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
				cursor.moveToPrevious();
			case 4:
				dv_diaryView4.setDiaryId(cursor.getString(1));
				dv_diaryView4.setDiaryName(cursor.getString(2));
				//TODO set image
				dv_diaryView4.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
				cursor.moveToPrevious();
			case 3:
				dv_diaryView3.setDiaryId(cursor.getString(1));
				dv_diaryView3.setDiaryName(cursor.getString(2));
				//TODO set image
				dv_diaryView3.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
				cursor.moveToPrevious();
			case 2:
				dv_diaryView2.setDiaryId(cursor.getString(1));
				dv_diaryView2.setDiaryName(cursor.getString(2));
				//TODO set image
				dv_diaryView2.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
				cursor.moveToPrevious();
			case 1:
				dv_diaryView1.setDiaryId(cursor.getString(1));
				dv_diaryView1.setDiaryName(cursor.getString(2));
				dv_diaryView1.setImageResource(R.drawable.ic_launcher);
				dv_diaryView1.setVisibility(View.VISIBLE);
				Connection.synchronizeDiaryContent(this, cursor.getString(1));
			case 0:
				//do nothing
			default:
				//do nothing
			}
			
			cursor.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, Menu.FIRST, 1, "Create New Diary");
		menu.add(0, Menu.FIRST + 1, 1, "Logout");
		menu.add(0, Menu.FIRST + 2, 2, "Synchronize Diary");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			// TODO create new diary.
			if(userId.equals("") || userId==null){
				Toast.makeText(DiaryListActivity.this, "Please Login First", Toast.LENGTH_SHORT).show();
			} else {
				LayoutInflater inflater = LayoutInflater.from(this);
				View dialogView = inflater.inflate(R.layout.create_diary_dialog, null);
				final EditText et_newDiaryName = (EditText) dialogView.findViewById(R.id.input);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(DiaryListActivity.this);
				builder.setTitle("New Diary")
					   .setView(dialogView)
					   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO create a new diary 
								String diaryName = et_newDiaryName.getText().toString();
								Date date = new Date();
								DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
								
								mActionBar.setProgressBarVisibility(View.VISIBLE);
								
								//TODO 在本機端創日記本
								dbHelper.creatNewDiary(df.format(date), diaryName, "android icon", userId, "0");
								dbHelper.insertNewCoWorker(df.format(date), userId, userName, userId);
								
								//在GAE上創日記本
								if( Connection.createDiary(df.format(date), diaryName, "", "0") ){
									Toast.makeText(getApplicationContext(), "Create Diary on Server Succeed!", Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getApplicationContext(), "Create Diary on Server Failure...", Toast.LENGTH_SHORT).show();
								}
								
								mActionBar.setProgressBarVisibility(View.GONE);
								
								setDiaries(); 
							}
						})
					   .setNegativeButton("Cancel", null)
					   .show();
			}
			break;
		case Menu.FIRST + 1:
			settings.edit().putString(USER_ID, "")
						   .putString(USER_NAME, "")
						   .commit();
			userId = "";
			userName = "";
			Intent intent = new Intent(DiaryListActivity.this,
					LoginActivity.class);
			startActivity(intent);
			break;
		case Menu.FIRST + 2:
			//TODO synchronized diary content
			Cursor cursor = dbHelper.getAllDiaryList();
			if ( cursor.moveToFirst()) {
				do{
					Connection.synchronizeDiaryContent(DiaryListActivity.this,
													   cursor.getString(1) );
				} while(cursor.moveToNext());
			}
			cursor.close();
		default:
			// Do nothing
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//onClickListener for DiaryView
	public void OnDiaryClickListener(View v){
		DiaryView dv = (DiaryView) v;
		tv_selectedDiaryName.setText(dv.getDiaryId());
		Intent intent = new Intent();
     	intent.setClass(DiaryListActivity.this, DiaryContentViewActivity.class);
		Bundle bundle = new Bundle();
     	bundle.putString("authorId", userId);
     	bundle.putString("diaryId", dv.getDiaryId().toString());
     	bundle.putString("authorName", userName);

     	intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_LOGIN) {
			switch (resultCode) {
			case RESULT_OK:
				mActionBar.setTitle(userName + getResources().getString(R.string.diary_list));
				Toast.makeText(DiaryListActivity.this, R.string.fb_login_succeed,
						Toast.LENGTH_LONG).show();
				setDiaries();
				break;
			case RESULT_CANCELED:
				Toast.makeText(DiaryListActivity.this, R.string.fb_login_failure,
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}
}
