package com.sk.scdoenloader;

import com.sk.scdoenloader.R;
import android.os.AsyncTask;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import android.util.Log;
import android.widget.CompoundButton.*;
import android.widget.*;

public class CustomAdapter extends ArrayAdapter
{
	List<String> modelItems;
	boolean[] checkBoxState;
	Context context;
	int layoutResourceId;	
	ViewHolder viewholder;

	public CustomAdapter(Context context, int layoutResourceId, List<String> modelItems)
	{
		super(context, layoutResourceId, modelItems);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.modelItems = modelItems;
	}

	private class ViewHolder
	{
		CheckBox checkbox;
		TextView texview;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.row, parent, false); 
		viewholder = new ViewHolder();
		viewholder.checkbox = (CheckBox) convertView.findViewById(R.id.cb);
		viewholder.texview = (TextView) convertView.findViewById(R.id.tvv);
		viewholder.texview.setText(modelItems.get(position));
		viewholder.checkbox.setChecked(true);
		
		viewholder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					// TODO: Implement this method
					viewholder.checkbox.setChecked(true);
					boolean state = p1.isChecked();
					boolean s = p2;
					Log.d("s",""+s);
					Log.d("state",""+state);
					if(state==false)
					{
						
						Toast.makeText(context,":P",Toast.LENGTH_SHORT).show();
					}
				}		
		});
		return convertView;
	}
}
