/**
 * 進來第一個畫面。
 * 處理Facebook登入。
 * 
 * @author 蔡昇哲
 * 
 */

package com.ntu.iddc.exnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LoginActivity extends Activity {
	private static final String FACEBOOK_API_KEY = "313743562047452";
		
	private Button ib_loginButton;
	
	public static Facebook mFacebook = new Facebook(FACEBOOK_API_KEY);;
	private AsyncFacebookRunner mAsync;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setFacebook();
        setViews();
        setListeners();
    }
    
    private void setFacebook(){
//    	mFacebook = new Facebook(FACEBOOK_API_KEY);
        mAsync = new AsyncFacebookRunner(mFacebook);
    }
    
    private void setViews(){
    	ib_loginButton = (Button)findViewById(R.id.loginButton);
    }
    
    private void setListeners(){
    	ib_loginButton.setOnClickListener(loginListener);
    }
    
    private OnClickListener loginListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			loginFacebook();
		}
    };
    
    private void loginFacebook() {
		mFacebook.authorize(LoginActivity.this,new String[]{},Facebook.FORCE_DIALOG_AUTH,new DialogListener(){
			@Override
			public void onComplete(Bundle values) {
				getFriendJSONDiaryList();
				getUserIdAndShowDiaryList();
			}

			@Override
			public void onFacebookError(FacebookError e) {
				Toast.makeText(LoginActivity.this, "facebook login is error now", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(DialogError e) {
				// TODO show some error information to user
				Toast.makeText(LoginActivity.this, "FB Login Error", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				// TODO show some error information to user
				Toast.makeText(LoginActivity.this, "FB Login Be Cancel", Toast.LENGTH_LONG).show();
			} 
        });
	}
    
    private void getFriendJSONDiaryList() {
    	mAsync.request("me/friends", new RequestListener(){
			@Override
			public void onComplete(String response, Object state) {
				DiaryListActivity.friendJSONString = response;
				DiaryListActivity.settings.edit()
					.putString(DiaryListActivity.FRIEND_JSON, response).commit();
			}
			@Override
			public void onIOException(IOException e, Object state) {
			}
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
			}
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
			}
			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
    }
    
    private void getUserIdAndShowDiaryList() {
		mAsync.request("me", new RequestListener(){
			@Override
			public void onComplete(String response, Object state) {
				try {
					JSONObject json = new JSONObject(response);//將FB的user資料存起來					
					DiaryListActivity.userId = json.getString("id");
					DiaryListActivity.userName = json.getString("username");
					DiaryListActivity.settings.edit()
										.putString(DiaryListActivity.USER_ID, DiaryListActivity.userId)
										.putString(DiaryListActivity.USER_NAME, DiaryListActivity.userName)
										.commit();
					
					setResult(RESULT_OK);
					finish();
										
				} catch (JSONException e) {
					Toast.makeText(LoginActivity.this, "FB 回傳資料異常", Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onIOException(IOException e, Object state) {
				//TODO show some error information to user
			}
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				//TODO show some error information to user
			}
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				//TODO show some error information to user
			}
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				//TODO show some error information to user
			}
		});
	}
            
    //處理intent的Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
}