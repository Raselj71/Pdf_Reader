package com.techtutor.pdfreadermaker.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface RecentDao {

   @Insert
    public void insertData(RecentPdf recentPdf);

   @Delete
    public void deleteData(RecentPdf recentPdf);

   @Update
   public void updateData(RecentPdf recentPdf);


    @Query("SELECT * FROM Recent_file  ORDER BY timestamp DESC")
    List<RecentPdf> getRecentPdfFiles();





    @Query("SELECT * FROM Recent_file WHERE path = :filePath")
    RecentPdf getPdfByFilePath(String filePath);

}
