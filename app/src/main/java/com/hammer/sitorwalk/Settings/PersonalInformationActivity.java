package com.hammer.sitorwalk.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.hammer.sitorwalk.MainActivity;
import com.hammer.sitorwalk.R;

import org.w3c.dom.Text;

public class PersonalInformationActivity extends AppCompatActivity {

    private Spinner spinnerGender;
    private TextView textWeight;
    private TextView textHeight;
    private Button btnSubmit;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "We need these information to provide customize suggestions.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinnerGender = (Spinner)findViewById(R.id.spinner);
        textWeight = (TextView)findViewById(R.id.input_weight);
        textHeight = (TextView)findViewById(R.id.input_height);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = settings.edit();
                boolean valid = validateForm();
                if (valid == true) {
                    editor.putInt("personal", 1).commit();
                    Intent intent = new Intent(PersonalInformationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        String height = textHeight.getText().toString();
        if (TextUtils.isEmpty(height)) {
            textHeight.setError("Required.");
            valid = false;
        } else {
            editor.putString("key_height", height).commit();
        }

        String weight = textWeight.getText().toString();
        if (TextUtils.isEmpty(weight)) {
            textHeight.setError("Required.");
            valid = false;
        } else {
            editor.putString("key_weight", weight).commit();
        }

        String gender = spinnerGender.getSelectedItem().toString();

        String genderValue = "0";
        if (gender.equals("Male"))
            genderValue = "0";
        else if (gender.equals("Female"))
            genderValue = "1";
        else
            genderValue = "2";
        editor.putString("key_gender", genderValue).commit();
        return valid;
    }
}
