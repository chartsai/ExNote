package com.ntu.iddc.exnote;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DiaryView extends ImageView {

	private String diaryName;
	
	public DiaryView(Context context) {
		super(context);
	}

	public DiaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DiaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	 /**
     * Set the text to show in DiaryView
     * @param set text to bar
     */
    public synchronized void setText(String text){
    	this.diaryName = text +"";
    	drawableStateChanged(); 
    }
    
    /**
     * Get the text in DiaryView
     * @return text in the bar
     */
    public String getText(){
    	if(this.diaryName == null){
    		return null;
    	}
    	return this.diaryName+"";
    }
}
