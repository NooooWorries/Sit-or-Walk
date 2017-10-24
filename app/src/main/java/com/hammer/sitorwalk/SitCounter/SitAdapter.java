package com.hammer.sitorwalk.SitCounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hammer.sitorwalk.R;
import com.hammer.sitorwalk.SitCounter.SitModel;

import java.util.ArrayList;

/**
 * Created by Cheng on 22/10/17.
 */

public class SitAdapter extends ArrayAdapter<SitModel> {
    public SitAdapter(Context context, ArrayList<SitModel> sit) {
        super(context, 0, sit);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SitModel sitModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sit_history, parent, false);
        }
        // Lookup view for data population
        TextView tvDateSit = (TextView) convertView.findViewById(R.id.tvDateSit);
        TextView tvSit = (TextView) convertView.findViewById(R.id.tvSit);
        // Populate the data into the template view using the data object
        tvDateSit.setText("Date:" + sitModel.getDate());
        int value = sitModel.getSit();
        int hours = value / 60;
        int minutes = value % 60;
        tvSit.setText("Sit: "+ Integer.toString(hours)+ " hours " + Integer.toString(minutes) + " minutes");
        // Return the completed view to render on screen
        return convertView;
    }

}
