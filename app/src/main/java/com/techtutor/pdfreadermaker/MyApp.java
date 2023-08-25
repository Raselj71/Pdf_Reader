package com.techtutor.pdfreadermaker;


import android.app.Application;


import androidx.room.Room;



import com.techtutor.pdfreadermaker.RoomDatabase.DatabaseForPDF;



public class MyApp extends Application {

     public static DatabaseForPDF db;



    @Override
    public void onCreate() {
        super.onCreate();
        initializedDatabase();


    }


    

    public void initializedDatabase(){
        db= Room.databaseBuilder(getApplicationContext(),DatabaseForPDF.class,"pdfDatabase").allowMainThreadQueries().build();


    }
}
