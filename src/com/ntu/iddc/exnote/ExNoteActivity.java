package com.ntu.iddc.exnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ExNoteActivity extends Activity {
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
        
        setViews();
        setListeners();
        setFacebook();
    }
    
    private void setViews(){
    	ib_loginButton = (ImageButton)findViewById(R.id.loginButton);
    	tv_userId = (TextView)findViewById(R.id.userId);
    	tv_userName = (TextView)findViewById(R.id.userName);
    }
    
    private void setListeners(){
    	ib_loginButton.setOnClickListener(new LoginListener());
    }
    
    private class LoginListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			ExNoteActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					loginFacebook();
					ib_loginButton.setImageResource(R.drawable.logout_button);
					ib_loginButton.setOnClickListener(new LogoutListener());
				}
			});
		}
    }
    
    private class LogoutListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			ExNoteActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					loginFacebook();
					ib_loginButton.setImageResource(R.drawable.logout_button);
					ib_loginButton.setOnClickListener(new LogoutListener());
					tv_userId.setText("");
					tv_userName.setText("");
				}
			});
		}
    }
    
    private void setFacebook(){
    	mFacebook = new Facebook(FACEBOOK_API_KEY);
        mAsync = new AsyncFacebookRunner(mFacebook);
    }
    
    private void loginFacebook(){
    	mFacebook.authorize(this, new DialogListener(){
			@Override
			public void onComplete(Bundle values) {
				getLoginUserInformation();
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
    
    private void getLoginUserInformation(){//call this function to
    	mAsync.request("me", new RequestListener(){
			@Override
			public void onComplete(final String response, Object state) {
				try {
					JSONObject jsonobject;
					jsonobject = new JSONObject(response);
					
					//login後要做的事，這邊用一個簡單範例
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
    
    //拿到資料後要幹麻的範例
    private void example_showUserNameAndId(){
    	ExNoteActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					String id = userData.getString("id");//get facebook id
					String name = userData.getString("name");//get user name
					tv_userName.setText(id);
					tv_userId.setText(name);
				} catch (JSONException e) {}
			}
		});
    }
}