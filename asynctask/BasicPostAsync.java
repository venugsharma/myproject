package com.tattleup.app.tattleup.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.tattleup.app.tattleup.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class BasicPostAsync extends AsyncTask<String, Void, String> {
    //private TextView statusField, roleField;
    private String urls;
    private OnAsyncResult listener;
    private Context context;
    private ProgressDialog prd;
    private boolean isProgressEnabled;

    //flag 0 means get and 1 means post.(By default it is get.)
    public BasicPostAsync(String urls, OnAsyncResult listener, Context context, boolean isProgressEnabled) {
        this.urls = urls;
        this.listener = listener;
        this.context = context;
        this.isProgressEnabled = isProgressEnabled;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressEnabled) {
            prd = new ProgressDialog(context, R.style.MyTheme);
            prd.setCancelable(false);
            prd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            try {
                // if (prd != null && prd.isShowing())
                prd.show();
            } catch (Exception ex) {
                //prd.dismiss();
            }
        }
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String username = "";
            String password = "";

            String link = "http://api.tattleup.leoinfotech.in/index.php/api/version1";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            wr.write(urls);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
               // break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (isProgressEnabled) {
            if (prd != null)
                prd.dismiss();
        }
        if (!result.equals("error")) {
            if (listener != null) {
                listener.OnAsynResult(result);
            } else {
                listener.OnAsynResult("error");
            }
        } else {
            listener.OnAsynResult("error");
        }
    }

    public interface OnAsyncResult {
        String OnAsynResult(String result);
    }

}
