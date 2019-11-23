package com.my.dailyexpense;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.Bitmap.CompressFormat.JPEG;

public class Add_Expense extends AppCompatActivity {

    private EditText addamount,adddate,addtime;
    private Button addimgBtn,addexpBtn;
    private AutoCompleteTextView addexptypeId;
    private ImageView addimage;
    private ImageButton closeimg;
    private DatabaseHelper helper;
    private RelativeLayout imglayout;
    private String [] exp_suggestion = {"Breakfast","Lunch","Dinner","Transport","Bill","Shopping","Medical","Payment","Insurance","Others"};

    String [] expensetype;
    String type,amount,date,time,imagestring=null;
    int updateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__expense);
        setTitle("Add Expense");

        init();



       adddate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getDate();
           }
       });

       addtime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getTime();
           }
       });

       addimgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               imglayout.setVisibility(View.VISIBLE);
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(intent, 0);
           }
       });

       closeimg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addimage.setImageBitmap(null);
               imglayout.setVisibility(View.GONE);
           }
       });

       addexpBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (addexptypeId==null){
                   Toast.makeText(Add_Expense.this,"Please Enter Expense Type",Toast.LENGTH_LONG);
               }
             else if (addamount==null){
                 Toast.makeText(Add_Expense.this, "Please enter amount", Toast.LENGTH_SHORT).show();
             }
             else if (adddate==null) {
                 Toast.makeText(Add_Expense.this, "Please enter date", Toast.LENGTH_SHORT).show();
             }
             else{
                 String exp_type = addexptypeId.getText().toString();
                 String amount =  addamount.getText().toString();
                 String date = adddate.getText().toString();
                 String time = addtime.getText().toString();

                 long id = helper.insertData(exp_type,date,time,amount,imagestring);
                 Toast.makeText(Add_Expense.this, "Data added & id "+id, Toast.LENGTH_SHORT).show();
             }
           }
       });

       updateData();

    }

    private void updateData() {
        Intent intent = getIntent();
        String newId = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        String Amount = intent.getStringExtra("amount");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        if (newId!=null){

            addexpBtn.setText("Update Expense");
            setTitle("Update Expense");
            addexptypeId.setText(type);
            updateId = Integer.valueOf(newId);
            addimgBtn.setVisibility(View.GONE);
            addamount.setText(Amount);
            adddate.setText(date);
            addtime.setText(time);
            addexpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String type = addexptypeId.getText().toString();
                    String amount = addamount.getText().toString();
                    String date = adddate.getText().toString();
                    String time = addtime.getText().toString();
                    helper.updateData(type,amount,updateId,date,time);
                    Toast.makeText(Add_Expense.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


    public void getTime(){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(Add_Expense.this);

        View view = getLayoutInflater().inflate(R.layout.custom_timepicker,null);

        final TimePicker timePicker = view.findViewById(R.id.timepicker);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
                @SuppressLint({"NewApi", "LocalSuppress"}) int hour = timePicker.getHour();
                @SuppressLint({"NewApi", "LocalSuppress"}) int min = timePicker.getMinute();

                Time time = new Time(hour,min,0);
                addtime.setText(timeFormat.format(time));
                dialog.dismiss();
            }
        });
    }

    private void getDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String currentDate = year+"/"+month+"/"+day;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                adddate.setText(dateFormat.format(date));
                long milisec = date.getTime();
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Expense.this, dateSetListener,year,month,day);
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                Bitmap cameraimage = (Bitmap) bundle.get("data");
                addimage.setImageBitmap(cameraimage);
                imagestring =encodeToBase64(cameraimage,JPEG,100);
            }
        }
    }
    private void init() {
        addamount = findViewById(R.id.addamount);
        adddate = findViewById(R.id.adddate);
        addtime = findViewById(R.id.addtime);
        addimage = findViewById(R.id.addimage);
        addexpBtn = findViewById(R.id.addexpBtn);
        addimgBtn = findViewById(R.id.addimgBtn);
        addexptypeId = findViewById(R.id.spinneraddId);
        imglayout = findViewById(R.id.imgLayout);
        closeimg = findViewById(R.id.closeimg);
        helper = new DatabaseHelper(this);
        ArrayAdapter ar = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,exp_suggestion);
        addexptypeId.setAdapter(ar);
        addexptypeId.setThreshold(1);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}
