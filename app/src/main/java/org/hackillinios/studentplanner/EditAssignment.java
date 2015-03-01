package org.hackillinios.studentplanner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class EditAssignment extends ActionBarActivity {

    EditText title, dueDate, dueTime, reminderDate, reminderTime, description;
    Spinner classes;
    Bundle args;
    int month, day, year, orNew, success;
    String tOD, api, rText;
    LinearLayout ll;
    Calendar due, reminder;
    HttpClient client;
    HttpPost post;
    HttpResponse response;
    JSONObject json;
    Assignments temp;
    private pushAssignmentTask push;


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
        orNew = prefs.getInt("orNew", 0);
        //String tmp = prefs.getString("classes", "");

        String[] temp = {"CS 2050", "IT 2610", "MATH 1700", "IT 2810"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classes.setAdapter(adapter);

        if(orNew == 0) {
            Gson g = new Gson();
            Assignments assign = g.fromJson(json, Assignments.class);

            title.setText(assign.getTitle());
            description.setText(assign.getDescription());
            dueDate.setText(args.getString("dDate"));
            dueTime.setText(args.getString("dTime"));
            reminderDate.setText(args.getString("rDate"));
            reminderTime.setText(args.getString("rTime"));
            getSupportActionBar().setTitle("Edit Assignment");
        }else{
            getSupportActionBar().setTitle("Add new assignment");
        }

        /*dueDate.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_DATE);
        dueTime.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_TIME);
        reminderDate.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_DATE);
        reminderTime.setInputType(EditorInfo.TYPE_DATETIME_VARIATION_TIME);*/

        View.OnFocusChangeListener date = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar d = Calendar.getInstance();
                    year = d.get(Calendar.YEAR);
                    month = d.get(Calendar.MONTH);
                    day = d .get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dpd = new DatePickerDialog(EditAssignment.this, new DatePickerDialog.OnDateSetListener() {
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
                    Calendar d = Calendar.getInstance();
                    int hour=0, minute=0;
                    hour = d.get(Calendar.HOUR_OF_DAY);
                    minute = d.get(Calendar.MINUTE);
                    TimePickerDialog tp = new TimePickerDialog(EditAssignment.this, new TimePickerDialog.OnTimeSetListener() {
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
                    }, hour, minute, true);
                    tp.show();
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
        getMenuInflater().inflate(R.menu.menu_edit_assignment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            // 0 means coming from view assignment 1 means from assignment fragment
            if(orNew == 0) {
                temp = new Assignments();
                temp.setAll(title.getText().toString(), classes.getSelectedItem().toString(), description.getText().toString(), due.getTime(), reminder.getTime());
                SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                Gson g = new Gson();
                String json = g.toJson(temp, Assignments.class);
                editor.putString("assignment", json);
                editor.commit();
                Intent result = new Intent();
                result.putExtra("result", 1);
                setResult(Activity.RESULT_OK, result);
                finish();
            }else{
                temp = new Assignments();
                temp.setAll(title.getText().toString(), classes.getSelectedItem().toString(), description.getText().toString(), due.getTime(), reminder.getTime());
                Intent result = new Intent();
                result.putExtra("result", 0);
                push = new pushAssignmentTask();
                push.execute();
                setResult(Activity.RESULT_OK, result);
                finish();
            }


        }
       return true;
    }

    public class pushAssignmentTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SharedPreferences prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String user = prefs.getString("user", " ");

            api = getResources().getString(R.string.upload);
            client = new DefaultHttpClient();
            post = new HttpPost(api);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("user", user));
            pairs.add(new BasicNameValuePair("title", temp.getTitle()));
            pairs.add(new BasicNameValuePair("class", temp.getClassA()));
            pairs.add(new BasicNameValuePair("description", temp.getDescription()));
            pairs.add(new BasicNameValuePair("dueDate", temp.getDueDate().getTime().toString()));
            pairs.add(new BasicNameValuePair("reminderDate", temp.getReminderDate().getTime().toString()));
            pairs.add(new BasicNameValuePair("id", temp.getUUID()));

            try {
                post.setEntity(new UrlEncodedFormEntity(pairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                rText = EntityUtils.toString(response.getEntity());
                rText = Html.fromHtml(rText).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                json = new JSONObject(rText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                success = json.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(success == 1){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                finish();
            }
        }
    }
}
