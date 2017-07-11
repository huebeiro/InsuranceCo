package com.huebeiro.insuranceco.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomAdapter extends ArrayAdapter<String> {

	protected int selectedIndex = -1;
	protected int textViewResourceId;
	protected int resourceId;
	protected Context context;
	protected final int selectedColor = Color.rgb(33, 149, 150); //Background color for select lines
	protected final int evenColor = Color.argb(10, 255, 255, 255); //Background color for even lines
	
	
	public CustomAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		this.context = context;
		this.resourceId = resource;
		this.textViewResourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(resourceId, parent, false);
	    if(position % 2 == 1){
	    	rowView.findViewById(textViewResourceId).setBackgroundColor(evenColor);
	    }
	    
	    if(position == selectedIndex){
	    	rowView.findViewById(textViewResourceId).setBackgroundColor(selectedColor);
	    }
	    return rowView;
	}
		
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}
