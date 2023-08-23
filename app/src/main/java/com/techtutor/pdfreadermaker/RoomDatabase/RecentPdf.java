package com.techtutor.pdfreadermaker.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recent_file")
public class RecentPdf {

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

     @ColumnInfo(name = "timestamp")
    public long timestamp;


     @Ignore
    public RecentPdf() {
    }


    public RecentPdf(int id, String name, String size, String date, String path, long timestamp) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
