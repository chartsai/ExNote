/**
 * 新增日記內容的Editor。
 * 
 * @author 張子文
 * 
 */

package com.ntu.iddc.exnote;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class DiaryEditorActivity extends Activity {
	private Button button_save;
	private SQLite dbHelper;
	private EditText titleText,articleText; 
	String diaryId, authorId, authorName, date, time, title, article;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("NewDiary");
		setContentView(R.layout.diary_editor);
		button_save = (Button) findViewById(R.id.saveButton);
		titleText = (EditText) findViewById(R.id.titleText);
		articleText = (EditText) findViewById(R.id.articleText);
		button_save.setOnClickListener(saveDiary);
		dbHelper = new SQLite(this);
		Bundle bundle = this.getIntent().getExtras();
		date = bundle.getString("date");
		authorId = bundle.getString("authorId");
		authorName = bundle.getString("authorName");
		diaryId = bundle.getString("diaryId");
		Log.e("aaaaaa", date);
	}
	
	private Button.OnClickListener saveDiary = new Button.OnClickListener()
	{
	     public void onClick(View v)
	     	{
	    	 
	    	 Date nowDate = new Date(); 
	    	 DateFormat shortFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSSSS");
	    	 
	    	 
	    	 
	    	 title = titleText.getText().toString();
	    	 article = articleText.getText().toString();
	    	 time = shortFormat.format(nowDate);
	    	 dbHelper.insertNewDiary(diaryId, authorId, authorName, date, time, title+" by "+authorName, article);
	    	 // Close this Activity
	    	 DiaryEditorActivity.this.setResult(RESULT_OK);
	    	 DiaryEditorActivity.this.finish();              
	     	}
	};

}
