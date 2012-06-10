/**
 * 處理GAE連接問題。
 * 同步日記本，日記內容，邀請朋友等，被邀請等。
 * 
 * @author 蔡昇哲
 * 
 */

package com.ntu.iddc.exnote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SynchronizerService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
