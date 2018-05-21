package com.tattleup.app.tattleup.asynctask;

/**
 * Created by Shiva on 6/20/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.tattleup.app.tattleup.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
    //private TextView statusField, roleField;
    private String urls;
    private OnAsyncResult listener;
    private Context context;
    private ProgressDialog prd;
    private JSONObject postData;
    private boolean isProgressEnabled;

    //flag 0 means get and 1 means post.(By default it is get.)
    public HttpPostAsyncTask(String urls, OnAsyncResult listener, Context context, boolean isProgressEnabled, JSONObject jsonObject) {
        this.urls = urls;
        this.listener = listener;
        this.context = context;
        this.isProgressEnabled = isProgressEnabled;

            this.postData = jsonObject;

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
    public String doInBackground(String... arg0) {

        try {
            String username = "";
            String password = "";

            String link = "http://mobileadminapi.shopincity.com/Controller/ServiceAsyncController.php?action=adminlogin&";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


           URL url = new URL(urls);
//            URLConnection conn = url.openConnection();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");


//
//            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
//            urlConnection.getOutputStream();
//            wr.write( String.valueOf( urls ) );
//            wr.flush();
            OutputStream out =  urlConnection.getOutputStream();
            out.write ( String.valueOf( postData ).getBytes("UTF-8") );
            out.close();
            StringBuilder sb = new StringBuilder();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }

//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            //StringBuilder sb = new StringBuilder();
            //String line = null;

            // Read Server Response
            //while ((line = reader.readLine()) != null) {
              //  sb.append(line);
               // break;
            //}
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
