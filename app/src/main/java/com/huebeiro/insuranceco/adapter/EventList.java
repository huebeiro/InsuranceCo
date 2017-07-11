package com.huebeiro.insuranceco.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huebeiro.insuranceco.R;
import com.huebeiro.insuranceco.model.Car;
import com.huebeiro.insuranceco.model.Event;

public class EventList extends CustomAdapter {

	private final Event[] events;
	private final static int resourceID = R.layout.event_adapter_row;
	private final static int containerID = R.id.trList;
	private final static int col1ID = R.id.txt_list_col1;
	private final static int col2ID = R.id.txt_list_col2;
	private final static int col3ID = R.id.txt_list_col3;

	public EventList(Context context, Event[] events) {
		super(context, resourceID, containerID);
		this.events = events;
		if(events != null){
			for(Event event : this.events)
				super.add(String.valueOf(event.getId()));
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    View rowView = super.getView(position, convertView, parent);
	    //Gets the view with default resources and inflated
	    if(events.length > position) {
	    	Event event = events[position];
	    	//Populate the view's items with the arraylist content
		    ((TextView)rowView.findViewById(col1ID)).setText("Event " + event.getId());
			((TextView)rowView.findViewById(col2ID)).setText(event.getTotalAttachments() + " attachment(s)");
			((TextView)rowView.findViewById(col3ID)).setText(event.getTotalCauses() + " related cause(s)");
		}
	    return rowView;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setRowColor(View row,boolean even){
		if(even)
			row.findViewById(containerID).setBackgroundResource(R.color.listview_even);
	    else
	    	row.findViewById(containerID).setBackgroundResource(R.color.listview_odd);
	}
}
