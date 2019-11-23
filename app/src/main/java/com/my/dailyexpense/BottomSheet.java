package com.my.dailyexpense;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private TextView showtype,showamount,showdate,showtime;
    private ImageView showimageId;
    private Button showdocument;
    private String image=null;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_expense_details,container,false);

        showtype = view.findViewById(R.id.showtype);
        showamount = view.findViewById(R.id.showamount);
        showdate = view.findViewById(R.id.showdate);
        showtime = view.findViewById(R.id.showtime);
        showdocument = view.findViewById(R.id.showdocument);
        showimageId = view.findViewById(R.id.showimageId);


        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.get("image")!=null) {
                String type = bundle.get("type").toString();
                String amount = bundle.get("amount").toString();
                String date = bundle.get("date").toString();
                String time = bundle.get("time").toString();
                image = bundle.get("image").toString();


                Bitmap bitmap = decodeBase64(image);
                showtype.setText(type);
                showamount.setText(amount);
                showdate.setText(date);
                showtime.setText(time);
                showimageId.setImageBitmap(bitmap);
            }
            else{
                String type = bundle.get("type").toString();
                String amount = bundle.get("amount").toString();
                String date = bundle.get("date").toString();
                String time = bundle.get("time").toString();

                showtype.setText(type);
                showamount.setText(amount);
                showdate.setText(date);
                showtime.setText(time);

            }
        }

        showdocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimageId.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
