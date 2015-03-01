package org.hackillinios.studentplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class ViewAssignment extends ActionBarActivity {

    TextView title, classA, dueDate, description, reminderDate;
    Button complete;
    CheckBox is_complete;
    Assignments assignment;
    String tOD, dDate, dTime, rDate, rTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);
        initialize();
    }

    private void initialize() {
        final SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String json = prefs.getString("assignment", "No Assignment");
        Gson gson = new Gson();
        assignment = gson.fromJson(json, Assignments.class);
        getSupportActionBar().setHomeButtonEnabled(true);


        title = (TextView)findViewById(R.id.tvViewTitle);
        classA = (TextView)findViewById(R.id.tvClassTitle);
        dueDate = (TextView)findViewById(R.id.tvViewDueDate);
        description = (TextView)findViewById(R.id.tvDescription);
        reminderDate = (TextView)findViewById(R.id.tvViewReminderDate);
        complete = (Button)findViewById(R.id.bComplete);
        is_complete = (CheckBox)findViewById(R.id.cbCompleted);

        if(assignment.getComplete()){
            complete.setVisibility(View.INVISIBLE);
            is_complete.setVisibility(View.VISIBLE);
            is_complete.setChecked(true);
        }
        title.setText(assignment.getTitle());
        classA.setText(assignment.getClassA());
        description.setText(assignment.getDescription());
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignment.setComplete(true);
                SharedPreferences.Editor editor = prefs.edit();
                Gson g = new Gson();
                String j = g.toJson(assignment);
                editor.putString("assignment", j);
                editor.commit();
                finish();
            }
        });

        Calendar c = assignment.getDueDate();
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int dw = c.get(Calendar.DAY_OF_WEEK);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int am_pm = c.get(Calendar.AM_PM);
        String dayOfWeek = new DateFormatSymbols().getShortWeekdays()[dw];
        tOD = new DateFormatSymbols().getAmPmStrings()[am_pm];
        String month = new DateFormatSymbols().getShortMonths()[m];
        String time = hour + ":" + min + " " + tOD;
        dDate = month + " " + d;
        dTime = time;
        dueDate.setText("Assignment due: " + dayOfWeek + " " + month + " " + d + " at " + time);

        c = assignment.getReminderDate();
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);
        dw = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR);
        min = c.get(Calendar.MINUTE);
        am_pm = c.get(Calendar.AM_PM);
        dayOfWeek = new DateFormatSymbols().getShortWeekdays()[dw];
        tOD = new DateFormatSymbols().getAmPmStrings()[am_pm];
        month = new DateFormatSymbols().getShortMonths()[m];
        time = hour + ":" + min + " " + tOD;
        rDate =  month + " " + d;
        rTime = time;
        reminderDate.setText("Assignment due: " + dayOfWeek + " " + month + " " + d + " at " + time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_view_assignment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.action_edit){
           SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
           SharedPreferences.Editor editor = prefs.edit();
           editor.putInt("orNew", 0);
           editor.commit();

           Intent i = new Intent(this, EditAssignment.class);
           Bundle bun = new Bundle();
           bun.putString("dDate", dDate);
           bun.putString("dTime", dTime);
           bun.putString("rDate", rDate);
           bun.putString("rTime", rTime);
           i.putExtras(bun);
           startActivity(i);
       }else if(item.getItemId() == android.R.id.home){
           Intent i = new Intent();
           i.putExtra("result", 1);
           setResult(Activity.RESULT_OK, i);
           finish();
       }
        return true;
    }

    @Override
    protected void onResume() {
        final SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String json = prefs.getString("assignment", "No Assignment");
        Gson gson = new Gson();
        assignment = gson.fromJson(json, Assignments.class);

        if(assignment.getComplete()){
            complete.setVisibility(View.INVISIBLE);
            is_complete.setVisibility(View.VISIBLE);
            is_complete.setChecked(true);
        }
        title.setText(assignment.getTitle());
        classA.setText(assignment.getClassA());
        description.setText(assignment.getDescription());

        Calendar c = assignment.getDueDate();
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int dw = c.get(Calendar.DAY_OF_WEEK);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int am_pm = c.get(Calendar.AM_PM);
        String dayOfWeek = new DateFormatSymbols().getShortWeekdays()[dw];
        tOD = new DateFormatSymbols().getAmPmStrings()[am_pm];
        String month = new DateFormatSymbols().getShortMonths()[m];
        String time = hour + ":" + min + " " + tOD;
        dDate = month + " " + d;
        dTime = time;
        dueDate.setText("Assignment due: " + dayOfWeek + " " + month + " " + d + " at " + time);

        c = assignment.getReminderDate();
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);
        dw = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR);
        min = c.get(Calendar.MINUTE);
        am_pm = c.get(Calendar.AM_PM);
        dayOfWeek = new DateFormatSymbols().getShortWeekdays()[dw];
        tOD = new DateFormatSymbols().getAmPmStrings()[am_pm];
        month = new DateFormatSymbols().getShortMonths()[m];
        time = hour + ":" + min + " " + tOD;
        rDate =  month + " " + d;
        rTime = time;
        reminderDate.setText("Assignment due: " + dayOfWeek + " " + month + " " + d + " at " + time);
    }
}
