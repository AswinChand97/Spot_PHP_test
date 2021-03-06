package com.hackathon.alphac.spot_php_test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri = null;
    String picPath = null;
    String filePath = null;
    int count = 0;
    private ImageButton btnCapturePicture, btnChooseFromGallery;
    ImageButton addFab,changeLanguage;
    private Uri selectedUri = null;
    LinearLayout takePictureContainer, pickFromGalleryContainer;
    PrefManager pm;
    Animation fadeIn1,fadeOut1,fadeOut2,fadeIn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCapturePicture = (ImageButton) findViewById(R.id.btnCapturePicture);
        btnChooseFromGallery = (ImageButton) findViewById(R.id.btnChooseFromGallery);
        addFab = (ImageButton) findViewById(R.id.pictureFab);
        takePictureContainer = (LinearLayout) findViewById(R.id.takePictureContainer);
        pickFromGalleryContainer = (LinearLayout) findViewById(R.id.pickFromGalleryContainer);
        changeLanguage = (ImageButton)findViewById(R.id.changeLanguage);
        pm = new PrefManager(this);
        fadeIn1 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in1);
        fadeIn2 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in2);
        fadeOut1 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out1);
        fadeOut2 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out2);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count++;
                if (count % 2 != 0) {
                    addFab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_close_black_48dp));
                    takePictureContainer.setClickable(true);
                    pickFromGalleryContainer.setClickable(true);
                    takePictureContainer.startAnimation(fadeIn1);
                    pickFromGalleryContainer.startAnimation(fadeIn2);
                } else {
                    addFab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_add_circle_black_48dp));
                    takePictureContainer.startAnimation(fadeOut1);
                    pickFromGalleryContainer.startAnimation(fadeOut2);
                    takePictureContainer.setClickable(false);
                    pickFromGalleryContainer.setClickable(false);
                }


            }
        });

        takePictureContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


        pickFromGalleryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseFromGallery();
            }
        });
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromGallery();
            }
        });

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pm.increment();
                int num = pm.returnLanguageCount();
                Log.e("number",num+"");
                if(num%2==1) {
                    changeLanguage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.ab));
                    pm.setTamil(true);
                    Toast.makeText(MainActivity.this,"Language set to Tamil",Toast.LENGTH_SHORT).show();
                }
                else {
                    changeLanguage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.aa));
                    pm.setTamil(false);
                    Toast.makeText(MainActivity.this,"Language set to English!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();

            finish();
        }
    }


    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST_CODE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                launchUploadActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedUri = data.getData();
                picPath = getRealPathFromURI(selectedUri, this);
                launchUploadActivity(false);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled picking process", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to pick the photo from the gallery", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void launchUploadActivity(boolean isCaptured) {
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        if (isCaptured) {
            Log.d("fileUri", fileUri.getPath());
            filePath = fileUri.getPath();
        } else {
            Log.d("galleryPick", picPath);
            filePath = picPath;
        }
        i.putExtra("filePath", filePath);
        i.putExtra("isCaptured", isCaptured);
        startActivity(i);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = getContentResolver().query(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            cursor.close();
            return s;
        }
        cursor.close();
        return null;
    }
}