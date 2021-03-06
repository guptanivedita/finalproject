package trainedge.cashtrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity  {

    private SharedPreferences pref;
    private TextView tvTimeOption;
    private Switch switchTts;
    private Switch switchDaily;
    private Switch switchVibRing;
    private AlarmReceiver alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alarm = new AlarmReceiver();
        tvTimeOption = (TextView) findViewById(R.id.tvTimeOption);
        switchTts = (Switch) findViewById(R.id.switchTts);
        switchDaily = (Switch) findViewById(R.id.switchDaily);
        switchVibRing = (Switch) findViewById(R.id.switchVibRing);
        ViewGroup vClearNLogout = (ViewGroup) findViewById(R.id.vClearNLogout);
        pref = getSharedPreferences(Constants.SETTING_PREF, MODE_PRIVATE);
        updateUI();

        switchTts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean(Constants.KEY_TTS, isChecked).apply();
            }
        });
        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean(Constants.KEY_DAILY_NOTIF, isChecked).apply();
            }
        });
        switchVibRing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean(Constants.KEY_VIB_RING, isChecked).apply();
            }
        });
        tvTimeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClockDialog();
            }
        });
        vClearNLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Long press to clear app database", Toast.LENGTH_SHORT).show();
            }
        });
        vClearNLogout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteExpenseDb();
                updateUI();
                return true;
            }
        });
    }


    private void deleteExpenseDb() {
        ExpenseDatabaseAdapter expenseAdapter = new ExpenseDatabaseAdapter(this);

        expenseAdapter.deleteAll();
        pref.edit().clear().apply();
    }

    private void showClockDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pref.edit().putInt(Constants.KEY_HOUR, hourOfDay).putInt(Constants.KEY_MINUTE, minute).apply();
                tvTimeOption.setText("" + hourOfDay + ":" + minute);
                startRepeatingTimer(hourOfDay,minute);

            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
        dialog.show();
    }

    private void updateUI() {
        switchTts.setChecked(pref.getBoolean(Constants.KEY_TTS, false));
        switchDaily.setChecked(pref.getBoolean(Constants.KEY_DAILY_NOTIF, false));
        switchVibRing.setChecked(pref.getBoolean(Constants.KEY_VIB_RING, false));
        int hour = pref.getInt(Constants.KEY_HOUR, 0);
        int minute = pref.getInt(Constants.KEY_MINUTE, 0);
        tvTimeOption.setText("" + hour + ":" + minute);
            }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startRepeatingTimer(int hourOfDay, int minute) {
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.SetAlarm(context,hourOfDay,minute);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer(){
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.CancelAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer(){
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.setOnetimeTimer(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

}
