package com.ntu.iddc.exnote;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

class CustomListAdapter extends ArrayAdapter<String> {
Context mContext;
private LayoutInflater mInflater;
	
    public CustomListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
    
        String string = getItem(position);
        String a = null;
        String b = null;
        try {
			JSONObject json = new JSONObject(string);
			a = json.getString("title");
			b = json.getString("article");
		} catch (JSONException e) {}
        
        ((TextView)convertView.findViewById(R.id.title)).setText(a);
        ((TextView)convertView.findViewById(R.id.article)).setText(b);
        // Resets the toolbar to be closed
        View toolbar = convertView.findViewById(R.id.toolbar);
        ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
        toolbar.setVisibility(View.GONE);
    

        return convertView;
    }
}