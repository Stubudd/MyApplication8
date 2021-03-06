package com.example.stubu_000.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements Serializable {

    long toEndDate;
    long fromStartDate;
    private EditText fromDate;
    private EditText toDate;
    private DatePickerDialog.OnDateSetListener fromListener;
    private DatePickerDialog.OnDateSetListener toListener;
    private ArrayList<File> photoGallery2;
    private ArrayList<File> updatedPhotoGallery;
    private ArrayList<Integer> indexPhotoArray;
    private int currentPhotoIndex = 0;
    Intent data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fromDate = (EditText) findViewById(R.id.search_fromDate);
        toDate = (EditText) findViewById(R.id.search_toDate);
        TextView ToDateLabel = (TextView) findViewById(R.id.search_toDateLabel);
        TextView FromDateLabel = (TextView) findViewById(R.id.search_fromDate);
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery2 = populateGallery(minDate, maxDate);
        data = new Intent(SearchActivity.this, MainActivity.class);
    }

    public String displayToDate(String update) {
        TextView ToDate = (TextView) findViewById(R.id.search_toDateLabel);
        ToDate.setText(update);
        return update;
    }

    public String displayFromDate(String update) {
        final TextView FromDate = (TextView) findViewById(R.id.search_fromDateLabel);
        FromDate.setText(update);
        return update;
    }
    public String getFromDate (){
        String myString;
        final EditText fromDate = (EditText) findViewById(R.id.search_fromDate);
        myString = fromDate.getText().toString();
        return myString;
    }
    public String getToDate (){
        String myString;
        final EditText toDate = (EditText) findViewById(R.id.search_toDate);
        myString = toDate.getText().toString();
        return myString;
    }
    public String getCaption (){
        String myString;
        final EditText newCaption = (EditText) findViewById(R.id.search_toCaption);
        myString = newCaption.getText().toString();
        return myString;
    }
    public String getLongitude (){
        String myString;
        final EditText newLongitude = (EditText) findViewById(R.id.search_toLongitude);
        myString = newLongitude.getText().toString();
        return myString;
    }
    public String getLatitude (){
        String myString;
        final EditText newLatitude = (EditText) findViewById(R.id.search_toLatitude);
        myString = newLatitude.getText().toString();
        return myString;
    }


    public void cancel(final View v) {
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
        //finish();
    }

    public void search(final View v) {
        String tempFromDate = getFromDate();
        String tempToDate = getToDate();
        String tempCaption = getCaption();
        String tempLongitude = getLongitude();
        String tempLatitude = getLatitude();
        boolean fromDateSearched = tempFromDate != null && !tempFromDate.isEmpty() && !tempFromDate.equals("null");
        boolean toDateSearched = tempToDate != null && !tempToDate.isEmpty() && !tempToDate.equals("null");
        boolean tempCaptionSearched = tempCaption != null && !tempCaption.isEmpty() && !tempCaption.equals("null");
        boolean tempLongitudeSearched = tempLongitude != null && !tempLongitude.isEmpty() && !tempLongitude.equals("null");
        boolean tempLatitudeSearched = tempLatitude != null && !tempLatitude.isEmpty() && !tempLatitude.equals("null");

        if(fromDateSearched && toDateSearched) {
            data.putExtra("STARTDATE", fromDate.getText().toString());
            data.putExtra("ENDDATE", toDate.getText().toString());
            String endDateInput = toDate.getText().toString();
            String startDateInput = fromDate.getText().toString();


            try {
                SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMdd");

                Date endDate = toDateFormat.parse(endDateInput);
                toEndDate = endDate.getTime();

                Date startDate = toDateFormat.parse(startDateInput);
                fromStartDate = startDate.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            displayFromDate(startDateInput);
            displayToDate(endDateInput);
            updatedPhotoGallery = iterateGallery(fromStartDate, toEndDate);
            data.putExtra("minDate", startDateInput);

            data.setData(Uri.parse(endDateInput));

            setResult(RESULT_OK, data);
            finish();
        }
        else if(tempCaptionSearched){
            data.putExtra("captionEntered", tempCaption);
            data.putExtra("captionSet", true);
            setResult(RESULT_OK, data);
            finish();
        }
        else if(tempLatitudeSearched && tempLongitudeSearched){
            data.putExtra("longitudeEntered", tempLongitude);
            data.putExtra("latitudeEntered", tempLatitude);
            data.putExtra("longLatSet", true);
            setResult(RESULT_OK, data);
            finish();
        }
        else {
            Intent startNewActivity = new Intent(this, MainActivity.class);
            startActivity(startNewActivity);
        }

    }

    private ArrayList<File> populateGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        photoGallery2 = new ArrayList<File>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : file.listFiles()) {
                photoGallery2.add(f);
            }
        }
        return photoGallery2;
    }

    public ArrayList<File> iterateGallery(Long minDate, Long maxDate) {
        int indexArray = 0;
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        updatedPhotoGallery = new ArrayList<File>();
        indexPhotoArray = new ArrayList<Integer>();
        intent.putExtra("newPhotos", indexPhotoArray);
        File[] fList = file.listFiles();

        if (fList != null) {
            for (int i = 0; i < fList.length; ++i ) {
                File f = fList[i];
                long lastModifiedDate = f.lastModified();
                Date d = new Date(file.lastModified());
                long time = d.getTime();
                if ((lastModifiedDate >= minDate) && (lastModifiedDate <= maxDate)) {
                    indexPhotoArray.add(indexArray);
                    updatedPhotoGallery.add(f);
                }
                indexArray++;
            }
        }
        return updatedPhotoGallery;
    }

}

