package com.hammer.sitorwalk.StepCounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hammer.sitorwalk.R;

import java.util.ArrayList;

/**
 * Created by Cheng on 15/10/17.
 */

public class HistoryAdapter extends ArrayAdapter<HistoryModel> {

    public HistoryAdapter(Context context, ArrayList<HistoryModel> history) {
        super(context, 0, history);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HistoryModel historyModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_step_history, parent, false);
        }
        // Lookup view for data population
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvSteps = (TextView) convertView.findViewById(R.id.tvStep);
        // Populate the data into the template view using the data object
        tvDate.setText("Date:" + historyModel.getDate());
        tvSteps.setText("Steps: "+ Integer.toString(historyModel.getSteps()));
        // Return the completed view to render on screen
        return convertView;
    }

}
