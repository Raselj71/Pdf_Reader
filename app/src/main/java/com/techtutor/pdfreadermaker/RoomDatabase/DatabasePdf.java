package com.techtutor.pdfreadermaker.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pdf_table")
public class DatabasePdf {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "size")
    private String size;

     @ColumnInfo(name = "date")
    private String date;


     @ColumnInfo(name = "path")
    private String path;

     @ColumnInfo(name = "isRecent")
    public boolean isRecent;

     @ColumnInfo(name = "isBookmarked")
    public boolean isBookmarked;

     @ColumnInfo(name = "timestamp")
    public long timestamp;


     @Ignore
    public DatabasePdf() {
    }


    public DatabasePdf(int id, String name, String size, String date, String path, boolean isRecent, boolean isBookmarked, long timestamp) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
        this.isRecent = isRecent;
        this.isBookmarked = isBookmarked;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRecent() {
        return isRecent;
    }

    public void setRecent(boolean recent) {
        isRecent = recent;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
