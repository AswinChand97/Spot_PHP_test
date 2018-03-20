package com.hackathon.alphac.spot_php_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ShowResult extends AppCompatActivity {
    TextView result;
    String resultMessage;
    String finalResult;
    Float percentageFloat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        result = (TextView)findViewById(R.id.result);
        Intent intent = getIntent();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
