/**
 * 顯示?��??�容????��?
 * ???�??/??��?????
 * �???????all DiaryEditorActivity�?��??
 * ?��???all SynchronizerService�??步�?�?
 * 
 * @author 張�???
 * 
 */

package com.ntu.iddc.exnote;

import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class DiaryContentViewActivity extends Activity {

	private ViewFlow viewFlow;
	AsyncAdapter adapter;
	Button button;
	String authorId, authorName, diaryId;
	private SQLite dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.async_title);
		setContentView(R.layout.title_layout);
		Bundle bundle = this.getIntent().getExtras();
		authorId = bundle.getString("authorId");
		authorName = bundle.getString("authorName");
		diaryId = bundle.getString("diaryId");		

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		adapter = new AsyncAdapter(this, diaryId, authorId, authorName);
		viewFlow.setAdapter(adapter, adapter.getTodayId());
		
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		adapter.notifyDataSetChanged();
		
		dbHelper = new SQLite(this);
    }

    
    /* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, Menu.FIRST, 1, "Involve Friend!");
		menu.add(0, Menu.FIRST + 1, 1, "Synchronized Diary");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent(DiaryContentViewActivity.this, InvolveFriendActivity.class);
			intent.putExtra("diaryId", diaryId);
			startActivity(intent);
			break;
		case Menu.FIRST + 1:
			Connection.synchronizeDiaryContent(dbHelper, diaryId);
			break;
		default:
			// Do nothing
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
