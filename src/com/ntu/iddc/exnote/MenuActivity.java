/**
 * 進來第一個畫面。
 * 處理Facebook登入。
 * 
 * @author 顏培峻
 * @author Facebook登入機制：蔡昇哲
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

public class MenuActivity extends Activity {
	private static final String FACEBOOK_API_KEY = "313743562047452";
	
	private ImageButton ib_loginButton;
	private TextView tv_userName;
	private TextView tv_userId;
	
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsync;
	private JSONObject userData;
	
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
    	mFacebook = new Facebook(FACEBOOK_API_KEY);
        mAsync = new AsyncFacebookRunner(mFacebook);
    }
    
    private void setViews(){
    	ib_loginButton = (ImageButton)findViewById(R.id.loginButton);
    	tv_userId = (TextView)findViewById(R.id.userId);
    	tv_userName = (TextView)findViewById(R.id.userName);
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
		mFacebook.authorize(MenuActivity.this, new DialogListener(){
			@Override
			public void onComplete(Bundle values) {
				ib_loginButton.setImageResource(R.drawable.logout_button);
				ib_loginButton.setOnClickListener(logoutListener);
				getUserData();
			}

			@Override
			public void onFacebookError(FacebookError e) {
				// TODO show some error information to user
			}

			@Override
			public void onError(DialogError e) {
				// TODO show some error information to user
			}

			@Override
			public void onCancel() {
				// TODO show some error information to user
			} 
        });
	}
    
    private void getUserData() {
		mAsync.request("me", new RequestListener(){
			@Override
			public void onComplete(String response, Object state) {
				try {
					userData = new JSONObject(response);//��o��ơI

					//�o���ƫ�n�����ơA�o��Τ@��²��d�ҡAcall�@�ӧ���TextView��function(��138��)
					example_showUserNameAndId();

				} catch (JSONException e) {
					//TODO Tell user facebook feedback error.
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
    
    private OnClickListener logoutListener = new OnClickListener(){//logout�Ϊ�Listener
		@Override
		public void onClick(View v) {
			try {
				mFacebook.logout(MenuActivity.this);
			} catch (Exception e) {
				//TODO tell user logout failure
			}
			ib_loginButton.setImageResource(R.drawable.login_button);
			ib_loginButton.setOnClickListener(loginListener);
			userData = null;
			tv_userId.setText("Click Button to re-login");
			tv_userName.setText("Logout Succeed!");
		}
    };
        
    //�����ƫ�n�F�ª��d��
    private void example_showUserNameAndId(){
    	//��runOnUiThread�קK�Dmain Thread�h�ʨ�UI��crash
    	MenuActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					String id = userData.getString("id");// get facebook id
					String name = userData.getString("name");// get user name
					tv_userId.setText(id);
					tv_userName.setText(name);
				} catch (JSONException e) {
				}
			}
		});
    }
    
    //�B�zintent��Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
}