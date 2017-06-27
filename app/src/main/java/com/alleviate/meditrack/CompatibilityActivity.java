package com.alleviate.meditrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alleviate.meditrack.constants.Constants;

public class CompatibilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compability);

        getSupportActionBar().hide();

        TextView info = (TextView) findViewById(R.id.setting_info);
        info.setMovementMethod(new ScrollingMovementMethod());

        Button settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences reset_alarm_sp = getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
                reset_alarm_sp.edit().putString(Constants.sp_alarm_reset, Constants.sp_alarm_reset_yes).apply();
                reset_alarm_sp.edit().putBoolean(Constants.power_saver_mode, false).apply();

                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));

                finish();
            }
        });
    }
}
