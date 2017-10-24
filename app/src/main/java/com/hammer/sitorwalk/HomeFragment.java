package com.hammer.sitorwalk;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hammer.sitorwalk.SitCounter.SitChart;
import com.hammer.sitorwalk.SitCounter.SitCountHistoryFragment;
import com.hammer.sitorwalk.SitCounter.SitCounterFragment;
import com.hammer.sitorwalk.StepCounter.HistoryChart;
import com.hammer.sitorwalk.StepCounter.PedometerFragment;
import com.hammer.sitorwalk.StepCounter.StepCountHistoryFragment;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PREFERENCE_NAME = "stepCountPref";

    // Buttons
    private Button btnWalkDetail;
    private Button btnWalkHistory;
    private Button btnSitDetail;
    private Button btnSitHistory;

    // View
    View view;

    // Global variables for widgets
    private TextView textSteps;
    private TextView textConnection;
    private TextView textSit;


    // Global variables for settings
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    SharedPreferences settings;

    // Global variables for fragment
    Fragment fragment;
    FragmentManager fragmentManager;

    private int recordSteps;
    private int connectionStatus = 0;

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
        btnWalkHistory = (Button)view.findViewById(R.id.btnWalkHistory);
        btnSitDetail = (Button)view.findViewById(R.id.btnSitDetail);
        btnSitHistory = (Button)view.findViewById(R.id.btnSitHistory);

        // Find text view
        textSteps = (TextView)view.findViewById(R.id.textStep);
        textConnection = (TextView)view.findViewById(R.id.textConnection);
        textSit = (TextView)view.findViewById(R.id.textSit);

        // Get shared preference
        sharedPref = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Load numSteps from shared preference
        recordSteps = sharedPref.getInt(getString(R.string.settings_num_steps), 0);
        textSteps.setText("You walked " + recordSteps + " steps today");

        // Load sit duration from shared preference
        int value = settings.getInt("sit", 0);
        int hours = value / 60;
        int minutes = value % 60;
        textSit.setText("You sitted " + Integer.toString(hours) + " hours " + Integer.toString(minutes) + " minutes today");

        textConnection.setText("WAIT FOR ONE MINUTE");
        textConnection.setTextColor(Color.BLUE);

        // Add preference on changed listener (detect step counts change)
        SharedPreferences.OnSharedPreferenceChangeListener numStepsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(isAdded() && key.equals("numSteps")){
                    textSteps.setText("You walked " + sharedPref.getInt(getString(R.string.settings_num_steps), 0) + " steps today");
                }
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(numStepsChangeListener);

        // Load connection status
        connectionStatus = settings.getInt("sensor", 0);
        if (connectionStatus == 0) {
            textConnection.setText("DISCONNECTED");
            textConnection.setTextColor(Color.RED);
        }
        else if (connectionStatus == 1) {
            textConnection.setText("CONNECTED");
            textConnection.setTextColor(Color.BLUE);
        }
        else {
            textConnection.setText("INTERNET ERROR");
            textConnection.setTextColor(Color.RED);
        }

        SharedPreferences.OnSharedPreferenceChangeListener sitCountChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isAdded()) {
                    if (key.equals("sit")) {
                        int value = settings.getInt("sit", 0);
                        int hours = value / 60;
                        int minutes = value % 60;
                        textSit.setText("You sitted " + Integer.toString(hours) + " hours " + Integer.toString(minutes) + " minutes today");
                    }
                    // Load connection status
                    connectionStatus = settings.getInt("sensor", 0);
                    if (connectionStatus == 0) {
                        textConnection.setText("DISCONNECTED");
                        textConnection.setTextColor(Color.RED);
                    }
                    else if (connectionStatus == 1) {
                        textConnection.setText("CONNECTED");
                        textConnection.setTextColor(Color.BLUE);
                    }
                    else {
                        textConnection.setText("INTERNET ERROR");
                        textConnection.setTextColor(Color.RED);
                    }
                }
            }
        };

        settings.registerOnSharedPreferenceChangeListener(sitCountChangeListener);
        // Add button listener
        // Walk detail
        btnWalkDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PedometerFragment();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

        // Walk history
        btnWalkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HistoryChart();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

        // Sit detail
        btnSitDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SitCounterFragment();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

        // Sit hostory
        btnSitHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SitChart();
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
