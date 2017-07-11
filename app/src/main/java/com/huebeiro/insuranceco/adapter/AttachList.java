package com.huebeiro.insuranceco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huebeiro.insuranceco.R;
import com.huebeiro.insuranceco.model.Attachment;
import com.huebeiro.insuranceco.model.Cause;

public class AttachList extends CustomAdapter {

    private int imageRes = R.drawable.ic_delete;

	private final Attachment[] attachments;
	private final static int resourceID = R.layout.cause_adapter_row;
	private final static int containerID = R.id.trList;
	private final static int col1ID = R.id.txt_list_col1;
	private final static int col2ID = R.id.txt_list_col2;

	public AttachList(Context context, Attachment[] attachments) {
		super(context, resourceID, containerID);
		this.attachments = attachments;
		if(attachments != null){
			for(Attachment attachment : this.attachments)
				super.add(attachment.getDescription());
		}
	}

    //For overriding
	public void itemClick(Attachment attachment){

    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    View rowView = super.getView(position, convertView, parent);
	    //Gets the view with default resources and inflated
	    if(attachments.length > position) {
	    	Attachment attachment = attachments[position];
	    	//Populate the view's items with the arraylist content
		    ((TextView)rowView.findViewById(col1ID)).setText(attachment.getDescription());
            ((ImageButton)rowView.findViewById(col2ID)).setImageResource(getImageRes());
			((ImageButton)rowView.findViewById(col2ID)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    itemClick(attachments[position]);
				}
			});
		}
	    return rowView;
	}
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
