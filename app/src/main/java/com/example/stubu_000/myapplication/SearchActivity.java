package com.example.stubu_000.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    long toEndDate;
    long fromStartDate;
    private EditText fromDate;
    private EditText toDate;
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private DatePickerDialog.OnDateSetListener fromListener;
    private DatePickerDialog.OnDateSetListener toListener;
    private ArrayList<File> photoGallery2;
    private int currentPhotoIndex = 0;

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
    }

    public String displayToDate(String update){
        TextView ToDate = (TextView) findViewById(R.id.search_toDateLabel);
        ToDate.setText(update);
        return update;
    }

    public String displayFromDate(String update){
        final TextView FromDate =  (TextView) findViewById(R.id.search_fromDateLabel);
        FromDate.setText(update);
        return update;
    }


    public void cancel(final View v) {
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
        //finish();
    }

    public void search(final View v) {
        Intent i = new Intent(SearchActivity.this, MainActivity.class);
        i.putExtra("STARTDATE", fromDate.getText().toString());
        i.putExtra("ENDDATE", toDate.getText().toString());
        setResult(RESULT_OK, i);
        String endDateInput = toDate.getText().toString();
        String startDateInput = fromDate.getText().toString();

        try {
            SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMdd");

            Date endDate = toDateFormat.parse(endDateInput);
            toEndDate = endDate.getTime();

            Date startDate = toDateFormat.parse(startDateInput);
            fromStartDate = startDate.getTime();

        } catch(ParseException e) {
        e.printStackTrace();
        }

        displayFromDate(startDateInput);
        displayToDate(endDateInput);


        //finish();

    }
    private ArrayList<File> populateGallery(Date minDate, Date maxDate){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.stubu_000.myapplication/files/Pictures");
        photoGallery2 = new ArrayList<File>();
        File[] fList = file.listFiles();
        if (fList != null){
            for (File f :file.listFiles()){
                photoGallery2.add(f);
            }
        }
        return photoGallery2;
    }



}

