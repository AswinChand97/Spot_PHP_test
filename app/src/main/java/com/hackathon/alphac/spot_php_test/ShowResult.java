package com.hackathon.alphac.spot_php_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ShowResult extends AppCompatActivity {
    TextView result;
    String resultMessage;
    String finalResult;
    Float percentageFloat;
    String filePath;
    ImageView preview;
    ListView lv;
    ArrayList<String> text;
    ArrayList<ArrayList<String>> plantsImagesSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        result = (TextView)findViewById(R.id.result);
        preview = (ImageView)findViewById(R.id.image_preview_result);
        lv = (ListView)findViewById(R.id.listView);
        text = new ArrayList<String>();
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        //DecimalFormat twoPlaceFormatter = new DecimalFormat(".##");
        resultMessage = intent.getStringExtra("results");
        try {
            JSONArray results = new JSONArray(resultMessage);
            for(int i=0;i<results.length();i++)
            {
                JSONObject obj = results.getJSONObject(i);
                String plant = obj.getString("plant");
                String percentage = obj.getString("percentage");
                String symptoms = obj.getString("symptoms");
                JSONArray plantImages = obj.getJSONArray("images");
                String plants[];
                plants = new String[plantImages.length()];
                for(int j=0;j<plantImages.length();j++)
                {
                    plants[j] = plantImages.getString(j);

                }
                ArrayList<String> plantImagesSend = new ArrayList<String>(Arrays.asList(plants));
                plantsImagesSend.add(plantImagesSend);
                String totalText = "Plant:"+plant+"\n"+"Percentage:"+percentage+"\n"+"Symptoms:"+symptoms;
                text.add(totalText);

            }
//            JSONObject resultObject = new JSONObject(resultMessage);
//            String percentage = resultObject.getString("percentage");
//            String plant = resultObject.getString("plant");
//            percentageFloat = Float.parseFloat(percentage);
//            percentage = twoPlaceFormatter.format(percentageFloat);
//            finalResult = Html.fromHtml("<b>PERCENTAGE:</b>") + percentage + "\n" + Html.fromHtml("<b>PLANT:</b>") + plant;
//            result.setText(finalResult);


            final Bitmap bitmap = BitmapFactory.decodeFile(filePath);


            preview.setImageBitmap(bitmap);
            lv.setAdapter(new ResultAdapter(ShowResult.this,text,plantsImagesSend));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
