package com.hammer.sitorwalk.StepCounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hammer.sitorwalk.MainActivity;
import com.hammer.sitorwalk.R;

public class PedometerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PREFERENCE_NAME = "stepCountPref";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Global variables for settings
    SharedPreferences sharedPref;
    private int recordSteps;

    // View
    View view;

    // Widgets
    TextView textPedo;

    private OnFragmentInteractionListener mListener;

    public PedometerFragment() {
        // Required empty public constructor
    }

    public static PedometerFragment newInstance(String param1, String param2) {
        PedometerFragment fragment = new PedometerFragment();
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
        actionBar.setTitle("Pedometer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_pedometer, container, false);

        // Find widgets
        textPedo = (TextView)view.findViewById(R.id.textPedo);

        // Initialize shared settings
        sharedPref = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Set pedometer step counts
        recordSteps = sharedPref.getInt(getString(R.string.settings_num_steps), 0);
        textPedo.setText(Integer.toString(recordSteps));

        // Create step counts on change listener
        SharedPreferences.OnSharedPreferenceChangeListener stepCountChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isAdded())// Avoid fragment crash
                    if (key.equals(getString(R.string.settings_num_steps)))
                        textPedo.setText(Integer.toString(sharedPreferences.getInt(getString(R.string.settings_num_steps), 0)));
            }
        };

        // Register shared perference on changed listener
        sharedPref.registerOnSharedPreferenceChangeListener(stepCountChangeListener);

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
