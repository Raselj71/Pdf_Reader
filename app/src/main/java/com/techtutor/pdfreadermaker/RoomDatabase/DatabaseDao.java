package com.techtutor.pdfreadermaker.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.techtutor.pdfreadermaker.Adapter.PdfData;

import java.util.List;
@Dao
public interface DatabaseDao {

   @Insert
    public void insertData(DatabasePdf databasePdf);

   @Delete
    public void deleteData(DatabasePdf databasePdf);

   @Update
   public void updateData(DatabasePdf databasePdf);


    @Query("SELECT * FROM pdf_table WHERE isRecent = 1 ORDER BY id DESC")
    List<DatabasePdf> getRecentPdfFiles();

    @Query("SELECT * FROM pdf_table WHERE isBookmarked = 1")
    List<DatabasePdf> getBookmarkedPdfFiles();

    @Query("SELECT * FROM pdf_table WHERE path = :filePath")
    DatabasePdf getPdfByFilePath(String filePath);

}
