/**
 * 處理GAE連接問題。
 * 同步日記本，日記內容，邀請朋友等，被邀請等。
 * 
 * @author 蔡昇哲
 * 
 */

package com.ntu.iddc.exnote;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SynchronizerService extends Service {
	// Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    
    private String userId;
    
    private int value = 0;
    
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
    	SynchronizerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SynchronizerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void addValue() {
    	this.value = this.value++;
    }
    
    /** method for clients */
    public int getValue() {
    	return this.value;
    }

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	public void setFacebookUserId(String id){
		this.userId = id;
	}
	
	public void clearFacebookUserId(String id){
		this.userId = null;
	}
 
}
