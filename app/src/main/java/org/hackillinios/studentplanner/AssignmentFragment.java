package org.hackillinios.studentplanner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_assignment, container, false);
        assign = (ListView)rootview.findViewById(R.id.lvAssignments);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewAssignment.class);

                SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson= new Gson();
                position = assign.getPositionForView(v);
                String json = gson.toJson(array[position]);
                editor.putString("assignment", json);
                editor.commit();

                startActivity(i);
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
       SharedPreferences prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
       String json = prefs.getString("assignment", "");
       Gson gson = new Gson();
       array[position] = gson.fromJson(json, Assignments.class);
       adapter.notifyDataSetChanged();
    }

    public class fetchAssignmentsTaskFromNet extends AsyncTask<Void, Void, Assignments[]>{

        @Override
        protected Assignments[] doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            ArrayList<Assignments> temp = new ArrayList<>();



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
                DateFormat df = new SimpleDateFormat("yyyy MM dd HH mm ss");
                try {
                    tempA.setAll(json2.getString("title"), json2.getString("class"), json2.getString("description"), json2.getString("type"), df.parse(json2.getString("dueDate")), df.parse("reminderDate"));
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
        }
    }
}
