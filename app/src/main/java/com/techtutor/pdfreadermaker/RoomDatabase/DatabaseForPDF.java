package com.techtutor.pdfreadermaker.RoomDatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {RecentPdf.class,BookMarkPdf.class},exportSchema = false,version = 1)
public abstract  class DatabaseForPDF extends RoomDatabase {
        public abstract RecentDao databaseDao();
        public abstract BookMarkDao bookMarkDao();
}
