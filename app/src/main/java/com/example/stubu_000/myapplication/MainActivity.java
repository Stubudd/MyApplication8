package com.example.stubu_000.myapplication;

import android.content.Intent;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView ImView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        //Not sure what this button will be, search button?
        //Button btnFilter = (Button)findViewById(R.id.btnFilter);
        ImView = (ImageView) findViewById(R.id.imageViewer);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        //btnFilter.setOnClickListener(filterListener);

        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery = populateGallery(minDate, maxDate);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);
    }
    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    public void search(View v) {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    private ArrayList<String> populateGallery(Date minDate, Date maxDate){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null){
            for (File f :file.listFiles()){
                photoGallery.add(f.getPath());
            }
        }
        return photoGallery;
    }
    private void displayPhoto(String path){
        ImageView iv = (ImageView) ImView;
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void onClick( View v) {
        switch (v.getId()){
            case R.id.btnLeft:
                --currentPhotoIndex;
                break;
            case R.id.btnRight:
                ++currentPhotoIndex;
                break;
            default:
                break;
        }
        if (currentPhotoIndex <0)
            currentPhotoIndex = 0;
        if (currentPhotoIndex >= photoGallery.size())
            currentPhotoIndex = photoGallery.size() - 1;

        currentPhotoPath = photoGallery.get(currentPhotoIndex);
        Log.d("photoleft, size", Integer.toString(photoGallery.size()));
        Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
        displayPhoto(currentPhotoPath);
    }

/*    public void goToSettings(View v) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void goToDisplay(String x) {
        Intent i = new Intent(this, DisplayActivity.class);
        i.putExtra("DISPLAY_TEXT", x);
        startActivity(i);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("ENDDATE"));

                photoGallery = populateGallery(new Date(), new Date());
                Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Log.d("createImageFile", "Picture Taken");
                photoGallery = populateGallery(new Date(), new Date());
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);
            }
        }
    }
    protected void takePic(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("FileCreation", "Failed");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.stubu_000.myapplication.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d("createImageFile", currentPhotoPath);

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


   /* private void setPic() {
        // Get the dimensions of the View
        int targetW = ImView.getWidth();
        int targetH = ImView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ImView.setImageBitmap(bitmap);
    }*/



}