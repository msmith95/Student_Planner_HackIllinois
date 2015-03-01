package org.hackillinios.studentplanner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class AssignmentFragment extends android.app.Fragment {
    ListView assign;
    String api, rText;
    HttpClient client;
    HttpPost post;
    HttpResponse response;
    JSONObject json, json2;
    Assignments[] array;
    ArrayAdapter<Assignments> adapter;
    private fetchAssignmentsTaskFromNet fetch;
    int position=0;
    boolean go = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_assignment, container, false);
        setHasOptionsMenu(true);
        assign = (ListView)rootview.findViewById(R.id.lvAssignments);
        assign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ViewAssignment.class);

                SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson= new Gson();;
                String json = gson.toJson(array[position]);
                editor.putString("assignment", json);
                editor.commit();

                startActivityForResult(i, 1);
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!go) {
            SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String json = prefs.getString("assignment", "");
            Gson gson = new Gson();
            array[position] = gson.fromJson(json, Assignments.class);
            adapter.notifyDataSetChanged();
        }else{
            fetch = new fetchAssignmentsTaskFromNet();
            fetch.execute();
        }
    }

    public class fetchAssignmentsTaskFromNet extends AsyncTask<Void, Void, Assignments[]>{

        @Override
        protected Assignments[] doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            ArrayList<Assignments> temp = new ArrayList<>();
            SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String user = prefs.getString("user", " ");
            pairs.add(new BasicNameValuePair("user", user));


            api = getResources().getString(R.string.fetch);
            client = new DefaultHttpClient();
            post = new HttpPost(api);

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
            int count = 0;
            try {
                count = json.getInt("count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i<count; i++){
                Assignments tempA = new Assignments();
                try {
                    json2 = json.getJSONObject(String.valueOf(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    String title = json2.getString("title");
                    String classes = json2.getString("class");
                    String d = json2.getString("description");
                    String d1 = json2.getString("dueDate");
                    String d2 = json2.getString("reminderDate");

                    Log.v("AssignFrag", "Reached line");
                    tempA.setTitle(json2.getString("title"));

                    Date f = df.parse(d1);
                    Date f2 = df.parse(d2);

                    tempA.setAll(title,classes,d, f, f2);

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                temp.add(tempA);
            }
            return temp.toArray(new Assignments[temp.size()]);
        }

        @Override
        protected void onPostExecute(Assignments[] array2) {
            array = array2;
            adapter = new AssignmentAdapter(getActivity(), array2);
            assign.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_assignment_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_new){
            SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("orNew", 1);
            editor.commit();

            Intent i = new Intent(getActivity(), EditAssignment.class);
            startActivityForResult(i, 0);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //0 means new assignment 1 means an assignment was edited.
        switch(requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    int temp = data.getIntExtra("result", 1);
                    if(temp ==0){
                        go = true;
                    }
                }

                break;

            case 1:
                if(resultCode == Activity.RESULT_OK){
                    int temp = data.getIntExtra("result", 1);
                    if(temp == 1){
                        go = false;
                    }
                }
                break;
        }
    }
}
