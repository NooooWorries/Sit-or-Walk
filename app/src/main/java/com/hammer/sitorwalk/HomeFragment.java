package com.hammer.sitorwalk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hammer.sitorwalk.StepCounter.PedometerFragment;
import com.hammer.sitorwalk.StepCounter.StepDetector;
import com.hammer.sitorwalk.StepCounter.StepListener;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PREFERENCE_NAME = "stepCountPref";

    // Buttons
    Button btnWalkDetail;

    // View
    View view;

//    // Global variables for pedometer
//    private StepDetector simpleStepDetector;
//    private SensorManager sensorManager;
//    private Sensor accel;
//    private int recordSteps;

    // Global variables for widgets
    private TextView textSteps;

    // Global variables for settings
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // Global variables for fragment
    Fragment fragment;
    FragmentManager fragmentManager;

    private int recordSteps;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        actionBar.setTitle("Home");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set fragment manager
        fragmentManager = (getActivity()).getSupportFragmentManager();

        // Find buttons
        btnWalkDetail = (Button)view.findViewById(R.id.btnWalkDetail);

        // Find text view
        textSteps = (TextView)view.findViewById(R.id.textStep);

        // Get shared preference
        sharedPref = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Load numSteps from shared preference
        recordSteps = sharedPref.getInt(getString(R.string.settings_num_steps), 0);
        textSteps.setText("You walked " + recordSteps + " steps today");

        // Add preference on changed listener (detect step counts change)
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(isAdded() && key.equals("numSteps")){
                    textSteps.setText("You walked " + sharedPref.getInt(getString(R.string.settings_num_steps), 0) + " steps today");
                }
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        // Add button listener
        // Walk detail
        btnWalkDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PedometerFragment();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

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
