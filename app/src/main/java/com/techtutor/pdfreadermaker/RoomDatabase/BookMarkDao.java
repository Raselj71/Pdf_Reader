package com.techtutor.pdfreadermaker.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookMarkDao {


    @Insert
    public void insertData(BookMarkPdf bookMarkPdf);

    @Delete
    public void deleteData(BookMarkPdf bookMarkPdf);

    @Update
    public void updateData(BookMarkPdf bookMarkPdf);


    @Query("SELECT * FROM bookmark_file  ORDER BY timestamp DESC")
    List<BookMarkPdf> getBookmarkFiles();





    @Query("SELECT * FROM bookmark_file WHERE path = :filePath")
    BookMarkPdf getPdfByFilePath(String filePath);

}
