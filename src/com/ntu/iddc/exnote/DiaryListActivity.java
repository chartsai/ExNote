/**
 * 第二個畫面，(FB登入後第一個畫面)
 * 顯示所有日記的畫面(日記架)，以及增刪日記，Help等
 * 
 * @author 顏培峻
 * 
 */

package com.ntu.iddc.exnote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryListActivity extends Activity {

	private TextView tv;
	
	public static final String APP_SP = "applicationSharedPreferences";
	public static final String USER_ID = "userFacebookId";
	
	public static final int REQUEST_CODE_LOGIN = 11; 
	
	public static SharedPreferences settings;
	public static String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Now in DiaryListActivity");
		tv = new TextView(this);
		setContentView(tv);

		settings = getSharedPreferences(APP_SP, MODE_PRIVATE);
		String check = settings.getString(USER_ID, "");//get User pre-used id.
		
		if( check != "" && check != null) { //get userId succeed.
			Intent intent = new Intent(DiaryListActivity.this, LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
		} else {
			userId = check;
		}
		 
		setViews();
		setListeners();
	}
	
	private void setViews(){
		//TODO set Views
	}
	
	private void setListeners(){
		//TODO set Listeners
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, Menu.FIRST  , 1, "New Diary");
		menu.add(0, Menu.FIRST+1, 1, "Logout");
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case Menu.FIRST:
			//TODO create new diary.
			break;
		case Menu.FIRST+1:
			settings.edit().putString(USER_ID, "")
						   .commit();
			userId = "";
			Intent intent = new Intent(DiaryListActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
		default:
			//Do nothing
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_LOGIN){
			switch(resultCode){
			case RESULT_OK:
				Toast.makeText(DiaryListActivity.this, "登入成功！", Toast.LENGTH_LONG).show();
				break;
			case RESULT_CANCELED:
				Toast.makeText(DiaryListActivity.this, "尚未登入…", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}

}
