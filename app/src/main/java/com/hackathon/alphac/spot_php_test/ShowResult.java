package com.hackathon.alphac.spot_php_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ShowResult extends AppCompatActivity {
    TextView result;
    String resultMessage;
    String finalResult;
    Float percentageFloat;
    String filePath;
    ImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        result = (TextView)findViewById(R.id.result);
        preview = (ImageView)findViewById(R.id.image_preview_result);
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        DecimalFormat twoPlaceFormatter = new DecimalFormat(".##");
        resultMessage = intent.getStringExtra("results");
        try {
            JSONObject resultObject = new JSONObject(resultMessage);
            String percentage = resultObject.getString("percentage");
            String plant = resultObject.getString("plant");
            percentageFloat = Float.parseFloat(percentage);
            percentage = twoPlaceFormatter.format(percentageFloat);
            finalResult = Html.fromHtml("<b>PERCENTAGE:</b>") + percentage + "\n" + Html.fromHtml("<b>PLANT:</b>") + plant;
            result.setText(finalResult);


            final Bitmap bitmap = BitmapFactory.decodeFile(filePath);


            preview.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
