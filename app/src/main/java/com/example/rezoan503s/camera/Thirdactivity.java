package com.example.rezoan503s.camera;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class Thirdactivity extends AppCompatActivity {
    FrameLayout canvas;
    Button Thirdactivity_ButtonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdactivity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Result_Pic_location = sharedPreferences.getString("Result Pic location", null);
        Log.d("picloaction", Result_Pic_location + " Now in thirdactivity");
        File edited_imagefile = new File(Result_Pic_location);
        Bitmap edited_bitmap = BitmapFactory.decodeFile(String.valueOf(edited_imagefile));
        Thirdactivity_ButtonShare=(Button)findViewById(R.id.Thirdactivity_ButtonShare);

        Log.d("picloaction", Result_Pic_location + " AfterFile");

        ImageView Thirdctivity_imageView = (ImageView) findViewById(R.id.Thirdctivity_imageView);
        Thirdctivity_imageView.setImageBitmap(edited_bitmap);
        final File[] allFiles;
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/" + "Shirts/");
        allFiles = folder.listFiles();
        canvas = (FrameLayout) findViewById(R.id.canvasView);
        StickerImageView iv_sticker = new StickerImageView(Thirdactivity.this);
        iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.tshirtplain));
        canvas.addView(iv_sticker);

        Thirdactivity_ButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }



}