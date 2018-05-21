package com.tattleup.app.tattleup.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.tattleup.app.tattleup.R;

import java.io.InputStream;


public class AsyncTaskForDowloadImages extends AsyncTask<String, Void, Bitmap> {
    public static final String RESULT_ERROR = "error";

    private String url;
    private OnImageDownloadComplete OnImageDownloadComplete;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean isProgressEnabled;

    public AsyncTaskForDowloadImages(String url, OnImageDownloadComplete OnImageDownloadComplete, Context context, boolean isProgressEnabled) {
        this.url = url;
        this.context = context;
        this.OnImageDownloadComplete = OnImageDownloadComplete;
        this.isProgressEnabled = isProgressEnabled;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressEnabled) {
            progressDialog = new ProgressDialog(context, R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            try {
                 if (progressDialog != null && progressDialog.isShowing())
                progressDialog.show();
            } catch (Exception ex) {
                //prd.dismiss();
            }
        }
    }


    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1 / 8; // 1 = 100% if you write 4 means 1/4 = 25%
            // url = "http://s16.postimg.org/49v8bm7ad/2012_06_30_12_30_17.jpg";
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
        } catch (Exception e) {
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inSampleSize = 1 / 8; // 1 = 100% if you write 4 means 1/4 = 25%
//            // url = "http://s16.postimg.org/49v8bm7ad/2012_06_30_12_30_17.jpg";
//            InputStream in = null;
//            try {
//                in = new java.net.URL("http://mobileadminapi.shopincity.com/Images/NoImageAvailable.png").openStream();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
            Log.e("RESULT_ERROR", e.getMessage());
        }
        return bitmap;
    }


    protected void onPostExecute(Bitmap result) {

        if (isProgressEnabled) {
            if (progressDialog != null)
                progressDialog.dismiss();
        }

        if (OnImageDownloadComplete != null) {
            OnImageDownloadComplete.OnImageDownloadComplete(result);
        }
    }

    public interface OnImageDownloadComplete {
        void OnImageDownloadComplete(Bitmap result);
    }
}
