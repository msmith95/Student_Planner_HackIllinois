package org.hackillinios.studentplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class EditAssignment extends ActionBarActivity {

    EditText title, dueDate, dueTime, reminderDate, reminderTime, description;
    Spinner classes;
    Bundle args;
    int month, day, year;
    String tOD;
    LinearLayout ll;
    Calendar due, reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        args = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);
        initialize();


    }

    private void initialize() {
        title = (EditText)findViewById(R.id.etEditTitle);
        dueDate = (EditText)findViewById(R.id.etEditDate);
        dueTime = (EditText)findViewById(R.id.etEditTime);
        reminderTime= (EditText)findViewById(R.id.etEditReminderTime);
        reminderDate = (EditText)findViewById(R.id.etEditReminderDate);
        description = (EditText)findViewById(R.id.etEditDescription);
        classes = (Spinner)findViewById(R.id.sClasses);
        ll = (LinearLayout)findViewById(R.id.llEdit);
        due = Calendar.getInstance();
        reminder = Calendar.getInstance();

        SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String json = prefs.getString("assignment", "");
        Gson g = new Gson();
        Assignments assign = g.fromJson(json, Assignments.class);

        title.setText(assign.getTitle());
        description.setText(assign.getDescription());
        dueDate.setText(args.getString("dDate"));
        dueTime.setText(args.getString("dTime"));
        reminderDate.setText(args.getString("rDate"));
        reminderTime.setText(args.getString("rTime"));

        dueDate.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_DATE);
        dueTime.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_TIME);
        reminderDate.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_DATE);
        reminderTime.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_TIME);

        View.OnFocusChangeListener date = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog dpd = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         Calendar c = Calendar.getInstance();
                            c.set(year, monthOfYear, dayOfMonth);
                            int m = c.get(Calendar.MONTH);
                            int d = c.get(Calendar.DAY_OF_MONTH);
                            int dw = c.get(Calendar.DAY_OF_WEEK);
                            String dayOfWeek = new DateFormatSymbols().getShortWeekdays()[dw];
                            String month = new DateFormatSymbols().getShortMonths()[m];
                            EditText temp = (EditText)findViewById(v.getId());
                            temp.setText(dayOfWeek + " " + month + " " + d);

                            if(v.getId() == R.id.etEditDate){
                                due.set(year, monthOfYear, dayOfMonth);
                            }else{
                                reminder.set(year, monthOfYear, dayOfMonth);
                            }

                            ll.requestFocus();
                        }
                    }, year, month, day);

                    dpd.show();
                }
            }
        };

        View.OnFocusChangeListener time = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if(hasFocus){
                    int hour=0, minute=0;
                    TimePickerDialog tp = new TimePickerDialog(getApplicationContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            int hour = c.get(Calendar.HOUR);
                            int min = c.get(Calendar.MINUTE);
                            int am_pm = c.get(Calendar.AM_PM);
                            tOD = new DateFormatSymbols().getAmPmStrings()[am_pm];

                            EditText temp = (EditText)findViewById(v.getId());
                            temp.setText(hour+":" + min + " " + tOD);
                            if(v.getId() == R.id.etEditDate){
                                due.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                due.set(Calendar.MINUTE, minute);
                            }else{
                                reminder.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                reminder.set(Calendar.MINUTE, minute);
                            }

                            ll.requestFocus();
                        }
                    }, hour, minute, false);
                }
            }
        };

        dueDate.setOnFocusChangeListener(date);
        reminderDate.setOnFocusChangeListener(date);
        dueTime.setOnFocusChangeListener(time);
        reminderTime.setOnFocusChangeListener(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_assignment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            Assignments temp = new Assignments();
            temp.setAll(title.getText().toString(), classes.toString(), description.getText().toString(),due.getTime(), reminder.getTime());
            SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            Gson g = new Gson();
            String json = g.toJson(temp, Assignments.class);
            editor.putString("assignment", json);
            editor.commit();

            finish();
        }
       return true;
    }
}
