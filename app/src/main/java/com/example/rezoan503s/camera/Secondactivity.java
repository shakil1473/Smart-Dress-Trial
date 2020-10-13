package com.example.rezoan503s.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Secondactivity extends AppCompatActivity {
    String mCurrentPhotoPath="hello";
    int count = 0;
    Bitmap mBitmap;
    Bitmap scaledbitmap;
    ImageView mImageView;
    int touchCount = 0;
    String tempfilepath;
    Point t1;
    Point br;
    float x=0;
    float y=0;
    float x2=0;
    float y2=0;
    boolean targetChose = false;
    ProgressBar secondactivity_progressBar;
    private static final String TAG = "Opencv";
    String result_choosen;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);
        t1=new Point();
        br=new Point();
        mImageView = (ImageView) findViewById(R.id.secondactivity_imageView);
        count = getIntent().getIntExtra("arg", 0);

        secondactivity_progressBar=(ProgressBar)findViewById(R.id.secondactivity_progressBar);

        String mCurrentPhotoPath = getIntent().getStringExtra("Openimagepicture");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        result_choosen = sharedPreferences.getString("Result choosen", null);
        Log.d("Sharedpref", result_choosen);

        // result choose=10 means camera

        if (result_choosen.equals("10")) {
            Log.d("Cameraopended", "opended");
            File imgFile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/" + "picturenumber" + count + ".jpg");
            BitmapFactory.Options opt=new BitmapFactory.Options();
            opt.inMutable=true;
            Bitmap mBitmap = BitmapFactory.decodeFile(String.valueOf(imgFile),opt);
            scaledbitmap=Bitmap.createScaledBitmap(mBitmap,1000,1200,true);
            try {
                Log.d("cameraaopended", "opended");

                File temp=File.createTempFile("filename",".jpg", this.getCacheDir());

                Log.d("aftertemp","Beforefos");

                FileOutputStream out = new FileOutputStream(temp);

                Log.d("aftertemp","afterfos");

                String s = temp.getName();
                tempfilepath=temp.getPath();
                Log.d("tempfilename",s);
                Log.d("tempfiledir",tempfilepath);
                scaledbitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);


            } catch (IOException e) {
                Log.d("aftertemp","aftertempincatch");
                e.printStackTrace();
            }
            mImageView.setImageBitmap(scaledbitmap);


            mCurrentPhotoPath=Environment.getExternalStorageDirectory().toString() + "/Pictures/" + "picturenumber" + count + ".jpg";
        }



        // result choose=1 means gallery
        if (result_choosen.equals("1")) {
            mCurrentPhotoPath = getIntent().getStringExtra("Openimagepicture");
            File openimgFile = new File(mCurrentPhotoPath);
            BitmapFactory.Options opt=new BitmapFactory.Options();
            opt.inMutable=true;
            Bitmap mBitmap = BitmapFactory.decodeFile(String.valueOf(openimgFile),opt);
            scaledbitmap=Bitmap.createScaledBitmap(mBitmap,900,1000,true);

            try {
                Log.d("Galleryaopended", "opended");

                File temp=File.createTempFile("filename",".jpg", this.getCacheDir());

                Log.d("aftertemp","Beforefos");

                FileOutputStream out = new FileOutputStream(temp);

                Log.d("aftertemp","afterfos");

                String s = temp.getName();
                tempfilepath=temp.getPath();
                Log.d("tempfilename",s);
                Log.d("tempfiledir",tempfilepath);
                scaledbitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);


            } catch (IOException e) {
                Log.d("aftertemp","aftertempincatch");
                e.printStackTrace();
            }

            mImageView.setImageBitmap(scaledbitmap);


        }




        Log.d("current", mCurrentPhotoPath);
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "2ndactivity cv Loaded");
            mImageView = (ImageView) findViewById(R.id.secondactivity_imageView);

            final String finalMCurrentPhotoPath = tempfilepath;
            Log.d("aftertemp",finalMCurrentPhotoPath);
            mImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        if (touchCount == 0) {
                              x = event.getX();
                              y = event.getY();
                            Log.d("findxandy",String.valueOf(x));
                            touchCount++;
                        }
                        else if (touchCount == 1) {
                             x2 = event.getX();
                             y2= event.getY();

                            Paint rectPaint = new Paint();
                            rectPaint.setARGB(255, 255, 203,0);
                            rectPaint.setStyle(Paint.Style.STROKE);
                            rectPaint.setStrokeWidth(3);


                            File openimgFile = new File(finalMCurrentPhotoPath);
                            Bitmap mBitmap = BitmapFactory.decodeFile(String.valueOf(openimgFile));
                            Log.d("aftertemp",String.valueOf(mBitmap.getWidth())+"hello");
                            Bitmap tmpBm = Bitmap.createBitmap(mBitmap.getWidth(),
                                    mBitmap.getHeight(), Bitmap.Config.RGB_565);

                            Canvas tmpCanvas = new Canvas(tmpBm);

                            Log.d("findxandy",String.valueOf(x));

                            tmpCanvas.drawBitmap(mBitmap, 0, 0, null);
                            tmpCanvas.drawRect(new RectF(x, y, x2, y2),
                                    rectPaint);
                            mImageView.setImageDrawable(new BitmapDrawable(getResources(), tmpBm));

                            targetChose = true;
                            touchCount = 0;
                            mImageView.setOnTouchListener(null);
                        }
                    }


                    return true;

                }

            });


        } else {
            Log.d(TAG, "2nd activity cv Not loaded");
        }

    }

    public void exec(View view) {
         Log.d("backlock","Now in exec");
        new PreocessImage().execute();
    }


    public class PreocessImage extends AsyncTask<Integer,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            secondactivity_progressBar.setMax(100);

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            //UPDATE PROGRESSBAR

            secondactivity_progressBar.setProgress(values[0]);
        }
        @Override
        protected String doInBackground(Integer... params) {
            Log.d("backlock","Now in back");
            String picloation="This is processed pic location";
            if(result_choosen.equals("10")) {
                mCurrentPhotoPath = tempfilepath;
                Log.d("aftertemp",mCurrentPhotoPath+"     for camera");
            }
            else {
                mCurrentPhotoPath = tempfilepath;
                Log.d("aftertemp", mCurrentPhotoPath + "     for gallery");
            }
                t1.x=x;
                t1.y=y;
                br.x=x2;
                br.y=y2;
                Mat img = Imgcodecs.imread(mCurrentPhotoPath);
                Mat background = new Mat(img.size(), CvType.CV_8UC3,
                        new Scalar(255, 255, 255));
                Mat firstMask = new Mat();
                Mat bgModel = new Mat();
                Mat fgModel = new Mat();
                Mat mask;
                publishProgress(5);
                Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
                Mat dst = new Mat();
                publishProgress(25);
                Rect rect = new Rect(t1, br);
                publishProgress(33);
                Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel,
                        5, Imgproc.GC_INIT_WITH_RECT);
                Core.compare(firstMask, source, firstMask, Core.CMP_EQ);

                Mat foreground = new Mat(img.size(), CvType.CV_8UC3,
                        new Scalar(255, 255, 255));
                img.copyTo(foreground, firstMask);
                publishProgress(40);
                Scalar color = new Scalar(255, 0, 0, 255);
                Imgproc.rectangle(img, t1, br, color);

                Mat tmp = new Mat();
                Imgproc.resize(background, tmp, img.size());
                background = tmp;
                mask = new Mat(foreground.size(), CvType.CV_8UC1,
                        new Scalar(255, 255, 255));
                publishProgress(60);
                Imgproc.cvtColor(foreground, mask, Imgproc.COLOR_BGR2GRAY);
                Imgproc.threshold(mask, mask, 254, 255, Imgproc.THRESH_BINARY_INV);
                System.out.println();
                Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));
                background.copyTo(dst);

                background.setTo(vals, mask);
                publishProgress(70);
                Core.add(background, foreground, dst, mask);

                firstMask.release();
                source.release();
                bgModel.release();
                fgModel.release();
                vals.release();
                SimpleDateFormat sdf = null;
                publishProgress(90);
                String currentDateandTime = "Notset";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentDateandTime = sdf.format(new Date());
                }
                picloation=Environment.getExternalStorageDirectory().toString() + "/Pictures/" + "Editedpic  " + currentDateandTime + ".jpg";
                Imgcodecs.imwrite(picloation, dst);

                publishProgress(100);


            return picloation;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.d("picloaction",result);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("Result Pic location", result);

            editor.commit();

            Intent secondactivityintent = new Intent(Secondactivity.this,Thirdactivity.class);
            startActivity(secondactivityintent);
        }

    }


}




