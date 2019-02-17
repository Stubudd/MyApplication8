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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery;
    private ArrayList<File> photoGallery2;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView ImView;
    public ArrayList<Integer> indexPhotoArray;
    String classMinDate;
    String classMaxDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        Button btnCaption = (Button)findViewById(R.id.buttonCaption);
        //Not sure what this button will be, search button?
        Button Search = (Button)findViewById(R.id.search_search);
        ImView = (ImageView) findViewById(R.id.imageViewer);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnCaption.setOnClickListener(this);
        Search.setOnClickListener(filterListener);
        //timeTaken.setText(R.string.class(photoGallery(minDate,maxDate));

        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        classMinDate = "19500101";
        classMaxDate = "20301231";
        photoGallery = populateGallery(minDate, maxDate);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);
        File f = photoGallery2.get(currentPhotoIndex);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(f.lastModified()));
        updateTimeStamp(timeStamp);
        String caption = new String (f.getName());
        updateCaption(caption);

    }
    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            //indexPhotoArray = (ArrayList<Integer>) getIntent().getSerializableExtra("newPhotos");
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
            //Toast.makeText(this,data.getData().toString(), Toast.LENGTH_SHORT).show();
            //startActivityForResult(i,1);
        }
    };

    public String updateTimeStamp(String toThis){
        final TextView timeTaken = (TextView) findViewById(R.id.TimeStamp);
        timeTaken.setText(toThis);
        return toThis;
    }
    public String updateCaption (String toThis){
        final TextView captionAdded = (TextView) findViewById(R.id.Caption);
        captionAdded.setText(toThis);
        return toThis;
    }
    public String storeCaption (String toThis){
        final EditText newCaption = (EditText) findViewById(R.id.CaptionCaptured);
        newCaption.setText(toThis);
        return toThis;
    }
    public String getCaption (){
        String myString;
        final EditText newCaption = (EditText) findViewById(R.id.CaptionCaptured);
        myString = newCaption.getText().toString();
        return myString;
    }
    public void setCaption (String changeCaption){
        final TextView newCaption = (TextView) findViewById(R.id.Caption);
        newCaption.setText(changeCaption);

    }

    public void search(View v) {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    private ArrayList<String> populateGallery(Date minDate, Date maxDate){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        photoGallery = new ArrayList<String>();
        photoGallery2 = new ArrayList<File>();
        File[] fList = file.listFiles();
        if (fList != null){
            for (File f :file.listFiles()){
                photoGallery.add(f.getPath());
                photoGallery2.add(f);
            }
        }
        return photoGallery;
    }
    private void repopulateGalleryBasedOnCaption(String Caption){
        photoGallery2.clear();
        String fileName;
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        File[] fList = file.listFiles();
        if (fList != null){
            for (File f :file.listFiles()) {
                fileName = f.getName();
                String [] separated = fileName.split("_");
                String tempstring = separated[1];  //test2_.jpg
                if(tempstring.compareTo(Caption) == 0){
                    photoGallery2.add(f);
                }
                /*String currentString = "Fruit: they taste good";
                String[] separated = currentString.split(":");
                separated[0]; // this will contain "Fruit"
                separated[1];*/
            }

            }
            //return photoGallery;
    }

    private ArrayList<String> repopulateGallery(String newMinDate, String newMaxDate){
        photoGallery2.clear();
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        long fromStartDate = 0;
        long toEndDate = 0;

        try {
            SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMdd");

            Date endDate = toDateFormat.parse(newMaxDate);
            toEndDate = endDate.getTime();

            Date startDate = toDateFormat.parse(newMinDate);
            fromStartDate = startDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        File[] fList = file.listFiles();
        if (fList != null){
            for (File f :file.listFiles()){
                long lastModifiedDate = f.lastModified();
                if ((lastModifiedDate >= fromStartDate) && (lastModifiedDate <= toEndDate)) {
                    photoGallery2.add(f);
                }

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
        if(v.getId() == R.id.buttonCaption){
           File photoCaption = photoGallery2.get(currentPhotoIndex);
           photoCaption.setWritable(true);
           String captionName = getCaption();
           String tempName = "/Android/data/com.example.stubu_000.myapplication/files/Pictures/JPEG_"
                   + captionName + "_.jpg";
           File tempFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), tempName);
           //File tempFile = new File(photoCaption.getPath() + "Stu");
            tempFile.setWritable(true);
           //boolean test = tempFile.renameTo(photoCaption);
           boolean test2 = photoCaption.renameTo(tempFile);
           repopulateGallery(classMinDate, classMaxDate);
           setCaption(captionName);
           Toast.makeText(this,"Happy", Toast.LENGTH_SHORT).show();
           //String newCaption = new String(updateCaption(photoCaption.getName()));
           //storeCaption(newCaption);
        }
        else {
            switch (v.getId()) {
                case R.id.btnLeft:
                    --currentPhotoIndex;
                    break;
                case R.id.btnRight:
                    ++currentPhotoIndex;
                    break;
                default:
                    break;
            }
            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= photoGallery2.size())
                currentPhotoIndex = photoGallery2.size() - 1;
            File freshPhoto = photoGallery2.get(currentPhotoIndex);
            currentPhotoPath = freshPhoto.getPath();
            Log.d("photoleft, size", Integer.toString(photoGallery2.size()));
            Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
            displayPhoto(currentPhotoPath);
            //File f = photoGallery2.get(currentPhotoIndex);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(freshPhoto.lastModified()));
            String caption = new String(freshPhoto.getName());
            updateTimeStamp(timeStamp);
            updateCaption(caption);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                boolean testCaption = data.getBooleanExtra("captionSet", false);
                if(testCaption){
                    String captionSearched = data.getStringExtra("captionEntered");
                    repopulateGalleryBasedOnCaption(captionSearched);
                    currentPhotoIndex = 0;
                    File firstNewPhoto = photoGallery2.get(currentPhotoIndex);
                    currentPhotoPath = firstNewPhoto.getPath();
                    displayPhoto(currentPhotoPath);
                }else {
                    Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                    Log.d("createImageFile", data.getStringExtra("ENDDATE"));

                    //Toast.makeText(this,data.getData().toString(), Toast.LENGTH_SHORT).show();
                    data.getStringExtra("minDate");
                    //Toast.makeText(this,data.getStringExtra("minDate"), Toast.LENGTH_SHORT).show();
                    photoGallery = repopulateGallery(data.getStringExtra("minDate"), data.getData().toString());
                    classMinDate = data.getStringExtra("minDate");
                    classMaxDate = data.getData().toString();
                    Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                    currentPhotoIndex = 0;
                    File firstNewPhoto = photoGallery2.get(currentPhotoIndex);
                    currentPhotoPath = firstNewPhoto.getPath();
                    displayPhoto(currentPhotoPath);
                }
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
        String timeStampTemp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Date lastModDate = new Date(file.lastModified());
        //Log.i("File last modified @ : "+ lastModDate.toString());
        String imageFileName = "JPEG_" + timeStampTemp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(image.lastModified()));
        Log.d("createImageFile", currentPhotoPath);
        timeStamp = image.getPath();
        updateTimeStamp(timeStamp);
        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }



}