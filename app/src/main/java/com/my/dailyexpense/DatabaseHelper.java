package com.my.dailyexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;



import java.sql.Blob;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "expense.db";
    private static String TABLE_NAME = "expense";
    public static String COL_EXP_TYPE = "Exp_type";
    public static String COL_EXP_DATE = "Exp_date";
    public static String COL_EXP_TIME = "Exp_time";
    public static String COL_EXP_AMOUNT = "Exp_amount";
    public static String COL_ID = "Id";
    public static String COL_IMAGE = "Image";
    private static int VERSION = 1;
    private String expensetable = "create table "+TABLE_NAME+"(Id INTEGER PRIMARY KEY AUTOINCREMENT,Exp_type TEXT,Exp_amount INTEGER,Exp_date LONG,Exp_time TEXT,Image TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME , null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(expensetable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long insertData(String Exp_type, String Exp_date, String Exp_time, String Exp_amount,String Image){
        ContentValues cv = new ContentValues();
        cv.put(COL_EXP_TYPE,Exp_type);
        cv.put(COL_EXP_DATE,Exp_date);
        cv.put(COL_EXP_TIME,Exp_time);
        cv.put(COL_EXP_AMOUNT,Exp_amount);
        cv.put(COL_IMAGE,Image);
        SQLiteDatabase sq = getWritableDatabase();
        long id = sq.insert(TABLE_NAME,null,cv);
        sq.close();
        return id;
    }


    public Cursor showData(){

        String show_all ="select * From "+TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all,null);
        return cursor;
    }
    public void deleteData(int id){
        getWritableDatabase().delete(TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
    }


    public void updateData(String type,String amount,int id,String date,String time){
        SQLiteDatabase sq = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ID,id);
        cv.put(COL_EXP_TYPE, type);
        cv.put(COL_EXP_AMOUNT, amount);
        cv.put(COL_EXP_DATE, date);
        cv.put(COL_EXP_TIME, time);
        sq.update(TABLE_NAME,cv,"Id=?",new String[]{String.valueOf(id)});
        sq.close();
    }

    public Cursor searchData(String fromdate,String todate){

        SQLiteDatabase sq = getReadableDatabase();
        return sq.rawQuery("SELECT * FROM expense WHERE Exp_date>=? and Exp_date<?",
                new String[] { String.valueOf(fromdate), String.valueOf(todate)});
    }

    public Cursor calculateAll(){
        SQLiteDatabase sq = getReadableDatabase();
        return sq.rawQuery("SELECT SUM("+COL_EXP_AMOUNT+") AS TOTAL FROM "+TABLE_NAME,null);
    }

    public Cursor showamount(String fromdate,String todate){

        SQLiteDatabase sq = getReadableDatabase();
        return sq.rawQuery("SELECT SUM("+COL_EXP_AMOUNT+") AS MYTOTAL FROM expense WHERE Exp_date>=? and Exp_date<?",
                new String[] { String.valueOf(fromdate), String.valueOf(todate)});

    }
}
