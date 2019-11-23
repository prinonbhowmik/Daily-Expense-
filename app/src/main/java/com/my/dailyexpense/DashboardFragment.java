package com.my.dailyexpense;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    private AutoCompleteTextView dashexptype;
    private ImageButton showexpBtn;
    String[] expenseType;
    private TextView fromdateId,todateId,totalexpenseId;
    private String [] exp_suggestion = {"Breakfast","Lunch","Dinner","Transport","Bill","Shopping","Medical","Payment","Insurance","Others"};
    private DatabaseHelper helper;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().setTitle("Dashboard");
        fromdateId = view.findViewById(R.id.fromdateId);
        todateId = view.findViewById(R.id.todateId);
        dashexptype=view.findViewById(R.id.spinnerId);
        showexpBtn=view.findViewById(R.id.showexpBtn);
        totalexpenseId = view.findViewById(R.id.totalexpenseId);
        ArrayAdapter ar = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,exp_suggestion);
        dashexptype.setAdapter(ar);
        dashexptype.setThreshold(1);
        helper = new DatabaseHelper(getContext());


        fromdateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        fromdateId.setText(dateFormat.format(date));
                        long milisec = date.getTime();
                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

        todateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        todateId.setText(dateFormat.format(date));
                        long milisec = date.getTime();
                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

        Cursor cursor1 = helper.calculateAll();
        String result = "";
        if (cursor1.moveToNext()){
            result = String.valueOf(cursor1.getInt(cursor1.getColumnIndex("TOTAL")));
            totalexpenseId.setText("BDT  "+result);
        }

        showexpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   Cursor cursor11 = helper.showamount(String.valueOf(fromdateId.getText()),String.valueOf(todateId.getText()));
                   String getresult = "";
                   if (cursor11.moveToNext()){
                       getresult = String.valueOf(cursor11.getInt(cursor11.getColumnIndex("MYTOTAL")));
                       totalexpenseId.setText("BDT  "+getresult);
                   }
            }

        });
         return view;
    }

}
