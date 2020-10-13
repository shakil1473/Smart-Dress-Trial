package com.example.rezoan503s.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    int count = 0;
    String mCurrentPhotoPath;
    Bitmap mBitmap;
    ImageView mImageView;
    int touchCount = 0;
    Point tl;
    Point br;
    boolean targetChose = false;
    ProgressDialog dlg;
    static final int REQUEST_OPEN_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
    private static final String TAG = "Opencv";

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Main Activity CV Loaded");
        } else {
            Log.d(TAG, "Main Activity CV Not loaded");
        }
    }
    Integer TAKE_PICTURE=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FloatingActionButton mainmenu_floatingbutton_add = (FloatingActionButton) findViewById(R.id.mainmenu_floatingbutton_add);

        mainmenu_floatingbutton_add.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {

                                                               count++;
                                                               File imagefile = new File(path, "picturenumber" + count + ".jpg");
                                                               Uri uri = Uri.fromFile(imagefile);

                                                               Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                               i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                               Integer TAKE_PICTURE=10;
                                                               startActivityForResult(i,TAKE_PICTURE);
                                                           }
                                                       }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == RESULT_OK) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("Result choosen", String.valueOf(requestCode));

            editor.commit();

            // Make sure the request was successful
            if (requestCode == TAKE_PICTURE ) {

                Log.d("scndactiv", "now in ONactivityresutl");

                Intent secondactivityintent = new Intent(MainActivity.this, Secondactivity.class);
                secondactivityintent.putExtra("arg",count);
                startActivity(secondactivityintent);

            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Intent secondactivityintent = new Intent(MainActivity.this, Secondactivity.class);
                secondactivityintent.putExtra("Openimagepicture", selectedImagePath);
                startActivity(secondactivityintent);

            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if git is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_open_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);




        }
        return super.onOptionsItemSelected(item);
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            Log.d("null","Uri_null");
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

}