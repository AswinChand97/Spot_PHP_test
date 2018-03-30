package com.hackathon.alphac.spot_php_test;

import com.hackathon.alphac.spot_php_test.AndroidMultiPartEntity;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private Button btnUpload;
    long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        boolean isCaptured = i.getBooleanExtra("isCaptured", true);

        if (filePath != null) {
            previewMedia(isCaptured);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new UploadFileToServer().execute();
            }
        });

    }


    private void previewMedia(boolean isCaptured) {


        imgPreview.setVisibility(View.VISIBLE);


        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);


        imgPreview.setImageBitmap(bitmap);

    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            Log.d("positionHere", "I am here");
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            progressBar.setVisibility(View.VISIBLE);


            progressBar.setProgress(progress[0]);


            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("doInBackground", "here");
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            Log.d("uploadFile", "here");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.SERVER_URL);
            String boundary = "-------------" + System.currentTimeMillis();
            //httppost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                Log.d("try method", "here");
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                entity.addPart("photo", new FileBody(sourceFile));
                Log.d("added photo", "here");

                entity.addPart("name",
                        new StringBody("Alpha 6c"));
                entity.addPart("email", new StringBody("alpha6c@gmail.com"));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                Log.d("making server call", "here");

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                Log.d("catch block of exp", "here");
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            showAlert(result);

            super.onPostExecute(result);
        }

    }

    private void showAlert(String message) {
        Intent intent = new Intent(UploadActivity.this, ShowResult.class);
        intent.putExtra("results", message);
        intent.putExtra("filePath", filePath);
        startActivity(intent);

    }

}