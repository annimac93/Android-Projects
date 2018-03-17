package com.appart.hp.nearme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 7/26/2017.
 */

public class DBO_place extends SQLiteOpenHelper{

    SQLiteDatabase db;
    ContentValues cv;

    public DBO_place(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Findme", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.close();
    }



    //update records
//    public boolean updateuser(Integer userid,String username, String name, String email, String mobile, String password, byte[] bytePhoto)
//    {
//        db=this.getWritableDatabase();
//        cv=new ContentValues();
//        cv.put("username", username);
//        cv.put("name", name);
//        cv.put("email", email);
//        cv.put("mobile",mobile);
//        cv.put("password",password);
//        cv.put("photo",bytePhoto);
//        db.update("tbluser", cv, "userid=?", new String[]{Integer.toString(userid)} );
//
//        return true;
//    }

//    public Cursor getid(String username, String name, String email, String mobile)
//    {
//        db=this.getReadableDatabase();
//        Cursor res=db.rawQuery("select userid from tbluser where username='"+ username+"' and name='"+name+"' and email='"+email+"' and mobile='"+mobile+"'", null);
//        return res;
//    }

    //show records

}
