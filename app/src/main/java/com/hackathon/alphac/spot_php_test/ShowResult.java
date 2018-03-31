package com.hackathon.alphac.spot_php_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    String filePath;
    ImageView preview;
    ListView lv;
    ArrayList<String> text;
    ArrayList<ArrayList<String>> plantsImagesSend;
//    final String PLANT_HEADING = Html.fromHtml("<b>Plant:</b>")+"";
//    final String DISEASE_HEADING = Html.fromHtml("<b>Disease:</b>")+"";
//    final String PER_HEADING = Html.fromHtml("<b>Percentage:</b>")+"";
//    final String DESC_HEADING = Html.fromHtml("<b>Description:</b>")+"";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        //result = (TextView)findViewById(R.id.result);
        preview = (ImageView)findViewById(R.id.image_preview_result);
        lv = (ListView)findViewById(R.id.listView);
        text = new ArrayList<String>();
        plantsImagesSend = new ArrayList<ArrayList<String>>();
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        //DecimalFormat twoPlaceFormatter = new DecimalFormat(".##");
        resultMessage = intent.getStringExtra("results");
        //Log.e("Results",resultMessage);
        try {
            JSONObject jo = new JSONObject(resultMessage);
            JSONArray results = jo.getJSONArray("results");
            Log.e("Result Length",results.length()+" ");
            for(int i=0;i<3;i++)
            {
                JSONObject obj = results.getJSONObject(i);
                String plant = obj.getString("plant");
                Log.e("Plant from object",plant);
                String percentage = obj.getString("percentage");
                String disease = obj.getString("disease");
                String totalText = Html.fromHtml("<b>Plant:</b>")+plant+"\n"+Html.fromHtml("<b>Percentage:</b>")+percentage+"\n"+Html.fromHtml("<b>Disease:</b>")+disease+"\n"+Html.fromHtml("<b>Description:</b>");
                String description = obj.getString("description");
                StringBuilder sb = new StringBuilder();
                String[] keyValuePairs = description.split("%%");
                TextView[] tv ;
                tv = new TextView[keyValuePairs.length];
                for(int k=0;k<keyValuePairs.length;k++)
                {
                    String key = keyValuePairs[k].split("##")[0];
                    String value = keyValuePairs[k].split("##")[1];
                    sb.append(Html.fromHtml("<b>"+key+"</b>"));
                    sb.append(value);
                    sb.append("\n");
                }
                totalText+=sb.toString();
                JSONArray plantImages = obj.getJSONArray("images");
               ArrayList<String> plants = new ArrayList<String>();
                for(int j=0;j<plantImages.length();j++)
                {
                    plants.add(plantImages.getString(j));
                    Log.e("Plant image name",plants.get(j));

                }
                plantsImagesSend.add(plants);
                text.add(totalText);
                System.out.println("Text:"+totalText+"-Images:"+plants);

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
