package com.ntu.iddc.exnote;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class InvolveFriendActivity extends ListActivity{

	private ListView  listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listview = new ListView(this);
		setContentView(listview);
		
		
	}

}
