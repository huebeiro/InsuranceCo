package com.huebeiro.insuranceco.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huebeiro.insuranceco.R;
import com.huebeiro.insuranceco.model.Car;

import java.util.ArrayList;

public class CarList extends CustomAdapter {

	private final Car[] cars;
	private final static int resourceID = R.layout.car_adapter_row;
	private final static int containerID = R.id.trList;
	private final static int col1ID = R.id.txt_list_col1;
	private final static int col2ID = R.id.txt_list_col2;
	private final static int col3ID = R.id.txt_list_col3;

	public CarList(Context context, Car[] cars) {
		super(context, resourceID, containerID);
		this.cars = cars;
		if(cars != null){
			for(Car car : this.cars)
				super.add(car.getPlate());
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    View rowView = super.getView(position, convertView, parent);
	    //Gets the view with default resources and inflated
	    if(cars.length > position) {
	    	Car car = cars[position];
	    	//Populate the view's items with the arraylist content
		    ((TextView)rowView.findViewById(col1ID)).setText(car.getFormattedPlate());
			((TextView)rowView.findViewById(col2ID)).setText(car.getBrand() + " - " + car.getModel());
			((TextView)rowView.findViewById(col3ID)).setText(car.getTotalEvents() + " event(s)");
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
