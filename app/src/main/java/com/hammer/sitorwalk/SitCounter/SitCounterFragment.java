package com.hammer.sitorwalk.SitCounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hammer.sitorwalk.MainActivity;
import com.hammer.sitorwalk.R;

import org.w3c.dom.Text;


public class SitCounterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // View
    View view;

    // Widgets
    private TextView textHours;
    private TextView textMinutes;
    private Button btnHistory;
    private Button btnHistoryAll;

    // Data
    private int value;
    private int hours;
    private int minutes;

    // Global variables for fragment
    Fragment fragment;
    FragmentManager fragmentManager;

    // Global variables for settings;
    SharedPreferences settings;

    public SitCounterFragment() {
        // Required empty public constructor
    }

    public static SitCounterFragment newInstance(String param1, String param2) {
        SitCounterFragment fragment = new SitCounterFragment();
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

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Sit Counter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sit_counter, container, false);

        // Find widgets by id
        textHours = (TextView)view.findViewById(R.id.textSitHours);
        textMinutes = (TextView)view.findViewById(R.id.textSitMinutes);
        btnHistory = (Button) view.findViewById(R.id.btnSitHistoryFra);
        btnHistoryAll = (Button)view.findViewById(R.id.btnSitHistoryAll);

        // Set fragment manager
        fragmentManager = (getActivity()).getSupportFragmentManager();

        // Initialize shared settings
        settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        // Set sit time
        value = settings.getInt("sit", 0);
        hours = value / 60;
        minutes = value % 60;
        textHours.setText(Integer.toString(hours) + " Hours");
        textMinutes.setText(Integer.toString(minutes) + " Minutes");

        // Create sit count on change listener
        SharedPreferences.OnSharedPreferenceChangeListener sitChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isAdded()) {
                    value = settings.getInt("sit", 0);
                    hours = value / 60;
                    minutes = value % 60;
                    textHours.setText(Integer.toString(hours) + " Hours");
                    textMinutes.setText(Integer.toString(minutes) + " Minutes");
                }
            }
        };
        settings.registerOnSharedPreferenceChangeListener(sitChangeListener);

        // Create button on click listener
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SitChart();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

        btnHistoryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SitCountHistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, "TAG").commit();
            }
        });

        return view;
    }

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
