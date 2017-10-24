package com.hammer.sitorwalk.SitCounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hammer.sitorwalk.MainActivity;
import com.hammer.sitorwalk.R;
import com.hammer.sitorwalk.StepCounter.HistoryRepo;

import java.util.ArrayList;

public class SitChart extends Fragment {


    View view;
    BarChart barChart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SitChart() {
        // Required empty public constructor
    }
    public static SitChart newInstance(String param1, String param2) {
        SitChart fragment = new SitChart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Update activity title
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Sit History Chart");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sit_chart, container, false);
        barChart = (BarChart) view.findViewById(R.id.sitchart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        SitRepo repo = new SitRepo(this.getContext());
        ArrayList<SitModel> sitList = repo.getList();
        ArrayList<String> labels = new ArrayList<String>();

        int length = sitList.size();
        int size = sitList.size();
        int index = 6;
        if (length < 7) index = 6 - (7 - length);
        if (length > 7) length = 7;

        for (int i = 0; i < length; i ++) {
            labels.add(sitList.get(size - index - 1).getDate());
            BarEntry barEntry = new BarEntry(sitList.get(size - 1 - i).getSit(), index);
            entries.add(barEntry);
            index --;
        }

        BarDataSet bardataset = new BarDataSet(entries, "minutes");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextSize(15f);
        barChart.animateY(1);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(15f);

        barChart.setDrawValueAboveBar(true);
        BarData data = new BarData(labels, bardataset);
        barChart.setData(data);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
