/**
 * 顯示日記內容的畫面。
 * 會有增加/同步按紐。
 * 增加按紐會call DiaryEditorActivity來修改
 * 更新則call SynchronizerService來同步內容
 * 
 * @author 張子文
 * 
 */

package com.ntu.iddc.exnote;

import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class DiaryContentViewActivity extends Activity {

	private ViewFlow viewFlow;
	AsyncAdapter adapter;
	Button button;
	String authorId="1", authorName="1", diaryId="1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.async_title);
		setContentView(R.layout.title_layout);
		Bundle bundle = this.getIntent().getExtras();
//		authorId = bundle.getString("authorId");
//		authorName = bundle.getString("authorName");
//		diaryId = bundle.getString("diaryId");		
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		adapter = new AsyncAdapter(this, diaryId, authorId, authorName);
		viewFlow.setAdapter(adapter, adapter.getTodayId());
		
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		adapter.notifyDataSetChanged();
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
}
