package org.hackillinios.studentplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText email, password;
    Button login;
    CheckBox newUser;
    private LoginTask loginT;
    private RegisterTask registerT;
    HttpPost post;
    HttpClient client;
    HttpResponse response;
    String api, rText;
    JSONObject json;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();
    }

    private void Initialize() {
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);
        login = (Button)findViewById(R.id.bLogin);
        newUser = (CheckBox)findViewById(R.id.cbNewUser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                password.setError(null);

                if(email.getText().toString().isEmpty() || !email.getText().toString().contains("@")){
                    email.setError("Invalid email!");
                    return;
                }else if(password.getText().toString().length() < 5 || password.getText().toString().isEmpty()){
                    password.setError("Password must be longer than 5 characters");
                    return;
                }
                if(newUser.isChecked()){
                    registerT = new RegisterTask(email.getText().toString(), password.getText().toString());
                    registerT.execute();
                }else{
                    loginT = new LoginTask(email.getText().toString(), password.getText().toString());
                    loginT.execute();
                }
            }
        });
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean>{

        private String mEmail, mPassword;
        ProgressDialog dialog;

        public LoginTask(String email, String password){
            this.mEmail = email;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("email", mEmail));
            pairs.add(new BasicNameValuePair("pass", mPassword));

            api = getResources().getString(R.string.login);
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
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Please Wait...", "Logging in", true, true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            if(aBoolean){
                Intent i = new Intent(MainActivity.this, HomeworkList.class);
                startActivity(i);
            }else{
                Toast t = Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public class RegisterTask extends AsyncTask<Void, Void, Boolean>{

        private String mEmail, mPassword;
        ProgressDialog dialog;

        public RegisterTask(String email, String password){
            this.mEmail = email;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            api = getResources().getString(R.string.register);
            client = new DefaultHttpClient();
            post = new HttpPost(api);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("email", mEmail));
            pairs.add(new BasicNameValuePair("pass", mPassword));

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
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Please Wait...", "Processing Registration...", true, true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            if(aBoolean){
                Intent i = new Intent(MainActivity.this, HomeworkList.class);
                startActivity(i);
            }else{
                Toast t = Toast.makeText(MainActivity.this, "Registration Failed!", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
}
