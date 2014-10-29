package com.intel.hid_kb_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter{

	private class RecordHolder {
		ImageView ItemIcon;
		TextView ItemTitle;
		TextView ItemText;
		ImageView ItemRemove;
	}
	
	private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private RecordHolder holder;
    private RecordDatabase db;
    private Cursor mCursor;
    
    public RecordAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource,
            String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView != null) {
            holder = (RecordHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item, null);
            holder = new RecordHolder();
            holder.ItemIcon = (ImageView)convertView.findViewById(valueViewID[0]);
            holder.ItemTitle = (TextView)convertView.findViewById(valueViewID[1]);
            holder.ItemText = (TextView)convertView.findViewById(valueViewID[2]);
            holder.ItemRemove = (ImageView)convertView.findViewById(valueViewID[3]);
            convertView.setTag(holder);
        }
        
        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String Title = (String) appInfo.get(keyString[1]);
            String Text = (String) appInfo.get(keyString[2]);
            int IconId = (Integer)appInfo.get(keyString[0]);
            int RemoveId = (Integer)appInfo.get(keyString[3]);
            
            db = (RecordDatabase) appInfo.get("Database");
            
            holder.ItemIcon.setImageDrawable(holder.ItemIcon.getResources().getDrawable(IconId));
            holder.ItemTitle.setText(Title);
            holder.ItemText.setText(Text);
            holder.ItemRemove.setImageDrawable(holder.ItemRemove.getResources().getDrawable(RemoveId));
            holder.ItemRemove.setOnClickListener( (OnClickListener) new RecordImageListener(position));
            
        }
		return convertView;
	}
	
	public void RemoveItem(int pos) {
		
		HashMap<String, Object> data = mAppList.get(pos);
	    String TableName= (String) data.get("TableName");
	    int CursorID = Integer.parseInt(data.get("CursorID").toString());
	    mCursor = db.select(TableName);
	    mCursor.moveToPosition(CursorID);
	    int RecordID = mCursor.getInt(0);
	    
	    //Log.i("TableName", "TableName:"+TableName+",ID:"+RecordID+","+db.getDatabaseName());
		db.delete(TableName, RecordID);
		mAppList.remove(pos);
		this.notifyDataSetChanged();
	}
	
	class RecordImageListener implements OnClickListener {
		private int position;

		public RecordImageListener(int pos) {
			// TODO Auto-generated constructor stub
			position = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int vid = v.getId();
			if (vid == holder.ItemRemove.getId()) {
				RemoveItem(position);
			}
		}
	}

}
