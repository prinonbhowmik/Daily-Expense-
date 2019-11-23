package com.my.dailyexpense;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpenseFragment extends Fragment {

    private AutoCompleteTextView exptypeId;
    private String [] expenseType = {"Breakfast","Lunch","Dinner","Transport","Bill","Shopping","Medical","Payment","Insurance","Others"};
    private FloatingActionButton floatBtn;
    private DatabaseHelper helper;
    private List<GetExpense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView recyclerId;
    private TextView expfromdateId,exptodateId;
    private ImageButton search;
    private Context context;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_expense, container, false);

        getActivity().setTitle("Expense");
        expfromdateId = view.findViewById(R.id.expfromdateId);
        exptodateId = view.findViewById(R.id.exptodateId);
        recyclerId=view.findViewById(R.id.recyclerId);
        search = view.findViewById(R.id.searchBtn);


        helper = new DatabaseHelper(getContext());

        recyclerId.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList,getContext());
        recyclerId.setAdapter(expenseAdapter);

        exptypeId = view.findViewById(R.id.spinnerexpId);
        floatBtn = view.findViewById(R.id.floatBtn);
        ArrayAdapter ar = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,expenseType);
        exptypeId.setAdapter(ar);
        exptypeId.setThreshold(1);

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(),Add_Expense.class);
                startActivity(intent);
            }
        });

        expfromdateId.setOnClickListener(new View.OnClickListener() {
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
                        expfromdateId.setText(dateFormat.format(date));
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

        exptodateId.setOnClickListener(new View.OnClickListener() {
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
                        exptodateId.setText(dateFormat.format(date));
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
        final Cursor cursor = helper.showData();
        while (cursor.moveToNext()) {
            int amount = cursor.getInt(cursor.getColumnIndex(helper.COL_EXP_AMOUNT));
            String type = cursor.getString(cursor.getColumnIndex(helper.COL_EXP_TYPE));
            String date = cursor.getString(cursor.getColumnIndex(helper.COL_EXP_DATE));
            String time = cursor.getString(cursor.getColumnIndex(helper.COL_EXP_TIME));
            int id = cursor.getInt(cursor.getColumnIndex(helper.COL_ID));
            String image = cursor.getString(cursor.getColumnIndex(helper.COL_IMAGE));

            expenseList.add(new GetExpense(type, amount, time, date,id,image));
            expenseAdapter.notifyDataSetChanged();
        }

      search.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Cursor cursor1 = helper.searchData(String.valueOf(expfromdateId.getText()),String.valueOf(exptodateId.getText()));
              while (cursor1.moveToNext()){
                  expenseList.clear();
                  int id  = cursor1.getInt(cursor1.getColumnIndex(helper.COL_ID));
                  String type1 = cursor1.getString(cursor1.getColumnIndex(helper.COL_EXP_TYPE));
                  String date = cursor1.getString(cursor1.getColumnIndex(helper.COL_EXP_DATE));
                  String time = cursor1.getString(cursor1.getColumnIndex(helper.COL_EXP_TIME));
                  int amount1 = cursor1.getInt(cursor1.getColumnIndex(helper.COL_EXP_AMOUNT));
                  String image = cursor1.getString(cursor1.getColumnIndex(helper.COL_IMAGE));
                  expenseList.add(new GetExpense(type1,amount1,time,date,id,image));
                  expenseAdapter.notifyDataSetChanged();
              }
          }
      });

            return view;
    }
}
